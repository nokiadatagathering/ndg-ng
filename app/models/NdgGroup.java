/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

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
