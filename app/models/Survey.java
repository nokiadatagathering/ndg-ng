package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "survey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Survey.findAll", query = "SELECT s FROM Survey s"),
    /*used*/@NamedQuery(name = "Survey.findBySurveyId", query = "SELECT s FROM Survey s WHERE s.surveyId = :surveyId"),
    @NamedQuery(name = "Survey.findByTitle", query = "SELECT s FROM Survey s WHERE s.title = :title"),
    @NamedQuery(name = "Survey.findByAvailable", query = "SELECT s FROM Survey s WHERE s.available = :available"),
    @NamedQuery(name = "Survey.findByUploadDate", query = "SELECT s FROM Survey s WHERE s.uploadDate = :uploadDate")})
public class Survey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "surveyId")
    private String surveyId;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "lang")
    private String lang;

    @Column(name = "available")
    private Integer available;
    @Column(name = "uploadDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveysSurveyId")
    private Collection<Question> questionsCollection;
    @OneToMany(mappedBy = "surveySurveyId")
    private Collection<Transactionlog> transactionlogCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveysSurveyId")
    private Collection<NdgResult> resultsCollection;
    @JoinColumn(name = "ndgUserNdgUserId", referencedColumnName = "ndgUserId")
    @ManyToOne(optional = false)
    private NdgUser ndgUserNdgUserId;

    public Survey() {
    }

    public Survey(String surveyId) {
        this.surveyId = surveyId;
    }

    public Survey(String surveyId, String title) {
        this.surveyId = surveyId;
        this.title = title;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @XmlTransient
    public Collection<Question> getQuestionsCollection() {
        return questionsCollection;
    }

    public void setQuestionsCollection(Collection<Question> questionsCollection) {
        this.questionsCollection = questionsCollection;
    }

    @XmlTransient
    public Collection<Transactionlog> getTransactionlogCollection() {
        return transactionlogCollection;
    }

    public void setTransactionlogCollection(Collection<Transactionlog> transactionlogCollection) {
        this.transactionlogCollection = transactionlogCollection;
    }

    @XmlTransient
    public Collection<NdgResult> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection(Collection<NdgResult> resultsCollection) {
        this.resultsCollection = resultsCollection;
    }

    public NdgUser getUseridUser() {
        return ndgUserNdgUserId;
    }

    public void setUseridUser(NdgUser useridUser) {
        this.ndgUserNdgUserId = useridUser;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (surveyId != null ? surveyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Survey)) {
            return false;
        }
        Survey other = (Survey) object;
        if ((this.surveyId == null && other.surveyId != null) || (this.surveyId != null && !this.surveyId.equals(other.surveyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Surveys[ surveyId=" + surveyId + " ]";
    }

}
