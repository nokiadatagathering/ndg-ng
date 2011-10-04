package controllers;

import controllers.exceptions.SurveySavingException;
import controllers.logic.SurveyPersister;
import flexjson.JSONDeserializer;
import flexjson.ObjectBinder;
import java.lang.reflect.Type;
import play.mvc.Controller;
import flexjson.JSONSerializer;
import flexjson.ObjectFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import models.Category;
import models.NdgUser;
import models.Question;
import models.QuestionType;
import models.Survey;
import models.TransactionLog;
import models.constants.TransactionlogConsts;
import models.utils.NdgQuery;
import models.utils.SurveyDuplicator;

public class Application extends Controller {

    public static void index() {
        render();
    }
    static class QuestionTypeObjectFactory implements ObjectFactory{
        
        public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
            QuestionType qType = null;
            HashMap map = ( HashMap )o;
            if( map.containsKey( "id" ) ){
                Integer typeId = ( Integer ) map.get( "id" );
                qType = QuestionType.findById(  new Long(typeId) );
            }
            return qType;
        }
    }    
    
    static class NdgUserObjectFactory implements ObjectFactory{

        public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
            NdgUser user = null;

            HashMap map = ( HashMap )o;
            if( map.containsKey( "id" ) ){
                Integer typeId = ( Integer ) map.get( "id" );
                user = NdgUser.findById( new Long( typeId ) );
            }
            return user;
        }
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
                .use( null, Survey.class )
                .use( "ndgUser", new NdgUserObjectFactory() )
                .use( "categoryCollection", ArrayList.class )
                .use( "categoryCollection.values", Category.class )
                .use( "categoryCollection.values.questionCollection", ArrayList.class )
                .use( "categoryCollection.values.questionCollection.values", Question.class )
                .use( "categoryCollection.values.questionCollection.values.questionType", new QuestionTypeObjectFactory());
        
        Survey survey = deserializer.deserialize( surveyData, Survey.class );
        survey = survey.merge();
        
        for( Category cat : survey.categoryCollection ){
            cat.survey = survey;
            for( Question q : cat.questionCollection ){
                q.category = cat;
            }
        }
        survey.save();
    }

    public static void getSurvey( long surveyId ){//TODO exclude more not needed params
        Survey survey = Survey.findById( surveyId );
        JSONSerializer surveySerializer = new JSONSerializer();
        surveySerializer.include(
                    "categoryCollection",
                    "categoryCollection.questionCollection",
                    "categoryCollection.questionCollection.questionType.id" )
                .exclude(
                    "transactionLogCollection", 
                    "uploadDate", 
                    "resultCollection", 
                    "categoryCollection.survey",
                    "categoryCollection.questionCollection.category" )
            .rootName( "survey" );
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