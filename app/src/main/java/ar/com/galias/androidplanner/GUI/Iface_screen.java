package ar.com.galias.androidplanner.GUI;

import android.view.View;

import ar.com.galias.androidplanner.Controller.Controller;

/**
 * Created by usuario on 25/01/2018.
 */

public interface Iface_screen {

    View getView();
    void setController(Controller c);
    void hide();
    void setVisible();
    void setViewIndex(int viewIndex);
    int getViewIndex();
    void throwErrMsg(String msg);

}
