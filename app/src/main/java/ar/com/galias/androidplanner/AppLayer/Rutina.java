package ar.com.galias.androidplanner.AppLayer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Class representing a routine in the system.
 */

public class Rutina extends Plan {
    private int frec_tipo;
    private int frec_cant;
    private int ud_tiempo_notif;
    private int cant_tiempo_notif;
    private TreeSet<EventoRutina> eventos;


    public static final int FREC_TYPE_DAY = 0;
    public static final int FREC_TYPE_WEEK = 1;
    public static final int FREC_TYPE_MONTH = 2;
    public static final int FREC_TYPE_YEAR = 3;

    /**
     * Constructor. Creates a new routine and inserts the first event in it.
     * @param titulo
     * (String) Plan's title. Must be a non null/empty string (Max. 25 chars).
     * @param descripcion
     * (String) Plan's description. Could be an empty or null String (Max. 200 chars).
     * @param categ
     * (Categoria) Plan's associated category. Must be a non null existing category.
     * @param frec_tipo
     * (int) Routine redo frequency. Must be one of FREC_TYPE.
     * @param frec_cant
     * (int) Routine redo time quantity. The unit depends on de frec_tipo attribute. Must be an
     * integer value higher than 1.
     * @param fec_inicial
     * (Calendar) Date for the first routine event. Must be a non null Calendar object.
     * @param ud_tiempo_notif
     * (int) Routine's default notification time unit for its active event. Must be a valid
     * frequency type.
     * @param cant_tiempo_notif
     * (int) Routine's default notification time quantity for its active event. Must be a
     * integer value higher than 0.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    public Rutina(String titulo, String descripcion, Categoria categ,
                  int frec_tipo, int frec_cant, Calendar fec_inicial, int ud_tiempo_notif,
                  int cant_tiempo_notif) throws AppLayerException{
        this(titulo, descripcion, categ, frec_tipo, frec_cant, ud_tiempo_notif, cant_tiempo_notif);
        if(fec_inicial == null)
            throw  new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        agregarEvento(fec_inicial);
    }

    /**
     * Creates a new routine. This constructor doesn't initialize the newly created routine.
     * Designed to be used by the creation mappers.
     * @param titulo
     * (String) Plan's title. Must be a non null/empty string (Max. 25 chars).
     * @param descripcion
     * (String) Plan's description. Could be an empty or null String (Max. 200 chars).
     * @param categ
     * (Categoria) Plan's associated category. Must be a non null existing category.
     * @param frec_tipo
     * (int) Routine redo frequency. Must be one of FREC_TYPE.
     * @param frec_cant
     * (int) Routine redo time quantity. The unit depends on de frec_tipo attribute. Must be an
     * integer value higher than 1.
     * @param ud_tiempo_notif
     * (int) Routine's default notification time unit for its active event. Must be a valid
     * frequency type.
     * @param cant_tiempo_notif
     * (int) Routine's default notification time quantity for its active event. Must be a
     * integer value higher than 0.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    public Rutina(String titulo, String descripcion, Categoria categ,
                  int frec_tipo, int frec_cant, int ud_tiempo_notif,
                  int cant_tiempo_notif) throws AppLayerException{
        super(Mappeable.ID_RUTINA, titulo, descripcion, categ);
        if(frec_tipo < 0 || frec_tipo >3)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_FREC_TYPE);
        if(frec_cant < 1)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        if(ud_tiempo_notif < 0 || ud_tiempo_notif > 3)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_FREC_TYPE);
        if(cant_tiempo_notif <= 0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        this.frec_tipo = frec_tipo;
        this.frec_cant = frec_cant;
        this.ud_tiempo_notif = ud_tiempo_notif;
        this.cant_tiempo_notif = cant_tiempo_notif;
        this.eventos = new TreeSet<EventoRutina>();
    }

    public int getFrec_tipo() {
        return frec_tipo;
    }

    public int getFrec_cant() {
        return frec_cant;
    }

    public int getUd_tiempo_notif() {
        return ud_tiempo_notif;
    }

    public int getCant_tiempo_notif() {
        return cant_tiempo_notif;
    }

    public TreeSet<EventoRutina> getEventos() {
        return eventos;
    }

    /**
     * Inserts a new event in the routine's event list. It only requires a new planned date,
     * the other information is retrieved from the routine data.
     * @param fecRealizacion
     * (Calendar) Planned realization date for the new event. Must be a non null Calendar object.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    private void agregarEvento(Calendar fecRealizacion) throws AppLayerException{
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        if(fecRealizacion == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        this.eventos.add(new EventoRutina(this.getTitulo(), this.getDescripcion(),
                fecRealizacion, this.ud_tiempo_notif, this.cant_tiempo_notif));
        super.setChanged();
    }

    /**
     * Inserts a new event. The event must be fully initialized. This method should be only used
     * to load a routine form data storage. The routine is not set as changed.
     * @param e
     * (EventoRutina) Event to be added. Must be a non null, fully initialized event.
     */
    public void agregarEvento(EventoRutina e) {
        this.eventos.add(e);
    }

