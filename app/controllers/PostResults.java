/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import br.org.indt.ndg.server.exceptions.MSMApplicationException;
import br.org.indt.ndg.server.persistence.logic.ResultPersister;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import play.mvc.Controller;

/**
 *
 * @author wojciech.luczkow
 */
public class PostResults extends Controller{
    
    public static void upload(String surveyId, File filename )
    {
        try {
            ResultPersister persister = new ResultPersister();
            FileReader reader = new FileReader(filename); 
            persister.postResult(reader, surveyId);
        } catch (IOException ex) {
            Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MSMApplicationException ex) {
            Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
}
