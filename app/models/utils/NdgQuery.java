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
package models.utils;

import java.util.ArrayList;
import models.NdgUser;
import models.Survey;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.db.jpa.JPA;

public class NdgQuery {

    private static final Logger log = Logger.getLogger(NdgQuery.class.getName());

    public static NdgUser getUserByUserName(String aUserName) {
        NdgUser user = null;

        if (aUserName != null && !aUserName.isEmpty()) {
            user = NdgUser.find("byUsername", aUserName).first();
        }
        return user;
    }

    public static NdgUser getUsersbyId(Long aId) {
        NdgUser result = null;
        try {
            result = NdgUser.find("byId", aId).first();

        } catch (NoResultException nex) {
            log.log(Level.WARNING, "NoResultsException", nex);
        }
        return result;
    }

    public static Survey getSurveyById(String aId) {
        Survey result = null;
        try {
            result = Survey.find("bySurveyId", aId).first();

        } catch (NoResultException nex) {
            log.log(Level.WARNING, "NoResultsException", nex);
        }
        return result;
    }

    public static ArrayList<NdgUser> listAllUsers() {
        ArrayList<NdgUser> results = null;
        List<NdgUser> AllUsers = NdgUser.findAll();
        results = (ArrayList<NdgUser>) AllUsers;

        if (results == null) {
            results = new ArrayList<NdgUser>();
        }
        return results;
    }

    public static ArrayList<Survey> listAllSurveys() {
        List<Survey> results = null;
        try {
            results = Survey.findAll();

        } catch (NoResultException nex) {
            log.log(Level.WARNING, "NoResultsException", nex);
            results = new ArrayList<Survey>();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "exception", ex);
            results = new ArrayList<Survey>();
        }
        return (ArrayList<Survey>) results;
    }
}
