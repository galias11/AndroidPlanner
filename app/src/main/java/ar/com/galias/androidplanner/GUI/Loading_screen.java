package ar.com.galias.androidplanner.GUI;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 25/01/2018.
 */

public class Loading_screen implements Iface_loading_screen{
    private Controller controller;
    private Context viewContext;
    private View view;
    private int viewIndex;

    public Loading_screen(Context viewContext, Controller controller, int viewIndex){
        this.viewContext = viewContext;
        this.viewIndex = viewIndex;
        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.loading_screen, null);
        this.view.setVisibility(View.VISIBLE);
        setController(controller);
     }

     @Override
     public View getView(){
        this.view.setVisibility(View.VISIBLE);
        return this.view;
     }

     @Override
     public void setController(Controller c){
         this.controller = c;
         this.view.setOnClickListener(c.getClickListener());
         this.view.setOnKeyListener(c.getKeyListener());
     }

     @Override
     public void throwErrMsg(String msg){
         Toast errMsg = Toast.makeText(viewContext, msg, Toast.LENGTH_SHORT);
         errMsg.show();
     }

     @Override
    public void hide(){
         this.view.setVisibility(View.GONE);
     }

    @Override
    public void setVisible() {
        this.view.setVisibility(View.VISIBLE);
    }

    @Override
    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    @Override
    public int getViewIndex() {
        return this.viewIndex;
    }
}
