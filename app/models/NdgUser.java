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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table( name = "ndg_user" )
@XmlRootElement
@NamedQueries( { 
    @NamedQuery( name = "NdgUser.findAll", query = "SELECT u FROM NdgUser u" ),
    /*used*/@NamedQuery(name = "NdgUser.findByNdgUserId", query = "SELECT u FROM NdgUser u WHERE u.ndgUserId = :ndgUserId"),
    @NamedQuery( name = "NdgUser.findByPassword", query = "SELECT u FROM NdgUser u WHERE u.password = :password" ),
    /*used*/@NamedQuery(name = "NdgUser.findByUsername", query = "SELECT u FROM NdgUser u WHERE u.username = :username"),
    @NamedQuery( name = "NdgUser.findByEmail", query = "SELECT u FROM NdgUser u WHERE u.email = :email" ),
    @NamedQuery( name = "NdgUser.findByFirstName", query = "SELECT u FROM NdgUser u WHERE u.firstName = :firstName" ),
    @NamedQuery( name = "NdgUser.findByLastName", query = "SELECT u FROM NdgUser u WHERE u.lastName = :lastName" ),
    @NamedQuery( name = "NdgUser.findByCountryCode", query = "SELECT u FROM NdgUser u WHERE u.countryCode = :countryCode" ),
    @NamedQuery( name = "NdgUser.findByAreaCode", query = "SELECT u FROM NdgUser u WHERE u.areaCode = :areaCode" ),
    @NamedQuery( name = "NdgUser.findByPhoneNumber", query = "SELECT u FROM NdgUser u WHERE u.phoneNumber = :phoneNumber" ),
    @NamedQuery( name = "NdgUser.findByUserAdmin", query = "SELECT u FROM NdgUser u WHERE u.userAdmin = :userAdmin" ),
    @NamedQuery( name = "NdgUser.findByUserValidated", query = "SELECT u FROM NdgUser u WHERE u.userValidated = :userValidated" ),
    @NamedQuery( name = "NdgUser.findByWhoUseIt", query = "SELECT u FROM NdgUser u WHERE u.whoUseIt = :whoUseIt" ),
    @NamedQuery( name = "NdgUser.findByEmailPreferences", query = "SELECT u FROM NdgUser u WHERE u.emailPreferences = :emailPreferences" ),
    @NamedQuery( name = "NdgUser.findByHowDoYouPlanUseNdg", query = "SELECT u FROM NdgUser u WHERE u.howDoYouPlanUseNdg = :howDoYouPlanUseNdg" ),
    @NamedQuery( name = "NdgUser.findByFirstTimeUse", query = "SELECT u FROM NdgUser u WHERE u.firstTimeUse = :firstTimeUse" ),
    @NamedQuery( name = "NdgUser.findByValidationKey", query = "SELECT u FROM NdgUser u WHERE u.validationKey = :validationKey" ),
    @NamedQuery( name = "NdgUser.findByHasFullPermissions", query = "SELECT u FROM NdgUser u WHERE u.hasFullPermissions = :hasFullPermissions" ) } )
