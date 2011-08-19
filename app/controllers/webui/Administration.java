package controllers.webui;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.org.indt.ndg.server.persistence.NdgQuery;
import br.org.indt.ndg.server.persistence.structure.NdgRole;
import br.org.indt.ndg.server.persistence.structure.NdgUser;
import flexjson.JSONSerializer;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class Administration extends Controller {
	
	public static void index () {
		render();
	}
	
    public static void roles () {
		List<NdgRole> roles =  JPA.em().createNamedQuery("NdgRole.findAll")
											.getResultList();
		JSONSerializer roleListSerializer = new JSONSerializer()
			.include("idRole","roleName").exclude("*").rootName("roles");
        renderJSON(roleListSerializer.serialize(roles));       
    }
    
    public static void users () {
		List<NdgUser> users =  NdgQuery.listAllUsers();											
		JSONSerializer userListSerializer = new JSONSerializer()
			.include("idUser","username", "email").exclude("*").rootName("users");
        renderJSON(userListSerializer.serialize(users));       
    }
    
    public static void user(String id) {
    	NdgUser user = NdgQuery.getUsersbyId(id);
    	JSONSerializer userListSerializer = new JSONSerializer().prettyPrint(true);
    	renderJSON(userListSerializer.serialize(user));       
    }
}
