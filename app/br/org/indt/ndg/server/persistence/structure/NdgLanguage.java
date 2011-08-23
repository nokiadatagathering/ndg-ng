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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "ndg_language")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NdgLanguage.findAll", query = "SELECT l FROM NdgLanguage l"),
    @NamedQuery(name = "NdgLanguage.findByLanguageId", query = "SELECT l FROM NdgLanguage l WHERE l.languageId = :languageId"),
    @NamedQuery(name = "NdgLanguage.findByName", query = "SELECT l FROM NdgLanguage l WHERE l.name = :name"),
    @NamedQuery(name = "NdgLanguage.findByLocaleString", query = "SELECT l FROM NdgLanguage l WHERE l.localeString = :localeString"),
    @NamedQuery(name = "NdgLanguage.findByTranslationFilePath", query = "SELECT l FROM NdgLanguage l WHERE l.translationFilePath = :translationFilePath"),
    @NamedQuery(name = "NdgLanguage.findByFontFilePath", query = "SELECT l FROM NdgLanguage l WHERE l.fontFilePath = :fontFilePath")})
public class NdgLanguage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "languageId")
    private Integer languageId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "localeString")
    private String localeString;
    @Basic(optional = false)
    @Column(name = "translationFilePath")
    private String translationFilePath;
    @Column(name = "fontFilePath")
    private String fontFilePath;

    public NdgLanguage() {
    }

    public NdgLanguage(Integer languageId) {
        this.languageId = languageId;
    }

    public NdgLanguage(Integer languageId, String name, String localeString, String translationFilePath) {
        this.languageId = languageId;
        this.name = name;
        this.localeString = localeString;
        this.translationFilePath = translationFilePath;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocaleString() {
        return localeString;
    }

    public void setLocaleString(String localeString) {
        this.localeString = localeString;
    }

    public String getTranslationFilePath() {
        return translationFilePath;
    }

    public void setTranslationFilePath(String translationFilePath) {
        this.translationFilePath = translationFilePath;
    }

    public String getFontFilePath() {
        return fontFilePath;
    }

    public void setFontFilePath(String fontFilePath) {
        this.fontFilePath = fontFilePath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (languageId != null ? languageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NdgLanguage)) {
            return false;
        }
        NdgLanguage other = (NdgLanguage) object;
        if ((this.languageId == null && other.languageId != null) || (this.languageId != null && !this.languageId.equals(other.languageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Languages[ languageId=" + languageId + " ]";
    }
    
}