public class NdgUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "ndgUserId" )
    private Integer ndgUserId;
    @Basic( optional = false )
    @Column( name = "password" )
    private String password;
    @Basic( optional = false )
    @Column( name = "username", unique = true )
    private String username;
    @Basic( optional = false )
    @Column( name = "email" )
    private String email;
    @Basic( optional = false )
    @Column( name = "firstName" )
    private String firstName;
    @Basic( optional = false )
    @Column( name = "lastName" )
    private String lastName;
    @Column( name = "countryCode" )
    private String countryCode;
    @Column( name = "areaCode" )
    private String areaCode;
    @Column( name = "phoneNumber" )
    private String phoneNumber;
    @Basic( optional = false )
    @Column( name = "userAdmin" )
    private String userAdmin;
    @Basic( optional = false )
    @Column( name = "userValidated" )
    private char userValidated;
    @Basic( optional = false )
    @Column( name = "whoUseIt" )
    private char whoUseIt;
    @Basic( optional = false )
    @Column( name = "emailPreferences" )
    private char emailPreferences;
    @Column( name = "howDoYouPlanUseNdg" )
    private String howDoYouPlanUseNdg;
    @Basic( optional = false )
    @Column( name = "firstTimeUse" )
    private char firstTimeUse;
    @Column( name = "validationKey" )
    private String validationKey;
    @Basic( optional = false )
    @Column( name = "hasFullPermissions" )
    private char hasFullPermissions;
    @Lob
    @Column( name = "editorSettings" )
    private String editorSettings;
    @OneToMany( mappedBy = "ndgUserNdgUserId" )
    private Collection<Userbalance> userbalanceCollection;
    @JoinColumn( name = "companyId", referencedColumnName = "companyId" )
    @ManyToOne( optional = false )
    private Company companyId;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUserNdgUserId" )
    private Collection<Transactionlog> transactionlogCollection;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUserNdgUserId" )
    private Collection<NdgResult> resultsCollection;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    private Collection<Survey> surveysCollection;
    @OneToMany(mappedBy = "username")
    private Collection<UserRole> userRoleCollection;

    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }

    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }

    public NdgUser() {
    }

    public NdgUser( Integer idUser ) {
        this.ndgUserId = idUser;
    }

    public NdgUser( Integer idUser, String password, String username, String email, String firstName, String lastName, String userAdmin, char userValidated, char whoUseIt, char emailPreferences, char firstTimeUse, char hasFullPermissions ) {
        this.ndgUserId = idUser;
        this.password = password;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAdmin = userAdmin;
        this.userValidated = userValidated;
        this.whoUseIt = whoUseIt;
        this.emailPreferences = emailPreferences;
        this.firstTimeUse = firstTimeUse;
        this.hasFullPermissions = hasFullPermissions;
    }

    public Integer getIdUser() {
        return ndgUserId;
    }

    public void setIdUser( Integer idUser ) {
        this.ndgUserId = idUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode( String countryCode ) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode( String areaCode ) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin( String userAdmin ) {
        this.userAdmin = userAdmin;
    }

    public char getUserValidated() {
        return userValidated;
    }

    public void setUserValidated( char userValidated ) {
        this.userValidated = userValidated;
    }

    public char getWhoUseIt() {
        return whoUseIt;
    }

    public void setWhoUseIt( char whoUseIt ) {
        this.whoUseIt = whoUseIt;
    }

    public char getEmailPreferences() {
        return emailPreferences;
    }

    public void setEmailPreferences( char emailPreferences ) {
        this.emailPreferences = emailPreferences;
    }

    public String getHowDoYouPlanUseNdg() {
        return howDoYouPlanUseNdg;
    }

    public void setHowDoYouPlanUseNdg( String howDoYouPlanUseNdg ) {
        this.howDoYouPlanUseNdg = howDoYouPlanUseNdg;
    }

    public char getFirstTimeUse() {
        return firstTimeUse;
    }

    public void setFirstTimeUse( char firstTimeUse ) {
        this.firstTimeUse = firstTimeUse;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey( String validationKey ) {
        this.validationKey = validationKey;
    }

    public char getHasFullPermissions() {
        return hasFullPermissions;
    }

    public void setHasFullPermissions( char hasFullPermissions ) {
        this.hasFullPermissions = hasFullPermissions;
    }

    public String getEditorSettings() {
        return editorSettings;
    }

    public void setEditorSettings( String editorSettings ) {
        this.editorSettings = editorSettings;
    }

    @XmlTransient
    public Collection<Userbalance> getUserbalanceCollection() {
        return userbalanceCollection;
    }

    public void setUserbalanceCollection( Collection<Userbalance> userbalanceCollection ) {
        this.userbalanceCollection = userbalanceCollection;
    }

    public Company getIdCompany() {
        return companyId;
    }

    public void setIdCompany( Company idCompany ) {
        this.companyId = idCompany;
    }

    @XmlTransient
    public Collection<Transactionlog> getTransactionlogCollection() {
        return transactionlogCollection;
    }

    public void setTransactionlogCollection( Collection<Transactionlog> transactionlogCollection ) {
        this.transactionlogCollection = transactionlogCollection;
    }

    @XmlTransient
    public Collection<NdgResult> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection( Collection<NdgResult> resultsCollection ) {
        this.resultsCollection = resultsCollection;
    }

    @XmlTransient
    public Collection<Survey> getSurveysCollection() {
        return surveysCollection;
    }

    public void setSurveysCollection( Collection<Survey> surveysCollection ) {
        this.surveysCollection = surveysCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ndgUserId != null ? ndgUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof NdgUser) ) {
            return false;
        }
        NdgUser other = (NdgUser) object;
        if ( (this.ndgUserId == null && other.ndgUserId != null) || (this.ndgUserId != null && !this.ndgUserId.equals( other.ndgUserId )) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Users[ userId=" + ndgUserId + " ]";
    }

    
}
