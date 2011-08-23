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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "question_option")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuestionOption.findAll", query = "SELECT q FROM QuestionOption q"),
    @NamedQuery(name = "QuestionOption.findByQuestionOptionId", query = "SELECT q FROM QuestionOption q WHERE q.questionOptionId = :questionOptionId"),
    @NamedQuery(name = "QuestionOption.findByOptionIndex", query = "SELECT q FROM QuestionOption q WHERE q.optionIndex = :optionIndex"),
    @NamedQuery(name = "QuestionOption.findByLabel", query = "SELECT q FROM QuestionOption q WHERE q.label = :label"),
    @NamedQuery(name = "QuestionOption.findByOptionValue", query = "SELECT q FROM QuestionOption q WHERE q.optionValue = :optionValue")})
public class QuestionOption implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "questionOptionId")
    private Integer questionOptionId;
    @Basic(optional = false)
    @Column(name = "optionIndex")
    private int optionIndex;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Basic(optional = false)
    @Column(name = "optionValue")
    private String optionValue;
    @JoinColumn(name = "questionQuestionId", referencedColumnName = "questionId")
    @ManyToOne(optional = false)
    private Question questionQuestionId;

    public QuestionOption() {
    }

    public QuestionOption(Integer questionOptionId) {
        this.questionOptionId = questionOptionId;
    }

    public QuestionOption(Integer questionOptionId, int index, String label, String optionValue) {
        this.questionOptionId = questionOptionId;
        this.optionIndex = index;
        this.label = label;
        this.optionValue = optionValue;
    }

    public Integer getIdQuestionOptions() {
        return questionOptionId;
    }

    public void setIdQuestionOptions(Integer idQuestionOptions) {
        this.questionOptionId = idQuestionOptions;
    }

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int index) {
        this.optionIndex = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Question getQuestionsQuestionId() {
        return questionQuestionId;
    }

    public void setQuestionsQuestionId(Question questionsQuestionId) {
        this.questionQuestionId = questionsQuestionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionOptionId != null ? questionOptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionOption)) {
            return false;
        }
        QuestionOption other = (QuestionOption) object;
        if ((this.questionOptionId == null && other.questionOptionId != null) || (this.questionOptionId != null && !this.questionOptionId.equals(other.questionOptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.QuestionOptions[ idQuestionOptions=" + questionOptionId + " ]";
    }
    
}
