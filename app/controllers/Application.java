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
        public Object instantiate(ObjectBinder ob, Object o, Type type, Class type1) {
            QuestionType qType = null;
            HashMap map = (HashMap)o;
            if(map.containsKey("type")){
                String typeId = (String)map.get("type");
                qType = QuestionType.findById(Long.decode(typeId));
            }
            return qType;
        }
    }

    public static void questionType(){
        List<QuestionType> types = QuestionType.find("bySupported", 1).fetch();

        JSONSerializer typeSerializer = new JSONSerializer();
        typeSerializer.include("typeName", "id").exclude("*").rootName("types");
        renderJSON(typeSerializer.serialize(types));
    }

    public static void saveSurvey(String id, String surveyData){

        JSONDeserializer<ArrayList<Category>> deserializer = new JSONDeserializer<ArrayList<Category>>();
        deserializer
                .use("values", Category.class)
                .use("values.questionCollection", ArrayList.class)
                .use("values.questionCollection.values", Question.class)
                .use("values.questionCollection.values.questionType", new QuestionTypeObjectFactory());
        ArrayList<Category> categoryList = deserializer.deserialize(surveyData, ArrayList.class);


        Survey survey = Survey.findById( Long.decode( id ) );

        for(Category cat : survey.categoryCollection){
            cat.delete();
        }

        for(Category cat : categoryList){
            for(Question q : cat.questionCollection){
                q.category = cat;
            }
            cat.survey = survey;
            survey.categoryCollection.add(cat);
            cat.save();
        }
    }

    public static void getSurvey( long surveyId){
        Survey survey = Survey.findById( surveyId );
        JSONSerializer surveySerializer = new JSONSerializer();
        surveySerializer.include("categoryCollection",
                "categoryCollection.questionCollection",
                "categoryCollection.questionCollection.questionType",
                "categoryCollection.questionCollection.questionType.id").exclude(
                "transactionLogCollection", "uploadDate", "surveyId", "resultCollection", "ndgUser" )
            .rootName( "survey" );
        renderJSON(surveySerializer.serialize(survey));
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