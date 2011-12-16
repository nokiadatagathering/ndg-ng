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

import controllers.exceptions.MSMApplicationException;
import controllers.exceptions.ResultSaveException;
import controllers.logic.AuthorizationUtils;
import controllers.logic.ResultPersister;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.NdgResult;
import models.NdgUser;
import models.Survey;
import models.TransactionLog;
import play.mvc.Before;
import play.mvc.Controller;

public class PostResults extends Controller {

    private static final int CONFLICT = 409;

    @Before(unless={"upload","checkAuthorization"})
    public static void checkAccess() throws Throwable {
        NdgController.checkAccess();
    }

    public static void upload( String surveyId, File filename ) {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) ) {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            try {
                NdgUser userName = AuthorizationUtils.extractUserFromHeader(request.headers.get("authorization"));
                Survey survey = Survey.find("bySurveyId", surveyId).first();
                if (survey == null) {
                    throw new ResultSaveException(ResultSaveException.ERROR_CODE_NO_SURVEY);
                }

                List<TransactionLog> transactionList = TransactionLog.find("byNdg_user_idAndsurvey",
                    userName.id,
                    survey).fetch();
                if( transactionList == null || transactionList.isEmpty() ) {//this survey wasn't sent to this user -  rejecting result...
                    error( CONFLICT , "This survey has not been sent to this user" );
                    return;
                }

                ResultPersister persister = new ResultPersister();
                FileReader reader = new FileReader(filename);
                persister.postResult(reader, surveyId, userName);
            } catch (IOException ex) {
                Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MSMApplicationException ex) {
                Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void checkAuthorization() {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) ) {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            renderText("OK");
        }
    }

    public static void deleteResult( long id ) {
        NdgResult deleted = NdgResult.findById( id );
        deleted.delete();
    }
}
