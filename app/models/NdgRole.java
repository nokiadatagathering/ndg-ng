/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_role" )
@XmlRootElement
@NamedQueries( { 
    @NamedQuery( name = "NdgRole.findAll", query = "SELECT r FROM NdgRole r" ),
    @NamedQuery( name = "NdgRole.findByIdRole", query = "SELECT r FROM NdgRole r WHERE r.ndgRoleId = :ndgRoleId" ),
    @NamedQuery( name = "NdgRole.findByRoleName", query = "SELECT r FROM NdgRole r WHERE r.roleName = :roleName" ) } )
public class NdgRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "ndgRoleId" )
    private Integer ndgRoleId;
    @Column( name = "roleName", unique = true )
    private String roleName;
    @OneToMany(mappedBy = "roleName")
    private Collection<UserRole> userRoleCollection;

    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }

    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }
  
    public NdgRole() {
    }

    public NdgRole( Integer idRole ) {
        this.ndgRoleId = idRole;
    }

    public Integer getIdRole() {
        return ndgRoleId;
    }

    public void setIdRole( Integer idRole ) {
        this.ndgRoleId = idRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName( String roleName ) {
        this.roleName = roleName;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ndgRoleId != null ? ndgRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof NdgRole) ) {
            return false;
        }
        NdgRole other = (NdgRole) object;
        if ( (this.ndgRoleId == null && other.ndgRoleId != null) || (this.ndgRoleId != null && !this.ndgRoleId.equals( other.ndgRoleId )) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Roles[ idRole=" + ndgRoleId + " ]";
    }
    
}
