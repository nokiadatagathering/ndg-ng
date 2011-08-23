/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import br.org.indt.ndg.server.persistence.logic.ProcessDownloadSurvey;
import br.org.indt.ndg.server.util.PropertiesUtil;
import br.org.indt.ndg.server.util.SettingsProperties;
import java.util.Properties;
import play.mvc.Controller;

/**
 *
 * @author wojciech.luczkow
 */
public class ReceiveSurvey extends Controller {

    public static void index() {
        render();
    }

    public static void list() {
        ProcessDownloadSurvey processDownloadSurvey =
                new ProcessDownloadSurvey(PropertiesUtil.getSettingsProperties().getProperty(SettingsProperties.URLSERVER, "http://localhost:9000"),
                "admin");//TODO get userName from WWW-Authentication header

        String result = processDownloadSurvey.processListCommand();

        renderXml(result);
    }

    public static void download(String formID) {
        ProcessDownloadSurvey processDownloadSurvey =
                new ProcessDownloadSurvey(PropertiesUtil.getSettingsProperties().getProperty(SettingsProperties.URLSERVER, "http://localhost:9000"),
                "admin");//TODO get userName from WWW-Authentication header
        String result = processDownloadSurvey.processDownloadCommand(formID);

        renderXml(result);
    }
}
