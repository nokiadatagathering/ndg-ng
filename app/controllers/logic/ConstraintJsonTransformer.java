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

package controllers.logic;

import controllers.logic.SurveyJsonTransformer;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author damian.janicki
 */
public class ConstraintJsonTransformer extends AbstractTransformer {

    String constraintMax = "";
    String constraintMin = "";
    String constraintString  = null;

    public static final String MIN_REGEX = ".\\s*>=\\s* \\d+(-\\d{1,2}-\\d{1,2})?";
    public static final String MAX_REGEX = ".\\s*<=\\s* \\d+(-\\d{1,2}-\\d{1,2})?";


    public void transform( Object o ) {
        if(o == null){
            return;
        }

        constraintString = (String)o;
        constraintString.trim();
        if( constraintString == null || constraintString.isEmpty()){
            return;
        }

        TypeContext typeContext = getContext().peekTypeContext();
        if ( !typeContext.isFirst() ) {
            getContext().writeComma();
        }
        typeContext.setFirst(false);

        parseConstraintString();

        getContext().writeName( SurveyJsonTransformer.CONSTRAINT_MAX );
        getContext().writeQuoted( constraintMax );

        getContext().writeComma();

        getContext().writeName( SurveyJsonTransformer.CONSTRAINT_MIN );
        getContext().writeQuoted( constraintMin );

        constraintMax = "";
        constraintMin = "";
        constraintString  = null;
    }

    public void parseConstraintString(){

        Pattern patternMin = Pattern.compile( MIN_REGEX );
        Matcher matcherMin = patternMin.matcher( constraintString );

        Pattern patternMax = Pattern.compile( MAX_REGEX );
        Matcher matcherMax = patternMax.matcher( constraintString );


        int index;
        if( ( index = constraintString.indexOf( SurveyJsonTransformer.STRING_LENGTH_CONSTRAINT )) != -1 ){
            constraintMax = constraintString.substring(
                        index + SurveyJsonTransformer.STRING_LENGTH_CONSTRAINT.length() ).trim();
            return;
        }

        if( matcherMin.find() ){
            int start = matcherMin.start();
            int stop = matcherMin.end();
            String minString = constraintString.substring( start, stop );
            constraintMin = parseMinMax( minString );
        }

        if( matcherMax.find() ){
            int start = matcherMax.start();
            int stop = matcherMax.end();
            String maxString = constraintString.substring( start, stop );
            constraintMax = parseMinMax( maxString );
        }
    }

    public String parseMinMax(String expr){
        int i = -1;
        if( expr.contains( "<=" ) ){
            i = expr.indexOf( "<=" );
        }else if( expr.contains( ">=" ) ){
            i = expr.indexOf( ">=" );
        }

        return expr.substring( i + 2 ).trim();
    }

    @Override
    public Boolean isInline() {
        return Boolean.TRUE;
    }

}
