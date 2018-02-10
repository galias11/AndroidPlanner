package ar.com.galias.androidplanner.AppLayer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Class representing a task in the system.
 */

public class Tarea extends Plan {
    private double avanc;
    private HashMap<Long, EventoTarea> eventos;

    protected static final int NOTIF_UD_DAY = 0;
    protected static final int NOTIF_UD_WEEK = 1;
    protected static final int NOTIF_UD_MONTH = 2;
    protected static final int NOTIF_UD_YEAR = 3;

    /**
     * Constructor. Creates a new Task.
     * @param titulo
     * (String) Task's title. Must be a non null/empty string (Max. 25 chars).
     * @param descripcion
     * (String) Task's description. Could be a null/empty string (Max. 200 chars.)
     * @param categ
     * (Categoria) Task's current category. It must be a non null existing category.
     */
    public Tarea(String titulo, String descripcion, Categoria categ)
            throws AppLayerException {
        super(Mappeable.ID_TAREA, titulo, descripcion, categ);
        this.eventos = new HashMap<Long, EventoTarea>();
        actualizarAvance();
    }

    public double getAvanc(){
        return avanc;
    }

    public HashMap<Long, EventoTarea> getEventos() {
        return eventos;
    }


    /**
     * Creates a new event in the task.
     * @param titulo
     * (String) Event's title. Must be a non null/empty string (Max. 25 chars.)
     * @param desc
     * (String) Event's description. Could be a null or empty string (Max. 200 chars).
     * @param fecPlan
     * (Calendar) Event's planned realization date. Must  be a non null Calendar object.
     * @param cantPlan
     * (double) Planned quantity (quantity to be done). Must be a real value higher than 0.0.
     * @param udMedida
     * (String) Meassurement unit for the planned quantity. Must be non null/empty string (Max.
     * 2 chars.)
     * @param ponderacion
     * (int) Determines this event's weight in the overall value for the task completion percentage.
     * Must be a integer value higher than 0.
     * @param ud_frec_notif
     * (int) Determines the unit for the time lapse in which the event must be notified to the user.
     * Must be one of FREC_TYPE constant values.
     * @param cant_frec_notif
     * (int) Determines the quantity for the time lapse in which the event must be notified to
     * the user. Must be a integer value higher than 0.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    public void agregarEvento(String titulo, String desc, Calendar fecPlan, double cantPlan,
                              String udMedida, int ponderacion, int ud_frec_notif,
                              int cant_frec_notif) throws AppLayerException {
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        if(titulo == null || titulo.isEmpty())
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_NULL);
        if(titulo.length() > 25)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(desc != null && desc.length() > 200)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(fecPlan == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        if(cantPlan <= 0.0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_REAL_H0);
        if(udMedida == null || udMedida.isEmpty())
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_NULL);
        if(udMedida.length() > 2)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(ponderacion <= 0)
            throw  new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        if(ud_frec_notif < 0 || ud_frec_notif > 3)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_FREC_TYPE);
        if(cant_frec_notif <= 0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        EventoTarea new_evento = new EventoTarea(titulo, desc, fecPlan, ud_frec_notif,
                cant_frec_notif, cantPlan, udMedida, ponderacion);
        this.eventos.put((long) -1, new_evento);
        actualizarAvance();
        super.setChanged();
    }

    /**
     * Creates a new event in the task. Used to reconstruct tasks from data storage. It doesn't
     * mark this task as modified.
     * @param evento
     * (EventoTarea) Event to be added. It must be a full initialized no null event, with
     * assigned id. (PRECOND: Must be checked by caller)
     */
    public void agregarEvento(EventoTarea evento) {
        this.eventos.put(evento.getId(), evento);
        actualizarAvance();
        super.setChanged();
    }


