/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.structure;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "userbalance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userbalance.findAll", query = "SELECT u FROM Userbalance u"),
    @NamedQuery(name = "Userbalance.findByIdUSerBalance", query = "SELECT u FROM Userbalance u WHERE u.idUSerBalance = :idUSerBalance"),
    @NamedQuery(name = "Userbalance.findByUsers", query = "SELECT u FROM Userbalance u WHERE u.users = :users"),
    @NamedQuery(name = "Userbalance.findByImeis", query = "SELECT u FROM Userbalance u WHERE u.imeis = :imeis"),
    @NamedQuery(name = "Userbalance.findBySendAlerts", query = "SELECT u FROM Userbalance u WHERE u.sendAlerts = :sendAlerts"),
    @NamedQuery(name = "Userbalance.findByResults", query = "SELECT u FROM Userbalance u WHERE u.results = :results"),
    @NamedQuery(name = "Userbalance.findBySurveys", query = "SELECT u FROM Userbalance u WHERE u.surveys = :surveys")})
public class Userbalance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUSerBalance")
    private Integer idUSerBalance;
    @Column(name = "users")
    private Integer users;
    @Column(name = "imeis")
    private Integer imeis;
    @Column(name = "sendAlerts")
    private Integer sendAlerts;
    @Column(name = "results")
    private Integer results;
    @Column(name = "surveys")
    private Integer surveys;
    @JoinColumn(name = "ndgUserNdgUserId", referencedColumnName = "ndgUserId")
    @ManyToOne
    private NdgUser ndgUserNdgUserId;

    public Userbalance() {
    }

    public Userbalance(Integer idUSerBalance) {
        this.idUSerBalance = idUSerBalance;
    }

    public Integer getIdUSerBalance() {
        return idUSerBalance;
    }

    public void setIdUSerBalance(Integer idUSerBalance) {
        this.idUSerBalance = idUSerBalance;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getImeis() {
        return imeis;
    }

    public void setImeis(Integer imeis) {
        this.imeis = imeis;
    }

    public Integer getSendAlerts() {
        return sendAlerts;
    }

    public void setSendAlerts(Integer sendAlerts) {
        this.sendAlerts = sendAlerts;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Integer getSurveys() {
        return surveys;
    }

    public void setSurveys(Integer surveys) {
        this.surveys = surveys;
    }

    public NdgUser getIdUser() {
        return ndgUserNdgUserId;
    }

    public void setIdUser(NdgUser idUser) {
        this.ndgUserNdgUserId = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUSerBalance != null ? idUSerBalance.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userbalance)) {
            return false;
        }
        Userbalance other = (Userbalance) object;
        if ((this.idUSerBalance == null && other.idUSerBalance != null) || (this.idUSerBalance != null && !this.idUSerBalance.equals(other.idUSerBalance))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Userbalance[ idUSerBalance=" + idUSerBalance + " ]";
    }
    
}
