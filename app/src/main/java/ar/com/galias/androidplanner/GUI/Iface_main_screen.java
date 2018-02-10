package ar.com.galias.androidplanner.GUI;

/**
 * Created by usuario on 25/01/2018.
 */

public interface Iface_main_screen extends Iface_screen{
    void addTaskElement(long id, String title, String desc, String date, int progress);
    void clear_tasks();
}
