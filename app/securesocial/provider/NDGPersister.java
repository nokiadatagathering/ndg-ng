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

package securesocial.provider;

import play.libs.Codec;

import models.NdgUser;
import models.Company;
import models.NdgRole;
import models.UserRole;

public class NDGPersister {

    public static SocialUser find(UserId id) {
        NdgUser ndgUser = NdgUser.find("byUserName", id.id).first();
        SocialUser user = new SocialUser();
        user.id = id;
        try {
            user.firstName = ndgUser.firstName;
            user.lastName = ndgUser.lastName;
            user.email = ndgUser.email;
        } catch (Exception ex) {
            return null;
        }

        return user;
    }

    public static void save(SocialUser user) {
        NdgUser ndgUser = new NdgUser(user.password, user.id.id, user.email, user.firstName, user.lastName, user.phoneNumber, user.id.id, 'N', 'Y', 'Y');
        Company userCompany = new Company(user.company, "type", "country", "industry", "size");
        userCompany.save();
        ndgUser.company = userCompany;
        ndgUser.save();
        UserRole mapRole = new UserRole();
        mapRole.ndgUser = ndgUser;
        mapRole.ndgRole = NdgRole.find("byRoleName", "Admin").first();
        mapRole.save();
    }

    public static String createActivation(SocialUser user) {
        final String uuid = Codec.UUID();
        NdgUser ndgUser = NdgUser.find("byUserName", user.id.id).first();
        ndgUser.validationKey = uuid;
        ndgUser.save();
        return uuid;
    }

    public static boolean activate(String uuid) {
        NdgUser ndgUser = NdgUser.find("byValidationKey", uuid).first();
        boolean result = false;

        if( ndgUser != null ) {
            ndgUser.userValidated =  'Y';
            ndgUser.save();
            result = true;
        }
        return result;
    }

    public static void deletePendingActivations() {
    }
}
