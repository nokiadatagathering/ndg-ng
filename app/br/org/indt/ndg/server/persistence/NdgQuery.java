/*
 *  Copyright (C) 2011  INdT - Instituto Nokia de Tecnologia
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
package br.org.indt.ndg.server.persistence;

import br.org.indt.ndg.server.persistence.structure.NdgUser;
import br.org.indt.ndg.server.persistence.structure.Survey;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.db.jpa.JPA;


public class NdgQuery {

    private static final Logger log = Logger.getLogger( NdgQuery.class.getName() );

    public static NdgUser getUserByUserName( String aUserName ) {
        NdgUser user = null;
        
        if ( aUserName != null && !aUserName.isEmpty() ) {
            try {

                Query userQuery = JPA.em().createNamedQuery( "NdgUser.findByUsername" );
                userQuery.setParameter( "username", aUserName );
                user = (NdgUser) userQuery.getSingleResult();
            }catch ( NoResultException ex ) {
                log.log( Level.INFO, "NdgQuery:getUserByUserName:NoResultException", ex );
                log.log( Level.INFO, "NoUser for given name {0} ", aUserName );
            }
        }
        return user;
    }
    
    public static NdgUser getUsersbyId( String aId ) {
        NdgUser result = null;
        try {
            Query userIdQuery = JPA.em().createNamedQuery( "NdgUser.findByNdgUserId" );
            userIdQuery.setParameter( "ndgUserId", Integer.parseInt( aId ) );
            result = (NdgUser) userIdQuery.getSingleResult();

        } catch ( NoResultException nex ) {
            log.log( Level.WARNING, "NoResultsException", nex );
        } 
        return result;
    }

    public static Survey getSurveyById( String aId ) {
        Survey result = null;
        try {
            Query userIdQuery = JPA.em().createNamedQuery( "Survey.findBySurveyId" );
            userIdQuery.setParameter( "surveyId", aId );
            result = (Survey) userIdQuery.getSingleResult();

        } catch ( NoResultException nex ) {
            log.log( Level.WARNING, "NoResultsException", nex );
        } 
        return result;
    }
    
    public static ArrayList<NdgUser> listAllUsers( ) {
        ArrayList<NdgUser> results = null;
        try {
           
            Query users = JPA.em().createNamedQuery( "NdgUser.findAll" );

            results = (ArrayList)users.getResultList();

        } catch ( NoResultException nex ) {
            log.log( Level.WARNING, "NoResultsException", nex );
            results = new ArrayList<NdgUser>();
        } catch ( Exception ex ) {
            log.log( Level.SEVERE, "exception", ex );
            results = new ArrayList<NdgUser>();
        } 
        return results;
    }

    public static ArrayList<Survey> listAllSurveys(  ) {
        ArrayList<Survey> results = null;
        try {
            Query surveysQuery = JPA.em().createNamedQuery( "Survey.findAll" );

            results = new ArrayList<Survey>( surveysQuery.getResultList() );

        } catch ( NoResultException nex ) {
            log.log( Level.WARNING, "NoResultsException", nex );
            results = new ArrayList<Survey>();
        } catch ( Exception ex ) {
            log.log( Level.SEVERE, "exception", ex );
            results = new ArrayList<Survey>();
        } 
        return results;
    }

}
