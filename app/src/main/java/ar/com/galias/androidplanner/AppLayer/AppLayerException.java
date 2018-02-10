package ar.com.galias.androidplanner.AppLayer;


/**
 * Created by gerardo on 10/01/18.
 */

public class AppLayerException extends Exception {
    private int err_code;
    private String message;

    public static final String[] ERR_CODES = {
            "ERR: Target already cancelled",
            "ERR: Target is not cancelled",
            "ERR: Event not found",
            "ERR: Value not valid, expected >0.0",
            "ERR: Null date",
            "ERR: String out of bonds",
            "ERR: Empty/null String",
            "ERR: Value not valid, expected >= 0.0",
            "ERR: Value not valid, expected > 0",
            "ERR: FREC TYPE not found.",
            "ERR: Value not valid, expected >= 0.",
            "ERR: Null category.",
            "ERR: Item not found.",
            "ERR: Item already exists.",
            "ERR: Event no active."
    };


    public static final int ERR_CODE_ALREADY_CANC = 0;
    public static final int ERR_CODE_NOT_CANC = 1;
    public static final int ERR_CODE_NON_EXISTENT_EVENT = 2;
    public static final int ERR_CODE_INV_VALUE_REAL_H0 = 3;
    public static final int ERR_CODE_NULL_DATE = 4;
    public static final int ERR_CODE_STRING_OUT_BONDS = 5;
    public static final int ERR_CODE_STRING_NULL = 6;
    public static final int ERR_CODE_INV_VALUE_REAL_HE0 = 7;
    public static final int ERR_CODE_INV_VALUE_INT_H0 = 8;
    public static final int ERR_CODE_INV_FREC_TYPE = 9;
    public static final int ERR_CODE_INV_VALUE_INT_HE0 = 10;
    public static final int ERR_CODE_NULL_CATEG = 11;
    public static final int ERR_CODE_NOT_FOUND = 12;
    public static final int ERR_CODE_ITEM_EXISTS = 13;
    public static final int ERR_CODE_NOT_ACTIVE = 14;

    public AppLayerException(int err_code){
        if(err_code < 0 || err_code >= ERR_CODES.length){
            this.err_code = -1;
            this.message = "Error desconocido.";
        } else {
            this.err_code = err_code;
            this.message = ERR_CODES[err_code];
        }
    }

    public String getMessage(){
        return this.message;
    }
}
