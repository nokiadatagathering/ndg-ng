package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey" )
    public List<Question> questionCollection;
    
    @OneToMany(mappedBy = "surveySurveyId")
    public Collection<Transactionlog> transactionLogCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
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
