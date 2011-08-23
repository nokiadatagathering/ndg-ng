/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.structure;

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
@Table(name = "question_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuestionType.findAll", query = "SELECT q FROM QuestionType q"),
    @NamedQuery(name = "QuestionType.findByQuestionTypeId", query = "SELECT q FROM QuestionType q WHERE q.questionTypeId = :questionTypeId"),
    /*used*/@NamedQuery(name = "QuestionType.findByTypeName", query = "SELECT q FROM QuestionType q WHERE q.typeName = :typeName"),
    @NamedQuery(name = "QuestionType.findBySupported", query = "SELECT q FROM QuestionType q WHERE q.supported = :supported")})
public class QuestionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "questionTypeId")
    private Integer questionTypeId;
    @Column(name = "typeName")
    private String typeName;
    @Column(name = "supported")
    private Integer supported;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionTypeQuestionTypeId")
    private Collection<Question> questionCollection;

    public QuestionType() {
    }

    public QuestionType(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getSupported() {
        return supported;
    }

    public void setSupported(Integer supported) {
        this.supported = supported;
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
        hash += (questionTypeId != null ? questionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionType)) {
            return false;
        }
        QuestionType other = (QuestionType) object;
        if ((this.questionTypeId == null && other.questionTypeId != null) || (this.questionTypeId != null && !this.questionTypeId.equals(other.questionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.QuestionTypes[ questionTypeId=" + questionTypeId + " ]";
    }
    
}
