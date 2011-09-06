package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.exceptions.SurveySavingException;
import models.utils.NdgQuery;
import controllers.logic.SurveyPersister;
import controllers.logic.SurveyXmlBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import models.TransactionLog;
import models.constants.TransactionlogConsts;
import play.mvc.Controller;
import play.mvc.Http;

public class Management extends Controller {

    private static void initialContent() {
        renderArgs.put("surveys", NdgQuery.listAllSurveys());
        renderArgs.put("users", NdgQuery.listAllUsers());
    }

    public static void index() {
        String param = params.get("do");
        if (param == null)
            param = "config"; // default is config, can be change later
        if (param.equals("config")) {
            initialContent();
            render();
        } else {
            error(Http.StatusCode.FORBIDDEN, "");
        }
    }

    public static void save(String selectedUser, List<String> selectedSurveyIds) {

        for (int i = 0; i < selectedSurveyIds.size(); i++) {
            TransactionLog transaction = new TransactionLog();
            transaction.transactionDate =new Date();
            transaction.transactionType = TransactionlogConsts.TransactionType.TYPE_SEND_SURVEY;
            transaction.transactionStatus = TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE;
            transaction.transmissionMode = TransactionlogConsts.TransactionMode.MODE_HTTP;

            transaction.address = request.remoteAddress;
            transaction.ndgUser  = NdgQuery.getUsersbyId(Long.parseLong(selectedUser));
            transaction.survey = NdgQuery.getSurveyById(selectedSurveyIds.get(i));

            transaction.save();
        }

        initialContent();
        renderArgs.put("surveysForUserResult", true);
        render("@index");
    }

    public static void upload(File filename) throws IOException, SurveySavingException {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            SurveyPersister persister = new SurveyPersister(is);
            persister.saveSurvey();
         } finally {
            if (is != null) {
                is.close();
            }
        }
        redirect("Application.index");
    }

    public static void get(String id) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);

            SurveyXmlBuilder builder = new SurveyXmlBuilder();
            builder.printSurveyXml(id, printWriter);
            InputStream inputStream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
            renderBinary(inputStream, "survey_" + id + ".xml");
        } catch (Exception ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
