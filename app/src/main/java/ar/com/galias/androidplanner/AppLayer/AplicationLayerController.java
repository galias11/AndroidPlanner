package ar.com.galias.androidplanner.AppLayer;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;
import ar.com.galias.androidplanner.PersistencyController.PersistencyController;
import ar.com.galias.androidplanner.PersistencyController.PersistencyException;
import ar.com.galias.androidplanner.PersistencyController.SQLiteAndroidConn;

/**
 * Created by @galias on 8/01/18.
 * Entry point for AndroidPlanner aplication layer.
 */

public class AplicationLayerController implements Serializable{
    private HashMap<Long, Rutina> rutinas;
    private HashMap<Long, Tarea> tareas;
    private HashMap<String, Categoria> categorias;
    private PersistencyController persistency_controller;

    public static final int FILTER_KEY_CATEG = 0;
    public static final int FILTER_KEY_FINIHED = 1;
    public static final int FILTER_KEY_PENDING = 2;
    public static final int FILTER_KEY_PASSED = 3;

    public static final int ORDER_KEY_TITLE = 0;
    public static final int ORDER_KEY_DATE = 1;
    public static final int ORDER_AVANC = 2;

    public AplicationLayerController(Context appContext) throws PersistencyException{
        persistency_controller = new PersistencyController(new SQLiteAndroidConn(appContext));
        rutinas = new HashMap<Long, Rutina>();
        tareas = new HashMap<Long, Tarea>();
        categorias = new HashMap<String, Categoria>();
        cargar_categorias();
        cargar_rutinas();
        cargar_tareas();
    }

    /**
     * Loads routines from data storage.
     */
    private void cargar_rutinas() throws PersistencyException{
        Iterator<Mappeable> it = persistency_controller.get(Mappeable.ID_RUTINA, null).iterator();
        while(it.hasNext()){
            Rutina r = (Rutina) it.next();
            this.rutinas.put(r.getId(), r);
        }
    }

    /**
     * Loads tasks from data storage.
     */
    private void cargar_tareas() throws PersistencyException{
        Iterator<Mappeable> it = persistency_controller.get(Mappeable.ID_TAREA, null).iterator();
        while(it.hasNext()){
            Tarea t = (Tarea) it.next();
            this.tareas.put(t.getId(), t);
        }
    }

    /**
     * Load categories from data storage.
     */
    private void cargar_categorias() throws PersistencyException{
        Iterator<Mappeable> it = persistency_controller.get(Mappeable.ID_CATEGORIA, null).iterator();
        while(it.hasNext()){
            Categoria c = (Categoria) it.next();
            this.categorias.put(c.getNombre(), c);
        }
    }



