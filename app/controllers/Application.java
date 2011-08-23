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

    public static void users() {
        EntityManager em = JPA.em();
        Query getAllUsersQuery = em.createNamedQuery("NdgUser.findAll");
        List<NdgUser> users =  getAllUsersQuery.getResultList();
        System.out.println(users.size());
        render(users);
    }
    
    public static void roles_json () {
        EntityManager em = JPA.em();
        Query getAllUsersQuery = em.createNamedQuery("NdgRole.findAll");
        List<NdgRole> roles =  getAllUsersQuery.getResultList();
        
        JSONSerializer roleListSerializer = new JSONSerializer().include(
                "idRole",
                "roleName").exclude("*").rootName("roles");

        renderJSON(roleListSerializer.serialize(roles));       
    }
}