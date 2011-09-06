/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
    
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column( nullable= false)
    public String objectName;
    
    @Lob
    @Column(nullable=false)
    public  String label;
    
    @Lob
    public String hint;
    
    public String constraintText;
    
    public Integer required;
    
    public Integer readonly;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<Answer> answerCollection;
    
    @ManyToOne(optional = false)
    public Survey survey;
    
    @ManyToOne(optional = false)
    public QuestionType questionType;
    
    @ManyToOne
    public DefaultAnswer defaultAnswer;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<QuestionOption> questionOptionCollection;


    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Questions[ id=" +  " ]";
    }
    
}
