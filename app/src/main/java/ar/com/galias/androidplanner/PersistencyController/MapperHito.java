package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.Hito;

/**
 * Created by @galias on 7/01/18.
 * Mapps an Hito class objet to the data storage.
 */

public class MapperHito extends  Mapper{
    private final String TABLE_01 = SQLiteAndroidConn.TABLE_HITO;

    private final String ATT_ID_HITO = "id_hito";
    private final String ATT_ID_EVENTO = "evento";
    private final String ATT_FEC = "fec";
    private final String ATT_CANT_REAL = "cant_realizada";
    private final String ATT_OBS = "obs";
    private final String ATT_CIERRE = "cierre";




    private final long parent_table_id;

    public MapperHito(SQLiteAndroidConn sqLiteAndroidConn, long parent_event_id)
    throws PersistencyException{
        super(sqLiteAndroidConn);
        this.parent_table_id = parent_event_id;
        this.table_mapping.put(ATT_ID_HITO, TABLE_01);
        this.table_mapping.put(ATT_ID_EVENTO, TABLE_01);
        this.table_mapping.put(ATT_FEC, TABLE_01);
        this.table_mapping.put(ATT_CANT_REAL, TABLE_01);
        this.table_mapping.put(ATT_OBS, TABLE_01);
        this.table_mapping.put(ATT_CIERRE, TABLE_01);
    }

    @Override
    protected String createSelectionStringHeader(){
        return "SELECT * FROM " + TABLE_01;
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m) {
        Hito h = (Hito) m;
        LinkedHashMap<String, ContentValues> insert_values_table = new LinkedHashMap<String, ContentValues>();
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_ID_EVENTO, parent_table_id);
        values_01.put(ATT_FEC, getDateTime(h.getFecReal()));
        values_01.put(ATT_CANT_REAL, h.getCantReal());
        values_01.put(ATT_OBS, h.getObs());
        values_01.put(ATT_CIERRE, h.isCierraEvento());
        insert_values_table.put(TABLE_01,  values_01);
        return insert_values_table;
    }

    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list) {
        Hito h = (Hito) m;
        h.setId_hito(new_id_list.get(TABLE_01));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m) {
        Hito h = (Hito) m;
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID_HITO + " = ? AND " + ATT_ID_EVENTO + " = ?");
        return where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m) {
        Hito h = (Hito) m;
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        String[] args_01 = {String.valueOf(h.getId_hito()), String.valueOf(parent_table_id)};
        where_args_table.put(TABLE_01, args_01);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()){
            Hito new_hito = new Hito(
                    c.getLong(get_table_pos(TABLE_01, ATT_ID_HITO)),
                    getDateTime(c.getString(get_table_pos(TABLE_01, ATT_FEC))),
                    c.getDouble(get_table_pos(TABLE_01, ATT_CANT_REAL)),
                    c.getString(get_table_pos(TABLE_01, ATT_OBS)),
                    c.getInt(get_table_pos(TABLE_01, ATT_CIERRE)) == 1);
            resultSet.add(new_hito);
        }
        return  resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int op_code){

    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> object_mapping) {

    }
}