    /**
     * Creates a new routine, adds it to the routine list and saves it to data storage.
     * @param titulo
     * (String) Plan's title. Must be a non null/empty string (Max. 25 chars).
     * @param desc
     * (String) Plan's description. Could be an empty or null String (Max. 200 chars).
     * @param categoria
     * (Categoria) Plan's associated category. Must be a non null existing category.
     * @param frec_param
     * (int) Routine redo frequency. Must be one of FREC_TYPE.
     * @param frec_rate
     * (int) Routine redo time quantity. The unit depends on de frec_tipo attribute. Must be an
     * integer value higher than 1.
     * @param fecha_inic
     * (Calendar) Date for the first routine event. Must be a non null Calendar object.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public Rutina crearRutina(String titulo, String desc, int frec_param, int frec_rate,
                            Calendar fecha_inic, Categoria categoria, int ud_tiempo_notif,
                            int cant_tiempo_notif)
    throws AppLayerException, PersistencyException {
        if(categoria == null || !categorias.containsKey(categoria.getNombre()))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Rutina new_rutina = new Rutina(titulo, desc, categoria, frec_param,
                frec_rate, fecha_inic, ud_tiempo_notif, cant_tiempo_notif);
        this.rutinas.put(new_rutina.getId(), new_rutina);
        persistency_controller.save(new_rutina);
        return new_rutina;
    }

    /**
     * Updates the selected routine (idRutina). Set its active event finished and opens a new
     * active event. Finally, it saves everything to the data storage.
     * @param idRutina
     * (long) Indicates the routine to be updated. It must refer to a existing routine id.
     * @param fechaRealizacion
     * (Calendar) Date in which the previous active event was finished. It must be a non null
     * Calendar object.
     * @param obs
     * (String) Observations on the completion of the previous active event. Could be a null or
     * empty string (Max. 400 chars.)
     * @param conservarFecha
     * (boolean) If enabled, the next event will have a planned date related to the previous
     * active event planned date. Otherwise, this date will be related to realization date.
     * @throws AppLayerException
     * If any of the params constraints is not satisfied, an exception will be thrown.
     */
    public void actualizarRutina(long idRutina, Calendar fechaRealizacion, String obs,
                                 boolean conservarFecha)
    throws AppLayerException, PersistencyException{
        if(!rutinas.containsKey(idRutina))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Rutina selected_routine = this.rutinas.get(idRutina);
        selected_routine.actualizar(fechaRealizacion, obs, conservarFecha);
        persistency_controller.edit(selected_routine);
    }

    /**
     * Cancels the selected routine and saves changes to the data storage.
     * @param idRutina
     * (long) ID of the to be cancelled routine.
     * @throws AppLayerException
     * If the id doesn't point to an existing routine an exception will be thrown.
     */
    public void cancelarRutina(long idRutina) throws AppLayerException, PersistencyException {
        if(!rutinas.containsKey(idRutina))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Rutina selected_routine = this.rutinas.get(idRutina);
        selected_routine.cancelar();
        persistency_controller.edit(selected_routine);
    }

    /**
     * Creates a new category and inserts it on the categories list Finally, it saves changes
     * to data storage.
     * @param nombre
     * (String) Category's title. It must be a non null/empty string (Max. 25 chars).
     * @param desc
     * (String) Category's description. It could be a null or empty string (Max. 200 chars).
     * @throws AppLayerException
     * If any of the detailed constraints is not checked or a category with the same names
     * is already registered an exception will be thrown.
     */
    public void crearCategoria(String nombre, String desc) throws AppLayerException, PersistencyException{
        if(categorias.containsKey(nombre))
            throw new AppLayerException(AppLayerException.ERR_CODE_ITEM_EXISTS);
        Categoria new_categ = new Categoria(nombre, desc);
        this.categorias.put(new_categ.getNombre(), new_categ);
        persistency_controller.save(new_categ);

    }

    /**
     * Creates a new task, adds it to the task list and saves it to the data storage.
     * @param titulo
     * (String) Task's title. Must be a non null/empty string (Max. 25 chars).
     * @param desc
     * (String) Task's description. Could be a null or empty string (Max. 200 chars)
     * @param categ
     * (Categoria) Task's category. Must be a non null existing category.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied an exception will be thrown.
     */
    public Tarea crearTarea(String titulo, String desc, Categoria categ)
            throws AppLayerException, PersistencyException{
        if(categ == null || !categorias.containsKey(categ.getNombre()))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Tarea new_tarea = new Tarea(titulo, desc, categ);
        this.tareas.put(new_tarea.getId(), new_tarea);
        persistency_controller.save(new_tarea);
        return new_tarea;
    }

