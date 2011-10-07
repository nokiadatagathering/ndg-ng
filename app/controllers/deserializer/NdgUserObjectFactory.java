package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import models.NdgUser;

/**
 *
 * @author damian.janicki
 */
public class NdgUserObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {
        NdgUser user = null;

        HashMap map = ( HashMap )o;
        if( map.containsKey( "id" ) ){
            long lId = new Long( ( Integer ) map.get( "id" ) );
            user = NdgUser.findById(  lId );
        }
        return user;
    }
}
