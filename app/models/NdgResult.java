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

import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;
import models.constants.QuestionTypesConsts;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_result" )
public class NdgResult extends Model {
    
    @Required
    @Column( name = "ndg_result_id" )
    public String resultId;
    
    @Required
    @Column( nullable = false, name = "start_time" )
    @Temporal( TemporalType.TIMESTAMP )
    public Date startTime;
    
    @Required
    @Column( nullable = false, name = "end_time" )
    @Temporal( TemporalType.TIMESTAMP )
    public Date endTime;

    @Column( name = "date_sent" )
    @Temporal( TemporalType.TIMESTAMP )
    public Date dateSent;
    
    public String title;
    
    public String latitude;
    
    public String longitude;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgResult" )
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<Answer> answerCollection;

    @ManyToOne( optional = false )
    public Survey survey;
    
    @ManyToOne( optional = false )
    @JoinColumn( name = "ndg_user_id")
    public NdgUser ndgUser;

    public NdgResult() {
    }

   
    public NdgResult( String resultId, Date startTime, Date endTime ) {
        this.resultId = resultId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void _delete() {
        super._delete();
        Collection<Answer> answers = answerCollection;

        for(Answer answer : answers) {
            if ( answer.question.questionType.typeName.equalsIgnoreCase( QuestionTypesConsts.IMAGE ) ) {
                answer.binaryData.getFile().delete();
            }
        }
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.NdgResult[ resultId=" + getId() + " ]";
    }
    
}
