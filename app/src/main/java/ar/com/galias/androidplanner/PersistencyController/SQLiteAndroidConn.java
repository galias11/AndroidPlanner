package ar.com.galias.androidplanner.PersistencyController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by @galias on 7/01/18.
 * SQLite database connection.
 */

public class SQLiteAndroidConn extends SQLiteOpenHelper implements Serializable{
    private final int DATABASE_VERSION;
    private final String DATABASE_FILE;

    public static final int TRANSACTION_FAILURE = -1;


    public static final String TABLE_CATEGORIA = "Table_Categoria";
    public static final String ATT_CATEGORIA_ID = "id_categ";
    public static final String ATT_CATEGORIA_NOMBRE = "nombre";
    public static final String ATT_CATEGORIA_DESC = "descr";

    public static final String TABLE_PLAN = "Table_Plan";
    public static final String ATT_PLAN_ID_PLAN = "id_plan";
    public static final String ATT_PLAN_TITULO = "titulo";
    public static final String ATT_PLAN_DESC = "descr";
    public static final String ATT_PLAN_ACTIVO = "activo";
    public static final String ATT_PLAN_CATEG = "categ";
    public static final String ATT_PLAN_TIPO = "tipo";

    public static final String TABLE_TAREA = "Table_Tarea";
    public static final String ATT_TAREA_ID_TAREA = "id_tarea";
    public static final String ATT_TAREA_AVANC = "avanc";

    public static final String TABLE_RUTINA = "Table_Rutina";
    public static final String ATT_RUTINA_ID_RUTINA = "id_rutina";
    public static final String ATT_RUTINA_FREC_TIPO = "frec_tipo";
    public static final String ATT_RUTINA_FREC_CANT = "frec_cant";
    public static final String ATT_RUTINA_UD_FREC_NOTIF = "ud_frec_notif";
    public static final String ATT_RUTINA_CANT_FREC_NOTIF = "cant_frec_notif";

    public static final String TABLE_EVENTO = "Table_Evento";
    public static final String ATT_EVENTO_ID_EVENT = "id_evento";
    public static final String ATT_EVENTO_TITULO = "titulo";
    public static final String ATT_EVENTO_DESC = "descr";
    public static final String ATT_EVENTO_FEC_PLAN = "fec_plan";
    public static final String ATT_EVENTO_CERRADO = "cerrado";
    public static final String ATT_EVENTO_TIPO = "tipo";
    public static final String ATT_EVENTO_UD_FREC_NOTIF = "ud_frec_notif";
    public static final String ATT_EVENTO_CANT_FREC_NOTIF = "cant_frec_notif";
    public static final String ATT_EVENTO_FEC_CIERRE = "fec_cierre";
    public static final String ATT_EVENTO_CANCELADO = "cancelado";

    public static final String TABLE_EVENTO_TAREA = "Table_Evento_tarea";
    public static final String ATT_EVENTO_TAREA_CANT_PLAN = "cant_plan";
    public static final String ATT_EVENTO_TAREA_UD_MED = "ud_med";
    public static final String ATT_EVENTO_TAREA_PONDER = "ponder";
    public static final String ATT_EVENTO_TAREA_CANT_ACT = "cant_act";
    public static final String ATT_EVENTO_TAREA_TAREA = "tarea";


    public static final String TABLE_EVENTO_RUTINA = "Table_Evento_rutina";
    public static final String ATT_EVENTO_RUTINA_ID_EVENTO = "id_evento";
    public static final String ATT_EVENTO_RUTINA_REALIZ = "realiz";
    public static final String ATT_EVENTO_RUTINA_RUTINA = "rutina";
    public static final String ATT_EVENTO_RUTINA_OBS = "obs";

    public static final String TABLE_HITO = "Table_Hito_evento_tarea";
    public static final String ATT_HITO_ID_HITO = "id_hito";
    public static final String ATT_HITO_ID_EVENTO = "evento";
    public static final String ATT_HITO_FEC = "fec";
    public static final String ATT_HITO_CANT_REAL = "cant_realizada";
    public static final String ATT_HITO_OBS = "obs";
    public static final String ATT_HITO_CIERRE = "cierre";

    private static HashMap<String, HashMap<String, Integer>> storage_map = new HashMap<>();

