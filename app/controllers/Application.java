package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import flexjson.JSONSerializer;

import models.NdgRole;
import models.NdgUser;

//import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
}