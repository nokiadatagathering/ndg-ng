package controllers.webui;

import java.util.List;


import models.utils.NdgQuery;
import models.NdgRole;
import models.NdgUser;
import flexjson.JSONSerializer;
import play.mvc.Controller;

public class Administration extends Controller {
    
    public static void index () {
        render();
    }
    
    public static void roles () {
        List<NdgRole> roles =  NdgRole.findAll();
                
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
    
    public static void user(Long id) {
        NdgUser user = NdgQuery.getUsersbyId(id);
        JSONSerializer userListSerializer = new JSONSerializer().prettyPrint(true);
        renderJSON(userListSerializer.serialize(user));       
    }
}
