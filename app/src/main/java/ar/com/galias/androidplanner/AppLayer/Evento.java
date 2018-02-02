package ar.com.galias.androidplanner.AppLayer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Abstract class thar represents a generic event. It could be either a task event or a routine
 * event.
 */

public abstract class Evento extends Mappeable implements Comparable{
    private long id;
    private String titulo;
    private String desc;
    private Calendar fecPlan;
    private int ud_frec_notif;
    private int cant_frec_notif;
    private boolean cerrado;
    private Calendar fecCierre;
    private boolean cancelado;

    public static final int FREC_NOTIF_TYPE_DAY = 0;
    public static final int FREC_NOTIF_TYPE_WEEK = 1;
    public static final int FREC_NOTIF_TYPE_MONTH = 2;
    public static final int FREC_NOTIF_TYPE_YEAR = 3;

    /**
     * Constructor. Creates a new Event.
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
    public Evento(String titulo, String desc, Calendar fecPlan, int ud_frec_notif,
                  int cant_frec_notif){
        super(Mappeable.ID_EVENTO);
        this.titulo = titulo;
        this.desc = desc;
        this.fecPlan = fecPlan;
        this.ud_frec_notif = ud_frec_notif;
        this.cant_frec_notif = cant_frec_notif;
        this.cerrado = false;
        this.cancelado = false;
        this.fecCierre = null;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesc() {
        return desc;
    }

    public Calendar getFecPlan() {
        return fecPlan;
    }

    public int getUd_frec_notif() {
        return ud_frec_notif;
    }

    public int getCant_frec_notif() {
        return cant_frec_notif;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public Calendar getFecCierre() {
        return fecCierre;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setId(long id){ this.id = id; }

    /**
     * Closes this event.
     * @param fecha
     * (Calendar) Must be a non null Calendar (date).
     * (PRECOND: Must be checked by the caller).
     */
    public void cerrar(Calendar fecha){
        this.fecCierre = fecha;
        this.cerrado = true;
        super.setChanged();
    }

    /**
     * Changes event's notification parameters.
     * @param ud_frec_notif
     * (int) Event's notification frequency unit. Must be one of class FREC_NOTIF_TYPE constants.
     * (PRECOND: Must be checked by the caller).
     * @param cant_frec_notif
     * (int) Event's notification quantity. Multiplies the unit to get notification time.
     * (PRECOND: Must be checked by the caller).
     */
    public void cambiarNotif(int ud_frec_notif, int cant_frec_notif){
        this.ud_frec_notif = ud_frec_notif;
        this.cant_frec_notif = cant_frec_notif;
        super.setChanged();
    }

    /**
     * Cancels the event. From now on, it will be treated as if it was not registered.
     * @throws AppLayerException
     * If this event is already cancelled, exception will be thrown.
     */
    public void cancelar() throws AppLayerException {
        if(this.cancelado = true)
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        this.cancelado = true;
        super.setChanged();
    }

    /**
     * This function indicates if the event must be notified to the user.
     * @return
     * (boolean) True indicates that this event has to be notified to the user. Otherwise, it
     * doesn't have to be notified.
     */
    public boolean hasToNotify(){
        Calendar today = GregorianCalendar.getInstance();
        Calendar min_date = fecPlan;
        switch(ud_frec_notif){
            case FREC_NOTIF_TYPE_DAY:
                min_date.add(Calendar.DAY_OF_YEAR, - cant_frec_notif);
                break;
            case FREC_NOTIF_TYPE_WEEK:
                min_date.add(Calendar.WEEK_OF_YEAR, - cant_frec_notif);
                break;
            case FREC_NOTIF_TYPE_MONTH:
                min_date.add(Calendar.MONTH, - cant_frec_notif);
                break;
            case FREC_NOTIF_TYPE_YEAR:
                min_date.add(Calendar.YEAR, - cant_frec_notif);
                break;
            default:
                break;
        }
        return !(today.before(min_date));
    }





    @Override
    public boolean equals(Object o){
        boolean eq = false;
        if(o != null && o.getClass() == this.getClass() && ((Evento)o).getId() == getId())
            eq = true;
        return eq;
    }

    @Override
    public int compareTo(Object o){
        int cmp = 0;
        if(o == null)
            cmp = 1;
        else {
            if(o.getClass() == this.getClass()){
                if(((Evento)o).getFecPlan().before(fecPlan))
                    cmp = 1;
                else if(((Evento)o).getFecPlan().after(fecPlan))
                    cmp = -1;
                else
                    cmp = (int) (id - ((Evento)o).getId());
            }
        }
        return cmp;
    }

}
