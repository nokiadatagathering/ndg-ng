/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

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