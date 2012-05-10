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

    public static NdgUser find(String username) {
        NdgUser user;
        try {
            user = NdgUser.find("byUserName", username).first();
        } catch (Exception ex) {
            return null;
        }

        return user;
    }

    public static void save(NdgUser user) {
        user.company.save();
        user.save();
        UserRole mapRole = new UserRole();
        mapRole.ndgUser = user;
        mapRole.ndgRole = NdgRole.find("byRoleName", "Admin").first();
        mapRole.save();
    }

    public static String createActivation(NdgUser user) {
        final String uuid = Codec.UUID();

        user.validationKey = uuid;
        user.save();
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
