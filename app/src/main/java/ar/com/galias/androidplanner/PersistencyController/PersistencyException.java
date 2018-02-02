package ar.com.galias.androidplanner.PersistencyController;

/**
 * Created by gerardo on 7/01/18.
 */

public class PersistencyException extends Exception {
    private String message;
    private int err_code;

    private static final String[] ERR_CODES = {
            "Query: One of query arguments is not defined.",
            "Query: Selection string not compatible with selection arguments.",
            "Mapper: SQLite connector is null.",
            "Mapper: Object to map is null.",
            "Mapper: Database transaction failure.",
            "Mapper: Failed to create object.",
            "Mapper: Invalid search attributes."
    };

    public static final int ERR_QUERY_INV_ARGS = 0;
    public static final int ERR_QUERY_INV_SELECTION = 1;
    public static final int ERR_MAPPER_NULL_CONN = 2;
    public static final int ERR_MAPPER_NULL_OBJ = 3;
    public static final int ERR_MAPPER_TRANS_FAILED = 4;
    public static final int ERR_MAPPER_OBJCONST_ERROR = 5;
    public static final int ERR_MAPPER_INVALID_SEARCH_ATTS = 6;


    public PersistencyException(int err_code){
        super();
        if(err_code < 0 || err_code >= ERR_CODES.length) {
            this.message = "Not defined.";
            err_code = -1;
        } else {
            this.message = ERR_CODES[err_code];
            this.err_code = err_code;
        }
    }


    @Override
    public String getMessage(){
        return message;
    }

}
