package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.AppLayerException;
import ar.com.galias.androidplanner.AppLayer.Categoria;

/**
 * Created by @galias on 7/01/18.
 * Maps class Categoria to the database.
 */

public class MapperCategoria extends Mapper{
    private final String ATT_ID = "id_categ";
    private final String ATT_NOMBRE = "nombre";
    private final String ATT_DESC = "descr";


    private final String TABLE_01 = SQLiteAndroidConn.TABLE_CATEGORIA;

    public MapperCategoria(SQLiteAndroidConn sqLiteAndroidConn) throws PersistencyException{
        super(sqLiteAndroidConn);
        this.table_mapping.put(ATT_ID, TABLE_01);
        this.table_mapping.put(ATT_NOMBRE, TABLE_01);
        this.table_mapping.put(ATT_DESC, TABLE_01);
    }

    @Override
    protected String createSelectionStringHeader(){
        return "SELECT * FROM " + TABLE_01;
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m){
        LinkedHashMap<String, ContentValues> insert_table = new LinkedHashMap<String, ContentValues>();
        Categoria c = (Categoria) m;
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_NOMBRE, c.getNombre());
        values_01.put(ATT_DESC, c.getDesc());
        insert_table.put(TABLE_01, values_01);
        return insert_table;
    }

    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list){
        Categoria c = (Categoria) m;
        c.setID(new_id_list.get(SQLiteAndroidConn.TABLE_CATEGORIA));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m){
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID + " = ?");
        return  where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m) {
        Categoria c = (Categoria) m;
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        String[] args_01 = {String.valueOf(c.getId())};
        where_args_table.put(TABLE_01, args_01);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()) {
            try {
                Categoria new_categ = new Categoria(c.getString(get_table_pos(TABLE_01, ATT_NOMBRE)),
                    c.getString(get_table_pos(TABLE_01, ATT_DESC)));
                new_categ.setID(c.getLong(get_table_pos(TABLE_01, ATT_ID)));
                resultSet.add(new_categ);
            } catch (AppLayerException e) {

            }
        }
        return resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int op_code){

    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> object_mapping) {

    }
}
