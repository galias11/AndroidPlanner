package ar.com.galias.androidplanner.GUI;

/**
 * Created by usuario on 26/01/2018.
 */

public interface Iface_new_task_screen extends Iface_screen{
    String getTaskTitle();
    String getTaskDescription();
    String getCategoria();
    void clearScreen();
    void refresh();
}
