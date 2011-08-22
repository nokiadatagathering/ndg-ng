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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByCompanyId", query = "SELECT c FROM Company c WHERE c.companyId = :companyId"),
    @NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "Company.findByCompanyType", query = "SELECT c FROM Company c WHERE c.companyType = :companyType"),
    @NamedQuery(name = "Company.findByCompanyCountry", query = "SELECT c FROM Company c WHERE c.companyCountry = :companyCountry"),
    @NamedQuery(name = "Company.findByCompanyIndustry", query = "SELECT c FROM Company c WHERE c.companyIndustry = :companyIndustry"),
    @NamedQuery(name = "Company.findByCompanySize", query = "SELECT c FROM Company c WHERE c.companySize = :companySize")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "companyId")
    private Integer companyId;
    @Basic(optional = false)
    @Column(name = "companyName")
    private String companyName;
    @Basic(optional = false)
    @Column(name = "companyType")
    private String companyType;
    @Basic(optional = false)
    @Column(name = "companyCountry")
    private String companyCountry;
    @Basic(optional = false)
    @Column(name = "companyIndustry")
    private String companyIndustry;
    @Basic(optional = false)
    @Column(name = "companySize")
    private String companySize;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId")
    private Collection<NdgUser> userCollection;

    public Company() {
    }

    public Company(Integer idCompany) {
        this.companyId = idCompany;
    }

    public Company(Integer idCompany, String companyName, String companyType, String companyCountry, String companyIndustry, String companySize) {
        this.companyId = idCompany;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyCountry = companyCountry;
        this.companyIndustry = companyIndustry;
        this.companySize = companySize;
    }

    public Integer getIdCompany() {
        return companyId;
    }

    public void setIdCompany(Integer idCompany) {
        this.companyId = idCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    @XmlTransient
    public Collection<NdgUser> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<NdgUser> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyId != null ? companyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.companyId == null && other.companyId != null) || (this.companyId != null && !this.companyId.equals(other.companyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Company[ idCompany=" + companyId + " ]";
    }
    
}
