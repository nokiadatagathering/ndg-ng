package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import flexjson.JSONSerializer;

import br.org.indt.ndg.server.persistence.structure.NdgRole;
import br.org.indt.ndg.server.persistence.structure.NdgUser;

//import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
}