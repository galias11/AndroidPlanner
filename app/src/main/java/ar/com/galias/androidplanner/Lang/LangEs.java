package ar.com.galias.androidplanner.Lang;

import java.util.HashMap;

/**
 * Created by usuario on 25/01/2018.
 */

public class LangEs implements Lang {
    private static LangEs self;
    private static HashMap<Integer, String> msgData;

    private LangEs(){
        this.msgData = new HashMap<Integer, String>();
        msgData.put(ERR_LOADING_DATA, "ERROR: Ocurrió un error al recuperar los datos.");
        msgData.put(ERR_FILT_AND_ORD, "ERROR: Ocurrió un error al filtrar/ordenar los datos.");
    }

    public static LangEs getInstance(){
        if(self == null)
            return new LangEs();
        return self;
    }

    @Override
    public String getMsg(int msgCode){
        if(msgData.containsKey(msgCode))
            return "Lang error.";
        return msgData.get(msgCode);
    }
}
