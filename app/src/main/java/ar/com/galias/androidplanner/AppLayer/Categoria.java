package ar.com.galias.androidplanner.AppLayer;

import ar.com.galias.androidplanner.PersistencyController.Mappeable;

/**
 * Created by @galias on 8/01/18.
 * Represents a category for a task or routine.
 */

public class Categoria extends Mappeable{
    private long id;
    private String nombre;
    private String desc;

    /**
     * Constructor. Creates a new category.
     * @param nombre
     * (String) Category's name. Must be a non null/empty String (Max. 25 chars).
     * @param desc
     * (String) Category's description. Could be an empty or null String (Max. 200 chars).
     */
    public Categoria(String nombre, String desc) throws AppLayerException{
        super(Mappeable.ID_CATEGORIA);
        super.setCreated();
        if(id < 0)
            throw new AppLayerException(AppLayerException.ERR_CODE_INV_VALUE_INT_H0);
        if(nombre == null || nombre.isEmpty())
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_NULL);
        if(nombre.length() > 25)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        if(desc != null && desc.length() > 200)
            throw new AppLayerException(AppLayerException.ERR_CODE_STRING_OUT_BONDS);
        this.id = id;
        this.nombre = nombre;
        this.desc = (desc != null ? desc : "");
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Modifies the category's description. It also sets the modified flag on.
     * @param desc
     * (String) New description. Could be an empty or null String (Max. 200 chars).
     * (PRECOND: Must be checked by the caller).
     */
    public void setDesc(String desc){
        this.desc = desc;
        super.setChanged();
    }

    public void setID(long id){
        this.id = id;
    }
}
