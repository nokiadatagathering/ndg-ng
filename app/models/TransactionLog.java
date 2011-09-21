/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;


@Entity
@Table( name = "transaction_log" )
public class TransactionLog extends Model {

    public String address;

    @Column(name = "transaction_type")
    public String transactionType;

    @Column( name = "transaction_status")
    public String transactionStatus;

    @Column( name = "transmission_mode")
    public String transmissionMode;
    
    @Temporal( TemporalType.DATE )
    @Column( name = "transaction_date")
    public Date transactionDate;
    
    @Column( name = "id_result")
    public String idResult;
    
    @ManyToOne
    public Survey survey;
    
    @ManyToOne( optional = false )
    @JoinColumn( name = "ndg_user_id")
    public NdgUser ndgUser;

    public TransactionLog() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Transactionlog[ idTransactionLog=" + getId() + " ]";
    }
    
}