    static{
        HashMap<String, Integer> categoria_map = new HashMap<String, Integer>();
        categoria_map.put(ATT_CATEGORIA_ID, 0);
        categoria_map.put(ATT_CATEGORIA_NOMBRE, 1);
        categoria_map.put(ATT_CATEGORIA_DESC, 2);
        storage_map.put(TABLE_CATEGORIA, categoria_map);

        HashMap<String, Integer> plan_map = new HashMap<String, Integer>();
        plan_map.put(ATT_PLAN_ID_PLAN, 0);
        plan_map.put(ATT_PLAN_TITULO, 1);
        plan_map.put(ATT_PLAN_DESC, 2);
        plan_map.put(ATT_PLAN_ACTIVO, 3);
        plan_map.put(ATT_PLAN_CATEG, 4);
        plan_map.put(ATT_PLAN_TIPO, 5);
        storage_map.put(TABLE_PLAN, plan_map);

        HashMap<String, Integer> tarea_map = new HashMap<String, Integer>();
        tarea_map.put(ATT_TAREA_ID_TAREA, 5 + 0);
        tarea_map.put(ATT_TAREA_AVANC, 5 + 1);
        storage_map.put(TABLE_TAREA, tarea_map);

        HashMap<String, Integer> rutina_map = new HashMap<String, Integer>();
        rutina_map.put(ATT_RUTINA_ID_RUTINA, 5 + 0);
        rutina_map.put(ATT_RUTINA_FREC_TIPO, 5 + 1);
        rutina_map.put(ATT_RUTINA_FREC_CANT, 5 + 2);
        rutina_map.put(ATT_RUTINA_UD_FREC_NOTIF, 5 + 3);
        rutina_map.put(ATT_RUTINA_CANT_FREC_NOTIF, 5 + 4);
        storage_map.put(TABLE_RUTINA, rutina_map);

        HashMap<String, Integer> evento_map = new HashMap<String, Integer>();
        evento_map.put(ATT_EVENTO_ID_EVENT, 0);
        evento_map.put(ATT_EVENTO_TITULO, 1);
        evento_map.put(ATT_EVENTO_DESC, 2);
        evento_map.put(ATT_EVENTO_FEC_PLAN, 3);
        evento_map.put(ATT_EVENTO_CERRADO, 4);
        evento_map.put(ATT_EVENTO_TIPO, 5);
        evento_map.put(ATT_EVENTO_UD_FREC_NOTIF, 6);
        evento_map.put(ATT_EVENTO_CANT_FREC_NOTIF, 7);
        evento_map.put(ATT_EVENTO_CANT_FREC_NOTIF, 8);
        evento_map.put(ATT_EVENTO_CANCELADO, 9);
        storage_map.put(TABLE_EVENTO, evento_map);

        HashMap<String, Integer> evento_tarea_map = new HashMap<String, Integer>();
        evento_tarea_map.put(ATT_EVENTO_ID_EVENT, 9 + 0);
        evento_tarea_map.put(ATT_EVENTO_TAREA_CANT_PLAN, 9 + 1);
        evento_tarea_map.put(ATT_EVENTO_TAREA_UD_MED, 9 + 2);
        evento_tarea_map.put(ATT_EVENTO_TAREA_PONDER, 9 + 3);
        evento_tarea_map.put(ATT_EVENTO_TAREA_CANT_ACT, 9 + 4);
        evento_tarea_map.put(ATT_EVENTO_TAREA_TAREA, 9 + 5);
        storage_map.put(TABLE_EVENTO_TAREA, evento_tarea_map);

        HashMap<String, Integer> evento_rutina_map = new HashMap<String, Integer>();
        evento_rutina_map.put(ATT_EVENTO_RUTINA_ID_EVENTO, 9 + 0);
        evento_rutina_map.put(ATT_EVENTO_RUTINA_REALIZ, 9 + 1);
        evento_rutina_map.put(ATT_EVENTO_RUTINA_RUTINA, 9 + 2);
        evento_rutina_map.put(ATT_EVENTO_RUTINA_OBS, 9 + 3);
        storage_map.put(TABLE_EVENTO_RUTINA, evento_rutina_map);

        HashMap<String, Integer> hito_map = new HashMap<String, Integer>();
        hito_map.put(ATT_HITO_ID_HITO, 0);
        hito_map.put(ATT_HITO_ID_EVENTO, 1);
        hito_map.put(ATT_HITO_FEC, 2);
        hito_map.put(ATT_HITO_CANT_REAL, 3);
        hito_map.put(ATT_HITO_OBS, 4);
        hito_map.put(ATT_HITO_CIERRE, 5);
        storage_map.put(TABLE_HITO, hito_map);
    }


