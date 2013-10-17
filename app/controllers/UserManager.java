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

import models.Company;
import models.NdgGroup;
import models.NdgRole;
import models.NdgUser;
import models.UserRole;

public class UserManager extends NdgController {

    public static void addUser(String username, String password, String firstName,
                               String lastName, String email, String role,
                               String phoneNumber) {
        NdgUser currentUser = NdgUser.find("byUserName", session.get("ndgUser")).first();
        NdgUser user = new NdgUser(password, username, email,
                                   firstName, lastName,
                                   phoneNumber,
                                   currentUser.userAdmin, 'Y', 'Y', 'Y');
        Company userCompany = currentUser.company;
        user.company = userCompany;
        user.save();
        UserRole mapRole = new UserRole();
        mapRole.ndgUser = user;
        mapRole.ndgRole = NdgRole.find("byRoleName", role).first();
        mapRole.save();
    }

    public static void addUserToGroup(long username, String groupname) {
        NdgUser user = null;

        try {
            user = NdgUser.findById(username);

            if (!user.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                    error(StatusCode.UNAUTHORIZED, "Unauthorized");
            }
        } catch (NullPointerException npe) {
            error(StatusCode.UNAUTHORIZED, "Unauthorized");
        }

        NdgGroup group = NdgGroup.find("byGroupName", groupname).first();
        user.ndg_group = group;
        user.save();
    }

    public static void addGroup(String groupname) {
        NdgUser user = NdgUser.find("byUserName", session.get("ndgUser")).first();
        NdgUser currentUserAdmin = NdgUser.find("byUserName", user.userAdmin).first();

        NdgGroup group = new NdgGroup();
        group.groupName = groupname;
        group.ndgUser = user;
        group.save();
    }

    public static void deleteUser(String userId) {
        NdgUser deleted = null;

        try {
            deleted = NdgUser.find("byId", Long.parseLong(userId)).first();

            if (!deleted.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                    error(StatusCode.UNAUTHORIZED, "Unauthorized");
            }
        } catch (NullPointerException npe) {
            error(StatusCode.UNAUTHORIZED, "Unauthorized");
        }

        String username = deleted.username;
        deleted.delete();
        if (username.equals(deleted.userAdmin)) {
            Company userCompany = deleted.company;
            userCompany.delete();
        }
    }

    public static void deleteGroup(String groupname) {
        NdgGroup group = null;

        try {
            group = NdgGroup.find("byGroupName", groupname).first();

            if (!group.ndgUser.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                    error(StatusCode.UNAUTHORIZED, "Unauthorized");
            }
        } catch (NullPointerException npe) {
            error(StatusCode.UNAUTHORIZED, "Unauthorized");
        }

        for (NdgUser user : group.userCollection) {
            user.ndg_group = null;
            user.save();
        }
        group.userCollection.clear();
        group.delete();
    }

    public static void editUser(String username, String password, String firstName,
                                String lastName, String email, String role,
                                String phoneNumber) {
        NdgUser user = null;

        try {
            user = NdgUser.find("byUserName", username).first();

            if (!user.userAdmin.equals(AuthorizationUtils.getSessionUserAdmin(session.get("ndgUser")))) {
                    error(StatusCode.UNAUTHORIZED, "Unauthorized");
            }
        } catch (NullPointerException npe) {
            error(StatusCode.UNAUTHORIZED, "Unauthorized");
        }

        if (!password.equals("")) {
            user.password = password;
        }
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.phoneNumber = phoneNumber;
        user.save();

        UserRole mapRole = UserRole.find("byUserName", user.username).first();
        mapRole.ndgRole = NdgRole.find("byRoleName", role).first();
        mapRole.save();
    }
}
