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

import controllers.exceptions.SurveyXmlCreatorException;
import controllers.logic.AuthorizationUtils;
import controllers.logic.SurveyXmlBuilder;
import controllers.util.PropertiesUtil;
import controllers.util.SettingsProperties;
import models.NdgUser;
import models.TransactionLog;
import models.constants.TransactionlogConsts;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;


public class Surveys extends Controller {

    public static void list() {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) ) {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            List<TransactionLog> transactionList = TransactionLog.find("byNdg_user_idAndTransactionStatus",
                    getCurrentUser().id,
                    TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE).fetch();
            ArrayList<TransactionLog> transactions = (ArrayList<TransactionLog>) transactionList;

            // TODO check if we could read it from PLAY ???
            String serverName = PropertiesUtil.getSettingsProperties().getProperty(SettingsProperties.URLSERVER,
                    "http://localhost:9000");
            renderTemplate("surveys.xml", transactions, serverName);
        }
    }

    public static void download(String formID) throws SurveyXmlCreatorException, IOException {
        if(!AuthorizationUtils.isAuthorized(request.headers.get("authorization"), request.method) ) {
            AuthorizationUtils.setDigestResponse(response);
        } else {
            TransactionLog transaction = TransactionLog.find("byNdg_user_idAndTransactionStatusAndSurvey_id",
                    getCurrentUser().id,
                    TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE,
                    formID).first();
            if (transaction == null) {
                error(StatusCode.NOT_FOUND, "No survey for given client");
            } else {
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);

                SurveyXmlBuilder builder = new SurveyXmlBuilder();
                builder.printSurveyXml(transaction.survey, printWriter);
                transaction.transactionStatus = TransactionlogConsts.TransactionStatus.STATUS_SUCCESS;
                transaction.save();
                renderXml(result.toString());
            }
        }
    }

    private static NdgUser getCurrentUser() {
        return AuthorizationUtils.extractUserFromHeader(request.headers.get("authorization"));
    }
}
