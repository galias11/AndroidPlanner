package ar.com.galias.androidplanner.GUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 21/01/2018.
 */

public class Main_screen implements Iface_main_screen{
    private Controller controller;
    private Context viewContext;
    private View view;
    private int viewIndex;

    private TabHost main_tabs;
    private LinearLayout tareas_main_table;
    private ImageButton new_task_button;
    private ArrayList<ImageButton> view_task_button;

    private long selected_task_id;

    public Main_screen(Context appContext, Controller controller, int viewIndex){
        this.viewContext = appContext;
        this.viewIndex = viewIndex;

        inflateView();

        setController(controller);
    }

    private void inflateView(){
        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.main_screen, null);
        this.view.setVisibility(View.VISIBLE);

        this.selected_task_id = -1;

        this.main_tabs = (TabHost) this.view.findViewById(android.R.id.tabhost);
        setupTabs();

        this.tareas_main_table = this.view.findViewById(R.id.tareas_main_table);

        this.new_task_button = (ImageButton) this.view.findViewById(R.id.task_new_button);

        this.view_task_button = new ArrayList<ImageButton>();

        this.view.setVisibility(View.VISIBLE);
    }


    private void setupTabs(){
        main_tabs.setup();
        main_tabs.addTab(newTab("tareas", "tareas", R.id.tareas));
        main_tabs.addTab(newTab("rutinas", "rutinas", R.id.rutinas));
        for(int i = 0; i < main_tabs.getTabWidget().getChildCount(); i++){
            TextView tv = (TextView) main_tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(this.view.getResources().getColor(R.color.white));
        }
    }

    private TabHost.TabSpec newTab(String tag, String indicator, int contentID){
        TabHost.TabSpec tabSpec = main_tabs.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(contentID);
        return tabSpec;
    }

    @Override
    public void clear_tasks() {
        tareas_main_table.removeAllViews();
    }

    @Override
    public void setController(Controller c){
        this.controller = c;
        this.new_task_button.setOnClickListener(c.getClickListener());
    }

    @Override
    public View getView(){
        return this.view;
    }

    @Override
    public void hide(){
        this.view.setVisibility(View.GONE);
    }

    @Override
    public void setVisible(){
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

    @Override
    public void addTaskElement(long id, String title, String desc, String date, int progress){
        Task_element task = new Task_element(this.viewContext, id, title, desc, date, progress);
        tareas_main_table.addView(task.getSeparator());
        tareas_main_table.addView(task.getTask_view());
        ImageButton new_view_task_button = task.getTask_view().findViewById(R.id.task_view_button);
        new_view_task_button.setOnClickListener(controller.getClickListener());
        view_task_button.add(new_view_task_button);
    }

    @Override
    public void throwErrMsg(String msg){
        Toast errMsg = Toast.makeText(viewContext, msg, Toast.LENGTH_SHORT);
        errMsg.show();
    }

    @Override
    public void activateReturnButton(Activity activity) {
        activity.moveTaskToBack(true);
    }
}
