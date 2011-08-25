/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
@Table(name = "ndg_language")
public class NdgLanguage extends Model {
    @Required
    @Column(nullable = false)
    public String name;
    
    @Required
    @Column(nullable = false)
    public String localeString;
    
    @Required
    @Column(nullable = false)
    public String translationFilePath;
    
    public String fontFilePath;

    public NdgLanguage() {
    }

    public NdgLanguage(String name, String localeString, String translationFilePath) {
        this.name = name;
        this.localeString = localeString;
        this.translationFilePath = translationFilePath;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Languages[ languageId=" + getId() + " ]";
    }
    
}
