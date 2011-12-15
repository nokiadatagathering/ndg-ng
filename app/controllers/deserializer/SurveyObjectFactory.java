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

import controllers.util.Constants;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.mail.Session;
import models.Category;
import models.NdgUser;
import models.Survey;
import play.cache.Cache;

/**
 *
 * @author damian.janicki
 */
public class SurveyObjectFactory implements ObjectFactory{

    private String username = null;

    public SurveyObjectFactory( String username ){
        this.username = username;
    }

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
        survey.ndgUser = (NdgUser) NdgUser.find( "byUsername", username ).first();
        survey.surveyId = newId;
        survey.lang = "eng";
        survey.uploadDate = new Date();
        survey.available = Constants.SURVEY_BUILDING;

        return survey;
    }
}