    /**
     * Adds a new event to a selected task and saves changes to the data storage.
     * @param idTarea
     * (long) Target task's ID. Must be non negative integer value related to an existing task ID.
     * @param titulo
     * (String) New event's title. Must be a non empty/null string (Max. 25 chars).
     * @param desc
     * (String) New event's description. Could be a null or empty string (Max. 200 chars).
     * @param fecPlan
     * (Calendar) New event's planned date. Must be a non null Calendar object.
     * @param cant
     * (double) New event's target quantity. Must be a real value higher than 0.0.
     * @param udMedida
     * (String) Meassurement unit for the target's quantity. Must be a non null/empty string
     * (Max. 2 chars)
     * @param ponder
     * (int) Event's weight on its current task. Must be an integer value higher than 0.
     * @param ud_frec_notif
     * (int) Event's notification time unit. Must be one of the available notification units.
     * @param cant_frec_notif
     * (int) Events notification time quantity. Must be an integer positive value.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied an exception will be thrown.
     */
    public void agregarEventoTarea(long idTarea, String titulo, String desc, Calendar fecPlan,
                                   double cant, String udMedida, int ponder, int ud_frec_notif,
                                   int cant_frec_notif)
            throws AppLayerException, PersistencyException {
        if(!this.tareas.containsKey(idTarea))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Tarea modified_task = this.tareas.get(idTarea);
        modified_task.agregarEvento(titulo, desc, fecPlan, cant, udMedida, ponder, ud_frec_notif,
                cant_frec_notif);
        persistency_controller.edit(modified_task);
    }

    /**
     * Cancels the selected task and saves changes to disk.
     * @param idTarea
     * (long) Task's ID. Must be a non negative integer value corresponding to an existing (not
     * closed o cancelled) task.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied an exception will be thrown.
     */
    public void cancelarTarea(long idTarea) throws AppLayerException, PersistencyException{
        if(!this.tareas.containsKey(idTarea))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Tarea modified_task = this.tareas.get(idTarea);
        modified_task.cancelar();
        persistency_controller.edit(modified_task);
    }

