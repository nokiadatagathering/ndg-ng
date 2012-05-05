/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package controllers;

import models.NdgUser;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    @Before(unless={"login", "authorize", "logout", "SurveyManager.upload"})
    public static void checkAccess() throws Throwable {
        if(!session.contains("ndgUser")) {
            login( null );
        }
    }

    public static void index() {
        render("Application/index.html");
    }

    public static void login( String lang ) {
        if( lang != null && !lang.isEmpty() ) {
            Lang.change( lang );
        }
        flash.keep("url");
        render("Application/login.html");
    }

    public static void authorize( String username, String password, String lang ) {
        if( lang != null && !lang.isEmpty() ) {
            Lang.change( lang );
        }

        NdgUser currentUser = NdgUser.find("byUsernameAndPassword", username, password ).first();

        if(currentUser != null && checkPermission(currentUser) && currentUser.userValidated == 'Y') {
            session.put("ndgUser", username);
            index();
        } else {
            flash.put("error", "wrong username / password");
            render("Application/login.html");
        }
    }

    public static void logout() {
        session.remove("ndgUser");
        session.clear();
        flash.put("url", "/");
        login(null);
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
