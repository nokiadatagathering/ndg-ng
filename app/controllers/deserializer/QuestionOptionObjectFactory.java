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
