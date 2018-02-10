package ar.com.galias.androidplanner.AppLayer;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by @galias on 8/01/18.
 * Class representing an event for a routine in the system.
 */

public class EventoTarea extends Evento{
    private double cantPlaneada;
    private String udMedida;
    private int ponderacion;
    private double cantActual;
    private ArrayList<Hito> hitos;


    /**
     * Constructor. Creates a new task event.
     * @param titulo
     * (String) Event's title. Must be a non null/empty string (Max. 25 chars).
     * (PRECOND: Must be checked by the caller).
     * @param desc
     * (String) Event's description. Could be a null/empty string (Max. 200 chars).
     * (PRECOND: Must be checked by the caller).
     * @param fecPlan
     * (Calendar) Event's planned realization date. Must be a non null Calendar object.
     * (PRECOND: Must be checked by the caller).
     * @param ud_frec_notif
     * (int) Event's notification frequency unit. Must be one of class FREC_NOTIF_TYPE constants.
     * (PRECOND: Must be checked by the caller).
     * @param cant_frec_notif
     * (int) Event's notification quantity. Multiplies the unit to get notification time. Must be
     * a positive integer value.
     * (PRECOND: Must be checked by the caller).
     * @param cantPlaneada
     * (double) Quantity to be done to finish this task. Must be a non negative real value.
     * (PRECOND: Must be checked by the caller).
     * @param udMedida
     * (String) Unit in which is meassured the quantity to be done. Must be a non null/empty string
     * (Max. 2 chars)
     * (PRECOND: Must be checked by the caller).
     * @param ponderacion
     * (int) Represents this event weight for the task's completion. Must be a positive integer.
     * (PRECOND: Must be checked by the caller).
     */
    public EventoTarea(String titulo, String desc, Calendar fecPlan,
                       int ud_frec_notif, int cant_frec_notif, double cantPlaneada,
                       String udMedida, int ponderacion){
        super(titulo, desc, fecPlan, ud_frec_notif, cant_frec_notif);
        super.setCreated();
        this.cantPlaneada = cantPlaneada;
        this.udMedida =udMedida;
        this.ponderacion = ponderacion;
        this.cantActual = 0.0;
        this.hitos = new ArrayList<Hito>();
    }

    /**
     * Constructor. This constructor is intended to be used by class mappers.
     * @param id
     * (long) Event's id. Must be a non negative integer value.
     * @param titulo
     * (String) Event's title. Must be a non null/empty string (Max. 25 chars).
     * (PRECOND: Must be checked by the caller).
     * @param desc
     * (String) Event's description. Could be a null/empty string (Max. 200 chars).
     * (PRECOND: Must be checked by the caller).
     * @param fecPlan
     * (Calendar) Event's planned realization date. Must be a non null Calendar object.
     * (PRECOND: Must be checked by the caller).
     * @param ud_frec_notif
     * (int) Event's notification frequency unit. Must be one of class FREC_NOTIF_TYPE constants.
     * (PRECOND: Must be checked by the caller).
     * @param cant_frec_notif
     * (int) Event's notification quantity. Multiplies the unit to get notification time. Must be
     * a positive integer value.
     * (PRECOND: Must be checked by the caller).
     * @param cantPlaneada
     * (double) Quantity to be done to finish this task. Must be a non negative real value.
     * (PRECOND: Must be checked by the caller).
     * @param udMedida
     * (String) Unit in which is meassured the quantity to be done. Must be a non null/empty string
     * (Max. 2 chars)
     * (PRECOND: Must be checked by the caller).
     * @param ponderacion
     * (int) Represents this event weight for the task's completion. Must be a positive integer.
     * (PRECOND: Must be checked by the caller).
     */
    public EventoTarea(long id, String titulo, String desc, Calendar fecPlan,
                       int ud_frec_notif, int cant_frec_notif, double cantPlaneada,
                       String udMedida, int ponderacion, boolean cancelado, boolean cerrado){
        super(titulo, desc, fecPlan, ud_frec_notif, cant_frec_notif);
        this.setId(id);
        this.cantPlaneada = cantPlaneada;
        this.udMedida =udMedida;
        this.ponderacion = ponderacion;
        this.cantActual = 0.0;
        this.hitos = new ArrayList<Hito>();
        if(cerrado)
            super.cierreForzado();
        if(cancelado)
            super.cancelacionForzada();
    }

    public double getCantPlaneada() {
        return cantPlaneada;
    }

    public String getUdMedida() {
        return udMedida;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public double getCantActual() {
        return cantActual;
    }

    public ArrayList<Hito> getHitos() {
        return hitos;
    }

    /**
     * Registers a fact in the event. It represents a partial (or total) realization of the event.
     * @param fec
     * (Calendar) Realization date. Must be a non null Calendar object.
     * (PRECOND: Must be checked by the caller).
     * @param cantReal
     * (double) Quantity done. Must be a non negative real value.
     * (PRECOND: Must be checked by the caller).
     * @param obs
     * (String) Observations. Could be null or empty (Max. 400 chars).
     * (PRECOND: Must be checked by the caller).
     * @param cierraEvento
     * (boolean) Indicates if this entry finishes the event.
     */
    public void actualizar(Calendar fec, double cantReal, String obs, boolean cierraEvento)
    throws AppLayerException{
        if(isCancelado() || isCerrado())
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_ACTIVE);
        this.hitos.add(new Hito(fec,  cantReal, obs, cierraEvento));
        if(cierraEvento)
            super.cerrar(fec);
        this.cantActual += cantReal;
        super.setChanged();
    }

    /**
     * Adds a new fact in the event. It is used to reconstruct objects from data storage. This
     * method doesn't set the Event as modified.
     * @param h
     * (Hito) Fact to be added. It must be a fully initialized Hito non null object, with assigned
     * id. (PRECOND: This condition must be checked by the caller)
     */
    public void actualizar(Hito h){
        this.hitos.add(h);
        if(h.isCierraEvento())
            try{
                cerrar(h.getFecReal());
            } catch(AppLayerException ex){

            }
        this.cantActual += h.getCantReal();
    }

    /**
     * Cancels the current event. Once canceled this event will be taken as if has not been
     * registered.
     */
    public void cancelar() throws AppLayerException {
        super.cancelar();
        this.hitos.add(new Hito(Calendar.getInstance(), 0.0,
                "--> Cancelación evento.", true));
        this.setChanged();
    }

    public double getPorcAvance(){
        double porc = 0.0;
        if(!(this.cantActual == 0.0))
            porc = this.cantActual / this.cantPlaneada;
        return porc;
    }

    public boolean isCompleted(){
        return getPorcAvance() == 1.0;
    }


}
