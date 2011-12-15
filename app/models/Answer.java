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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
@Table(name = "answer")
@XmlRootElement
public class Answer extends Model 
{
    @Required
    @Column( name = "answer_index", nullable= false )
    public int answerIndex;
    
    @Lob
    @MaxSize(10000) 
    @Column( name = "text_data" )
    public String textData;
    
    @Column( name = "binary_data" )
    public Blob binaryData;
    
    @Required
    @ManyToOne(optional= false)
    public Question question;
    
    @Required
    @ManyToOne(optional=false)
    @JoinColumn(name="ndg_result_id")
    public NdgResult ndgResult;

    public Answer(int index ) {
        this.answerIndex = index;
    }
}
