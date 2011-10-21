package controllers;

import controllers.deserializer.CategoryObjectFactory;
import controllers.deserializer.NdgUserObjectFactory;
import controllers.deserializer.QuestionObjectFactory;
import controllers.deserializer.QuestionOptionObjectFactory;
import controllers.deserializer.QuestionTypeObjectFactory;
import controllers.deserializer.SurveyObjectFactory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import models.Category;
import models.Question;
import models.Survey;

/**
 *
 * @author damian.janicki
 */
public class SurveyJsonTransformer {

    public static String getJsonSurvey( long surveyId ){

        Survey survey = Survey.findById( surveyId );
        sortSurvey( survey );

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

        return surveySerializer.serialize( survey );
    }

    public static long saveSurvey( String jsonSurvey ){
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

        Survey survey = deserializer.deserialize( jsonSurvey, new SurveyObjectFactory() );
        survey.save();

        return survey.id;
    }

    private static void sortSurvey(Survey survey){
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
    }

}
