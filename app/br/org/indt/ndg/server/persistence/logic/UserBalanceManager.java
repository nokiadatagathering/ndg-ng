/*
 *  Copyright (C) 2010-2011  INdT - Instituto Nokia de Tecnologia
 *
 *  NDG is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  NDG is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with NDG.  If not, see <http://www.gnu.org/licenses/
 */
package br.org.indt.ndg.server.persistence.logic;

import br.org.indt.ndg.server.exceptions.MSMApplicationException;
import br.org.indt.ndg.server.persistence.NdgQuery;
import br.org.indt.ndg.server.persistence.structure.Userbalance;
import br.org.indt.ndg.server.persistence.structure.NdgUser;
import br.org.indt.ndg.server.util.PropertiesUtil;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import play.db.jpa.JPA;

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
            userAdminHasFullPermission = NdgQuery.getUserByUserName( NdgQuery.getUserByUserName( aUserName ).getUserAdmin() ).getHasFullPermissions();
        } catch ( Exception e ) {
            log.log( Level.INFO, "No USER with given name!" + aUserName , e );
            e.printStackTrace();
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
                e.printStackTrace();
            }

            EntityManager manager = JPA.em();
            try {
                Query query = manager.createNamedQuery( "Userbalance.findByUserAdmin" );
                query.setParameter( "useradmin", onlineUser.getUserAdmin() );
                Userbalance userBalance = (Userbalance) query.getSingleResult();

                if ( balanceItem.intValue() == getUserLimit() ) {
                    hasPositiveBalance = (userBalance.getUsers() > 0);
                } else if ( balanceItem.intValue() == getImeiLimit() ) {
                    hasPositiveBalance = (userBalance.getImeis() > 0);
                } else if ( balanceItem.intValue() == getAlertLimit() ) {
                    hasPositiveBalance = (userBalance.getSendAlerts() > 0);
                } else if ( balanceItem.intValue() == getResultLimit() ) {
                    hasPositiveBalance = (userBalance.getResults() > 0);
                } else if ( balanceItem.intValue() == getSurveyLimit() ) {
                    hasPositiveBalance = (userBalance.getSurveys() > 0);
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
