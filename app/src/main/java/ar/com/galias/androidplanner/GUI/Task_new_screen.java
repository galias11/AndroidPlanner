package ar.com.galias.androidplanner.GUI;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 26/01/2018.
 */

public class Task_new_screen implements Iface_new_task_screen {
    private Context viewContext;
    private Controller controller;
    private View view;

    private int viewIndex;
    private String selectedCategory;

    private TextView titleTextView;
    private TextView descTextView;
    private Spinner categSpinner;
    private ImageButton acceptButton;
    private ImageButton cancelButton;
    private ImageButton newCategoryButton;


    public Task_new_screen(Context viewContext, Controller controller, int viewIndex){
        this.viewContext = viewContext;
        this.viewIndex = viewIndex;

        this.selectedCategory = null;

        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.tasks_new_task, null);
        this.view.setVisibility(View.VISIBLE);

        this.titleTextView = this.view.findViewById(R.id.new_task_title);
        this.descTextView = this.view.findViewById(R.id.new_task_desc);

        setUpTextViews(titleTextView);
        setUpTextViews(descTextView);

        this.categSpinner = this.view.findViewById(R.id.new_task_categ_spinner);
        this.acceptButton = this.view.findViewById(R.id.new_task_save_button);
        this.cancelButton = this.view.findViewById(R.id.new_task_cancel_button);
        this.newCategoryButton = this.view.findViewById(R.id.new_task_add_category_button);

        this.acceptButton.setEnabled(false);
        this.cancelButton.setEnabled(true);
        this.newCategoryButton.setEnabled(true);

        checkTextViews();

        setController(controller);
        setUpSpinners();
    }

    private void setUpSpinners(){
        ArrayList<String> availableCategs = controller.getCategorias();
        String[] spinnerValues = (String[]) availableCategs.toArray(new String[0]);
        Spinner s = (Spinner) view.findViewById(R.id.new_task_categ_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewContext, android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedCategory((String) adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpTextViews(TextView textView){
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkTextViews();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkTextViews(){
        if(titleTextView.getText().length() > 0 && titleTextView.getText().length() <= 25
                && descTextView.getText().length() > 0 && descTextView.getText().length() <= 200
                && categSpinner.getSelectedItemId() != -1)
            this.acceptButton.setEnabled(true);
        else
            this.acceptButton.setEnabled(false);
    }

    public void setSelectedCategory(String category){
        this.selectedCategory = category;
    }

    @Override
    public void clearScreen(){
        this.titleTextView.setText("");
        this.descTextView.setText("");
        this.categSpinner.setSelection(0);
    }

    @Override
    public void refresh(){
        setUpSpinners();
    }

    @Override
    public String getTaskTitle() {
        return this.titleTextView.getText().toString();
    }

    @Override
    public String getTaskDescription() {
        return this.descTextView.getText().toString();
    }

    @Override
    public String getCategoria() {
        return this.selectedCategory;
    }

    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
        this.cancelButton.setOnClickListener(c.getClickListener());
        this.acceptButton.setOnClickListener(c.getClickListener());
        this.newCategoryButton.setOnClickListener(c.getClickListener());
    }

    @Override
    public void hide() {
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
    public void throwErrMsg(String msg) {
        Toast errMsg = Toast.makeText(viewContext, msg, Toast.LENGTH_SHORT);
        errMsg.show();
    }
}
