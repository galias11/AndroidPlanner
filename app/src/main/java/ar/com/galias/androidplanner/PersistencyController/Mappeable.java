package ar.com.galias.androidplanner.PersistencyController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by gerardo on 7/01/18.
 */

public abstract class Mappeable implements Serializable{
    private final int classID;
    protected boolean modified;

    public static int ID_CATEGORIA = 1001;
    public static int ID_PLAN = 1002;
    public static int ID_TAREA = 1003;
    public static int ID_RUTINA = 1004;
    public static int ID_EVENTO = 1005;
    public static int ID_EVENTO_TAREA = 1006;
    public static int ID_EVENTO_RUTINA = 1007;
    public static int ID_HITO = 1008;

    /**
     * Constructor, it creates a new Mappeable instance. In the process, it defines the classID
     * attribute that correspond to a Mappeable class. The modified attribute is set to false by
     * default.
     * @param classID
     * (int) ID of the class that inherits from Mappeable. It's desirable that this ID is unique on
     * a single system.
     */
    public Mappeable(int classID){
        this.classID = classID;
        this.modified = false;
    }


    public int getClassID(){
        return classID;
    }


    public boolean isModified(){
        return modified;
    }

    /**
     * This method indicates that the Mappeable object has changed and has to be saved to
     * its current storage.
     */
    public void setChanged(){
        modified = true;
    }

    /**
     * This method indicates that the Mappeable object changes has benn saved to the its
     * current storage.
     */
    public void setSaved(){
        modified = false;
    }



}
