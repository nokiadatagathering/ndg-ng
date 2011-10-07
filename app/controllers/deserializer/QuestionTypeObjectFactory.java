package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import models.QuestionType;

/**
 *
 * @author damian.janicki
 */
public class QuestionTypeObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
        QuestionType qType = null;
        HashMap map = ( HashMap )o;
        if( map.containsKey( "id" ) ){
            long lId = new Long( ( Integer ) map.get( "id" ) );
            qType = QuestionType.findById(  lId );
        }
        return qType;
    }
}
