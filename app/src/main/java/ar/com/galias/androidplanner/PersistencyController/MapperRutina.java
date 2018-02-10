package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ar.com.galias.androidplanner.AppLayer.AppLayerException;
import ar.com.galias.androidplanner.AppLayer.Categoria;
import ar.com.galias.androidplanner.AppLayer.Evento;
import ar.com.galias.androidplanner.AppLayer.EventoRutina;
import ar.com.galias.androidplanner.AppLayer.Rutina;

/**
 * Created by @galias on 7/01/18.
 * Maps a Rutina object into it current data storage.
 */

public class MapperRutina extends Mapper {
    public final String TABLE_01 = SQLiteAndroidConn.TABLE_PLAN;
    public final String TABLE_02 = SQLiteAndroidConn.TABLE_RUTINA;

    private final String ATT_ID_PLAN = "id_plan";
    private final String ATT_TITULO = "titulo";
    private final String ATT_DESC = "descr";
    private final String ATT_ACTIVO = "activo";
    private final String ATT_CATEG = "categ";
    private final String ATT_TIPO = "tipo";

    private final String ATT_ID_RUTINA = "id_rutina";
    private final String ATT_FREC_TIPO = "frec_tipo";
    private final String ATT_FREC_CANT = "frec_cant";
    private final String ATT_UD_FREC_NOTIF = "ud_frec_notif";
    private final String ATT_CANT_FREC_NOTIF = "cant_frec_notif";

    public MapperRutina(SQLiteAndroidConn sqLiteAndroidConn) throws PersistencyException {
        super(sqLiteAndroidConn);
        this.table_mapping.put(ATT_ID_PLAN, TABLE_01);
        this.table_mapping.put(ATT_TITULO, TABLE_01);
        this.table_mapping.put(ATT_DESC, TABLE_01);
        this.table_mapping.put(ATT_ACTIVO, TABLE_01);
        this.table_mapping.put(ATT_CATEG, TABLE_01);
        this.table_mapping.put(ATT_TIPO, TABLE_01);
        this.table_mapping.put(ATT_ID_RUTINA, TABLE_02);
        this.table_mapping.put(ATT_FREC_TIPO, TABLE_02);
        this.table_mapping.put(ATT_FREC_CANT, TABLE_02);
    }

    @Override
    protected String createSelectionStringHeader() {
        return "SELECT * FROM " + TABLE_01 + " INNER JOIN " + TABLE_02 + " ON " +
                TABLE_01 + ".id_plan = " + TABLE_02 + ".id_rutina";
    }

    @Override
    protected LinkedHashMap<String, ContentValues> generateInsertValues(Mappeable m) {
        Rutina r = (Rutina) m;
        LinkedHashMap<String, ContentValues> insert_value_table = new LinkedHashMap<String, ContentValues>();
        ContentValues values_01 = new ContentValues();
        values_01.put(ATT_TITULO, r.getTitulo());
        values_01.put(ATT_DESC, r.getDescripcion());
        values_01.put(ATT_ACTIVO, r.isActivo());
        values_01.put(ATT_CATEG, r.getCateg().getId());
        values_01.put(ATT_TIPO, 1);
        insert_value_table.put(TABLE_01, values_01);
        ContentValues values_02 = new ContentValues();
        values_02.put(ATT_FREC_TIPO, r.getFrec_tipo());
        values_02.put(ATT_FREC_CANT, r.getFrec_cant());
        values_02.put(ATT_UD_FREC_NOTIF, r.getUd_tiempo_notif());
        values_02.put(ATT_CANT_FREC_NOTIF, r.getCant_tiempo_notif());
        insert_value_table.put(TABLE_02, values_02);
        return insert_value_table;
    }

    @Override
    protected void assign_IDs(Mappeable m, HashMap<String, Long> new_id_list) {
        Rutina r = (Rutina) m;
        r.setId(new_id_list.get(TABLE_01));
    }

