import java.io.File;

import javax.persistence.Query;

import play.*;
import play.db.jpa.JPA;
import play.jobs.*;
import play.test.*;
import play.vfs.VirtualFile;
 
//import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    final static String INITIAL_DATA_FILENAME  = "initial_data.sql";
    
    public void doJob() {
        // Check if the database is empty
        Query query = JPA.em().createQuery("select count(r) from NdgRole r");
        if( ((Long)query.getSingleResult()).intValue() == 0) {
            
             VirtualFile sqlFile = null;        
             for (VirtualFile vf : Play.javaPath) {
                 sqlFile = vf.child(INITIAL_DATA_FILENAME);
                 if (sqlFile != null && sqlFile.exists()) {
                     break;
                 }
             }
             if (sqlFile == null) {
                 throw new RuntimeException("Cannot load sql file " + INITIAL_DATA_FILENAME + ", the file was not found");
             } 
             Fixtures.executeSQL(sqlFile.contentAsString());
        }
    }
 
}