    /**
     * Calculates the next for the event from a base date. It uses frec_tipo and frec_cant
     * to determine the type and quantity to add.
     * @param fecha_base
     * (Calendar) Base date to calculate the next one. Must be a non null Calendar object.
     * @return
     * (Calendar) Calculated next date.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    private Calendar calcularProximaFecha(Calendar fecha_base) throws AppLayerException{
        if(fecha_base == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        Calendar proximaFecha = fecha_base;
        switch(this.frec_tipo){
            case FREC_TYPE_DAY:
                proximaFecha.add(Calendar.DAY_OF_YEAR, this.frec_cant);
                break;
            case FREC_TYPE_WEEK:
                proximaFecha.add(Calendar.WEEK_OF_YEAR, this.frec_cant);
                break;
            case FREC_TYPE_MONTH:
                proximaFecha.add(Calendar.MONTH, this.frec_tipo);
                break;
            case FREC_TYPE_YEAR:
                proximaFecha.add(Calendar.YEAR, this.frec_tipo);
                break;
            default:
                break;
        }
        return proximaFecha;
    }


    /**
     * Updates the routine. It sets the active event as completed and creates a new active event.
     * @param fecRealizacion
     * (Calendar) Current active event's realization date. It must be a non null Calendar object.
     * @param obs
     * (String) Current active event's observations. Could be an empty/null string (Max. 400 chars).
     * @param mantieneFecha
     * (boolean) If enabled, the next planned date will be calculated from the current active
     * event's planned date. Otherwise, it will be calculated from realization date.
     * @throws AppLayerException
     * If any of the restrictions detailed above or the desired event does not exist an exception
     * is thrown.
     */
    public void actualizar(Calendar fecRealizacion, String obs, boolean mantieneFecha)
    throws AppLayerException{
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        if(fecRealizacion == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_DATE);
        if(obs != null && obs.length() > 400)
            throw  new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        EventoRutina activeEvent = this.eventos.first();
        activeEvent.setRealizado(obs, fecRealizacion);
        Calendar proxima_fecha = calcularProximaFecha(
                (mantieneFecha ? calcularProximaFecha(activeEvent.getFecPlan())
                               : calcularProximaFecha(fecRealizacion))
        );
        agregarEvento(proxima_fecha);
        super.setChanged();
    }

    public void cancelar() throws AppLayerException {
        super.cancelar();
        EventoRutina activeEvent = this.eventos.first();
        activeEvent.cancelar();
    }

    public void reanudar(Calendar fecPlaneada, int frec_tipo, int frec_cant) throws AppLayerException{
        if(isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_CANC);
        this.frec_tipo = frec_tipo;
        this.frec_cant = frec_cant;
        super.setChanged();
    }

    /**
     * Checks if the planned date of this routine's active event is passed.
     * @return
     * (boolean) returns true if the planned date is passed.
     */
    public boolean vencida(){
        Calendar now = GregorianCalendar.getInstance();
        return this.eventos.first().getFecPlan().before(now);
    }

    /**
     * Returns planned date.
     * @return
     * (Calendar) Active event's planned date.
     */
    public Calendar primerVencimiento(){
        return this.eventos.first().getFecPlan();
    }

    public void modif_notif_time(int ud_tiempo_notif, int cant_tiempo_notif)
    throws AppLayerException{
        if(ud_tiempo_notif < 0 || ud_tiempo_notif > 3)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_FREC_TYPE);
        if(cant_tiempo_notif <= 0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        this.ud_tiempo_notif = ud_tiempo_notif;
        this.cant_tiempo_notif = cant_tiempo_notif;
        super.setChanged();
    }
}
