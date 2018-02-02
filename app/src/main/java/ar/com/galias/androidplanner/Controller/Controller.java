package ar.com.galias.androidplanner.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ar.com.galias.androidplanner.AppLayer.AplicationLayerController;
import ar.com.galias.androidplanner.AppLayer.AppLayerException;
import ar.com.galias.androidplanner.AppLayer.Categoria;
import ar.com.galias.androidplanner.AppLayer.Tarea;
import ar.com.galias.androidplanner.GUI.Category_new_screen;
import ar.com.galias.androidplanner.GUI.Iface_loading_screen;
import ar.com.galias.androidplanner.GUI.Iface_main_screen;
import ar.com.galias.androidplanner.GUI.Iface_new_category_screen;
import ar.com.galias.androidplanner.GUI.Iface_new_task_screen;
import ar.com.galias.androidplanner.GUI.Iface_screen;
import ar.com.galias.androidplanner.GUI.Iface_view_task_screen;
import ar.com.galias.androidplanner.GUI.Loading_screen;
import ar.com.galias.androidplanner.GUI.Main_screen;
import ar.com.galias.androidplanner.GUI.Task_element;
import ar.com.galias.androidplanner.GUI.Task_new_screen;
import ar.com.galias.androidplanner.GUI.Task_view_screen;
import ar.com.galias.androidplanner.Lang.Lang;
import ar.com.galias.androidplanner.Lang.LangEs;
import ar.com.galias.androidplanner.PersistencyController.PersistencyException;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 21/01/2018.
 */

public class Controller extends AppCompatActivity {
    private AplicationLayerController appModel;
    private Context appContext;
    private View.OnClickListener clickListener;
    private View.OnKeyListener keyListener;

    private Iface_screen currentScreen;
    private Iface_loading_screen loadingScreen;
    private Iface_main_screen mainScreen;
    private Iface_new_task_screen newTaskScreen;
    private Iface_new_category_screen newCategoryScreen;
    private Iface_view_task_screen viewTaskScreen;

    private ViewFlipper viewManager;
    private Lang lang_module;

    public static final int SCREEN_LOADING = 0;
    public static final int SCREEN_MAIN = 1;
    public static final int SCREEN_NEW_TASK = 2;
    public static final int SCREEN_NEW_CATEGORY = 3;
    public static final int SCREEN_VIEW_TASK = 4;

    private final int guiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.appContext = this;
        this.lang_module = LangEs.getInstance();

        setContentView(R.layout.app_layout);
        getWindow().getDecorView().setSystemUiVisibility(guiOptions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initialize_app();


    }


