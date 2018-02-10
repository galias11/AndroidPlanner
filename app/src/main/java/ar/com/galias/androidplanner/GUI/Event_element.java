package ar.com.galias.androidplanner.GUI;

import android.content.Context;
import android.content.res.ColorStateList;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 31/01/2018.
 */

public class Event_element {
    private Context viewContext;
    private View elementView;
    private View spaceView;
    private Controller controller;

    private long event_id;
    private String event_title;
    private String event_desc;
    private Calendar planned_date;
    private int avanc;
    private boolean completed;
    private boolean cancelled;
    private boolean closed;

    private TextView title_textView;
    private TextView desc_textView;
    private TextView date_textView;
    private TextView avanc_textView;
    private ProgressBar avanc_progressBar;

    private ImageButton view_button;
    private ImageButton close_button;

    private ImageButton closed_indicator;
    private ImageButton done_indicator;
    private ImageButton cancelled_indicator;

    public Event_element(Context viewContext, Controller controller){
        this.viewContext = viewContext;

        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.elementView = inflater.inflate(R.layout.task_event_element, null);
        this.spaceView = inflater.inflate(R.layout.separator, null);


        this.title_textView = elementView.findViewById(R.id.event_element_title);
        this.desc_textView = elementView.findViewById(R.id.event_element_description);
        this.date_textView = elementView.findViewById(R.id.event_element_planned_date);
        this.avanc_textView = elementView.findViewById(R.id.event_element_advance_text);
        this.avanc_progressBar = elementView.findViewById(R.id.event_element_progress_bar);

        this.view_button = elementView.findViewById(R.id.event_element_view);
        this.close_button = elementView.findViewById(R.id.event_element_close);

        this.closed_indicator = elementView.findViewById(R.id.event_element_closed_indicator);
        this.done_indicator = elementView.findViewById(R.id.event_element_done_indicator);
        this.cancelled_indicator = elementView.findViewById(R.id.event_element_active_indicator);

        setController(controller);
    }

    public Event_element(Context viewContext, long event_id, String event_title, String event_desc,
                         Calendar planned_date, int avanc, Controller controller){
        this(viewContext, controller);

        this.event_id = event_id;
        this.event_title = event_title;
        this.event_desc = event_desc;
        this.planned_date = planned_date;
        this.avanc = avanc;

        this.title_textView.setText(event_title);
        this.desc_textView.setText(event_desc);
        this.date_textView.setText(formatDate(planned_date));
        this.avanc_progressBar.setProgress(avanc);
        this.avanc_textView.setText(avanc + "%");
    }

    private void setController(Controller c){
        this.controller = c;
        this.view_button.setOnClickListener(c.getClickListener());
        this.close_button.setOnClickListener(c.getClickListener());
    }

    public View getElementView() {
        return elementView;
    }

    public View getSpaceView() {
        return spaceView;
    }

    public long getEvent_id() {
        return event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public Calendar getPlanned_date() {
        return planned_date;
    }

    public int getAvanc() {
        return avanc;
    }

    public void setCancelled(boolean status){
        this.cancelled = status;
        if(status)
            cancelled_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.red_900));
        else
            cancelled_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.grey_500));

    }

    public void setClosed(boolean status){
        this.closed = status;
        if(status) {
            closed_indicator.setBackground(this.viewContext.getResources().getDrawable(android.R.drawable.ic_secure));
            closed_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.yellow_500));
        } else {
            closed_indicator.setBackground(this.viewContext.getResources().getDrawable(android.R.drawable.ic_partial_secure));
            closed_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.grey_500));
        }
    }

    public void setCompleted(boolean status){
        this.completed = status;
        if(status) {
            done_indicator.setBackground(this.viewContext.getResources().getDrawable(android.R.drawable.checkbox_on_background));
            done_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.green_500));
        } else {
            done_indicator.setBackground(this.viewContext.getResources().getDrawable(android.R.drawable.checkbox_off_background));
            done_indicator.setBackgroundTintList(this.viewContext.getResources().getColorStateList(R.color.grey_500));
        }
    }

    private String formatDate(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date.getTime());
    }
}
