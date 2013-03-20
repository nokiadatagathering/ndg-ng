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

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
import play.vfs.VirtualFile;

import controllers.sms.SmsHandlerFactory;

import jobs.Scheduler;

import models.Jobs;
import models.NdgRole;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {

    final static String INITIAL_DATA_FILENAME = "initial_data.sql";

    @Override
    public void doJob() {
        // Check if the database is empty
        if (NdgRole.count() == 0) {

            VirtualFile sqlFile = null;
            for (VirtualFile vf : Play.javaPath) {
                sqlFile = vf.child(INITIAL_DATA_FILENAME);
                if (sqlFile != null && sqlFile.exists()) {
                    break;
                }
            }
            if (sqlFile == null) {
                throw new RuntimeException("Cannot load sql file " + INITIAL_DATA_FILENAME + ", the file was not found");
            }
            Fixtures.executeSQL(sqlFile.contentAsString());
        }

        //Initialize SMS modem/gateway
        if (SmsHandlerFactory.hasSmsSupport()) {
            SmsHandlerFactory.getInstance().getSmsHandler();
        } else {
            System.out.println( "SMS support disabled" );
        }

        // Check if there are any uncompleted jobs
        String query = "complete = false";


        // See if there are any jobs scheduled for today
        // Make a new Date object. It will be initialized to the
        // current time

        Date now = new Date();
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(now);


        for(Object jobz : Jobs.find(query).fetch()) {
            if (((Jobs)jobz).dateTo.equals(todaysDate)){
                // execute the export
                new Scheduler(((Jobs)jobz).id, ((Jobs)jobz).surveyId, ((Jobs)jobz).dateTo, ((Jobs)jobz).dateFrom, ((Jobs)jobz).email);
                                                }
            
                                                     }          

    }

}
