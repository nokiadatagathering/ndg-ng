package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import models.Category;
import models.NdgUser;
import models.Survey;
import play.cache.Cache;

/**
 *
 * @author damian.janicki
 */
public class SurveyObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
        Survey survey = null;

        HashMap map = ( HashMap )o;
        if( map.containsKey( "id" ) ){
            long lId = new Long( ( Integer ) map.get( "id" ) );
            survey = Survey.findById( lId );

            bind( ob, map, survey );
        }else {
            survey = createNewSurvey();
            survey.save();

            bind( ob, map, survey );
            survey.save();
        }
        return survey;
    }

    private void bind( ObjectBinder ob, HashMap map, Survey survey ){
        Cache.add( "currentSurvey", survey );
        ob.bindIntoObject( map, survey, Survey.class );
        Cache.delete( "currentSurvey" );
    }

    private Survey createNewSurvey(){

        SecureRandom random = new SecureRandom();
        String newId = new BigInteger(40, random).toString(32);

        Survey survey = new Survey();
        survey.title = "";
        survey.categoryCollection = new ArrayList<Category>();
        survey.ndgUser = (NdgUser) NdgUser.all().fetch().get( 0 ); //TODO get current user
        survey.surveyId = newId;


        return survey;
    }
}
