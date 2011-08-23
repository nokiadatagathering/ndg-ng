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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table( name = "answer" )
@XmlRootElement
@NamedQueries( { 
    @NamedQuery( name = "Answer.findAll", query = "SELECT a FROM Answer a" ),
    @NamedQuery( name = "Answer.findByAnswerId", query = "SELECT a FROM Answer a WHERE a.answerId = :answerId" ),
    @NamedQuery( name = "Answer.findByAnswerIndex", query = "SELECT a FROM Answer a WHERE a.answerIndex = :answerIndex" ) } )
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "answerId" )
    private Integer answerId;
    @Basic( optional = false )
    @Column( name = "answerIndex" )
    private int answerIndex;
    @Lob
    @Column( name = "textData" )
    private String textData;
    @Lob
    @Column( name = "binaryData" )
    private byte[] binaryData;
    @JoinColumn( name = "questionQuestionId", referencedColumnName = "questionId" )
    @ManyToOne( optional = false )
    private Question questionQuestionId;
    @JoinColumn( name = "ndgResultNdgResultId", referencedColumnName = "ndgResultId" )
    @ManyToOne( optional = false )
    private NdgResult ndgResultNdgResultId;

    public Answer() {
    }

    public Answer( Integer answerId ) {
        this.answerId = answerId;
    }

    public Answer( Integer answerId, int index ) {
        this.answerId = answerId;
        this.answerIndex = index;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId( Integer answerId ) {
        this.answerId = answerId;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex( int index ) {
        this.answerIndex = index;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData( String textData ) {
        this.textData = textData;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData( byte[] binaryData ) {
        this.binaryData = binaryData;
    }

    public Question getQuestionsQuestionId() {
        return questionQuestionId;
    }

    public void setQuestionsQuestionId( Question questionsQuestionId ) {
        this.questionQuestionId = questionsQuestionId;
    }

    public NdgResult getResultsResultId() {
        return ndgResultNdgResultId;
    }

    public void setResultsResultId( NdgResult resultsResultId ) {
        this.ndgResultNdgResultId = resultsResultId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answerId != null ? answerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof Answer) ) {
            return false;
        }
        Answer other = (Answer) object;
        if ( (this.answerId == null && other.answerId != null) || (this.answerId != null && !this.answerId.equals( other.answerId )) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Answers[ answerId=" + answerId + " ]";
    }
    
}
