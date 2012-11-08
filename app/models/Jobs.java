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

package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "jobs")
public class Jobs extends Model {

    @Required
    @Column(nullable = false)
    public String surveyId;

    @Required
    @Column(nullable = false)
    public String dateTo;

    @Required
    @Column(nullable = false)
    public String dateFrom;

    @Required
    @Column(nullable = false)
    public String email;

    public boolean complete;

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Jobs(String surveyId, String dateTo, String dateFrom, String email, Boolean complete) {
        this.surveyId = surveyId;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
        this.email = email;
        this.complete = complete;
    }

}
