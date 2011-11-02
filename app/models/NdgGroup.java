package models;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table( name = "ndg_group" )
public class NdgGroup extends Model {

    @Required
    @Column( unique = true, nullable=false,  name = "group_name" )
    public String groupName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ndg_group")
    public Collection<NdgUser> userCollection;

    public NdgGroup() {
    }

    public NdgGroup( String groupName ) {
          this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.NdgGroup[ groupId=" + getId() + " ]";
    }
}