    /**
     * Stores changes of an event's task.
     * @param id
     * (long) Task's event id. It must exist.
     * @param cantReal
     * (double) Quantity that has been done. Must be a non negative real value.
     * @param fecReal
     * (Calendar) Realization date. Must be a non null Calendar object.
     * @param obs
     * (String) Observations. Could be a null or empty string (Max. 400 chars.)
     * @param cierre
     * (boolean) Indicates if the event has to be closed.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    public void actualizarEvento(long id, double cantReal, Calendar fecReal, String obs,
                                 boolean cierre) throws AppLayerException {
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        if(!this.eventos.containsKey(id))
            throw new AppLayerException(AppLayerException.ERR_CODE_NON_EXISTENT_EVENT);
        if(!(cantReal >= 0.0))
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_REAL_HE0);
        if(fecReal == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        if(obs != null && obs.length() > 400)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        EventoTarea currentEvent = this.eventos.get(id);
        currentEvent.actualizar(fecReal, cantReal, obs, cierre);
        actualizarAvance();
        super.setChanged();
    }

    /**
     * Set avanc attribute to an actual value. It evaluates every event's task.
     */
    private void actualizarAvance(){
        double pond_total = 0.0;
        double pond_acum = 0.0;
        Iterator<EventoTarea> it = this.eventos.values().iterator();
        while(it.hasNext()){
            EventoTarea e = it.next();
            if(!e.isCancelado()) {
                pond_total += e.getPonderacion();
                pond_acum += e.getPonderacion() * e.getPorcAvance();
            }
        }
        this.avanc = pond_total == 0.0 ? 0.0 : pond_acum / pond_total;
        super.setChanged();
    }

    /**
     * Cancels the actual task. Additionally, cancels every task's event that's not already
     * closed or cancelled.
     * @throws AppLayerException
     * If this task is already closed, this method will throw an exception.
     */
    public void cancelar() throws AppLayerException {
        if(!this.isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        Iterator<EventoTarea> it = this.eventos.values().iterator();
        while(it.hasNext()){
            EventoTarea e = it.next();
            if(!e.isCancelado())
                e.cancelar();
        }
        super.cancelar();
        super.setChanged();
    }


    public void cancelarEvento(long idEvento) throws  AppLayerException{
        EventoTarea e = eventos.get(idEvento);
        if(e == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        e.cancelar();
        setChanged();
    }

    /**
     * Indicates whether the task is pending or not.
     * @return
     * (boolean) Returns true if the task is not closed and not completed.
     */
    public boolean pendiente(){
        return isActivo() && avanc < 1.0;
    }

    /**
     * Indicates if the task has any event which's planned date is passed.
     * @return
     * (boolean) true if it has any passed planned date, false otherwise.
     */
    public boolean tieneEventosVencidos(){
        Iterator<EventoTarea> itEvento = this.eventos.values().iterator();
        Calendar now = GregorianCalendar.getInstance();
        boolean tieneVenc = false;
        while(!tieneVenc && itEvento.hasNext()){
            EventoTarea e = itEvento.next();
            if(!e.isCerrado() && !e.isCancelado())
                tieneVenc = e.getFecPlan().before(now);
        }
        return tieneVenc;
    }

    /**
     * Returns the closest pending event's planned date.
     * @return
     * (Calendar) Closest pending event''s planned date.
     */
    public Calendar primerVencimiento(){
        Iterator<EventoTarea> itEvento = this.eventos.values().iterator();
        TreeSet<Calendar> date_set = new TreeSet<Calendar>();
        while(itEvento.hasNext()){{
            EventoTarea e = itEvento.next();
            if(!e.isCancelado() && !e.isCerrado())
                date_set.add(e.getFecPlan());
        }}
        if(date_set.isEmpty())
            return null;
        return date_set.first();
    }



    /**
     * Modifies notification time of a selected task's event.
     * @param idEvento
     * (long) Selected event's ID. Must be a non negative integer value corresponding to an
     * existing event.
     * @param ud_tiempo_notif
     * (int) New notification time unit. Must be one of the available frequency types.
     * @param cant_tiempo_notif
     * (int) New notification time quantity. Must be a non negative integer value higher than cero.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will bew thrown.
     */
    public void mdificarTiempoNotifEvento(long idEvento, int ud_tiempo_notif, int cant_tiempo_notif)
        throws AppLayerException {
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        if(!this.eventos.containsKey(idEvento))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        if(ud_tiempo_notif < 0 || ud_tiempo_notif > 3)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_FREC_TYPE);
        if(cant_tiempo_notif <= 0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        EventoTarea e = this.eventos.get(idEvento);
        e.cambiarNotif(ud_tiempo_notif, cant_tiempo_notif);
        super.setChanged();
    }

}
