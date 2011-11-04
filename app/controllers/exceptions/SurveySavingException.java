package controllers.exceptions;

public class SurveySavingException extends Exception {
    
    public final static String DATABASE_ERROR = "Database Error ";
    
    public SurveySavingException(String error)
    {
        super(error);
    }
}
