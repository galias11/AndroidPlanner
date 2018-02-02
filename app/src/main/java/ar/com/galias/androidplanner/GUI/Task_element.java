package ar.com.galias.androidplanner.GUI;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 21/01/2018.
 */

public class Task_element {
    private Context appContext;
    private View task_view;
    private View space_view;

    private long task_id;
    private String title;
    private String desc;
    private String first_venc;
    private int progress;

    private ImageButton view_task_button;

    public Task_element(Context appContext){
        this.appContext = appContext;
        LayoutInflater inflater = (LayoutInflater) this.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.task_view = inflater.inflate(R.layout.task_element, null);
        this.task_id = 0;
        this.title = appContext.getString(R.string.task_element_example_title);
        this.desc = appContext.getString(R.string.Task_element_example_desc);
        this.first_venc = appContext.getString(R.string.Task_element_first_date);
        this.progress = 100;
        ProgressBar progressBar = this.task_view.findViewById(R.id.task_progress_bar);
        progressBar.setProgress(this.progress);
        this.space_view = inflater.inflate(R.layout.separator, null);
        view_task_button = task_view.findViewById(R.id.task_view_button);
        view_task_button.setContentDescription("" + task_id);
        setUp();
    }

    public Task_element(Context appContext, long task_id, String title, String desc, String first_venc, int progress){
        this.appContext = appContext;
        LayoutInflater inflater = (LayoutInflater) this.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.task_view = inflater.inflate(R.layout.task_element, null);
        this.task_id = task_id;
        this.title = title;
        this.desc = desc;
        this.first_venc = first_venc;
        this.progress = progress;
        ProgressBar progressBar = this.task_view.findViewById(R.id.task_progress_bar);
        progressBar.setProgress(progress);
        this.space_view = inflater.inflate(R.layout.separator, null);
        view_task_button = task_view.findViewById(R.id.task_view_button);
        view_task_button.setContentDescription("" + task_id);
        setUp();
    }


    public View getTask_view(){
        return task_view;
    }

    public View getSeparator() {return this.space_view;}

    private void setUp(){
        TextView title_view = (TextView) task_view.findViewById(R.id.task_element_title);
        title_view.setText(this.title);
        TextView desc_view = (TextView) task_view.findViewById(R.id.task_element_desc);
        desc_view.setText(this.desc);
        TextView first_date_view = (TextView) task_view.findViewById(R.id.task_element_first_date);
        first_date_view.setText(this.first_venc);
        TextView progress_view = (TextView) task_view.findViewById(R.id.task_element_progress_bar_advance_text);
        progress_view.setText(String.valueOf(this.progress) + "%");
    }

    public long getTask_id() {
        return task_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getFirst_venc() {
        return first_venc;
    }

    public int getProgress() {
        return progress;
    }
}
