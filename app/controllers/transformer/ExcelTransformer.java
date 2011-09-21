/*
 *  Copyright (C) 2010  INdT - Instituto Nokia de Tecnologia
 *
 *  NDG is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  NDG is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with NDG.  If not, see <http://www.gnu.org/licenses/
 */
package controllers.transformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Answer;
import models.NdgResult;
import models.Question;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import models.Survey;
import models.constants.QuestionTypesConsts;

public class ExcelTransformer extends ResultsTransformer {

    public ExcelTransformer( Survey survey, Boolean exportWithImages ) {
        super( survey, exportWithImages );
    }

    public ExcelTransformer( Survey survey, Collection<NdgResult> results, Boolean exportWithImages ) {
        super( survey, results, exportWithImages );
    }

    public byte[] getBytes() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet1");

        if ( results == null ) {
            results = survey.resultCollection;
        }

        /** Header **/
        HSSFRow row = sheet.createRow( (short) 0 );
        int fieldcounter = 0;
        row.createCell( (short) fieldcounter++ ).setCellValue( "ResultId" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "SurveyId" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "Title" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "Start time" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "End time" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "User" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "Lat" );
        row.createCell( (short) fieldcounter++ ).setCellValue( "Lon" );


        /** Header Fields**/
        for ( Question question :survey.getQuestions() ) {
            row.createCell( (short) fieldcounter++ ).setCellValue( question.label );
        }

        int countrow = 0;
        row = sheet.createRow( (short) ++countrow );

        for ( NdgResult result :results ) {
            fieldcounter = 0;
            row.createCell( (short) fieldcounter++ ).setCellValue( result.resultId );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.survey.surveyId );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.title );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.startTime );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.endTime );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.ndgUser.username );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.latitude );
            row.createCell( (short) fieldcounter++ ).setCellValue( result.longitude );

            for ( Question question :survey.getQuestions() ) {//to ensure right answer order
                question.answerCollection.retainAll( result.answerCollection );//only one should left, hope that it does not modify results
                if ( question.answerCollection.isEmpty() ) {
                    row.createCell( (short) fieldcounter++ ).setCellValue( "NULL - No answer" );
                } else if ( question.answerCollection.size() == 1 ) {
                    Answer answer = question.answerCollection.iterator().next();
                    if ( answer.question.questionType.typeName.equalsIgnoreCase( QuestionTypesConsts.IMAGE ) ) {//TODO handle other binary data
                        row.createCell( (short) fieldcounter++ ).setCellValue( storeImagesAndGetValueToExport( survey.surveyId, result.resultId, answer.id, answer.binaryData ) );
                    } else {
                        String value = answer.textData;
                        value = value.trim().replaceAll( "\n", "" );
                        row.createCell( (short) fieldcounter++ ).setCellValue( value );
                    }
                } else {
                    Logger.getAnonymousLogger().log( Level.WARNING, "to many answers. ResID={0}questioId={1}answerCount={2}", new Object[]{ result.resultId, question.id, question.answerCollection.size() } );
                }
            }
            row = sheet.createRow( (short) ++countrow );
        }
        try {
            wb.write( out );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
