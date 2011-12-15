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

package controllers.logic;

import models.utils.NdgQuery;
import models.UserBalance;
import models.NdgUser;
import controllers.util.PropertiesUtil;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserBalanceManager {

    private static final Logger log = Logger.getLogger( UserBalanceManager.class.getName() );
    private static final String NDG_MODE = "NDG_MODE";
    private static final String LIMIT_USER = "LIMIT_USER";
    private static final String LIMIT_IMEIS = "LIMIT_IMEIS";
    private static final String LIMIT_SEND_ALERTS = "LIMIT_SEND_ALERTS";
    private static final String LIMIT_RESULTS = "LIMIT_RESULTS";
    private static final String LIMIT_SURVEYS = "LIMIT_SURVEYS";
    private Properties properties = PropertiesUtil.getCoreProperties();

    public boolean userAdminHasPositiveBalance( Integer balanceItem, String aUserName ) {
        boolean hasPositiveBalance = false;
        String ndgMode = null;

        if ( properties.containsKey( NDG_MODE ) ) {
            ndgMode = properties.getProperty( NDG_MODE );
        }

        char userAdminHasFullPermission = 'N';
        try {
            userAdminHasFullPermission = NdgQuery.getUserByUserName( NdgQuery.getUserByUserName( aUserName ).userAdmin ).hasFullPermissions;
        } catch ( Exception e ) {
            log.log( Level.INFO, "No USER with given name!" + aUserName , e );
        }

        if ( !("hosted".equals( ndgMode ))
             || (userAdminHasFullPermission == 'Y')
             || (userAdminHasFullPermission == 'y') ) {
            hasPositiveBalance = true;
        } else {
            NdgUser onlineUser = null;
            try {
                onlineUser = NdgQuery.getUserByUserName( aUserName );
            } catch ( Exception e ) {
            }

            try {
                UserBalance userBalance = UserBalance.find("byUserAdmin", onlineUser.userAdmin).first();

                if ( balanceItem.intValue() == getUserLimit() ) {
                    hasPositiveBalance = (userBalance.users > 0);
                } else if ( balanceItem.intValue() == getImeiLimit() ) {
                    hasPositiveBalance = (userBalance.imeis > 0);
                } else if ( balanceItem.intValue() == getAlertLimit() ) {
                    hasPositiveBalance = (userBalance.sendAlerts > 0);
                } else if ( balanceItem.intValue() == getResultLimit() ) {
                    hasPositiveBalance = (userBalance.results > 0);
                } else if ( balanceItem.intValue() == getSurveyLimit() ) {
                    hasPositiveBalance = (userBalance.surveys > 0);
                }
            } catch ( Exception e ) {
                hasPositiveBalance = false;
            }
        }
        return hasPositiveBalance;
    }

    private int getLimit( String aPropertyName ) {
        int result = 1;
        try {
            result = Integer.parseInt( properties.getProperty( aPropertyName ) );
        } catch ( NumberFormatException ex ) {
            log.log( Level.WARNING, "Cannot read property" + aPropertyName, ex );
        }
        return result;
    }

    private int getUserLimit() {
        return getLimit( LIMIT_USER );
    }

    private int getImeiLimit() {
        return getLimit( LIMIT_IMEIS );
    }

    private int getAlertLimit() {
        return getLimit( LIMIT_SEND_ALERTS );
    }

    private int getResultLimit() {
        return getLimit( LIMIT_RESULTS );
    }

    private int getSurveyLimit() {
        return getLimit( LIMIT_SURVEYS );
    }
}
