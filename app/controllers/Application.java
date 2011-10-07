package controllers;

import controllers.deserializer.CategoryObjectFactory;
import controllers.deserializer.NdgUserObjectFactory;
import controllers.deserializer.QuestionObjectFactory;
import controllers.deserializer.QuestionOptionObjectFactory;
import controllers.deserializer.QuestionTypeObjectFactory;
import controllers.deserializer.SurveyObjectFactory;
import controllers.exceptions.SurveySavingException;
import controllers.logic.SurveyPersister;
import flexjson.JSONDeserializer;
import play.mvc.Controller;
import flexjson.JSONSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import models.Category;
import models.Company;
import models.NdgRole;
import models.NdgUser;
import models.Question;
import models.QuestionType;
import models.Survey;
import models.TransactionLog;
import models.UserRole;
import models.constants.TransactionlogConsts;
import models.utils.NdgQuery;
import models.utils.SurveyDuplicator;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void questionType(){
        List<QuestionType> types = QuestionType.find( "bySupported", 1 ).fetch();

        JSONSerializer typeSerializer = new JSONSerializer();
        typeSerializer.include( "typeName", "id" ).exclude( "*" ).rootName( "types" );
        renderJSON( typeSerializer.serialize( types ) );
    }

    public static void saveSurvey( String surveyData ){

        JSONDeserializer<Survey> deserializer = new JSONDeserializer<Survey>();
        deserializer
                .use( "ndgUser", new NdgUserObjectFactory() )
                .use( "categoryCollection", ArrayList.class )
                .use( "categoryCollection.values", new CategoryObjectFactory() )
                .use( "categoryCollection.values.questionCollection", ArrayList.class )
                .use( "categoryCollection.values.questionCollection.values", new QuestionObjectFactory() )
                .use( "categoryCollection.values.questionCollection.values.questionOptionCollection", ArrayList.class )
                .use( "categoryCollection.values.questionCollection.values.questionOptionCollection.values", new QuestionOptionObjectFactory() )
                .use( "categoryCollection.values.questionCollection.values.questionType", new QuestionTypeObjectFactory());

        Survey survey = deserializer.deserialize( surveyData, new SurveyObjectFactory() );
        survey.save();

        renderText( survey.id );
}

    public static void getSurvey( long surveyId ){//TODO exclude more not needed params
        Survey survey = Survey.findById( surveyId );
        JSONSerializer surveySerializer = new JSONSerializer();
        surveySerializer.include(
                    "categoryCollection",
                    "categoryCollection.questionCollection",
                    "categoryCollection.questionCollection.questionType.id",
                    "categoryCollection.questionCollection.questionOptionCollection"
                )
                .exclude(
                    "transactionLogCollection",
                    "uploadDate",
                    "resultCollection",
                    "categoryCollection.survey",
                    "categoryCollection.questionCollection.category" )
            .rootName( "survey" );

        for( Category cat : survey.categoryCollection ){
            Collections.sort( cat.questionCollection, new Comparator<Question>(){
                public int compare( Question q, Question q1 ) {
                    if( q1.questionIndex == null || q.questionIndex == null ){
                        return 0;
                    }else{
                        return q.questionIndex - q1.questionIndex;
                    }
                }
            });
        }

        Collections.sort( survey.categoryCollection,  new Comparator<Category>(){
            public int compare( Category t, Category t1 ) {
                if( t1.categoryIndex == null || t.categoryIndex == null ){
                    return 0;
                }else{
                    return t.categoryIndex - t1.categoryIndex;
                }
            }
        });

        renderJSON( surveySerializer.serialize( survey ) );
    }

    public static void upload(File filename, String uploadSurveyId) throws IOException, SurveySavingException {
        InputStreamReader is = null;
        String result = "failure";
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

    public static void delete(String formID)
    {
        Survey deleted = Survey.find("bySurveyId", formID).first();
        deleted.delete();
    }

    public static void addUser(String username, String password, String fullName, String email, String role)
    {
        NdgUser user = new NdgUser(password, username, email, fullName, fullName, "Y", 'Y', 'Y', 'Y');
        Company userCompany = Company.all().first();
        user.company = userCompany;
        user.save();
        UserRole mapRole = new UserRole();
        mapRole.ndgUser = user;
        mapRole.ndgRole = NdgRole.find("byRoleName", role).first();
        mapRole.save();
    }

    public static void deleteUser(String userId)
    {
        NdgUser deleted = NdgUser.find("byId", Long.parseLong(userId)).first();
        deleted.delete();
    }

    public static void duplicate(String formID)
    {
        Survey origin = Survey.find("bySurveyId", formID).first();

        SecureRandom random = new SecureRandom();
        String newId = new BigInteger(40, random).toString(32);

        Survey copy = SurveyDuplicator.plainCopy(origin, newId);

        copy.save();
    }

    public static void sendSurveys(String formID, String users[])
    {
        for (int i = 0; i < users.length; i++) {
            TransactionLog transaction = new TransactionLog();
            transaction.transactionDate =new Date();
            transaction.transactionType = TransactionlogConsts.TransactionType.TYPE_SEND_SURVEY;
            transaction.transactionStatus = TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE;
            transaction.transmissionMode = TransactionlogConsts.TransactionMode.MODE_HTTP;

            transaction.address = request.remoteAddress;
            transaction.ndgUser  = NdgQuery.getUsersbyId(Long.parseLong(users[i]));
            transaction.survey = NdgQuery.getSurveyById(formID);

            transaction.save();
        }
    }
}