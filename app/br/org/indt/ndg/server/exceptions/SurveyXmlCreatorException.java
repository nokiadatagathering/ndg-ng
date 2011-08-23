/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.exceptions;

/**
 *
 * @author wojciech.luczkow
 */
public class SurveyXmlCreatorException extends Exception{
    public final static String SURVEY_NO_FOUND = "No Survey with given Id";
    public final static String DATABASE_ERROR = "Database Error ";
    
    public SurveyXmlCreatorException(String error)
    {
        super(error);
    }
}
