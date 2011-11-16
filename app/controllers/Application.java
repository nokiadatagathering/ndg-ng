package controllers;

import java.util.logging.Logger;
import models.NdgUser;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

     @Before(unless={"login", "authorize", "logout"})
     public static void checkAccess() throws Throwable {
        if(!session.contains("ndgUser")) {
            login();
        } else {
        }
    }

    public static void index() {
        render("Application/index.html");
    }

    public static void login() {
        flash.keep("url"); 
        render("Application/login.html");
    }

    public static void authorize(String username, String password) {
        String url = flash.get("url");
        if(url == null || !url.contains("/login")) {
            url = "/";
        }

        NdgUser currentUser = NdgUser.find("byUsernameAndPassword", username, password ).first();

        if(currentUser != null && checkPermission(currentUser)) {
            session.put("ndgUser", username);
            redirect(url);
        } else {
            flash.put("error", "wrong username / password");
            render("Application/login.html");
        }
    }

    public static void logout() {
        session.remove("ndgUser");
        session.clear();
        flash.put("url", "/");
        redirect("/login");
    }

    private static boolean checkPermission(NdgUser user) {
        boolean retval = false;
        if(user.hasRole("Operator")) {
            session.put("operator", true);
            retval = true;
        }
        if(user.hasRole("Admin")) {
            session.put("admin", true);
            retval = true;
        }
        return retval;
    }

}
