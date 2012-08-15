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

import models.Company;
import models.NdgUser;
import models.constants.TransactionlogConsts;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;

import play.Play;

import notifiers.securesocial.Mails;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Router;
import securesocial.provider.*;

public class Application extends Controller {

    private static final String USER_NAME = "userName";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String COMPANY = "company";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String COUNTRY = "country";
    private static final String INDUSTRY = "industry";
    private static final String USER_NAME_TAKEN = "views.login.userNameTaken";
    private static final String ERROR_CREATING_ACCOUNT = "views.login.errorCreatingAccount";
    private static final String ACCOUNT_CREATED = "views.login.accountCreated";
    private static final String INVALID_LINK = "views.login.invalidLink";
    private static final String ACTIVATION_SUCCESS = "views.login.activationSuccess";

    @Before(unless={"login", "authorize", "logout", "SurveyManager.upload", "createAccount", "activate"})
    public static void checkAccess() throws Throwable {
        if(!session.contains("ndgUser")) {
            login( null );
        }
    }

    public static void index() {
        renderArgs.put("attachPath",Play.configuration.getProperty("attachments.path"));
        render("Application/index.html");
    }

    public static void login( String lang ) {
        if( lang != null && !lang.isEmpty() ) {
            Lang.change( lang );
        }
        flash.keep("url");
        render("Application/login.html");
    }

    public static void authorize( String username, String pass, String lang ) {
        if( lang != null && !lang.isEmpty() ) {
            Lang.change( lang );
        }

        NdgUser currentUser = NdgUser.find("byUsernameAndPassword", username, pass ).first();

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

    /**
     * Creates an account
     *
     * @param userName      The username
     * @param displayName   The user's full name
     * @param email         The email
     * @param password      The password
     * @param password2     The password verification
     */
    public static void createAccount(@Required(message = "views.login.required") String userName,
                                     @Required String firstName,
                                     @Required String lastName,
                                     @Required @Email(message = "views.login.invalidEmail") String email,
                                     @Required String password,
                                     @Required @Equals(message = "views.login.passwordsMustMatch", value = "password") String password2,
                                     @Required String phoneNumber,
                                     @Required String company,
                                     @Required String country,
                                     @Required String industry) {
        if ( validation.hasErrors() ) {
            tryAgain(userName, firstName, lastName, email, phoneNumber, company, country, industry);
        }

        if ( NDGPersister.find(userName) != null ) {
            validation.addError(USER_NAME, Messages.get(USER_NAME_TAKEN));
        }

        NdgUser user = new NdgUser(password, userName,
                                   email, firstName, lastName,
                                   phoneNumber, userName, 'N', 'Y', 'Y');
        Company userCompany = new Company(company, "type", country, industry, "size");
        user.company = userCompany;

        try {
            NDGPersister.save(user);
            SurveyManager.addDemoSurveyToNewUser(user, "1263929563");
        } catch ( Throwable e ) {
            Logger.error(e, "Error while invoking NDGPersister.save()");
            flash.error(Messages.get(ERROR_CREATING_ACCOUNT));
            tryAgain(userName, firstName, lastName, email, phoneNumber, company, country, industry);
        }

        // creates an activation id
        final String uuid = NDGPersister.createActivation(user);
        Mails.sendActivationEmail(user, uuid);

        NDGPersister.logTransaction(TransactionlogConsts.TransactionType.NEW_USER_ADMIN,
                                    TransactionlogConsts.TransactionStatus.STATUS_PENDING,
                                    request.remoteAddress,
                                    user);

        flash.success(Messages.get(ACCOUNT_CREATED));
        render("Application/login.html");
    }

    private static void tryAgain(String username, String firstName, String lastName, String email, String phoneNumber,
                                    String company, String country, String industry) {
        flash.clear();
        flash.put(USER_NAME, username);
        flash.put(FIRST_NAME, firstName);
        flash.put(LAST_NAME, lastName);
        flash.put(EMAIL, email);
        flash.put(PHONE_NUMBER, phoneNumber);
        flash.put(COMPANY, company);
        flash.put(COUNTRY, country);
        flash.put(INDUSTRY, industry);
        render("Application/login.html");
    }

    /**
     * The action invoked from the activation email the user receives after signing up.
     *
     * @param uuid The activation id
     */
    public static void activate(String uuid) {
        try {
            if ( NDGPersister.activate(uuid) == false ) {
                flash.error( Messages.get(INVALID_LINK) );
            } else {
                flash.success(Messages.get(ACTIVATION_SUCCESS));
            }
        } catch ( Throwable t) {
            Logger.error(t, "Error while activating account");
            flash.error(Messages.get(ERROR_CREATING_ACCOUNT));
        }
        flash.remove(USER_NAME);
        flash.remove(FIRST_NAME);
        flash.remove(LAST_NAME);
        flash.remove(EMAIL);
        flash.remove(PHONE_NUMBER);
        flash.remove(COMPANY);
        flash.remove(COUNTRY);
        flash.remove(INDUSTRY);
        render("Application/login.html");
    }
}
