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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.db.jpa.Model;

@Entity
@Table( name = "user_role" )
public class UserRole extends Model {

    @JoinColumn(referencedColumnName = "username", name="username")
    @ManyToOne( optional = false )
    public NdgUser ndgUser;

    @JoinColumn(referencedColumnName = "role_name", name = "ndg_role_role_name")
    @ManyToOne( optional = false )
    public NdgRole ndgRole;

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.UserRole[ id=" + id + " ]";
    }

}
