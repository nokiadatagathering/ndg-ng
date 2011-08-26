/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_user" )
public class NdgUser extends Model {
    @Required
    @Column( nullable=false )
    public String password;
    
    @Required
    @Column( unique = true, nullable=false )
    public String username;
    
    @Required
    @Column(nullable = false )
    public String email;
    
    @Required
    @Column(nullable = false)
    public String firstName;
    
    @Required
    @Column(nullable = false)
    public String lastName;
    
    public String countryCode;
    
    public String areaCode;
    
    public String phoneNumber;
    
    @Required
    @Column( nullable = false )
    public String userAdmin;
    
    @Required
    @Column( nullable = false )
    public char userValidated;
    
    @Required
    @Column( nullable = false )
    public char emailPreferences;
    
    public String validationKey;
    
    @Required
    @Column( nullable = false )
    public char hasFullPermissions;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser"  )
    public Collection<UserBalance> userBalanceCollection;

    @ManyToOne( optional = false )
    public Company company;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<TransactionLog> transactionlogCollection;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<NdgResult> resultsCollection;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<Survey> surveysCollection;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser")
    public Collection<UserRole> userRoleCollection;

    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }

    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }

    public NdgUser() {
    }

    public NdgUser( String password, String username, String email, String firstName, String lastName, String userAdmin, char userValidated, char emailPreferences, char hasFullPermissions ) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAdmin = userAdmin;
        this.userValidated = userValidated;
        this.emailPreferences = emailPreferences;
        this.hasFullPermissions = hasFullPermissions;
    }
    
    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Users[ userId=" + getId() + " ]";
    }

    
}
