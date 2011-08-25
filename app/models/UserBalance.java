/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.db.jpa.Model;

@Entity
@Table(name = "user_balance")
public class UserBalance extends Model {

    public Integer users;

    public Integer imeis;

    public Integer sendAlerts;

    public Integer results;

    public Integer surveys;
    
    @ManyToOne
    public NdgUser ndgUser;

    public UserBalance() {
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Userbalance[ idUSerBalance=" + getId() + " ]";
    }
    
}
