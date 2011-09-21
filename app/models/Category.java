package models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;


/**
 *
 * @author damian.janicki
 */

@Entity
@Table(name = "category")
public class Category extends Model{

    @Column(nullable=false)
    public  String label;

    public Integer categoryIndex;

    @Required
    @ManyToOne(optional = false)
    public Survey survey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public List<Question> questionCollection;


    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Category[ id=" +  " ]";
    }
}
