package ar.com.galias.androidplanner.GUI;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.GregorianCalendar;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 29/01/2018.
 */

public class Task_view_screen implements Iface_view_task_screen{
    private Context viewContext;
    private int viewIndex;
    private Controller controller;
    private View view;

    private long current_task_ID;
    private String title;
    private String description;
    private int progress;

    private TextView title_textView;
    private TextView description_textView;
    private ProgressBar progress_bar;
    private TextView progress_bar_textView;

    private ImageButton add_event_button;
    private ImageButton return_button;


    public Task_view_screen(Context viewContext, Controller controller, int viewIndex){
        this.viewContext = viewContext;
        this.viewIndex = viewIndex;

        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.tasks_view_task, null);
        this.view.setVisibility(View.VISIBLE);

        this.title_textView = (TextView) this.view.findViewById(R.id.task_title);
        this.description_textView = (TextView) this.view.findViewById(R.id.task_desc);
        this.progress_bar = (ProgressBar) this.view.findViewById(R.id.task_progress_bar);
        this.progress_bar_textView = (TextView) this.view.findViewById(R.id.task_progress_bar_advance_text);
        this.return_button = (ImageButton) this.view.findViewById(R.id.task_view_return);
        this.add_event_button = (ImageButton) this.view.findViewById(R.id.task_view_new_event);

        setController(controller);

        setUpEvents();
    }

    private void setUpEvents(){
        LinearLayout events_area = this.getView().findViewById(R.id.task_event_area);
        Event_element e1 = new Event_element(viewContext, 1, "Evento de prueba 1",
                "Esto es un evento de prueba.", GregorianCalendar.getInstance(),
                35, this.controller);
        Event_element e2 = new Event_element(viewContext, 2, "Evento de prueba 2",
                "Esto es un evento de prueba.", GregorianCalendar.getInstance(),
                95, this.controller);
        Event_element e3 = new Event_element(viewContext, 3, "Evento de prueba 3",
                "Esto es un evento de prueba.", GregorianCalendar.getInstance(),
                60, this.controller);

        events_area.addView(e1.getElementView());
        events_area.addView(e1.getSpaceView());

        events_area.addView(e2.getElementView());
        events_area.addView(e2.getSpaceView());

        events_area.addView(e3.getElementView());
        events_area.addView(e3.getSpaceView());


    }

    @Override
    public void setCurrent_task_ID(long id){
        this.current_task_ID = id;
    }

    @Override
    public long getCurrent_task_ID(){
        return current_task_ID;
    }

    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
        this.return_button.setOnClickListener(c.getClickListener());
        this.add_event_button.setOnClickListener(c.getClickListener());
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        this.title_textView.setText(title);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        this.description_textView.setText(description);
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
        this.progress_bar.setProgress(progress);
        this.progress_bar_textView.setText(progress + "%");
    }

    @Override
    public void clearScreen() {
        this.title = "";
        this.description = "";
        this.progress = 0;

        this.title_textView.setText(this.title);
        this.description_textView.setText(this.description);
        this.progress_bar.setProgress(this.progress);
        this.progress_bar_textView.setText(this.progress + "%");

    }

    @Override
    public void hide() {
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

    @Override
    public void throwErrMsg(String msg) {
        Toast errMsg = Toast.makeText(viewContext, msg, Toast.LENGTH_SHORT);
        errMsg.show();
    }
}
