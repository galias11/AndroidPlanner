package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.AppLayerException;
import ar.com.galias.androidplanner.AppLayer.Categoria;
import ar.com.galias.androidplanner.AppLayer.EventoTarea;
import ar.com.galias.androidplanner.AppLayer.Tarea;

/**
 * Created by @galias on 7/01/18.
 * Maps a Tarea object into its current data storage.
 */

public class MapperTarea extends Mapper {
    private final String TABLE_01 = SQLiteAndroidConn.TABLE_PLAN;
    private final String TABLE_02 = SQLiteAndroidConn.TABLE_TAREA;

    private final String ATT_ID_PLAN = "id_plan";
    private final String ATT_TITULO = "titulo";
    private final String ATT_DESC = "descr";
    private final String ATT_ACTIVO = "activo";
    private final String ATT_CATEG = "categ";
    private final String ATT_TIPO = "tipo";

    private final String ATT_ID_TAREA = "id_tarea";
    private final String ATT_AVANC = "avanc";


    public MapperTarea(SQLiteAndroidConn sqLiteAndroidConn) throws PersistencyException{
        super(sqLiteAndroidConn);
        this.table_mapping.put(ATT_ID_PLAN, TABLE_01);
        this.table_mapping.put(ATT_TITULO, TABLE_01);
        this.table_mapping.put(ATT_DESC, TABLE_01);
        this.table_mapping.put(ATT_ACTIVO, TABLE_01);
        this.table_mapping.put(ATT_CATEG, TABLE_01);
        this.table_mapping.put(ATT_TIPO, TABLE_01);
        this.table_mapping.put(ATT_ID_TAREA, TABLE_02);
        this.table_mapping.put(ATT_AVANC, TABLE_02);
    }


    @Override
    protected String createSelectionStringHeader() {
        return "SELECT * FROM " + TABLE_01 + " INNER JOIN " + TABLE_02 + " ON " +
                TABLE_01 + ".id_plan = " + TABLE_02 + ".id_tarea";
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m) {
        Tarea t = (Tarea) m;
        LinkedHashMap<String, ContentValues> insert_value_table = new LinkedHashMap<String, ContentValues>();
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_TITULO, t.getTitulo());
        values_01.put(ATT_DESC, t.getDescripcion());
        values_01.put(ATT_ACTIVO, t.isActivo());
        values_01.put(ATT_CATEG, t.getCateg().getId());
        values_01.put(ATT_TIPO, 0);
        insert_value_table.put(TABLE_01, values_01);

        ContentValues values_02 = new ContentValues();
        values_02.put(ATT_AVANC, t.getAvanc());
        insert_value_table.put(TABLE_02, values_02);
        return insert_value_table;
    }

    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list) {
        Tarea t = (Tarea) m;
        t.setId(new_id_list.get(TABLE_01));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m) {
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID_PLAN + " = ?");
        where_clause_table.put(TABLE_02, ATT_ID_TAREA + " = ?");
        return where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m) {
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        Tarea t = (Tarea) m;
        String[] table01_args = {String.valueOf(t.getId())};
        where_args_table.put(TABLE_01, table01_args);
        String[] table02_args = {String.valueOf(t.getId())};
        where_args_table.put(TABLE_02, table02_args);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()){
            try {
                MapperCategoria mapper_categoria = new MapperCategoria(sqLiteDBConnector);
                ArrayList<SearchAttribute> search_att_array = new ArrayList<SearchAttribute>();
                search_att_array.add(new SearchAttribute(SQLiteAndroidConn.ATT_CATEGORIA_ID,
                        SearchAttribute.OP_EQUAL, c.getInt(get_table_pos(TABLE_01, ATT_CATEG))));
                Categoria categoria_search = (Categoria) mapper_categoria.select(search_att_array).get(0);
                Tarea t = new Tarea(c.getString(get_table_pos(TABLE_01, ATT_TITULO)),
                                    c.getString(get_table_pos(TABLE_01, ATT_DESC)),
                                    categoria_search);
                t.setId(c.getLong(get_table_pos(TABLE_01, ATT_ID_PLAN)));
                System.out.println(c.getLong(get_table_pos(TABLE_01, ATT_ID_PLAN)) + " - " + c.getInt(get_table_pos(TABLE_01, ATT_ACTIVO)));
                if(c.getInt(get_table_pos(TABLE_01, ATT_ACTIVO)) == 0)
                    t.cancelar();
                map_dependent_objects(t, OP_SELECT);
                resultSet.add(t);
            } catch(PersistencyException ex){
            } catch(AppLayerException ex){
            }
        }
        return resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int operation) {
        Tarea t = (Tarea) m;
        try{
            MapperEventoTarea mapper = new MapperEventoTarea(sqLiteDBConnector, t.getId());
            switch(operation){
                case OP_INSERT:
                    for(EventoTarea e : t.getEventos().values())
                        mapper.insert(e);
                    break;
                case OP_UPDATE:
                    EventoTarea lastInsertedEvent = null;
                    for(EventoTarea e : t.getEventos().values()) {
                        if (e.isModified())
                            mapper.update(e);
                        else if (e.isCreated()) {
                            mapper.insert(e);
                            lastInsertedEvent = e;
                        }
                    }
                    if(lastInsertedEvent != null){
                        t.getEventos().remove(-1);
                        t.getEventos().put(lastInsertedEvent.getId(), lastInsertedEvent);
                    }
                    break;
                case OP_DELETE:
                    for(EventoTarea e : t.getEventos().values())
                        mapper.delete(e);
                    break;
                case OP_SELECT:
                    SearchAttribute s = new SearchAttribute("tarea", SearchAttribute.OP_EQUAL, t.getId());
                    ArrayList<SearchAttribute> attSearchList = new ArrayList<>();
                    attSearchList.add(s);
                    ArrayList<Mappeable> events = mapper.select(attSearchList);
                    Iterator it = events.iterator();
                    while(it.hasNext()){
                        EventoTarea e = (EventoTarea) it.next();
                        t.agregarEvento(e);
                    }
                    break;
                default:
                 break;
            }
        } catch (PersistencyException e){

        }
    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> objMapping) {
        if(table.equals(TABLE_02)){
            objMapping.get(table).put(ATT_ID_TAREA, id);
        }
    }
}
