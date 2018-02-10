package ar.com.galias.androidplanner.AppLayer;

import java.util.Calendar;

/**
 * Created by @galias on 8/01/18.
 * Class representing a event for a routine in the system.
 */

public class EventoRutina extends Evento{
    private boolean realizado;
    private String obs;

    /**
     * Constructor. Calls Event (Superclass) constructor, and sets by default realizado to false,
     * and obs to null
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
     */
    public EventoRutina(String titulo, String desc, Calendar fecPlan,
                        int ud_frec_notif, int cant_frec_notif){
        super(titulo, desc, fecPlan, ud_frec_notif, cant_frec_notif);
        super.setCreated();
        realizado = false;
        obs = null;
    }

    /**
     * Constructor. This constructor is intended to be used by mappers, it doesn't mark object as
     * created.
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
     */
    public EventoRutina(long id, String titulo, String desc, Calendar fecPlan,
                        int ud_frec_notif, int cant_frec_notif, String obs, Calendar fecCierre,
                        boolean realizado, boolean cerrado, boolean cancelado){
        super(titulo, desc, fecPlan, ud_frec_notif, cant_frec_notif);
        this.setId(id);
        this.realizado = realizado;
        this.obs = obs;
        try {
            if (cerrado)
                super.cerrar(fecCierre);
            if (cancelado)
                super.cancelar();
        } catch (AppLayerException ex){

        }
    }

    public boolean isRealizado(){
        return realizado;
    }

    public String getObs(){
        return obs;
    }

    /**
     * Marks this event as successfully finished.
     * @param obs
     * (String) Event's observations. Could be a null/empty string (Max. 400 chars.)
     * (PRECOND: Must be checked by the caller).
     * @param fecRealizacion
     * (Calendar) Event's realization date. Must be a not null Calendar object.
     * (PRECOND: Must be checked by the caller).
     */
    public void setRealizado(String obs, Calendar fecRealizacion) throws
    AppLayerException{
        if(isCancelado() || isCerrado())
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_ACTIVE);
        this.realizado = true;
        super.cerrar(fecRealizacion);
    }

    @Override
    public void cancelar() throws  AppLayerException {
        super.cancelar();
        this.obs = "--> Cancelación de rutina.";
    }

}
