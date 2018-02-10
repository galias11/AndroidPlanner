package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;


/**
 * Created by @galias on 7/01/18.
 * Application data mappers base class.
 */

public abstract class Mapper implements Serializable{
    protected SQLiteAndroidConn sqLiteDBConnector;

    public final static int OP_INSERT = 0;
    public final static int OP_UPDATE = 1;
    public final static int OP_DELETE = 2;
    public final static int OP_SELECT = 3;

    protected HashMap<String, String> table_mapping;

    /**
     * Constructor, creates a new mapper.
     * @param sqLiteDBConnector
     * (SQLiteAndroidConn) Connector for SQLite data base.
     * @throws PersistencyException
     */
    public Mapper(SQLiteAndroidConn sqLiteDBConnector) throws PersistencyException{
        if(sqLiteDBConnector == null)
            throw new PersistencyException(PersistencyException.ERR_MAPPER_NULL_CONN);
        this.sqLiteDBConnector = sqLiteDBConnector;
        this.table_mapping = new HashMap<String, String>();
    }

    /**
     * Maps a mappeable object to its current data storage format, and saves it through sqlite
     * database connection.
     * @param m
     * (Mappeable) Object to map and insert into the database. Must be a non null Mappeable object.
     * @throws PersistencyException
     * If the mappeable object is null or operation is not completed by database connection, an
     * exception will be thrown.
     */
    public void insert(Mappeable m) throws PersistencyException {
        if(m == null)
            throw new PersistencyException(PersistencyException.ERR_MAPPER_NULL_OBJ);
        LinkedHashMap<String, ContentValues> objMapping = generateInsertValues(m);
        HashMap<String, Long> new_id_list = new HashMap<String, Long>();
        sqLiteDBConnector.beginTransaction();
        try{
            long new_id = -1;
            for(String table : objMapping.keySet()){
                verifyComposedID(table, new_id, objMapping);
                new_id = sqLiteDBConnector.insert(table, objMapping.get(table));
                if(new_id == SQLiteAndroidConn.TRANSACTION_FAILURE)
                    throw new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
                new_id_list.put(table, new_id);
            }
        } catch(SQLiteAbortException e){
            throw  new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
        }
        assign_IDs(m, new_id_list);
        map_dependent_objects(m, OP_INSERT);
        sqLiteDBConnector.endTransaction();
    }

    /**
     * Maps a mappeable object to its current data storage format, and modifies its entry on the
     * database through sqlite database connection.
     * @param m
     * (Mappeable) Object to map and modified on the database. Must be a non null Mappeable object.
     * @throws PersistencyException
     * If the mappeable object is null or operation is not completed by database connection, an
     * exception will be thrown.
     */
    public void update(Mappeable m) throws PersistencyException{
        if(m == null)
            throw new PersistencyException(PersistencyException.ERR_MAPPER_NULL_OBJ);
        if(m.isModified()) {
            System.out.println("entre 1 - ");
            HashMap<String, ContentValues> objMapping = generateInsertValues(m);
            HashMap<String, String> where_clause = generate_where_clause(m);
            HashMap<String, String[]> where_args = generate_where_args(m);
            sqLiteDBConnector.beginTransaction();
            try {
                for (String table : objMapping.keySet())
                    if (sqLiteDBConnector.update(table, objMapping.get(table),
                            where_clause.get(table), where_args.get(table)) ==
                            SQLiteAndroidConn.TRANSACTION_FAILURE)
                        throw new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
            } catch (SQLiteAbortException e) {
                throw new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
            }
            sqLiteDBConnector.endTransaction();
        }
        map_dependent_objects(m, OP_UPDATE);
    }

    /**
     * Maps a mappeable object to its curren data storage format, and deletes its entry from the
     * database through sqlite database connection.
     * @param m
     * (Mappeable) Object to map and delete on the database. Must be a non null Mappeable object.
     * @throws PersistencyException
     * If the mappeable object is null or operation is not completed by database connection, an
     * exception will be thrown.
     */
    public void delete(Mappeable m) throws PersistencyException{
        if(m == null)
            throw new PersistencyException(PersistencyException.ERR_MAPPER_NULL_OBJ);
        HashMap<String, String> where_clause = generate_where_clause(m);
        HashMap<String, String[]> where_args = generate_where_args(m);
        try{
            for(String table: where_clause.keySet())
                if(sqLiteDBConnector.delete(table, where_clause.get(table), where_args.get(table)) ==
                        SQLiteAndroidConn.TRANSACTION_FAILURE)
                    throw new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
        } catch (SQLiteAbortException e){
            throw new PersistencyException(PersistencyException.ERR_MAPPER_TRANS_FAILED);
        }
        sqLiteDBConnector.endTransaction();
        map_dependent_objects(m, OP_DELETE);
    }

    /**
     * Maps a mappeable object into a query to retrieve objects (of the same class) from the
     * database through sqlite database connection.
     * @param s
     * (Selection) Contains query attributes and searched values.
     * @return
     * (ArrayList<Mappeable>) Collection of Mappeable objects that satisfies the current query.
     * @throws PersistencyException
     * If the mappeable object is null or operation is not completed by database connection, an
     * exception will be thrown.
     */
    public ArrayList<Mappeable> select(ArrayList<SearchAttribute> s) throws PersistencyException{
        String selectionString = createSelectionString(s);
        String[] selectionArgs = createSelectionArgs(s);
        Cursor c = sqLiteDBConnector.select(selectionString, selectionArgs);
        ArrayList<Mappeable> resultSet = generateObjects(c);
        for(Mappeable mapped_obj : resultSet)
            map_dependent_objects(mapped_obj, OP_SELECT);
        return resultSet;
    }