    /**
     * Updates a selected event at a selected task and saves all changes to data storage.
     * @param idTarea
     * (long) Target task's ID. Must be a non negative integer value corresponding to an existing
     * task.
     * @param idEvento
     * (long) Target event's ID. Must be a non negative integer value corresponding to an existing
     * event.
     * @param cantReal
     * (double) Done quantity. Must be a non negative real value.
     * @param fecReal
     * (Calendar) Realization date. Must be a non null Calendar object.
     * @param obs
     * (String) Observations. Could be an empty o null string (Max. 400 chars)
     * @param cierre
     * (boolean) If enabled, the event is given for finished.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public void actualizarEventoTarea(long idTarea, long idEvento, double cantReal,
                                      Calendar fecReal, String obs, boolean cierre)
        throws AppLayerException, PersistencyException {
        if(!this.tareas.containsKey(idTarea))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Tarea modified_task = this.tareas.get(idTarea);
        modified_task.actualizarEvento(idEvento, cantReal, fecReal, obs, cierre);
        persistency_controller.edit(modified_task);
    }

    /**
     * Filters tasks list and returns a list of task satisfying the filter and order constraints.
     * @param filters
     * (boolean[]) Each value is a flag for one of the filters. Use FILTER_KEY constants to
     * access.
     * @param order
     * (int) Specifies the order criteria for the resulting set.
     * @param inv_order
     * (boolean) specifies if the order must be inverse.
     * @param categs
     * (ArrayList<String>) If category filter flag is on it indicates the desired categories. If
     * this flag is enabled this parameter can't be null or empty.
     * @return
     * (ArrayList<Tarea>) Returns a list of task filtered and ordered according to the input
     * parameters.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public ArrayList<Tarea> getTareas(boolean[] filters, int order, final boolean inv_order,
                                      ArrayList<String> categs)
    throws AppLayerException{
        if(filters[AplicationLayerController.FILTER_KEY_CATEG] && (categs == null || categs.isEmpty()))
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_CATEG);
        Iterator<Tarea> itTarea = this.tareas.values().iterator();
        ArrayList<Tarea> tareasList = new ArrayList<Tarea>();
        while(itTarea.hasNext()){
            Tarea t = itTarea.next();
            boolean filter01 = !filters[AplicationLayerController.FILTER_KEY_CATEG]
                    || categs.contains(t.getCateg().getNombre());
            boolean filter02 = !filters[AplicationLayerController.FILTER_KEY_PASSED]
                    || t.tieneEventosVencidos();
            boolean filter03 = !filters[AplicationLayerController.FILTER_KEY_PENDING]
                    || t.pendiente();
            boolean filter04 = !filters[AplicationLayerController.FILTER_KEY_FINIHED]
                    || !t.isActivo();
            if(filter01 && filter02 && filter03 && filter04)
                tareasList.add(t);
        }
        bublesortTareas(tareasList, order, inv_order);
        return tareasList;
    }



    /**
     * Filters routines list and returns a list of task satisfying the filter and order constraints.
     * @param filters
     * (boolean[]) Each value is a flag for one of the filters. Use FILTER_KEY constants to
     * access.
     * @param order
     * (int) Specifies the order criteria for the resulting set.
     * @param inv_order
     * (boolean) specifies if the order must be inverse.
     * @param categs
     * (ArrayList<String>) If category filter flag is on it indicates the desired categories. If
     * this flag is enabled this parameter can't be null or empty.
     * @return
     * (ArrayList<Rutina>) Returns a list of task filtered and ordered according to the input
     * parameters.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public ArrayList<Rutina> getRutinas(boolean[] filters, int order, final boolean inv_order,
                                      ArrayList<String> categs)
            throws AppLayerException{
        if(filters[AplicationLayerController.FILTER_KEY_CATEG] && (categs == null || categs.isEmpty()))
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_CATEG);
        Iterator<Rutina> itRutina = this.rutinas.values().iterator();
        ArrayList<Rutina> rutinasList = new ArrayList<Rutina>();
        while(itRutina.hasNext()){
            Rutina r = itRutina.next();
            boolean filter01 = !filters[AplicationLayerController.FILTER_KEY_CATEG]
                    || categs.contains(r.getCateg().getNombre());
            boolean filter02 = !filters[AplicationLayerController.FILTER_KEY_PASSED]
                    || r.vencida();
            boolean filter03 = !filters[AplicationLayerController.FILTER_KEY_PENDING]
                    || r.isActivo();
            boolean filter04 = !filters[AplicationLayerController.FILTER_KEY_FINIHED]
                    || !r.isActivo();
            if(filter01 && filter02 && filter03 && filter04)
                rutinasList.add(r);
        }
        bublesortRutinas(rutinasList, order, inv_order);
        return rutinasList;
    }

    /**
     * Modifies notification time for a event in a task.
     * @param idTarea
     * (long) Task's ID. Must be a non negative integer value (>0), corresponding to an existing task.
     * @param idEvento
     * (long) Event's ID. Must be a non negative integer value (>0), corresponding to an existing
     * event for the selected task.
     * @param frec_tipo
     * (int) New notification time unit.
     * @param cant_tipo
     * (int) New notification time quantity.
     * @throws AppLayerException
     * If any of the detailed constraint is not satisfied, an exception will be thrown.
     */
    public void modNotifTimeTarea(long idTarea, long idEvento, int frec_tipo, int cant_tipo)
    throws AppLayerException, PersistencyException{
        if(!this.tareas.containsKey(idTarea))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Tarea tareaSeleccionada = this.tareas.get(idTarea);
        tareaSeleccionada.mdificarTiempoNotifEvento(idEvento, frec_tipo, cant_tipo);
        persistency_controller.edit(tareaSeleccionada);
    }

