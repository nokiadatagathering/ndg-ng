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

package jobs;

import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;

import controllers.transformer.ExcelTransformer;
import controllers.util.FileUtilities;

import models.Category;
import models.constants.QuestionTypesConsts;
import models.Jobs;
import models.NdgResult;
import models.Question;
import models.Survey;

import notifiers.Mails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class Scheduler extends Job {

    public Long id;
    public String surveyId;
    public String dateTo;
    public String dateFrom;
    public String email;

    public Scheduler(Long id, String surveyId, String dateTo, String dateFrom, String email) {
        this.id = id;
        this.surveyId = surveyId;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
        this.email = email;
        boolean exists = (new File(Play.configuration.getProperty("attachments.path"))).exists();

        if (exists) {
            doJob();
        } else {
            new File(Play.configuration.getProperty("attachments.path")).mkdirs();
            doJob();
        }
    }
    
    public void doJob() {

        byte[] fileContent = null;
        String fileType = "";

        Boolean exportWithImages = surveyHasImages(surveyId);
        //System.out.println("The survey has images is " + exportWithImages);
        Jobs jobber = null;
        jobber = Jobs.find( "byId", id ).first();


        Collection<NdgResult> results = new ArrayList<NdgResult>();
        NdgResult result = null;
        List resultsIds0 = null;

        String query = "SELECT ndg_result.id FROM ndg_result, transaction_log WHERE ndg_result.date_sent BETWEEN '" + dateFrom +  "' and '"  + dateTo +  "' AND transaction_log.survey_id =" + Long.decode(surveyId) + " AND ndg_result.ndg_result_id = transaction_log.id_result";

        resultsIds0 = JPA.em().createNativeQuery(query).getResultList();


        if (resultsIds0.size() != 0) {
            for (int i = 0; i < resultsIds0.size(); i++) {
                String o = resultsIds0.get(i).toString();
 
                result = NdgResult.find( "byId", Long.parseLong(o)).first();

                if (result != null) {
                    results.add(result);
                }
             
            }

            ExcelTransformer transformer = new ExcelTransformer( result.survey, results, exportWithImages );
            fileContent = transformer.getBytes();

            if ( exportWithImages == true ) {
                new File(result.survey.surveyId ).mkdir();
                new File(result.survey.surveyId + File.separator + "photos" ).mkdir();
                fileType = FileUtilities.XLS;
                FileUtilities.zipSurvey(result.survey.surveyId, fileContent, fileType, Play.configuration.getProperty("attachments.path") + "/" + FileUtilities.SURVEY + surveyId + FileUtilities.ZIP);
                File zipFile = new File(FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.ZIP);
                File zipDir = new File(result.survey.surveyId );
                try {
                    FileOutputStream fop1 = new FileOutputStream(zipFile);
                    fileContent = FileUtilities.getBytesFromFile( zipFile );
                    fop1.write(fileContent);
                    fop1.flush();
                    fop1.close();
                    //System.out.println("Done with images");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                zipFile.delete();
                FileUtilities.deleteDir(zipDir);
            }

            if (exportWithImages == false) {
                try {
                    File file = new File(Play.configuration.getProperty("attachments.path") + "/" + FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.XLS);
                    FileOutputStream fop = new FileOutputStream(file);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    byte[] contentInBytes =  transformer.getBytes();
                    fop.write(contentInBytes);
                    fop.flush();
                    fop.close();
                    //System.out.println("Done no images");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String path = null;

            if (exportWithImages == true) {
                path = Play.getFile(Play.configuration.getProperty("attachments.path") + "/" + FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.ZIP ).getPath();
            } else {
                path = Play.getFile(Play.configuration.getProperty("attachments.path") + "/" + FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.XLS ).getPath();
            }

            Mails.sendScheduledResults(email, path);

            jobber.setComplete(true);
            jobber.save();

        } else {
            System.out.println("The job failed");
        }
    }

    public static Boolean surveyHasImages( String surveyId ) {
        Survey survey = Survey.findById( Long.decode( surveyId ) );

        boolean hasImages = false;

        for (Category category : survey.categoryCollection) {
            for (Question question :category.questionCollection) {
                if (question.questionType.typeName.equals( QuestionTypesConsts.IMAGE)) {
                    hasImages = true;
                }

            }
        }
        return hasImages;
    }

}
