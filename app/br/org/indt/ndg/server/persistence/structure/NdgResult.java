/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.structure;

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

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_result" )
@XmlRootElement
@NamedQueries( { 
    @NamedQuery( name = "NdgResult.findAll", query = "SELECT r FROM NdgResult r" ),
    @NamedQuery( name = "NdgResult.findByResultId", query = "SELECT r FROM NdgResult r WHERE r.ndgResultId = :ndgResultId" ),
    @NamedQuery( name = "NdgResult.findByStartTime", query = "SELECT r FROM NdgResult r WHERE r.startTime = :startTime" ),
    @NamedQuery( name = "NdgResult.findByEndTime", query = "SELECT r FROM NdgResult r WHERE r.endTime = :endTime" ),
    @NamedQuery( name = "NdgResult.findByTitle", query = "SELECT r FROM NdgResult r WHERE r.title = :title" ),
    @NamedQuery( name = "NdgResult.findByLatitude", query = "SELECT r FROM NdgResult r WHERE r.latitude = :latitude" ),
    @NamedQuery( name = "NdgResult.findByLongitude", query = "SELECT r FROM NdgResult r WHERE r.longitude = :longitude" ) } )
public class NdgResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic( optional = false )
    @Column( name = "ndgResultId" )
    private String ndgResultId;
    @Basic( optional = false )
    @Column( name = "startTime" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date startTime;
    @Basic( optional = false )
    @Column( name = "endTime" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date endTime;
    @Column( name = "title" )
    private String title;
    @Column( name = "latitude" )
    private String latitude;
    @Column( name = "longitude" )
    private String longitude;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgResultNdgResultId" )
    private Collection<Answer> answerCollection;
    @JoinColumn( name = "surveySurveyId", referencedColumnName = "surveyId" )
    @ManyToOne( optional = false )
    private Survey surveysSurveyId;
    @JoinColumn( name = "ndgUserNdgUserId", referencedColumnName = "ndgUserId" )
    @ManyToOne( optional = false )
    private NdgUser ndgUserNdgUserId;

    public NdgResult() {
    }

    public NdgResult( String resultId ) {
        this.ndgResultId = resultId;
    }

    public NdgResult( String resultId, Date startTime, Date endTime ) {
        this.ndgResultId = resultId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getResultId() {
        return ndgResultId;
    }

    public void setResultId( String resultId ) {
        this.ndgResultId = resultId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime( Date startTime ) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime( Date endTime ) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude( String latitude ) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude( String longitude ) {
        this.longitude = longitude;
    }

    @XmlTransient
    public Collection<Answer> getAnswersCollection() {
        return answerCollection;
    }

    public void setAnswersCollection( Collection<Answer> answersCollection ) {
        this.answerCollection = answersCollection;
    }

    public Survey getSurveysSurveyId() {
        return surveysSurveyId;
    }

    public void setSurveysSurveyId( Survey surveysSurveyId ) {
        this.surveysSurveyId = surveysSurveyId;
    }

    public NdgUser getUseridUser() {
        return ndgUserNdgUserId;
    }

    public void setUseridUser( NdgUser useridUser ) {
        this.ndgUserNdgUserId = useridUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ndgResultId != null ? ndgResultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof NdgResult) ) {
            return false;
        }
        NdgResult other = (NdgResult) object;
        if ( (this.ndgResultId == null && other.ndgResultId != null) || (this.ndgResultId != null && !this.ndgResultId.equals( other.ndgResultId )) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.NdgResult[ resultId=" + ndgResultId + " ]";
    }
    
}
