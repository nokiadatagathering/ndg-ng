package controllers;

import controllers.logic.SurveyJsonTransformer;
import controllers.exceptions.SurveySavingException;
import controllers.logic.AuthorizationUtils;
import controllers.logic.SurveyPersister;
import controllers.logic.SurveyXmlBuilder;
import controllers.util.Constants;
import flexjson.JSONSerializer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.QuestionType;
import models.Survey;
import models.TransactionLog;
import models.constants.TransactionlogConsts;
import models.utils.NdgQuery;
import models.utils.SurveyDuplicator;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;

public class SurveyManager extends Controller {

    public static void questionType() {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        List<QuestionType> types = QuestionType.find("bySupported", 1).fetch();

        JSONSerializer typeSerializer = new JSONSerializer();
        typeSerializer.include("typeName", "id").exclude("*").rootName("types");
        renderJSON(typeSerializer.serialize(types));
    }

    public static void saveSurvey(String surveyData) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }

        String username = session.get( "ndgUser" );
        renderText(SurveyJsonTransformer.saveSurvey( surveyData, username ) );
    }

    public static void getSurvey(long surveyId) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        renderJSON(SurveyJsonTransformer.getJsonSurvey(surveyId));
    }

    public static void upload(@Required File filename, String uploadSurveyId) throws IOException, SurveySavingException {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        InputStreamReader is = null;
        String result = "failure";
        if (!session.contains("ndgUser")) {
            response.status = StatusCode.UNAUTHORIZED;
        } else {
            try {
                is = new InputStreamReader(new FileInputStream(filename), "UTF-8");
                SurveyPersister persister = new SurveyPersister(is);
                persister.saveSurvey(uploadSurveyId);
                result = "success";

            } finally {
                if (is != null) {
                    is.close();
                }
            }
            renderText(result);
        }
    }

    public static void get(String id) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);

            SurveyXmlBuilder builder = new SurveyXmlBuilder();
            builder.printSurveyXml(id, printWriter);
            InputStream inputStream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
            renderBinary(inputStream, "survey_" + id + ".xml");
        } catch (Exception ex) {
            Logger.getLogger(SurveyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void delete(String formID) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        Survey deleted = Survey.find("bySurveyId", formID).first();
        deleted.delete();
    }

    public static void duplicate(String formID) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        Survey origin = Survey.find("bySurveyId", formID).first();

        SecureRandom random = new SecureRandom();
        String newId = new BigInteger(40, random).toString(32);

        Survey copy = SurveyDuplicator.plainCopy(origin, newId);

        copy.save();
    }

    public static void sendSurveys(String formID, String users[]) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        for (int i = 0; i < users.length; i++) {
            TransactionLog transaction = new TransactionLog();
            transaction.transactionDate = new Date();
            transaction.transactionType = TransactionlogConsts.TransactionType.TYPE_SEND_SURVEY;
            transaction.transactionStatus = TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE;
            transaction.transmissionMode = TransactionlogConsts.TransactionMode.MODE_HTTP;

            transaction.address = request.remoteAddress;
            transaction.ndgUser = NdgQuery.getUsersbyId(Long.parseLong(users[i]));

            Survey surveyTemp = NdgQuery.getSurveyById(formID);
            surveyTemp.available = Constants.SURVEY_AVAILABLE;
            surveyTemp.save();
            transaction.survey = surveyTemp;

            transaction.save();
        }
    }
}
