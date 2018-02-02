package ar.com.galias.androidplanner.PersistencyController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by @galias on 7/01/18.
 * Access point to storage module.
 */

public class PersistencyController implements Serializable
{
    private HashMap<Integer, Mapper> mappers;
    private SQLiteAndroidConn sqLiteAndroidConn;

    /**
     * Creates a new storage module.
     * @param sqLiteAndroidConn
     * (SQLiteAndroidConn) Data storage connector, must be non null storage connection.
     * @throws PersistencyException
     * If storage connection is null, an exception is thrown.
     */
    public PersistencyController(SQLiteAndroidConn sqLiteAndroidConn) throws PersistencyException{
        this.sqLiteAndroidConn = sqLiteAndroidConn;
        mappers = new HashMap<Integer, Mapper>();
        mappers.put(Mappeable.ID_TAREA, new MapperTarea(sqLiteAndroidConn));
        mappers.put(Mappeable.ID_RUTINA, new MapperRutina(sqLiteAndroidConn));
        mappers.put(Mappeable.ID_CATEGORIA, new MapperCategoria(sqLiteAndroidConn));
    }

    public void save(Mappeable m) throws PersistencyException{
        mappers.get(m.getClassID()).insert(m);
    }

    public void edit(Mappeable m)throws PersistencyException{
        mappers.get(m.getClassID()).update(m);
    }

    public void delete(Mappeable m) throws  PersistencyException{
        mappers.get(m.getClassID()).delete(m);
    }

    public ArrayList<Mappeable> get(int classID, ArrayList<SearchAttribute> s)
    throws PersistencyException {
        return mappers.get(classID).select(s);
    }
}
