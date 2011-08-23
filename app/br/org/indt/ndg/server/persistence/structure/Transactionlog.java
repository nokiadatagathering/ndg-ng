/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.structure;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table( name = "transactionlog" )
@XmlRootElement
@NamedQueries( { 
    @NamedQuery( name = "Transactionlog.findAll", query = "SELECT t FROM Transactionlog t" ),
    @NamedQuery( name = "Transactionlog.findByIdTransactionLog", query = "SELECT t FROM Transactionlog t WHERE t.idTransactionLog = :idTransactionLog" ),
    @NamedQuery( name = "Transactionlog.findByAddress", query = "SELECT t FROM Transactionlog t WHERE t.address = :address" ),
    @NamedQuery( name = "Transactionlog.findByTransactionType", query = "SELECT t FROM Transactionlog t WHERE t.transactionType = :transactionType" ),
    @NamedQuery( name = "Transactionlog.findByTransactionStatus", query = "SELECT t FROM Transactionlog t WHERE t.transactionStatus = :transactionStatus" ),
    @NamedQuery( name = "Transactionlog.findByTransmissionMode", query = "SELECT t FROM Transactionlog t WHERE t.transmissionMode = :transmissionMode" ),
    @NamedQuery( name = "Transactionlog.findByTransactionDate", query = "SELECT t FROM Transactionlog t WHERE t.transactionDate = :transactionDate" ),
    @NamedQuery( name = "Transactionlog.findByIdResult", query = "SELECT t FROM Transactionlog t WHERE t.idResult = :idResult" ),
    /*myQuery*/
    /*used*/@NamedQuery( name = "Transactionlog.findByUserIdAndStatus", 
                         query = "SELECT t FROM Transactionlog t WHERE t.ndgUserNdgUserId.ndgUserId = :ndgUserId AND t.transactionStatus= :transactionStatus" ),
    /*used*/@NamedQuery( name = "Transactionlog.findFromTransactionByUserIdAndSurveyId",
                         query = "SELECT t FROM Transactionlog t WHERE t.ndgUserNdgUserId.ndgUserId = :ndgUserId AND t.transactionStatus= :transactionStatus AND t.surveySurveyId.surveyId= :surveyId" ) } )
public class Transactionlog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "transactionLogId" )
    private Integer idTransactionLog;
    @Column( name = "address" )
    private String address;
    @Column( name = "transactionType" )
    private String transactionType;
    @Column( name = "transactionStatus" )
    private String transactionStatus;
    @Column( name = "transmissionMode" )
    private String transmissionMode;
    @Column( name = "transactionDate" )
    @Temporal( TemporalType.DATE )
    private Date transactionDate;
    @Column( name = "idResult" )
    private String idResult;
    @JoinColumn( name = "surveySurveyId", referencedColumnName = "surveyId" )
    @ManyToOne
    private Survey surveySurveyId;
    @JoinColumn( name = "ndgUserNdgUserId", referencedColumnName = "ndgUserId" )
    @ManyToOne( optional = false )
    private NdgUser ndgUserNdgUserId;

    public Transactionlog() {
    }

    public Transactionlog( Integer idTransactionLog ) {
        this.idTransactionLog = idTransactionLog;
    }

    public Integer getIdTransactionLog() {
        return idTransactionLog;
    }

    public void setIdTransactionLog( Integer idTransactionLog ) {
        this.idTransactionLog = idTransactionLog;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType( String transactionType ) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus( String transactionStatus ) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode( String transmissionMode ) {
        this.transmissionMode = transmissionMode;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate( Date transactionDate ) {
        this.transactionDate = transactionDate;
    }

    public String getIdResult() {
        return idResult;
    }

    public void setIdResult( String idResult ) {
        this.idResult = idResult;
    }

    public Survey getIdSurvey() {
        return surveySurveyId;
    }

    public void setIdSurvey( Survey idSurvey ) {
        this.surveySurveyId = idSurvey;
    }

    public NdgUser getUserUserId() {
        return ndgUserNdgUserId;
    }

    public void setUserUserId( NdgUser useridUser ) {
        this.ndgUserNdgUserId = useridUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransactionLog != null ? idTransactionLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof Transactionlog) ) {
            return false;
        }
        Transactionlog other = (Transactionlog) object;
        if ( (this.idTransactionLog == null && other.idTransactionLog != null) || (this.idTransactionLog != null && !this.idTransactionLog.equals( other.idTransactionLog )) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Transactionlog[ idTransactionLog=" + idTransactionLog + " ]";
    }
    
}
