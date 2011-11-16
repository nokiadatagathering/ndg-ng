package controllers;

import controllers.logic.AuthorizationUtils;
import models.Company;
import models.NdgGroup;
import models.NdgRole;
import models.NdgUser;
import models.UserRole;
import play.mvc.Controller;

public class UserManager extends Controller {

    public static void addUser( String username, String password, String firstName,
                                String lastName, String email, String role,
                                String phoneNumber )
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        NdgUser user = new NdgUser( password, username, email,
                                    firstName, lastName,
                                    phoneNumber,
                                    "Y", 'Y', 'Y', 'Y');
        Company userCompany = Company.all().first();
        user.company = userCompany;
        user.save();
        UserRole mapRole = new UserRole();
        mapRole.ndgUser = user;
        mapRole.ndgRole = NdgRole.find("byRoleName", role).first();
        mapRole.save();
    }

    public static void addUserToGroup( long username, String groupname )
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        NdgUser user = NdgUser.findById( username );
        NdgGroup group = NdgGroup.find( "byGroupName", groupname ).first();
        user.ndg_group = group;
        user.save();
    }

    public static void addGroup( String groupname )
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        NdgGroup group = new NdgGroup();
        group.groupName = groupname;
        group.save();
    }

    public static void deleteUser(String userId)
    {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        NdgUser deleted = NdgUser.find("byId", Long.parseLong(userId)).first();
        deleted.delete();
    }

}
