package ar.com.galias.androidplanner.GUI;

import java.util.Calendar;

/**
 * Created by usuario on 07/02/2018.
 */

public interface Iface_task_event_new_screen extends Iface_screen{
    String getTitle();
    String getDescription();
    Calendar getDate();
    int getPriority();
    int getNotificationTimeType();
    int getNotificationTimeQuantity();
    int getPlannedQuantity();
    int getUnit();

    void clearScreen();


}
