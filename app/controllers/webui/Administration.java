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