    private void initizalize_views(){

        this.mainScreen = new Main_screen(appContext, this, SCREEN_MAIN);
        this.newTaskScreen = new Task_new_screen(appContext, this, SCREEN_NEW_TASK);
        this.newCategoryScreen = new Category_new_screen(appContext, this, SCREEN_NEW_CATEGORY);
        this.viewTaskScreen = new Task_view_screen(appContext, this, SCREEN_VIEW_TASK);

        this.mainScreen.hide();
        this.newTaskScreen.hide();
        this.newCategoryScreen.hide();
        this.viewTaskScreen.hide();

        this.viewManager.addView(this.mainScreen.getView(), new ViewSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.viewManager.addView(this.newTaskScreen.getView(), new ViewSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.viewManager.addView(this.newCategoryScreen.getView(), new ViewSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.viewManager.addView(this.viewTaskScreen.getView(), new ViewSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    private void set_up_clickListener(){
        final Toast errMsg = Toast.makeText(appContext, "", Toast.LENGTH_SHORT);
        final Toast successMsg = Toast.makeText(appContext, "", Toast.LENGTH_SHORT);

        this.clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.task_new_button:
                        setCurrentScreen(newTaskScreen);
                        break;
                    case R.id.new_task_save_button:
                        try{
                            Tarea new_tarea = appModel.crearTarea(newTaskScreen.getTaskTitle(),
                                    newTaskScreen.getTaskDescription(),
                                    appModel.getCategorias().get(newTaskScreen.getCategoria()));
                            Calendar primerVencimiento = new_tarea.primerVencimiento();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String venc = "Primer vencimiento: ";
                            if(primerVencimiento != null)
                                venc += sdf.format(primerVencimiento.getTime());
                            mainScreen.addTaskElement(new_tarea.getId(), new_tarea.getTitulo(),
                                    new_tarea.getDescripcion(), venc,
                                    (int) new_tarea.getAvanc());
                            successMsg.setText("Tarea creada");
                            newTaskScreen.clearScreen();
                            setCurrentScreen(mainScreen);
                        } catch(AppLayerException modelEx){
                            errMsg.setText(modelEx.getMessage());
                            errMsg.show();
                        } catch(PersistencyException persistEx){
                            errMsg.setText(persistEx.getMessage());
                            errMsg.show();
                        }
                        break;
                    case R.id.new_task_cancel_button:
                        newTaskScreen.clearScreen();
                        setCurrentScreen(mainScreen);
                        break;
                    case R.id.new_task_add_category_button:
                        setCurrentScreen(newCategoryScreen);
                        break;
                    case R.id.new_category_cancel_button:
                        newCategoryScreen.clearScreen();
                        setCurrentScreen(newTaskScreen);
                        break;
                    case R.id.new_category_save_button:
                        try {
                            appModel.crearCategoria(newCategoryScreen.getNombre(), newCategoryScreen.getDesc());
                            newCategoryScreen.clearScreen();
                            newTaskScreen.refresh();
                            setCurrentScreen(newTaskScreen);
                        } catch (PersistencyException persistEx){
                            errMsg.setText(persistEx.getMessage());
                            errMsg.show();
                        } catch (AppLayerException modelEx){
                            errMsg.setText(modelEx.getMessage());
                            errMsg.show();
                        }
                        break;
                    case R.id.task_view_button:
                        long task_id = Long.parseLong(view.getContentDescription().toString());
                        viewTaskScreen.setCurrent_task_ID(task_id);
                        Tarea t = appModel.getTareas().get(task_id);
                        viewTaskScreen.setTitle(t.getTitulo());
                        viewTaskScreen.setDescription(t.getDescripcion());
                        viewTaskScreen.setProgress((int) t.getAvanc());
                        setCurrentScreen(viewTaskScreen);
                        break;
                    case R.id.task_view_return:
                        viewTaskScreen.clearScreen();
                        setCurrentScreen(mainScreen);
                    default:
                        break;
                }
            }
        };
    }

    private void set_up_keyListener(){
        this.keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        };
    }


    private void initialize_app(){
        set_up_clickListener();
        set_up_keyListener();

        this.viewManager = (ViewFlipper) this.findViewById(R.id.app_context_layout);

        this.loadingScreen = new Loading_screen(appContext, this, SCREEN_LOADING);
        this.loadingScreen.hide();
        this.viewManager.addView(this.loadingScreen.getView(), new ViewSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setCurrentScreen(loadingScreen);

        try{
            this.appModel = new AplicationLayerController(this.appContext);
            boolean[] filters = {false, false, true, false};

            initizalize_views();

            load_tasks(filters, AplicationLayerController.ORDER_KEY_DATE, false, null);

            setCurrentScreen(mainScreen);

        } catch(PersistencyException ex){
            this.currentScreen.throwErrMsg(lang_module.getMsg(Lang.ERR_LOADING_DATA));
        }
    }

    private void setCurrentScreen(Iface_screen screen){
        Iface_screen previous = this.currentScreen;
        screen.setVisible();
        this.currentScreen = screen;
        if(previous != null)
            previous.hide();
        this.viewManager.setDisplayedChild(screen.getViewIndex());
    }

    private void load_tasks(boolean[] filters, int order, boolean inv_order, ArrayList<String> categs){
        ArrayList<Tarea> arr_tareas = null;
        try {
            arr_tareas = appModel.getTareas(filters, order, inv_order, categs);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Tarea t : arr_tareas) {
                String str_primerVenc = "1er Vencimiento: ";
                Calendar primerVenc = t.primerVencimiento();
                if(primerVenc != null)
                    str_primerVenc += sdf.format(primerVenc.getTime());
                mainScreen.addTaskElement(t.getId(), t.getTitulo(), t.getDescripcion(),
                        str_primerVenc, (int) t.getAvanc());
            }
        } catch (AppLayerException ex) {
            currentScreen.throwErrMsg(lang_module.getMsg(Lang.ERR_FILT_AND_ORD));
        }

    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public View.OnKeyListener getKeyListener() {
        return keyListener;
    }

    public ArrayList<String> getCategorias(){
        ArrayList<String> categTitles = new ArrayList<>();
        for(Categoria c : appModel.getCategorias().values())
            categTitles.add(c.getNombre());
        return categTitles;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onRestart(){
        super.onRestart();

    }


}
