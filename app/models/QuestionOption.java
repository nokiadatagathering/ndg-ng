/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
