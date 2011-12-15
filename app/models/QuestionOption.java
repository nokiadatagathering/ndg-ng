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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
@Table(name = "question_option")
public class QuestionOption extends Model {
    @Required
    @Column(nullable=false, name = "option_index")
    public int optionIndex;

    @Required
    @Column(nullable=false)
    public String label;

    @Required
    @Column(nullable=false, name = "option_value")
    public String optionValue;

    @ManyToOne(optional = false)
    public Question question;

    public QuestionOption()
    {
    }

    public QuestionOption(int index, String label, String optionValue) {
        this.optionIndex = index;
        this.label = label;
        this.optionValue = optionValue;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.QuestionOptions[ idQuestionOptions=" + getId() + " ]";
    }

}
