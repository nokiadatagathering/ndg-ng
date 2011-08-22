/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wojciech.luczkow
 */
@Entity
@Table( name = "user_role" )
@XmlRootElement
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ManyToOne
    private NdgUser username;
    @JoinColumn(name = "roleName", referencedColumnName = "roleName")
    @ManyToOne
    private NdgRole roleName;

    public NdgRole getNdgRole() {
        return roleName;
    }

    public void setNdgRole(NdgRole ndgRole) {
        this.roleName = ndgRole;
    }

    public NdgUser getNdgUser() {
        return username;
    }

    public void setNdgUser(NdgUser ndgUser) {
        this.username = ndgUser;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.UserRole[ id=" + id + " ]";
    }
    
}
