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

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "category")
public class Category extends Model{

    @Column(nullable=false)
    public  String label;

    @Required
    @Column( nullable= false, name = "object_name")
    public String objectName;

    @Column(name="category_index")
    public Integer categoryIndex;

    @Required
    @ManyToOne(optional = false)
    public Survey survey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @OnDelete(action=OnDeleteAction.CASCADE)
    public List<Question> questionCollection;

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Category[ id=" +  " ]";
    }
}
