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
import play.db.jpa.Blob;


@Entity
@Table(name = "ndg_language")
public class NdgLanguage extends Model {
    @Required
    @Column(nullable = false)
    public String name;

    @Required
    @Column(nullable = false, name = "locale_string")
    public String localeString;

    @Required
    @Column(nullable = false, name = "translation_file" )
    public Blob translationFile;

    @Column(name = "font_file")
    public Blob fontFile;

    public NdgLanguage() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Languages[ languageId=" + getId() + " ]";
    }

}
