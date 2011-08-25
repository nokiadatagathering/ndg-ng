package controllers;

import java.util.List;
import play.mvc.Controller;
import flexjson.JSONSerializer;
import models.Survey;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void listSurveys() {
        List<Survey> surveys =  Survey.findAll();
        JSONSerializer surveyListSerializer = new JSONSerializer()
            .include("title","uploadDate","idUser","available","surveyId","ndgUser.username", "resultCollection" )
                .exclude("*").rootName("surveys");
        System.out.println(surveyListSerializer.serialize( surveys ) );
        renderJSON( surveyListSerializer.serialize( surveys ) );
    }
}