package ar.com.galias.androidplanner.AppLayer;

import java.util.Calendar;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Class representing a fact in the completion of certain event in a task.
 */

public class Hito extends Mappeable{
    private long id_hito;
    private Calendar fecReal;
    private double cantReal;
    private String obs;
    private boolean cierraEvento;


    /**
     * Constructor. Creates a new fact for a event.
     * @param fecReal
     * (Calendar) Realization date. Must be a non null Calendar object (present or past).
     * (PRECOND: Must be checked by the caller).
     * @param cantReal
     * (double) Quantity done. Must be a non negative real value.
     * (PRECOND: Must be checked by the caller).
     * @param obs
     * (String) Observations. Could be null or empty (Max. 400 chars).
     * (PRECOND: Must be checked by the caller).
     * @param cierraEvento
     * (boolean) If enabled, it means that this fact closes its corresponding event.
     * (PRECOND: Must be checked by the caller).
     */
    public Hito(Calendar fecReal, double cantReal, String obs, boolean cierraEvento){
        super(Mappeable.ID_HITO);
        this.fecReal = fecReal;
        this.cantReal = cantReal;
        this.obs = obs;
        this.cierraEvento = cierraEvento;
    }

    public Hito(){
        super(Mappeable.ID_HITO);
    }

    public Calendar getFecReal() {
        return fecReal;
    }

    public double getCantReal() {
        return cantReal;
    }

    public String getObs() {
        return obs;
    }

    public boolean isCierraEvento() {
        return cierraEvento;
    }

    public long getId_hito(){
        return id_hito;
    }

    public void setId_hito(long id_hito){
        this.id_hito = id_hito;
    }

}
