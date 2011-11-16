
import controllers.sms.SMSModemHandler;
import models.NdgRole;
import play.*;
import play.jobs.*;
import play.test.*;
import play.vfs.VirtualFile;

//import models.*;
@OnApplicationStart
public class Bootstrap extends Job {

    final static String INITIAL_DATA_FILENAME = "initial_data.sql";

    @Override
    public void doJob() {
        // Check if the database is empty
        if (NdgRole.count() == 0) {

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

        //Initialize SMS modem/gateway
        SMSModemHandler.getInstance();
    }
}