    /**
     * Modifies notification time for a particular Routine.
     * @param idRutina
     * (long) Selected routine's ID. Must be a non negative integer value, corresponding to an
     * existing routine.
     * @param frec_tipo
     * (int) New notification time type. Must be a valid frequency type.
     * @param cant_tipo
     * (int) New notification time quantity. Must be an integer value higher than 0.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public void modNotifTimeRutina(long idRutina, int frec_tipo, int cant_tipo)
    throws AppLayerException, PersistencyException {
        if (!this.rutinas.containsKey(idRutina))
            throw new AppLayerException(AppLayerException.ERR_CODE_NOT_FOUND);
        Rutina rutinaSeleccionada = this.rutinas.get(idRutina);
        rutinaSeleccionada.modif_notif_time(frec_tipo, cant_tipo);
        persistency_controller.edit(rutinaSeleccionada);
    }

    public HashMap<String, Categoria> getCategorias(){
        return categorias;
    }

    public HashMap<Long, Tarea> getTareas(){ return tareas; }

    public HashMap<Long, Rutina> getRutinas() { return rutinas; }

    private void bublesortTareas(ArrayList<Tarea> planes, int orderKey, boolean inv_order) {
        int i;
        boolean changed = true;
        int len = planes.size();
        Tarea aux;
        while(changed) {
            changed = false;
            i = 0;
            while (i < len - 1){
                if(compare(planes.get(i), planes.get(i+1), orderKey, inv_order) > 0){
                    changed = true;
                    aux = planes.get(i);
                    planes.set(i, planes.get(i + 1));
                    planes.set(i + 1, aux);
                }
                i++;
            }
        }
    }

    private int compare(Tarea t1, Tarea t2, int orderKey, boolean invOrder){
        int cmp = 0;
        switch (orderKey){
            case AplicationLayerController.ORDER_AVANC:
                cmp = (int) (t1.getAvanc() - t2.getAvanc());
                break;
            case AplicationLayerController.ORDER_KEY_DATE:
                Calendar vencT1 = t1.primerVencimiento();
                Calendar vencT2 = t2.primerVencimiento();
                if(vencT1 == null && vencT2 == null)
                    cmp = compare(t1, t2, AplicationLayerController.ORDER_KEY_TITLE, invOrder);
                else if(vencT1 == null)
                    cmp = 1;
                else if(vencT2 == null)
                    cmp = -1;
                else
                    cmp = vencT1.compareTo(vencT2);
                break;
            case AplicationLayerController.ORDER_KEY_TITLE:
                cmp = t1.getTitulo().toLowerCase().compareTo(t2.getTitulo().toLowerCase());
                break;
            default:
                break;
        }
        return !invOrder ? cmp : -cmp;
    }

    private void bublesortRutinas(ArrayList<Rutina> planes, int orderKey, boolean inv_order) {
        int i;
        boolean changed = true;
        int len = planes.size();
        Rutina aux;
        while(changed) {
            changed = false;
            i = 0;
            while (i < len - 1){
                if(compare(planes.get(i), planes.get(i+1), orderKey, inv_order) > 0){
                    changed = true;
                    aux = planes.get(i);
                    planes.set(i, planes.get(i + 1));
                    planes.set(i + 1, aux);
                }
                i++;
            }
        }
    }

    private int compare(Rutina r1, Rutina r2, int orderKey, boolean invOrder){
        int cmp = 0;
        switch (orderKey){
            case AplicationLayerController.ORDER_KEY_DATE:
                Calendar vencT1 = r1.primerVencimiento();
                Calendar vencT2 = r2.primerVencimiento();
                if(vencT1 == null && vencT2 == null)
                    cmp = compare(r1, r2, AplicationLayerController.ORDER_KEY_TITLE, invOrder);
                else if(vencT1 == null)
                    cmp = 1;
                else if(vencT2 == null)
                    cmp = -1;
                else
                    cmp = vencT1.compareTo(vencT2);
                break;
            case AplicationLayerController.ORDER_KEY_TITLE:
                cmp = r1.getTitulo().toLowerCase().compareTo(r2.getTitulo().toLowerCase());
                break;
            default:
                break;
        }
        return !invOrder ? cmp : -cmp;
    }

}
