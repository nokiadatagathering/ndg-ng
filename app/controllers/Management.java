package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.org.indt.ndg.server.exceptions.SurveySavingException;
import br.org.indt.ndg.server.persistence.NdgQuery;
import br.org.indt.ndg.server.persistence.logic.SurveyPersister;
import br.org.indt.ndg.server.persistence.logic.SurveyXmlBuilder;
import br.org.indt.ndg.server.persistence.structure.NdgUser;
import br.org.indt.ndg.server.persistence.structure.Survey;
import br.org.indt.ndg.server.persistence.structure.Transactionlog;
import br.org.indt.ndg.server.persistence.structure.consts.TransactionlogConsts;
import play.db.jpa.JPA;
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
            Transactionlog transaction = new Transactionlog();
            transaction.setTransactionDate(new Date());
            transaction.setTransactionType(TransactionlogConsts.TransactionType.TYPE_SEND_SURVEY);
            transaction.setTransactionStatus(TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE);
            transaction.setTransmissionMode(TransactionlogConsts.TransactionMode.MODE_HTTP);

            transaction.setAddress(request.remoteAddress);
            transaction.setUserUserId(NdgQuery.getUsersbyId(selectedUser));
            transaction.setIdSurvey(NdgQuery.getSurveyById(selectedSurveyIds.get(i)));

            JPA.em().persist(transaction);
        }

        initialContent();
        renderArgs.put("surveysForUserResult", true);
        render("@index");
    }

    public static void upload(File filename) throws IOException, SurveySavingException {
        boolean uploadedSurvey = false;
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            SurveyPersister persister = new SurveyPersister(is);
            persister.saveSurvey();
 
            uploadedSurvey = true;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        initialContent();
        render("@index", uploadedSurvey);
    }

    public static void get(String id) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);

            SurveyXmlBuilder builder = new SurveyXmlBuilder();
            builder.printSurveyXml(id, printWriter);
            renderXml(result.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
