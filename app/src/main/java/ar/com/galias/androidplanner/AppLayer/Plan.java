package ar.com.galias.androidplanner.AppLayer;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Abstract class representing a generic plan that could be either a task or a routine.
 */

public abstract class Plan extends Mappeable {
    private long id;
    private String titulo;
    private String descripcion;
    private boolean activo;
    private Categoria categ;


    /**
     * Contructor. Creates a new plan.
     * @param titulo
     * (String) Plan's title. Must be a non null/empty string (Max. 25 chars).
     * @param descripcion
     * (String) Plan's description. Could be an empty or null String (Max. 200 chars).
     * @param categ
     * (Categoria). Plan's associated category. Must be a non null existing category.
     * @throws AppLayerException
     * If any of the detailed constraints is not satisfied, an exception will be thrown.
     */
    public Plan(int classID, String titulo, String descripcion, Categoria categ)
    throws AppLayerException {
        super(classID);
        super.setCreated();
        if(titulo == null || titulo.isEmpty())
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_NULL);
        if(titulo.length() > 25)
            throw new  AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(descripcion != null && descripcion.length() > 200)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(categ == null)
            throw new AppLayerException(AppLayerException.ERR_CODE_NULL_CATEG);
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categ = categ;
        this.activo = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public Categoria getCateg() {
        return categ;
    }

    /**
     * Modifies description attribute.
     * @param descripcion
     * (String) New description. Could be an empty or null String (Max. 200 chars).
     * (PRECOND: Must be checked by the caller).
     */
    public void changeDescripcion(String descripcion){
        this.descripcion = descripcion;
        super.setChanged();
    }

    /**
     * Modifies category attribute.
     * @param categ
     * (Categoria) New category. Must be a non null category.
     * (PRECOND: Must be checked by the caller).
     */
    public void changeCategory(Categoria categ){
        this.categ = categ;
        super.setChanged();
    }

    /**
     * Makes this plan no longer active.
     * @throws AppLayerException
     * If the plan is already cancelled, exception will be thrown.
     */
    public void cancelar() throws AppLayerException {
        if(!isActivo())
            throw new AppLayerException(AppLayerException.ERR_CODE_ALREADY_CANC);
        this.activo = false;
        super.setChanged();
    }
}