    public SQLiteAndroidConn(Context appContext){
        super(appContext, "planner.db", null, 1);
        DATABASE_FILE = "plannerDB.db";
        DATABASE_VERSION = 1;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIA + "(" +
                ATT_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ATT_CATEGORIA_NOMBRE + " VARCHAR(25) NOT NULL," +
                ATT_CATEGORIA_DESC + " VARCHAR(200)," +
                "UNIQUE (nombre))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLAN + "(" +
                ATT_PLAN_ID_PLAN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ATT_PLAN_TITULO + " VARCHAR(25) NOT NULL," +
                ATT_PLAN_DESC + " VARCHAR(200)," +
                ATT_PLAN_ACTIVO + " BOOLEAN NOT NULL," +
                ATT_PLAN_CATEG + " INTEGER NOT NULL," +
                ATT_PLAN_TIPO + " INTEGER NOT NULL," +
                "FOREIGN KEY (categ) REFERENCES " + TABLE_CATEGORIA + " (id_categ))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TAREA + " (" +
                ATT_TAREA_ID_TAREA + " INTEGER PRIMARY KEY," +
                ATT_TAREA_AVANC + " DOUBLE NOT NULL," +
                "FOREIGN KEY (id_tarea) REFERENCES " + TABLE_PLAN + "(id_plan))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RUTINA + " (" +
                ATT_RUTINA_ID_RUTINA + " INTEGER PRIMARY KEY," +
                ATT_RUTINA_FREC_TIPO + " INTEGER NOT NULL," +
                ATT_RUTINA_FREC_CANT + " INTEGER NOT NULL," +
                ATT_RUTINA_UD_FREC_NOTIF + " INTEGER NOT NULL," +
                ATT_RUTINA_CANT_FREC_NOTIF + " INTEGER NOT NULL," +
                "FOREIGN KEY (id_rutina) REFERENCES " + TABLE_PLAN + " (id_plan))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTO + " (" +
                ATT_EVENTO_ID_EVENT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ATT_EVENTO_TITULO + " VARCHAR(25) NOT NULL," +
                ATT_EVENTO_DESC + " VARCHAR(200)," +
                ATT_EVENTO_FEC_PLAN + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                ATT_EVENTO_CERRADO + " BOOLEAN NOT NULL," +
                ATT_EVENTO_TIPO + " INTEGER NOT NULL," +
                ATT_EVENTO_UD_FREC_NOTIF + " INTEGER NOT NULL," +
                ATT_EVENTO_CANT_FREC_NOTIF + " INTEGER NOT NULL," +
                ATT_EVENTO_FEC_CIERRE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                ATT_EVENTO_CANCELADO + " BOOLEAN NOT NULL)");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTO_TAREA + " (" +
                ATT_EVENTO_ID_EVENT + " INTEGER PRIMARY KEY," +
                ATT_EVENTO_TAREA_CANT_PLAN + " DOUBLE NOT NULL," +
                ATT_EVENTO_TAREA_UD_MED + " TEXT NOT NULL," +
                ATT_EVENTO_TAREA_PONDER + " DOUBLE NOT NULL," +
                ATT_EVENTO_TAREA_CANT_ACT + " DOUBLE NOT NULL," +
                ATT_EVENTO_TAREA_TAREA + " INTEGER NOT NULL," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABLE_EVENTO + " (id_evento)," +
                "FOREIGN KEY (tarea) REFERENCES " + TABLE_TAREA + " (id_tarea))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTO_RUTINA + " (" +
                ATT_EVENTO_RUTINA_ID_EVENTO + " INTEGER PRIMARY KEY," +
                ATT_EVENTO_RUTINA_REALIZ + " BOOLEAN NOT NULL," +
                ATT_EVENTO_RUTINA_RUTINA + " INTEGER NOT NULL," +
                ATT_EVENTO_RUTINA_OBS + " VARCHAR(400)," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABLE_EVENTO + " (id_evento)," +
                "FOREIGN KEY (rutina) REFERENCES " + TABLE_RUTINA + " (id_tarea))");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HITO + " (" +
                ATT_HITO_ID_HITO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ATT_HITO_ID_EVENTO + " INTEGER," +
                ATT_HITO_FEC + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                ATT_HITO_CANT_REAL + " DOUBLE NOT NULL," +
                ATT_HITO_OBS + " VARCHAR(400)," +
                ATT_HITO_CIERRE + " BOOLEAN NOT NULL," +
                "FOREIGN KEY (evento) REFERENCES " + TABLE_EVENTO_TAREA + " (id_evento))");


    }

    public HashMap<String, HashMap<String, Integer>> getStorage_map() {
        return storage_map;
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

    }

    public long insert(String table_name, ContentValues values){
        return getWritableDatabase().insert(table_name, null, values);

    }

    public long update(String table_name, ContentValues values, String where_clause, String[] where_args){
        return getWritableDatabase().update(table_name, values, where_clause, where_args);
    }

    public long delete(String table_name, String where_clause, String[] where_args){
        return getWritableDatabase().delete(table_name, where_clause, where_args);

    }

    public Cursor select(String selectionString, String[] selectionArgs){
        Cursor resultSet = getReadableDatabase().rawQuery(selectionString, selectionArgs);
        return resultSet;
    }

    public void beginTransaction(){
        getWritableDatabase().beginTransaction();
    }

    public void endTransaction(){
        getWritableDatabase().setTransactionSuccessful();
        getWritableDatabase().endTransaction();
    }



}
