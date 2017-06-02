package exception;

import java.lang.Exception;
/**
 * Created by Colo on 1/6/2017.
 */
public class IndicadorException extends Exception {

    public IndicadorException(String message){
        super(message);
    }

    public IndicadorException(String message, Throwable throwable) {
        super(message, throwable);
        }
}
