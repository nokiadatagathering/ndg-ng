package controllers;

import controllers.logic.AuthorizationUtils;
import controllers.sms.SmsHandlerFactory;
import play.mvc.Controller;

public class SMSMessage extends Controller {

    public static void sendSMS( String phoneNumber, String message )
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        if ( SmsHandlerFactory.getInstance().getSmsHandler() != null) {
            SmsHandlerFactory.getInstance().getSmsHandler().sendTextSMS( phoneNumber, message, 0 );
        }
    }
}
