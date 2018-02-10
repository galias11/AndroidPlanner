package ar.com.galias.androidplanner.GUI;

import java.util.Calendar;

/**
 * Created by usuario on 29/01/2018.
 */

public interface Iface_view_task_screen extends Iface_screen {

    void setCurrent_task_ID(long id);
    long getCurrent_task_ID();

    void setTitle(String title);
    void setDescription(String description);
    void setProgress(int progress);

    void addEvent(long id, String title, String desc, Calendar plannedDate, boolean isCancelled,
                  boolean isClosed, boolean isCompleted, int progress);
    void clearScreen();
    void clearTasks();
}
