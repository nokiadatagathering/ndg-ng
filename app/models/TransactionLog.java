/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;


@Entity
@Table( name = "transaction_log" )
public class TransactionLog extends Model {

    public String address;

    public String transactionType;

    public String transactionStatus;

    public String transmissionMode;
    
    @Temporal( TemporalType.DATE )
    public Date transactionDate;
    
    public String idResult;
    
    @ManyToOne
    public Survey survey;
    
    @ManyToOne( optional = false )
    public NdgUser ndgUser;

    public TransactionLog() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Transactionlog[ idTransactionLog=" + getId() + " ]";
    }
    
}
