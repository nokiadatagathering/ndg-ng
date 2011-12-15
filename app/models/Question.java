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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
@Table(name = "question")
public class Question extends Model {

    @Required
    @Column( nullable= false, name = "object_name")
    public String objectName;

    @Lob
    @Column(nullable=false)
    public  String label;

    @Lob
    public String hint;


    @Column(name="question_index")
    public Integer questionIndex;

    @Column( name = "constraint_text")
    public String constraintText;

    @Column( name = "relevant")
    public String relevant;

    public Integer required;

    public Integer readonly;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<Answer> answerCollection;

    @ManyToOne(optional = true)
    public Category category; //TODO add (optional = false)


    @ManyToOne(optional = false)
    @JoinColumn(name = "question_type_id")
    public QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "default_answer_id")
    public DefaultAnswer defaultAnswer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<QuestionOption> questionOptionCollection;


    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Questions[ id=" +  " ]";
    }

}
