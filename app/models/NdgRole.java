/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.db.jpa.Model;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_role" )
public class NdgRole extends Model {

    @Column( unique = true, name = "role_name" )
    public String roleName;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ndgRole")
    public Collection<UserRole> userRoleCollection;

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Roles[ idRole=" + getId() + " ]";
    }
    
}
