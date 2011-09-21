/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name= "question_type" )
public class QuestionType extends Model {

    @Required
    @Column(name = "type_name", nullable = false)
    public String typeName;
    
    public Integer supported;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionType")
    public Collection<Question> questionCollection;

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.QuestionTypes[ questionTypeId=" + getId() + " ]";
    }
}
