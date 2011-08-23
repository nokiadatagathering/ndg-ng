/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.exceptions;

/**
 *
 * @author wojciech.luczkow
 */
public class SurveySavingException extends Exception {
    
    public final static String DATABASE_ERROR = "Database Error ";
    
    public SurveySavingException(String error)
    {
        super(error);
    }
}
