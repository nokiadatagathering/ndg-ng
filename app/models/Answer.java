/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
@XmlRootElement
public class Answer extends Model 
{
    @Required
    @Column( name = "answerIndex", nullable= false )
    public int answerIndex;
    
    @Lob
    @MaxSize(10000) 
    @Column( name = "textData" )
    public String textData;
    
    @Column( name = "binaryData" )
    public Blob binaryData;
    
    @Required
    @ManyToOne(optional= false)
    public Question question;
    
    @Required
    @ManyToOne(optional=false)
    public NdgResult ndgResult;

    public Answer(int index ) {
        this.answerIndex = index;
    }

    
}
