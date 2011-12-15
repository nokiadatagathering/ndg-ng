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

import controllers.logic.AuthorizationUtils;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;

public abstract class NdgController extends Controller {

    @Before
    public static void checkAccess() throws Throwable {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            error( StatusCode.UNAUTHORIZED, "Session expired" );
        }
    }
}
