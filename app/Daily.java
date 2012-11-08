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

import play.jobs.Job;
import play.jobs.On;

import jobs.Scheduler;

import models.Jobs;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Date;

@On("0 0 5 * * ?")
public class Daily extends Job {

    @Override
    public void doJob() {

        // Check if there are any uncompleted jobs
        String query = "complete = false";
        List<Jobs> jobs = Jobs.find(query).fetch();

        // See if there are any jobs scheduled for today
        // Make a new Date object. It will be initialized to the
        // current time

        Date now = new Date();
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(now);

        //System.out.println(todaysDate);

        for (int k = 0; k < jobs.size(); k++) {
            Jobs jobz = jobs.get(k);
            // compare the scheduled date in the database to todays
            if (jobz.dateTo.equals(todaysDate)) {
                // execute the export
                //System.out.println("Date1 is equal Date2");
                //System.out.println("The job id is " +  jobz.id );
                new Scheduler(jobz.id, jobz.surveyId, jobz.dateTo, jobz.dateFrom, jobz.email);
            }
        }
    }

}
