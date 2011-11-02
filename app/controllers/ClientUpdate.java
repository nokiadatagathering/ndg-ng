package controllers;

import java.util.Properties;

import controllers.util.Commands;
import controllers.util.PropertiesUtil;
import controllers.util.SettingsProperties;

import play.mvc.Controller;
import play.mvc.Http;

public class ClientUpdate extends Controller {

    private static final String DO_COMMAND = "do";

    public static void index () {
        if (params.get( DO_COMMAND ) == null) {
            render();
        }

        Commands command = Commands.valueOf( params.get( DO_COMMAND ) );
        Properties prop = PropertiesUtil.getSettingsProperties();

        switch ( command ) {
          case currentVersion:
              String version = prop.getProperty( SettingsProperties.CLIENT_VERSION, "0.0.0" );
              response.setHeader("Content-Length", String.valueOf(version.length()) );
              renderText(version);
              break;
          case updateMyClient:
              redirect(prop.getProperty( SettingsProperties.CLIENT_OTA , "http://localhost:8080/ndg-ota/client/ndg.jad"));
              break;
          default:
              error(Http.StatusCode.NOT_FOUND, "Given command is unrecognized");
        }
    }


}
