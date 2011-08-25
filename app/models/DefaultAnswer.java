/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
@Table(name = "default_answer")
public class DefaultAnswer extends Model {

    @Lob
    public String textData;
    
    @Column(name = "binaryData")
    public Blob binaryData;
    
    @OneToMany(mappedBy = "defaultAnswer")
    public Collection<Question> questionCollection;

    public DefaultAnswer() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.DefaultAnswers[ defaultAnswerId=" + getId() + " ]";
    }
}
