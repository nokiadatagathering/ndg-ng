/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.db.jpa.Model;

@Entity
@Table(name = "user_balance")
public class UserBalance extends Model {

    public Integer users;

    public Integer imeis;

    @Column( name = "send_alerts")
    public Integer sendAlerts;

    public Integer results;

    public Integer surveys;
    
    @ManyToOne
    @JoinColumn( name = "ndg_user_id")
    public NdgUser ndgUser;

    public UserBalance() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Userbalance[ idUSerBalance=" + getId() + " ]";
    }
    
}
