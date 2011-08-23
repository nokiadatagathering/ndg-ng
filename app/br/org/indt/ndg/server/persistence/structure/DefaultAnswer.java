/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.structure;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "default_answer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DefaultAnswer.findAll", query = "SELECT d FROM DefaultAnswer d"),
    @NamedQuery(name = "DefaultAnswer.findByDefaultAnswerId", query = "SELECT d FROM DefaultAnswer d WHERE d.defaultAnswerId = :defaultAnswerId")})
public class DefaultAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "defaultAnswerId")
    private Integer defaultAnswerId;
    @Lob
    @Column(name = "textData")
    private String textData;
    @Lob
    @Column(name = "binaryData")
    private byte[] binaryData;
    @OneToMany(mappedBy = "defaultAnswerDefaultAnswerId")
    private Collection<Question> questionCollection;

    public DefaultAnswer() {
    }

    public DefaultAnswer(Integer defaultAnswerId) {
        this.defaultAnswerId = defaultAnswerId;
    }

    public Integer getDefaultAnswerId() {
        return defaultAnswerId;
    }

    public void setDefaultAnswerId(Integer defaultAnswerId) {
        this.defaultAnswerId = defaultAnswerId;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    @XmlTransient
    public Collection<Question> getQuestionsCollection() {
        return questionCollection;
    }

    public void setQuestionsCollection(Collection<Question> questionsCollection) {
        this.questionCollection = questionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (defaultAnswerId != null ? defaultAnswerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefaultAnswer)) {
            return false;
        }
        DefaultAnswer other = (DefaultAnswer) object;
        if ((this.defaultAnswerId == null && other.defaultAnswerId != null) || (this.defaultAnswerId != null && !this.defaultAnswerId.equals(other.defaultAnswerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.DefaultAnswers[ defaultAnswerId=" + defaultAnswerId + " ]";
    }
    
}
