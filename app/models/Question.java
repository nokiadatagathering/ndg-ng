/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    @Column( name = "constraint_text")
    public String constraintText;
    
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
