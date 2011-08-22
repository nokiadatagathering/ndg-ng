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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findByQuestionId", query = "SELECT q FROM Question q WHERE q.questionId = :questionId"),
    @NamedQuery(name = "Question.findByObjectName", query = "SELECT q FROM Question q WHERE q.objectName = :objectName"),
    @NamedQuery(name = "Question.findByConstraintText", query = "SELECT q FROM Question q WHERE q.constraintText = :constraintText"),
    @NamedQuery(name = "Question.findByRequired", query = "SELECT q FROM Question q WHERE q.required = :required"),
    @NamedQuery(name = "Question.findByReadonly", query = "SELECT q FROM Question q WHERE q.readonly = :readonly")})
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "questionId")
    private Integer questionId;
    @Basic(optional = false)
    @Column(name = "objectName")
    private String objectName;
    @Basic(optional = false)
    @Lob
    @Column(name = "label")
    private String label;
    @Lob
    @Column(name = "hint")
    private String hint;
    @Column(name = "constraintText")
    private String constraintText;
    @Column(name = "required")
    private Integer required;
    @Column(name = "readonly")
    private Integer readonly;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionQuestionId")
    private Collection<Answer> answerCollection;
    @JoinColumn(name = "surveysSurveyId", referencedColumnName = "surveyId")
    @ManyToOne(optional = false)
    private Survey surveysSurveyId;
    @JoinColumn(name = "questionTypeQuestionTypeId", referencedColumnName = "questionTypeId")
    @ManyToOne(optional = false)
    private QuestionType questionTypeQuestionTypeId;
    @JoinColumn(name = "defaultAnswerDefaultAnswerId", referencedColumnName = "defaultAnswerId")
    @ManyToOne
    private DefaultAnswer defaultAnswerDefaultAnswerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionQuestionId")
    private Collection<QuestionOption> questionOptionCollection;

    public Question() {
    }

    public Question(Integer questionId) {
        this.questionId = questionId;
    }

    public Question(Integer questionId, String objectName, String label) {
        this.questionId = questionId;
        this.objectName = objectName;
        this.label = label;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getConstraintText() {
        return constraintText;
    }

    public void setConstraintText(String constraintText) {
        this.constraintText = constraintText;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getReadonly() {
        return readonly;
    }

    public void setReadonly(Integer readonly) {
        this.readonly = readonly;
    }

    @XmlTransient
    public Collection<Answer> getAnswersCollection() {
        return answerCollection;
    }

    public void setAnswersCollection(Collection<Answer> answersCollection) {
        this.answerCollection = answersCollection;
    }

    public Survey getSurveysSurveyId() {
        return surveysSurveyId;
    }

    public void setSurveysSurveyId(Survey surveysSurveyId) {
        this.surveysSurveyId = surveysSurveyId;
    }

    public QuestionType getQuestionTypesQuestionTypeId() {
        return questionTypeQuestionTypeId;
    }

    public void setQuestionTypesQuestionTypeId(QuestionType questionTypesQuestionTypeId) {
        this.questionTypeQuestionTypeId = questionTypesQuestionTypeId;
    }

    public DefaultAnswer getDefaultAnswerDefaultAnswerId() {
        return defaultAnswerDefaultAnswerId;
    }

    public void setDefaultAnswerDefaultAnswerId(DefaultAnswer defaultAnswerDefaultAnswerId) {
        this.defaultAnswerDefaultAnswerId = defaultAnswerDefaultAnswerId;
    }

    @XmlTransient
    public Collection<QuestionOption> getQuestionOptionsCollection() {
        return questionOptionCollection;
    }

    public void setQuestionOptionsCollection(Collection<QuestionOption> questionOptionsCollection) {
        this.questionOptionCollection = questionOptionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionId != null ? questionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.questionId == null && other.questionId != null) || (this.questionId != null && !this.questionId.equals(other.questionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Questions[ questionId=" + questionId + " ]";
    }
    
}
