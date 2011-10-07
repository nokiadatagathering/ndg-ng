package controllers.deserializer;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import models.Category;
import models.Question;
import models.Survey;
import play.cache.Cache;

/**
 *
 * @author damian.janicki
 */
public class CategoryObjectFactory implements ObjectFactory{

    public Object instantiate( ObjectBinder ob, Object o, Type type, Class type1 ) {

        Category cat = null;
        Survey s = (Survey)Cache.get( "currentSurvey" );
        s.merge();

        HashMap map = ( HashMap )o;
        if( map.containsKey( "id" ) ){
            long lId = new Long( (Integer)map.get( "id" ) );
            cat = Category.findById( lId );

            if( map.containsKey( "isDelete" ) ){ // delete object
                cat.delete();
                cat = null;
            }else{ //edit object
                bind( ob, map, cat );
                cat.save();
            }
        }else if ( !map.containsKey( "id" ) && !map.containsKey( "isDelete" ) ){ // add new object
            cat = createNewCategory();
            s.categoryCollection.add( cat );
            cat.survey = s;
            cat.save();

            bind( ob, map, cat );
            cat.save();
        }
        return cat;
    }

    private void bind(ObjectBinder ob, HashMap map, Category category){
            Cache.add( "currentCategory", category );
            ob.bindIntoObject( map, category, Category.class );
            Cache.delete( "currentCategory" );
    }

    private Category createNewCategory(){
        Category cat = new Category();
        cat.label = "";
        cat.objectName = "";
        cat.questionCollection = new ArrayList<Question>();
        return cat;
    }
}