    @Override
    protected HashMap<String, String> generate_where_clause(Mappeable m) {
        HashMap<String, String> where_clause_table = new HashMap<String, String>();
        where_clause_table.put(TABLE_01, ATT_ID_PLAN + " = ?");
        where_clause_table.put(TABLE_02, ATT_ID_RUTINA + " = ?");
        return where_clause_table;
    }

    @Override
    protected HashMap<String, String[]> generate_where_args(Mappeable m) {
        Rutina r = (Rutina) m;
        HashMap<String, String[]> where_args_table = new HashMap<String, String[]>();
        String[] values_01 = {String.valueOf(r.getId())};
        where_args_table.put(TABLE_01, values_01);
        String[] values_02 = {String.valueOf(r.getId())};
        where_args_table.put(TABLE_02, values_02);
        return where_args_table;
    }

    @Override
    protected ArrayList<Mappeable> generateObjects(Cursor c) {
        ArrayList<Mappeable> resultSet = new ArrayList<Mappeable>();
        while(c.moveToNext()){
            try{
                MapperCategoria mapper_categoria = new MapperCategoria(sqLiteDBConnector);
                ArrayList<SearchAttribute> search_att_array = new ArrayList<SearchAttribute>();
                search_att_array.add(new SearchAttribute("id_categ",
                    SearchAttribute.OP_EQUAL, c.getInt(get_table_pos(TABLE_01, ATT_CATEG))));
                Categoria categoria_search = (Categoria) mapper_categoria.select(search_att_array).get(0);
                Rutina r = new Rutina(c.getString(get_table_pos(TABLE_01, ATT_TITULO)),
                                      c.getString(get_table_pos(TABLE_01, ATT_DESC)),
                                      categoria_search,
                                      c.getInt(get_table_pos(TABLE_02, ATT_FREC_TIPO)),
                                      c.getInt(get_table_pos(TABLE_02, ATT_FREC_CANT)),
                                      c.getInt(get_table_pos(TABLE_02, ATT_UD_FREC_NOTIF)),
                                      c.getInt(get_table_pos(TABLE_02, ATT_CANT_FREC_NOTIF))
                        );
                r.setId(c.getLong(get_table_pos(TABLE_01, ATT_ID_PLAN)));
                map_dependent_objects(r, OP_SELECT);
                resultSet.add(r);
            } catch(PersistencyException ex){
            } catch(AppLayerException ex) {
            }
        }
        return resultSet;
    }

    @Override
    protected void map_dependent_objects(Mappeable m, int operation) {
        Rutina r = (Rutina) m;
        try {
            MapperEventoRutina mapper = new MapperEventoRutina(sqLiteDBConnector, r.getId());
            Iterator<EventoRutina> it = r.getEventos().iterator();
            switch (operation) {
                case OP_INSERT:
                    while (it.hasNext()) {
                        EventoRutina e = it.next();
                        mapper.insert(e);
                    }
                    break;
                case OP_UPDATE:
                    while(it.hasNext()){
                        EventoRutina e = it.next();
                        if (e.isModified())
                            mapper.update(e);
                        else if(e.isCreated())
                            mapper.insert(e);
                    }
                    break;
                case OP_DELETE:
                    while(it.hasNext()){
                        EventoRutina e = it.next();
                        mapper.delete(e);
                    }
                    break;
                case OP_SELECT:
                    SearchAttribute s = new SearchAttribute("rutina", SearchAttribute.OP_EQUAL, r.getId());
                    ArrayList<SearchAttribute> attSearchList = new ArrayList<>();
                    attSearchList.add(s);
                    ArrayList<Mappeable> events = mapper.select(attSearchList);
                    Iterator<Mappeable> it_events = events.iterator();
                    while(it.hasNext())
                        r.agregarEvento((EventoRutina) it_events.next());
                    break;
                default:
                    break;
            }
        } catch(PersistencyException e){

        }
    }

    @Override
    protected void verifyComposedID(String table, long id, HashMap<String, ContentValues> objMapping) {
        if(table.equals(TABLE_02)){
            objMapping.get(table).put(ATT_ID_RUTINA, id);
        }
    }
}
