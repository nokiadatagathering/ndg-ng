package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import models.Question;
import models.QuestionOption;
import play.cache.Cache;

/**
 *
 * @author damian.janicki
 */
public class QuestionOptionObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
        QuestionOption option = null;
        Question question = (Question) Cache.get( "currentQuestion" );

        HashMap map = ( HashMap )o;

        if( map.containsKey( "id" ) ){
            Long lId = new Long( ( Integer ) map.get( "id" ) );
            option = QuestionOption.findById( lId );

            if( map.containsKey( "isDelete" ) ){
                option.delete();
                option = null;
            }else{
                ob.bindIntoObject( map, option, QuestionOption.class );
                option.save();
            }

        }else if( !map.containsKey( "id" ) && !map.containsKey( "isDelete" ) ){
            option = new QuestionOption();
            ob.bindIntoObject( map, option, QuestionOption.class );

            question.questionOptionCollection.add( option );
            option.question = question;
            option.save();
        }

        return option;
    }

}
