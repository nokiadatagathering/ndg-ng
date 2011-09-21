/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_result" )
public class NdgResult extends Model {
    
    @Required
    @Column( name = "ndg_result_id" )
    public String resultId;
    
    @Required
    @Column( nullable = false, name = "start_time" )
    @Temporal( TemporalType.TIMESTAMP )
    public Date startTime;
    
    @Required
    @Column( nullable = false, name = "end_time" )
    @Temporal( TemporalType.TIMESTAMP )
    public Date endTime;
    
    public String title;
    
    public String latitude;
    
    public String longitude;
    
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgResult" )
    @OnDelete(action=OnDeleteAction.CASCADE)
    public Collection<Answer> answerCollection;

    @ManyToOne( optional = false )
    public Survey survey;
    
    @ManyToOne( optional = false )
    @JoinColumn( name = "ndg_user_id")
    public NdgUser ndgUser;

    public NdgResult() {
    }

   
    public NdgResult( String resultId, Date startTime, Date endTime ) {
        this.resultId = resultId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.NdgResult[ resultId=" + getId() + " ]";
    }
    
}
