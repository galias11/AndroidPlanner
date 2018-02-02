package ar.com.galias.androidplanner.PersistencyController;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by @galias on 15/01/18.
 * Defines an attribute, a comparison and a value for a data storage search.
 */

public class SearchAttribute implements Serializable{
    private String att_name;
    private int comp_code;
    private String att_value;

    public static final int OP_EQUAL = 0;
    public static final int OP_HIGHER = 1;
    public static final int OP_LOWER = 2;
    public static final int OP_HIGHEREQ = 3;
    public static final int OP_LOWEREQ = 4;

    /**
     * Creates a new search attribute.
     * @param att_name
     * (String) Attribute's name. Must be a non null/empty String.
     * @param comp_code
     * (int) Comparison code. Must be one of the available comparators.
     * @param att_value
     * Searched attribute value, can be a String, Calendar, int, double, byte, long, boolean
     */
    public SearchAttribute(String att_name, int comp_code, String att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = att_value;
    }

    public SearchAttribute(String att_name, int comp_code, Calendar att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        this.att_value = sdf.format(att_value.getTime());
    }

    public SearchAttribute(String att_name, int comp_code, int att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }

    public SearchAttribute(String att_name, int comp_code, char att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }

    public SearchAttribute(String att_name, int comp_code, long att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }

    public SearchAttribute(String att_name, int comp_code, float att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }

    public SearchAttribute(String att_name, int comp_code, double att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }

    public SearchAttribute(String att_name, int comp_code, boolean att_value){
        this.att_name = att_name;
        this.comp_code = comp_code;
        this.att_value = String.valueOf(att_value);
    }



    public String getAtt_name() {
        return att_name;
    }

    public int getComp_code() {
        return comp_code;
    }

    public String getAtt_value() {
        return att_value;
    }

    public String getSelection_string_part(){
        String comparator;
        switch(comp_code){
            case OP_EQUAL:
                comparator = " = ? ";
                break;
            case OP_HIGHER:
                comparator = " > ? ";
                break;
            case OP_LOWER:
                comparator = " < ? ";
                break;
            case OP_HIGHEREQ:
                comparator = " >= ? ";
                break;
            case OP_LOWEREQ:
                comparator = " <= ? ";
                break;
            default:
                comparator = "";
                break;
        }
        return att_name + comparator;
    }
}
