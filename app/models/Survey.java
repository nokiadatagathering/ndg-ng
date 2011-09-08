package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "survey")
public class Survey extends Model {

    @Required
    @Column(nullable = false)
    public String surveyId;
    
    @Required
    @Column(nullable = false)
    public String title;
    
    public String lang;
    
    public Integer available;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date uploadDate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public List<Question> questionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<TransactionLog> transactionLogCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<NdgResult> resultCollection;
    
    @ManyToOne(optional = false)
    public NdgUser ndgUser;

    public Survey() {
    }

    public Survey(String surveyId) {
        this.surveyId = surveyId;
    }

    public Survey(String surveyId, String title) {
        this.surveyId = surveyId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Surveys[ surveyId=" + getId() + " ]";
    }
}
