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

package controllers;

import play.mvc.Http.StatusCode;

import controllers.logic.AuthorizationUtils;
import controllers.transformer.CSVTransformer;
import controllers.transformer.ExcelTransformer;
import controllers.util.FileUtilities;

import models.Answer;
import models.Category;
import models.constants.QuestionTypesConsts;
import models.Jobs;
import models.NdgResult;
import models.NdgUser;
import models.Question;
import models.Survey;

import flexjson.JSONSerializer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Service extends NdgController {

    private static Log log = LogFactory.getLog(Service.class);
    
    public static void toSchedule(String surveyId, String dateTo, String dateFrom, String email , Boolean complete) {
        Jobs aJob = new Jobs (surveyId, dateTo, dateFrom, email, complete);
        aJob.save();

        Jobs job = null;
        job = Jobs.find("byId", aJob.id).first();

        JSONSerializer jobsSerializer = new JSONSerializer();
        jobsSerializer.rootName("job");
        renderJSON(jobsSerializer.serialize(job));
    }

    public static void toPreview(String surveyId , String resultIDs) {
        // Find the survey
        Survey survey = Survey.findById(Long.decode(surveyId));

        // make an array of results
        Collection<NdgResult> results = new ArrayList<NdgResult>();
        NdgResult result = null;

        // Get the result
        result = NdgResult.find("byId", Long.parseLong(resultIDs)).first();
        if (result != null) {
        	results.add(result);
        }

        // loop the result
        for(NdgResult current : results) {
            List<Question> questions = new ArrayList<Question>();
            questions = survey.getQuestions();
            LinkedList preview = new LinkedList();

            if(questions.isEmpty()) {
                preview.add("No question");
            }

            // loop the questions in the result
            for(Question question : questions) {
                preview.add(question.label);

                // get answers which correspond with questions
                Collection<Answer> answers = CollectionUtils.intersection(question.answerCollection, current.answerCollection);
                if (answers.isEmpty()) {
                    preview.add("No answer");
                }
                else if (answers.size() == 1) {
                    Answer answer = answers.iterator().next();
                    //System.out.println("Answer " + answer.textData);
                    if (answer.question.questionType.typeName.equalsIgnoreCase(QuestionTypesConsts.IMAGE)) {
                        preview.add(answer.binaryData);
                    } 
                    else {
                        preview.add(answer.textData);
                    }
                }
            }

            JSONSerializer previewSerializer = new JSONSerializer();
            previewSerializer.rootName("preview");
            renderJSON(previewSerializer.serialize(preview));
        }

    }

    public static void allToKML(String surveyId) {
        Survey survey = Survey.findById(Long.decode(surveyId));
        Collection<NdgResult> results = new ArrayList<NdgResult>();
        Collection<NdgResult> removalResults = new ArrayList<NdgResult>();
        results = survey.resultCollection;

        for (NdgResult current : results) {
            if(current.latitude == null || current.longitude == null) {
                removalResults.add(current);
            }
        }
        results.removeAll(removalResults);

        ByteArrayOutputStream arqExport = new ByteArrayOutputStream();
        String fileName = surveyId + ".kml";

        try {
            final Kml kml = new Kml();
            final Document document = kml.createAndSetDocument();

            for (NdgResult current : results) {
//                String description = "<![CDATA[ ";
                String description = "";
                int i = 0;

                List<Question> questions = new ArrayList<Question>();
                questions = survey.getQuestions();

                if(questions.isEmpty()) {
                    description += "<b> NO QUESTION </b> <br><br>";
                }

                for(Question question : questions) {
                    i++;
                    description += "<h3><b>" + i + " - " + question.label + "</b></h3><br>";

                    Collection<Answer> answers = CollectionUtils.intersection(question.answerCollection, current.answerCollection);
                    if (answers.isEmpty()) {
                        description += "<br><br>";
                    } else if (answers.size() == 1) {
                        Answer answer = answers.iterator().next();

                        if (answer.question.questionType.typeName.equalsIgnoreCase(QuestionTypesConsts.IMAGE)) {
/*                            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //TODO Include image, right
                            byte[] buf = new byte[1024];                                //now it adds base64 to the img
                            InputStream in = answer.binaryData.get();                   //tag but it doesn't show on
                            int n = 0;                                                  //the map
                            try {
                                while( (n = in.read(buf) ) >= 0) {
                                    baos.write(buf, 0, n);
                                }
                                in.close();
                            } catch(IOException ex) {
                                System.out.println("IO");
                            }

                            byte[] bytes = baos.toByteArray();
                            System.out.println("image = " + Base64.encodeBase64String(bytes));
                            description += "<img src='data:image/jpeg;base64," + Base64.encodeBase64String(bytes)
                                        + "'/> <br><br>"; */
                            description += "<b> #image</b> <br><br>";
                        } else {
                            description += "<h4 style='color:#3a77ca'><b>" + answer.textData + "</b></h4><br>";
                        }
                    }
                }
//                description += " ]]>";

                document.createAndAddPlacemark()
                    .withName(current.title).withOpen(Boolean.TRUE)
                    .withDescription(description)
                    .createAndSetPoint().addToCoordinates(current.longitude + ", " + current.latitude);
            }

            kml.marshal(arqExport);
            send(fileName, arqExport.toByteArray());
        } catch (FileNotFoundException ex) {
        }
    }

    public static void selectedToKML(String surveyId, String resultIDs) {
        Survey survey = Survey.findById(Long.decode(surveyId));
        String[] resultsIds = resultIDs.split(",");

        Collection<NdgResult> results = new ArrayList<NdgResult>();
        Collection<NdgResult> removalResults = new ArrayList<NdgResult>();
        NdgResult result = null;

        if (resultsIds.length > 0) {
            for (int i = 0; i < resultsIds.length; i++) {
                result = NdgResult.find("byId", Long.parseLong(resultsIds[i])).first();
                if (result != null) {
                    results.add(result);
                }
            }
        }

        for (NdgResult current : results) {
            if(current.latitude == null || current.longitude == null) {
                removalResults.add(current);
            }
        }
        results.removeAll(removalResults);

        ByteArrayOutputStream arqExport = new ByteArrayOutputStream();
        String fileName = surveyId + ".kml";

        try {
            final Kml kml = new Kml();
            final Document document = kml.createAndSetDocument();

            for (NdgResult current : results) {
//                String description = "<![CDATA[ ";
                String description = "";
                int i = 0;

                List<Question> questions = new ArrayList<Question>();
                questions = survey.getQuestions();

                if(questions.isEmpty()) {
                    description += "<b> NO QUESTION </b> <br><br>";
                }

                for(Question question : questions) {
                    i++;
                    description += "<h3><b>" + i + " - " + question.label + "</b></h3><br>";

                    Collection<Answer> answers = CollectionUtils.intersection(question.answerCollection, current.answerCollection);
                    if (answers.isEmpty()) {
                        description += "<br><br>";
                    } else if (answers.size() == 1) {
                        Answer answer = answers.iterator().next();

                        if (answer.question.questionType.typeName.equalsIgnoreCase(QuestionTypesConsts.IMAGE)) {
/*                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            InputStream in = answer.binaryData.get();
                            int n = 0;
                            try {
                                while( (n = in.read(buf) ) >= 0) {
                                    baos.write(buf, 0, n);
                                }
                                in.close();
                            } catch(IOException ex) {
                                System.out.println("IO");
                            }

                            byte[] bytes = baos.toByteArray();
                            System.out.println("image = " + Base64.encodeBase64String(bytes));
                            description += "<img src='data:image/jpeg;base64," + Base64.encodeBase64String(bytes)
                                        + "'/> <br><br>"; */
                            description += "<b> #image</b> <br><br>";
                        } else {
                            description += "<h4 style='color:#3a77ca'><b>" + answer.textData + "</b></h4><br>";
                        }
                    }
                }
//                description += " ]]>";

                document.createAndAddPlacemark()
                    .withName(current.title).withOpen(Boolean.TRUE)
                    .withDescription(description)
                    .createAndSetPoint().addToCoordinates(current.longitude + ", " + current.latitude);
            }

            kml.marshal(arqExport);
            send(fileName, arqExport.toByteArray());
        } catch (FileNotFoundException ex) {
        }
    }

    public static void getAllResults(String surveyId) {
        Survey survey = null;

        try {
            survey = Survey.findById(Long.decode(surveyId));

            if (!survey.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                    error( StatusCode.UNAUTHORIZED, "Unauthorized" );
            }
        } catch (NullPointerException npe) {
            error( StatusCode.UNAUTHORIZED, "Unauthorized" );
        }

        Collection<NdgResult> results = new ArrayList<NdgResult>();
        Collection<NdgResult> removalResults = new ArrayList<NdgResult>();
        results = survey.resultCollection;

        for (NdgResult current : results) {
            if (current.latitude == null || current.longitude == null) {
                removalResults.add(current);
            }
        }
        results.removeAll(removalResults);

        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include("id", "resultId", "title", "startTime", "endTime", "ndgUser", "latitude", "longitude")
                                    .exclude("*").rootName("items");

        renderJSON(surveyListSerializer.serialize(results));
    }

    public static void getResults(String surveyId, String resultIDs) {
        String[] resultsIds = resultIDs.split( "," );

        Collection<NdgResult> results = new ArrayList<NdgResult>();
        Collection<NdgResult> removalResults = new ArrayList<NdgResult>();
        NdgResult result = null;

        if (resultsIds.length > 0) {
            for (int i = 0; i < resultsIds.length; i++) {
                try {
                    result = NdgResult.find("byId", Long.parseLong(resultsIds[i])).first();
                    if (!result.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                        error( StatusCode.UNAUTHORIZED, "Unauthorized" );
                    }
                } catch (NullPointerException npe) {
                    error( StatusCode.UNAUTHORIZED, "Unauthorized" );
                }
                if (result != null) {
                    results.add(result);
                }
            }
        }

        for (NdgResult current : results) {
            if (current.latitude == null || current.longitude == null) {
                removalResults.add(current);
            }
        }
        results.removeAll(removalResults);

        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include("id", "resultId", "title", "startTime", "endTime", "ndgUser", "latitude", "longitude")
                                    .exclude("*").rootName("items");

        renderJSON(surveyListSerializer.serialize(results));
    }

    public static void surveyHasImages(String surveyId) {
        Survey survey = null;

        try {
            survey = Survey.findById(Long.decode(surveyId));

            if (!survey.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                error( StatusCode.UNAUTHORIZED, "Unauthorized" );
            }
        } catch (NullPointerException npe) {
            error( StatusCode.UNAUTHORIZED, "Unauthorized" );
        }

        boolean hasImages = false;

        for (Category category : survey.categoryCollection) {
            for (Question question :category.questionCollection) {
                if (question.questionType.typeName.equals(QuestionTypesConsts.IMAGE)) {
                    hasImages = true;
                    break;
                }
            }
        }

        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include("*").rootName("hasImages");
        renderJSON(surveyListSerializer.serialize(hasImages));
    }

    public static void prepareselected(String ids, String fileFormat, Boolean exportWithImages) {
        String fileType = "";
        byte[] fileContent = null;

        String[] resultsIds = ids.split(",");

        Collection<NdgResult> results = new ArrayList<NdgResult>();
        NdgResult result = null;

        if (resultsIds.length > 0) {
            for (int i = 0; i < resultsIds.length; i++) {
                try {
                    result = NdgResult.find("byId", Long.parseLong(resultsIds[i])).first();
                    if (!result.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                        error( StatusCode.UNAUTHORIZED, "Unauthorized" );
                    }
                }
                catch (NullPointerException npe) {
                    error( StatusCode.UNAUTHORIZED, "Unauthorized" );
                }
                if (result != null) {
                    results.add(result);
                }
            }
        }

        if (exportWithImages == true) {
            new File(result.survey.surveyId).mkdir();
            new File(result.survey.surveyId + File.separator + "photos").mkdir();
        }

        if (FileUtilities.CSV.equalsIgnoreCase(fileFormat)) {
            CSVTransformer transformer = new CSVTransformer(result.survey, results, exportWithImages);
            fileContent = transformer.getBytes();
            fileType = FileUtilities.CSV;
        } else if (FileUtilities.XLS.equalsIgnoreCase(fileFormat)) {
            try {
                ExcelTransformer transformer = new ExcelTransformer(result.survey, results, exportWithImages);
                fileContent = transformer.getBytes();
                fileType = FileUtilities.XLS;
            } catch (NullPointerException npe) {
            }
        }

        if (exportWithImages == true) {
            FileUtilities.zipSurvey(result.survey.surveyId, fileContent, fileType, FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.ZIP);
            File zipFile = new File(FileUtilities.SURVEY + result.survey.surveyId + FileUtilities.ZIP);
            File zipDir = new File(result.survey.surveyId);
            try {
                fileContent = FileUtilities.getBytesFromFile(zipFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileType = FileUtilities.ZIP;
            zipFile.delete();
            FileUtilities.deleteDir(zipDir);
        }
        String fileName = null;
        try {
            fileName = FileUtilities.SURVEY + result.survey.surveyId + fileType;
        } catch (NullPointerException npe) {
        }
        send( fileName, fileContent );
    }

    public static void prepare(String surveyId, String fileFormat, Boolean exportWithImages) {
        String fileType = "";
        byte[] fileContent = null;

        Survey survey = null;

        try {
            survey = Survey.findById(Long.decode(surveyId));

            if (!survey.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                error( StatusCode.UNAUTHORIZED, "Unauthorized" );
            }
        } catch (NullPointerException npe) {
            error( StatusCode.UNAUTHORIZED, "Unauthorized" );
        }

        if (exportWithImages == true) {
            new File(survey.surveyId).mkdir();
            new File(survey.surveyId + File.separator + "photos").mkdir();
        }

        if (FileUtilities.CSV.equalsIgnoreCase(fileFormat)) {
            CSVTransformer transformer = new CSVTransformer( survey, exportWithImages );
            fileContent = transformer.getBytes();//this is export all functionality
            fileType = FileUtilities.CSV;
        } else if (FileUtilities.XLS.equalsIgnoreCase(fileFormat)) {
            ExcelTransformer transformer = new ExcelTransformer(survey, exportWithImages);
            fileContent = transformer.getBytes();//this is export all functionality
            fileType = FileUtilities.XLS;
        }

        if (exportWithImages == true) {
            FileUtilities.zipSurvey(survey.surveyId, fileContent, fileType, FileUtilities.SURVEY + survey.surveyId + FileUtilities.ZIP);
            File zipFile = new File(FileUtilities.SURVEY + survey.surveyId + FileUtilities.ZIP);
            File zipDir = new File(survey.surveyId);
            try {
                fileContent = FileUtilities.getBytesFromFile(zipFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileType = FileUtilities.ZIP;
            zipFile.delete();
            FileUtilities.deleteDir(zipDir);
        }

        String fileName = FileUtilities.SURVEY + survey.surveyId + fileType;
        send(fileName, fileContent);
    }

    private static void send(String fileName, byte[] fileContent) {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(fileContent);
            renderBinary(in, fileName);
        } catch (NullPointerException npe) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }

}
