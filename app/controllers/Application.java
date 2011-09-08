package controllers;

import controllers.exceptions.SurveySavingException;
import controllers.logic.SurveyPersister;
import java.util.List;
import play.mvc.Controller;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import models.NdgResult;
import models.NdgUser;
import models.Survey;
import models.utils.SurveyDuplicator;

public class Application extends Controller {

    private static final int RESULTS_PER_SIDE = 10;

    public static void index() {
        render();
    }

    public static void listResults( int surveyId, int startIndex, boolean isAscending, String orderBy ) {
        String query = "survey_id = "+ String.valueOf( surveyId ) + " order by "+ orderBy + ( isAscending ? " asc" : " desc" );

        List<NdgResult> results = NdgResult.find( query ).fetch();//.from( RESULTS_PER_SIDE * startIndex ).fetch( RESULTS_PER_SIDE );
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.transform( new NdgResultCollectionTransformer(), "resultCollection" );
        surveyListSerializer.include( "id","resultId", "title", "startTime", "ndgUser.username", "latitude" )
            .exclude( "*" ).rootName( "results" );
        renderJSON( surveyListSerializer.serialize( results ) );
    }

    public static void listSurveysCount() {
        long surveys =  Survey.count();
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include( "*" ).rootName( "surveysCount" );
        renderJSON( surveyListSerializer.serialize( pages( surveys ) ) );
    }

    public static void listResultsCount( int surveyId ) {
        long results =  NdgResult.find( "survey_id", String.valueOf( surveyId ) ).fetch().size();
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include( "*" ).rootName( "resultsCount" );
        renderJSON( surveyListSerializer.serialize( pages( results) ) );
    }

    public static void listSurveys( int startIndex, boolean isAscending, String orderBy ) {
        String query = "order by "+ orderBy + ( isAscending ? " asc" : " desc" );

        List<Survey> surveys =  Survey.find( query ).from( RESULTS_PER_SIDE * startIndex ).fetch( RESULTS_PER_SIDE );
        serializeSurveys( surveys );
    }

    public static void listSurveysByResults( int startIndex, boolean isAscending ) {

        List<Survey> surveys =  Survey.all().fetch();
        Collections.sort( surveys, new SurveyNdgResultCollectionComapator() );
        if( !isAscending ) {
            Collections.reverse( surveys );
        }

        int subListEndIndex = startIndex * RESULTS_PER_SIDE + RESULTS_PER_SIDE < surveys.size() ?
                                                startIndex * RESULTS_PER_SIDE + RESULTS_PER_SIDE :
                                                surveys.size();

        List<Survey> subList = surveys.subList( startIndex * RESULTS_PER_SIDE , subListEndIndex );
        serializeSurveys( subList );
    }

    public static void listUsers( String orderBy ) {
        List<NdgUser> users = NdgUser.all().fetch();
        serializeUsers(users);
    }

    private static void serializeUsers(List<NdgUser> users) {
        JSONSerializer userListSerializer = new JSONSerializer();
        userListSerializer.transform(new NdgResultCollectionTransformer() , "resultCollection");
        userListSerializer.include("id","username", "phoneNumber", "email" ).exclude("*").rootName("users");
        renderJSON(userListSerializer.serialize(users));
    }

    private static void serializeSurveys( List<Survey> subList ) {
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.transform( new NdgResultCollectionTransformer(), "resultCollection" );
        surveyListSerializer.include( "id", "title", "uploadDate", "idUser", "surveyId", "ndgUser.username", "resultCollection" )
            .exclude( "*" ).rootName( "surveys" );
        renderJSON( surveyListSerializer.serialize( subList ) );
    }

    private static long pages( long entries ) {
        long pages = entries/RESULTS_PER_SIDE + ( entries%RESULTS_PER_SIDE > 0 ? 1 : 0 );
        return pages == 0 ? 1 : pages;
    }

    public static class SurveyNdgResultCollectionComapator implements Comparator<Survey> {

        public int compare( Survey o1, Survey o2 ) {
            return o1.resultCollection.size() < o2.resultCollection.size() ? 1 : -1;
        }
    }

    private static class NdgResultCollectionTransformer extends AbstractTransformer {

        public void transform( Object collection ) {
            Collection ndgResultCollection = (Collection)collection;
            getContext().write( String.valueOf( ndgResultCollection.size() ) );
        }
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

    public static void duplicate(String formID)
    {
        Survey origin = Survey.find("bySurveyId", formID).first();

        SecureRandom random = new SecureRandom();
        String newId = new BigInteger(40, random).toString(32);

        Survey copy = SurveyDuplicator.plainCopy(origin, newId);

        copy.save();
    }
}