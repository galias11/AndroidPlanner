package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.EventoRutina;

/**
 * Created by @galias on 7/01/18.
 * Maps an EventoRutina object into the data storage.
 */

public class MapperEventoRutina extends Mapper{
    private final String TABLE_01 = SQLiteAndroidConn.TABLE_EVENTO;
    private final String TABLE_02 = SQLiteAndroidConn.TABLE_EVENTO_RUTINA;

    private final String ATT_ID_EVENT = "id_evento";
    private final String ATT_TITULO = "titulo";
    private final String ATT_DESC = "descr";
    private final String ATT_FEC_PLAN = "fec_plan";
    private final String ATT_CERRADO = "cerrado";
    private final String ATT_TIPO = "tipo";
    private final String ATT_UD_FREC_NOTIF = "ud_frec_notif";
    private final String ATT_CANT_FREC_NOTIF = "cant_frec_notif";
    private final String ATT_FEC_CIERRE = "fec_cierre";
    private final String ATT_CANCELADO = "cancelado";

    private final String ATT_REALIZ = "realiz";
    private final String ATT_RUTINA = "rutina";
    private final String ATT_OBS = "obs";

    private final long parent_table_id;

    public MapperEventoRutina(SQLiteAndroidConn sqLiteAndroidConn, long parent_table_id)
    throws PersistencyException {
        super(sqLiteAndroidConn);
        this.parent_table_id = parent_table_id;
        table_mapping.put(ATT_ID_EVENT, TABLE_01);
        table_mapping.put(ATT_TITULO, TABLE_01);
        table_mapping.put(ATT_DESC, TABLE_01);
        table_mapping.put(ATT_FEC_PLAN, TABLE_01);
        table_mapping.put(ATT_CERRADO, TABLE_01);
        table_mapping.put(ATT_TIPO, TABLE_01);
        table_mapping.put(ATT_UD_FREC_NOTIF, TABLE_01);
        table_mapping.put(ATT_CANT_FREC_NOTIF, TABLE_01);
        table_mapping.put(ATT_FEC_CIERRE, TABLE_01);
        table_mapping.put(ATT_CANCELADO, TABLE_01);
        table_mapping.put(ATT_REALIZ, TABLE_02);
        table_mapping.put(ATT_RUTINA, TABLE_02);
        table_mapping.put(ATT_OBS, TABLE_02);
    }

    @Override
    protected String createSelectionStringHeader(){
        return "SELECT * FROM " + TABLE_01 + " INNER JOIN " + TABLE_02 + " ON " +
                TABLE_01 + ".id_evento = " + TABLE_02 + ".id_evento ";
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m){
        EventoRutina e = (EventoRutina) m;
        LinkedHashMap<String, ContentValues> insert_value_table = new LinkedHashMap<String, ContentValues>();
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_TITULO, e.getTitulo());
        values_01.put(ATT_DESC, e.getDesc());
        values_01.put(ATT_FEC_PLAN, getDateTime(e.getFecPlan()));
        values_01.put(ATT_CERRADO, e.isCerrado());
        values_01.put(ATT_TIPO, 1);
        values_01.put(ATT_UD_FREC_NOTIF, e.getUd_frec_notif());
        values_01.put(ATT_CANT_FREC_NOTIF, e.getUd_frec_notif());
        values_01.put(ATT_CANCELADO, e.isCancelado());
        insert_value_table.put(TABLE_01, values_01);
        ContentValues values_02 = new ContentValues();
        values_02.put(ATT_REALIZ, e.isRealizado());
        values_02.put(ATT_RUTINA, parent_table_id);
        values_02.put(ATT_OBS, e.getObs());
        insert_value_table.put(TABLE_02, values_02);
        return insert_value_table;
    }

    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list){
        EventoRutina e = (EventoRutina) m;
        e.setId(new_id_list.get(TABLE_01));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m){
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID_EVENT + " = ?");
        where_clause_table.put(TABLE_02, ATT_ID_EVENT + " = ?");
        return  where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m){
        EventoRutina e = (EventoRutina) m;
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        String[] table_01_args = {String.valueOf(e.getId())};
        where_args_table.put(TABLE_01, table_01_args);
        String[] table_02_args = {String.valueOf(e.getId())};
        where_args_table.put(TABLE_02, table_02_args);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()){
            EventoRutina e = new EventoRutina(c.getString(get_table_pos(TABLE_01, ATT_TITULO)),
                                              c.getString(get_table_pos(TABLE_01, ATT_DESC)),
                                              getDateTime(c.getString(get_table_pos(TABLE_01, ATT_FEC_PLAN))),
                                              c.getInt(get_table_pos(TABLE_01, ATT_UD_FREC_NOTIF)),
                                              c.getInt(get_table_pos(TABLE_01, ATT_CANT_FREC_NOTIF)));
            e.setId(c.getLong(get_table_pos(TABLE_01, ATT_ID_EVENT)));
            map_dependent_objects(e, OP_SELECT);
            resultSet.add(e);
        }
        return resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int operation) {

    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> objMapping) {
        if(table.equals(TABLE_02)){
            objMapping.get(table).put(ATT_ID_EVENT, id);
        }
    }
}
