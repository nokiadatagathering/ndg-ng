/*
 *  Copyright (C) 2010-2011  INdT - Instituto Nokia de Tecnologia
 *
 *  NDG is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  NDG is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with NDG.  If not, see <http://www.gnu.org/licenses/
 */
package controllers;

import controllers.exceptions.MSMApplicationException;
import controllers.logic.AuthorizationUtils;
import controllers.logic.ResultPersister;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.NdgResult;
import play.mvc.Controller;

public class PostResults extends Controller {

    public static void upload(String surveyId, File filename ) {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) )
        {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            try {
                ResultPersister persister = new ResultPersister();
                FileReader reader = new FileReader(filename);
                persister.postResult(reader, surveyId, AuthorizationUtils.extractUserFromHeader(request.headers.get("authorization")));
            } catch (IOException ex) {
                Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MSMApplicationException ex) {
                Logger.getLogger(PostResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void checkAuthorization() {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) )
        {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            renderText("OK");
        }
    }

    public static void deleteResult( long id ) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        NdgResult deleted = NdgResult.findById( id );
        deleted.delete();
    }
}
