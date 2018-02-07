package ar.com.galias.androidplanner.GUI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ar.com.galias.androidplanner.Controller.Controller;
import ar.com.galias.androidplanner.R;

/**
 * Created by usuario on 07/02/2018.
 */

public class Task_event_new_screen implements  Iface_task_event_new_screen{
    private Context viewContext;
    private Controller controller;
    private View view;
    private int viewIndex;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private Spinner prioritySpinner;
    private Spinner notifTimeTypeSpinner;
    private TextView notifTimeQuantityTextView;
    private TextView plannedQuantityTextView;
    private TextView unitTextView;

    private ImageButton cancelButton;
    private ImageButton saveButton;

    private Calendar planned_date;


    public Task_event_new_screen(Context viewContext, Controller controller, int viewIndex){
        this.viewContext = viewContext;
        this.viewIndex = viewIndex;

        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.task_event_add, null);

        this.titleTextView = this.view.findViewById(R.id.new_task_event_title);
        setUpTextViews(this.titleTextView);
        this.descriptionTextView = this.view.findViewById(R.id.new_task_event_desc);
        setUpTextViews(this.descriptionTextView);


        this.dateTextView = this.view.findViewById(R.id.new_task_event_date);
        setUpTextViews(this.dateTextView);
        setUpDatePicker();

        this.prioritySpinner = this.view.findViewById(R.id.new_task_event_priority_spinner);
        this.notifTimeTypeSpinner = this.view.findViewById(R.id.new_task_event_notif_type_spinner);
        this.notifTimeQuantityTextView = this.view.findViewById(R.id.new_task_event_notif_quantity);
        setUpTextViews(this.notifTimeQuantityTextView);
        setUpNumberPickerTextView(this.notifTimeQuantityTextView);

        this.plannedQuantityTextView = this.view.findViewById(R.id.new_task_event_planned_quantity);
        setUpTextViews(this.plannedQuantityTextView);
        setUpNumberPickerTextView(this.plannedQuantityTextView);

        this.unitTextView = this.view.findViewById(R.id.new_task_event_planned_unit);
        this.setUpTextViews(this.unitTextView);

        this.cancelButton = this.view.findViewById(R.id.new_task_event_cancel_button);
        this.saveButton = this.view.findViewById(R.id.new_task_event_save_button);
        this.saveButton.setEnabled(false);

        setController(controller);
    }

    private void setUpDatePicker(){
        this.planned_date = null;

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                planned_date = Calendar.getInstance();
                planned_date.set(Calendar.YEAR, year);
                planned_date.set(Calendar.MONTH, monthOfYear);
                planned_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = "";
                if(planned_date != null)
                    dateString = sdf.format(planned_date.getTime());
                dateTextView.setText(dateString);
            }

        };

        final Context context = this.viewContext;

        final Calendar today = GregorianCalendar.getInstance();

        this.dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateListener, today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void setUpNumberPickerTextView(final TextView textView){

        final NumberPicker numberPicker = new NumberPicker(this.viewContext);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(999999999);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old_value, int new_value) {
                numberPicker.setValue(new_value);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.viewContext);
        builder.setTitle("androidPlanner");
        builder.setMessage("Seleccione una cantidad");
        builder.setView(numberPicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textView.setText(numberPicker.getValue() + "");
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog numberPickerDialog = builder.create();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPickerDialog.show();
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
        boolean title = this.titleTextView.getText().length() > 0 && this.titleTextView.getText().length() <= 25;
        boolean desc = this.descriptionTextView.getText().length() < 0 && this.descriptionTextView.getText().length() <= 200;
        boolean date = getDate() != null;
        boolean priority = getPriority() != -1;
        boolean notif_type = getNotificationTimeType() != -1;
        boolean notif_quantity = getNotificationTimeQuantity() > -1;
        boolean planned_quantity = getPlannedQuantity() > -1;
        boolean unit = this.unitTextView.getText().length() > 0 && this.unitTextView.getText().length() <= 2;
        if(title && desc && date && priority && notif_type && notif_quantity && planned_quantity && unit)
            this.saveButton.setEnabled(true);
        else
            this.saveButton.setEnabled(false);
    }

    @Override
    public String getTitle() {
        return this.titleTextView.getText().toString();
    }

    @Override
    public String getDescription() {
        return this.descriptionTextView.getText().toString();
    }

    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;

        this.saveButton.setOnClickListener(c.getClickListener());
        this.cancelButton.setOnClickListener(c.getClickListener());
    }

    @Override
    public Calendar getDate() {
        return getDateString(this.dateTextView.getText().toString());
    }

    private Calendar getDateString(String dateString){
        return this.planned_date;
    }

    @Override
    public int getPriority() {
        int priority = 0;
        switch (((String) this.prioritySpinner.getSelectedItem()).toLowerCase()) {
            case "muy alta":
                priority = this.controller.PRIORITY_VERY_HIGH;
                break;
            case "alta":
                priority = this.controller.PRIORITY_HIGH;
                break;
            case "media":
                priority = this.controller.PRIORITY_MEDIUM;
                break;
            case "baja":
                priority = this.controller.PRIORITY_LOW;
                break;
            case "muy baja":
                priority = this.controller.PRIORITY_VERY_LOW;
                break;
            default:
                priority = -1;
                break;
        }
        return priority;
    }

    @Override
    public void hide() {
        this.view.setVisibility(View.GONE);
    }

    @Override
    public int getNotificationTimeType() {
        int type = 0;
        switch (((String) this.notifTimeTypeSpinner.getSelectedItem()).toLowerCase()) {
            case "días":
                type = this.controller.NOTIF_UD_DAY;
                break;
            case "semanas":
                type = this.controller.NOTIF_UD_WEEK;
                break;
            case "meses":
                type = this.controller.NOTIF_UD_MONTH;
                break;
            case "años":
                type = this.controller.NOTIF_UD_YEAR;
                break;
            default:
                type = -1;
                break;
        }
        return type;
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
    public int getNotificationTimeQuantity() {
        int quantity = -1;
        try {
            quantity = Integer.parseInt(this.notifTimeQuantityTextView.getText().toString());
        } catch(NumberFormatException ex){

        }
        return quantity;
    }

    @Override
    public int getViewIndex() {
        return this.viewIndex;
    }

    @Override
    public int getPlannedQuantity() {
        int quantity = -1;
        try {
            quantity =  Integer.parseInt(this.plannedQuantityTextView.getText().toString());
        } catch(NumberFormatException ex){

        }
        return quantity;
    }

    @Override
    public void throwErrMsg(String msg) {
        Toast errMsg = Toast.makeText(this.viewContext, msg, Toast.LENGTH_SHORT);
        errMsg.show();
    }

    @Override
    public int getUnit() {
        return Integer.parseInt(this.unitTextView.getText().toString());
    }

    @Override
    public void clearScreen() {
        this.titleTextView.setText("");
        this.descriptionTextView.setText("");
        this.dateTextView.setText("");
        this.notifTimeQuantityTextView.setText("");
        this.plannedQuantityTextView.setText("");
        this.unitTextView.setText("");
    }
}