    /**
     * Verifies that every attribute passed on a search attribute list is valid for the current
     * mapper.
     * @param s
     * (ArrayList<SearchAttribute>) List of search attributes.
     * @return
     * (boolean) Returns true if every attribute is valid.
     */
    protected boolean verifySearchAttributes(ArrayList<SearchAttribute> s){
        boolean verifyOK = true;
        Iterator<SearchAttribute> it = s.iterator();
        while(verifyOK && it.hasNext())
            verifyOK = table_mapping.containsKey(it.next().getAtt_name());
        return verifyOK;
    }

    /**
     * Generates necessary values for the selection string in the select query
     * @param s
     * (ArrayList<SearchAttribute>) Collection of attribute-value pairs for the query.
     * @return
     * (String[]) Array with search values in string format.
     */
    protected String[] createSelectionArgs(ArrayList<SearchAttribute> s) throws PersistencyException {
        if(s == null)
            return null;
        if(!verifySearchAttributes(s))
            throw new PersistencyException(PersistencyException.ERR_MAPPER_INVALID_SEARCH_ATTS);
        ArrayList<String> selectionArgs = new ArrayList<String>();
        for(SearchAttribute attribute : s){
            selectionArgs.add(attribute.getAtt_value());
        }
        return (String[]) selectionArgs.toArray(new String[0]);
    }

    /**
     * Generates the selection string for the required attributes.
     * @param s
     * (ArrayList<SearhAttribute>) Collection of attribute-value pairs for the query.
     * @return
     * (String) Selection string for the desired query.
     * @throws PersistencyException
     */
    protected String createSelectionString(ArrayList<SearchAttribute> s) throws
            PersistencyException{
        String selectionString = createSelectionStringHeader();
        if(s == null)
            return selectionString;
        if(!verifySearchAttributes(s))
            throw new PersistencyException(PersistencyException.ERR_MAPPER_INVALID_SEARCH_ATTS);
        boolean and_flag = false;
        selectionString += " WHERE ";
        for(SearchAttribute attribute : s){
            if(and_flag)
                selectionString += " AND ";
            selectionString += table_mapping.get(attribute.getAtt_name()) + "."
                    + attribute.getSelection_string_part();
            and_flag = true;
        }
        return selectionString;
    }

    /**
     * Generates the specific select query header for the current mapper.
     * @return
     * (String) Select statement query header.
     */
    protected abstract String createSelectionStringHeader();

    /**
     * Converts date object into standard UTC time string.
     * @param date
     * (Calendar) Date to convert.
     * @return
     * Returns a string with the converted date or null if the date is null.
     */
    protected String getDateTime(Calendar date){
        String date_str = null;
        if(date != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            date_str = sdf.format(date.getTime());
        }
        return date_str;
    }

    /**
     * Recovers a date object from a standard UTC time string
     * @param date
     * (String) Date String. Must be in UTC format.
     * @return
     * (Calendar) Date object created from string. If date string is null, this method will
     * return a null Calendar object.
     */
    protected Calendar getDateTime(String date){
        Calendar date_cal = Calendar.getInstance();
        if(date != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            try {
                date_cal.setTime(sdf.parse(date));
            } catch (ParseException e){

            }
        }
        return date_cal;
    }

    /**
     * Generates a value set for an insert operation.
     * @param m
     * (Mappeable) object to map into the data storage.
     * @return
     * (HashMap<String, ContentValues>) A map with [table, values] entries for the insert
     * operation.
     */
    protected abstract LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m);

    /**
     * Assigns new IDs to recently inserted mappeable objects.
     * @param m
     * (Mappeable) Object to assign the new ID.
     */
    protected abstract void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list);

    /**
     * Generates a where clause from a Mappeable object.
     * @param m
     * (Mappeable) Object to be mapped.
     * @return
     * (HashMap<String, String>) Where clause for every table involved.
     */
    protected  abstract HashMap<String, String> generate_where_clause(Mappeable m);

    /**
     * Generates where args from a Mappeable object.
     * @param m
     * (Mappeable) Object to be mapped.
     * @return
     * (HashMap<String, String[]>) Where args for every table involved.
     */
    protected abstract HashMap<String, String[]> generate_where_args(Mappeable m);

    /**
     * Generates current Mappeable objects from a Cursor object.
     * @param c
     * (Cursor) Cursor containing the results from the current query.
     * @return
     * (ArrayList<Mappeable>) Collection of Mappeable objects resulting from the query.
     */
    protected abstract ArrayList<Mappeable> generateObjects(Cursor c);

    /**
     * Propagates a transaction over an object dependent objects. If current mappeable object
     * has not any mappeable dependent objects this method has to be declared empty.
     * @param m
     * (Mappeable) Primary mappeable object.
     * @param operation
     * (int) operation code. Must be one of the available operations.
     */
    protected abstract void map_dependent_objects(Mappeable m, int operation);

    /**
     * Gets inner sqlite storage attribute position.
     * @param table
     * (String) table name
     * @param att
     * (String) attribute name.
     * @return
     * (int) Position of the desired attribute in the data storage table. If att not exist, returns
     * -1;
     */
    protected int get_table_pos(String table, String att){
        int position = -1;
        if(sqLiteDBConnector.getStorage_map().containsKey(table) &&
                sqLiteDBConnector.getStorage_map().get(table).containsKey(att))
            position = sqLiteDBConnector.getStorage_map().get(table).get(att);
        return position;
    }

    /**
     * Verifies and if it is necessary updates the object mapping table.
     * If the current mapper doesn't map an object to multiple tables related by foreign keys this
     * method should be left empty.
     * @param table
     * (String) Table's name.
     * @param id
     * (long) new ID (in case it is necessary to set it)
     */
    protected abstract void verifyComposedID(String table, long id,
                                             HashMap<String, ContentValues> object_mapping);
}