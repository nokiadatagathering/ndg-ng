package controllers;

import controllers.logic.AuthorizationUtils;
import controllers.sms.SMSModemHandler;
import play.mvc.Controller;

public class SMSMessage extends Controller {

    public static void sendSMS( String phoneNumber, String message )
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        SMSModemHandler.getInstance().sendTextSMS( phoneNumber, message, 0 );
    }
}
