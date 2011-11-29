package controllers;

import controllers.logic.AuthorizationUtils;
import play.mvc.Controller;

public class ClientUtils extends Controller{
    public static void checkUrl() {
        renderText("NdgServer");
    }

    public static void testConnection() {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method)) {
            AuthorizationUtils.setDigestResponse(response);
        } else {
        renderText("OK");
        }
    }
}
