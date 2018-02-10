package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.EventLog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.Evento;
import ar.com.galias.androidplanner.AppLayer.EventoTarea;
import ar.com.galias.androidplanner.AppLayer.Hito;

/**
 * Created by @galias on 7/01/18.
 * Maps a EventoTarea Object into data storage format.
 */

public class MapperEventoTarea extends Mapper{
    private final String TABLE_01 = SQLiteAndroidConn.TABLE_EVENTO;
    private final String TABLE_02 = SQLiteAndroidConn.TABLE_EVENTO_TAREA;


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

    private final String ATT_CANT_PLAN = "cant_plan";
    private final String ATT_UD_MED = "ud_med";
    private final String ATT_PONDER = "ponder";
    private final String ATT_CANT_ACT = "cant_act";
    private final String ATT_TAREA = "tarea";

    private final long parent_table_id;

    public MapperEventoTarea(SQLiteAndroidConn sqLiteAndroidConn, long parent_table_id)
        throws PersistencyException {
        super(sqLiteAndroidConn);
        this.parent_table_id = parent_table_id;
        this.table_mapping.put(ATT_ID_EVENT, TABLE_01);
        this.table_mapping.put(ATT_TITULO, TABLE_01);
        this.table_mapping.put(ATT_DESC, TABLE_01);
        this.table_mapping.put(ATT_FEC_PLAN, TABLE_01);
        this.table_mapping.put(ATT_CERRADO, TABLE_01);
        this.table_mapping.put(ATT_TIPO, TABLE_01);
        this.table_mapping.put(ATT_UD_FREC_NOTIF, TABLE_01);
        this.table_mapping.put(ATT_CANT_FREC_NOTIF, TABLE_01);
        this.table_mapping.put(ATT_FEC_CIERRE, TABLE_01);
        this.table_mapping.put(ATT_CANCELADO, TABLE_01);
        this.table_mapping.put(ATT_CANT_PLAN, TABLE_02);
        this.table_mapping.put(ATT_UD_MED, TABLE_02);
        this.table_mapping.put(ATT_PONDER, TABLE_02);
        this.table_mapping.put(ATT_CANT_ACT, TABLE_02);
        this.table_mapping.put(ATT_TAREA, TABLE_02);
    }

    @Override
    protected String createSelectionStringHeader(){
        return "SELECT * FROM " + TABLE_01 + " INNER JOIN " + TABLE_02 + " ON " +
                TABLE_01 + ".id_evento = " + TABLE_02 + ".id_evento";
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m) {
        EventoTarea e = (EventoTarea) m;
        LinkedHashMap<String, ContentValues> insert_value_table = new LinkedHashMap<String, ContentValues>();
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_TITULO, e.getTitulo());
        values_01.put(ATT_DESC, e.getDesc());
        values_01.put(ATT_FEC_PLAN, getDateTime(e.getFecPlan()));
        values_01.put(ATT_CERRADO, e.isCerrado());
        values_01.put(ATT_TIPO, 0);
        values_01.put(ATT_UD_FREC_NOTIF, e.getUd_frec_notif());
        values_01.put(ATT_CANT_FREC_NOTIF, e.getUd_frec_notif());
        values_01.put(ATT_CANCELADO, e.isCancelado());
        insert_value_table.put(TABLE_01, values_01);
        ContentValues values_02 = new ContentValues();
        values_02.put(ATT_CANT_PLAN, e.getCantPlaneada());
        values_02.put(ATT_UD_MED, e.getUdMedida());
        values_02.put(ATT_PONDER, e.getPonderacion());
        values_02.put(ATT_CANT_ACT, e.getCantActual());
        values_02.put(ATT_TAREA, parent_table_id);
        insert_value_table.put(TABLE_02, values_02);
        return insert_value_table;
    }
    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list) {
        EventoTarea e = (EventoTarea) m;
        System.out.println("New id: " + new_id_list.get(TABLE_01));
        e.setId(new_id_list.get(TABLE_01));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m) {
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID_EVENT + " = ?");
        where_clause_table.put(TABLE_02, ATT_ID_EVENT + " = ?");
        return where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m) {
        EventoTarea e = (EventoTarea) m;
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        String[] where_arg_01 = {String.valueOf(e.getId())};
        where_args_table.put(TABLE_01, where_arg_01);
        String[] where_arg_02 = {String.valueOf(e.getId())};
        where_args_table.put(TABLE_02, where_arg_02);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()){
            EventoTarea e = new EventoTarea(
                            c.getLong(get_table_pos(TABLE_01, ATT_ID_EVENT)),
                            c.getString(get_table_pos(TABLE_01, ATT_TITULO)),
                            c.getString(get_table_pos(TABLE_01, ATT_DESC)),
                            getDateTime(c.getString(get_table_pos(TABLE_01, ATT_FEC_PLAN))),
                            c.getInt(get_table_pos(TABLE_01, ATT_UD_FREC_NOTIF)),
                            c.getInt(get_table_pos(TABLE_01, ATT_CANT_FREC_NOTIF)),
                            c.getDouble(get_table_pos(TABLE_02, ATT_CANT_PLAN)),
                            c.getString(get_table_pos(TABLE_02, ATT_UD_MED)),
                            c.getInt(get_table_pos(TABLE_02, ATT_PONDER)));
            map_dependent_objects(e, OP_SELECT);
            resultSet.add(e);

        }
        return resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int op_code){
        EventoTarea e = (EventoTarea) m;
        try {
            MapperHito mapper = new MapperHito(sqLiteDBConnector, e.getId());
            switch (op_code) {
                case Mapper.OP_INSERT:
                    for(Hito h : e.getHitos())
                        mapper.insert(h);
                    break;
                case Mapper.OP_UPDATE:
                    for(Hito h : e.getHitos()){
                        if (h.isModified())
                            mapper.update(h);
                        else if (h.isCreated())
                            mapper.insert(h);
                    }
                    break;
                case Mapper.OP_DELETE:
                    for(Hito h : e.getHitos())
                        mapper.delete(h);
                    break;
                case Mapper.OP_SELECT:
                    SearchAttribute s = new SearchAttribute("evento", SearchAttribute.OP_EQUAL, e.getId());
                    ArrayList<SearchAttribute> attSearchList = new ArrayList<>();
                    attSearchList.add(s);
                    ArrayList<Mappeable> hitos_list = mapper.select(attSearchList);
                    for(Mappeable mapped_hito : hitos_list){
                        Hito h = (Hito) mapped_hito;
                        e.actualizar(h);
                    }
                    break;
                default:
                    break;
            }
        } catch (PersistencyException ex){

        }
    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> objMapping) {
        if(table.equals(TABLE_02)){
            objMapping.get(table).put(ATT_ID_EVENT, id);
        }
    }
}
