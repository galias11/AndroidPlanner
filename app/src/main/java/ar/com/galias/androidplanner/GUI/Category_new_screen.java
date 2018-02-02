package ar.com.galias.androidplanner.GUI;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ar.com.galias.androidplanner.R;

import ar.com.galias.androidplanner.Controller.Controller;

/**
 * Created by usuario on 28/01/2018.
 */

public class Category_new_screen implements Iface_new_category_screen{
    private Context viewContext;
    private Controller controller;
    private View view;

    private int viewIndex;

    private TextView nombreTextView;
    private TextView descTextView;
    private ImageButton cancelButton;
    private ImageButton saveButton;

    public Category_new_screen(Context viewContext, Controller controller, int viewIndex){
        this.viewContext = viewContext;
        this.viewIndex = viewIndex;

        LayoutInflater inflater = (LayoutInflater) this.viewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.category_new_category, null);
        this.view.setVisibility(View.VISIBLE);



        this.nombreTextView = this.view.findViewById(R.id.new_category_title);
        this.descTextView = this.view.findViewById(R.id.new_category_desc);

        setUpTextViews(nombreTextView);
        setUpTextViews(descTextView);

        this.saveButton = this.view.findViewById(R.id.new_category_save_button);
        this.cancelButton = this.view.findViewById(R.id.new_category_cancel_button);

        this.saveButton.setEnabled(false);
        this.cancelButton.setEnabled(true);

        setController(controller);


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
        if(this.nombreTextView.getText().length() > 0 && this.nombreTextView.getText().length() <= 25
                && this.descTextView.getText().length() > 0 && this.descTextView.getText().length() <= 200)
            this.saveButton.setEnabled(true);
        else
            this.saveButton.setEnabled(false);
    }

    @Override
    public String getNombre() {
        return this.nombreTextView.getText().toString();
    }

    @Override
    public String getDesc() {
        return this.descTextView.getText().toString();
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

    @Override
    public void clearScreen(){
        this.nombreTextView.setText("");
        this.descTextView.setText("");
    }
}
