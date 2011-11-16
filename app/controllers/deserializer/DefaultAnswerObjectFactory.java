package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import models.DefaultAnswer;

/**
 *
 * @author damian.janicki
 */
public class DefaultAnswerObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
        DefaultAnswer defAnswer = null;

        HashMap map = ( HashMap )o;
        if( map.containsKey( "id" ) ){
            Long lId = new Long( ( Integer ) map.get( "id" ) );
            defAnswer = DefaultAnswer.findById( lId );
        }else{
            defAnswer = new DefaultAnswer();
            defAnswer.textData = "";
            defAnswer.save();
        }

        ob.bindIntoObject( map, defAnswer, DefaultAnswer.class );
        defAnswer.save();

        return defAnswer;
    }

}
