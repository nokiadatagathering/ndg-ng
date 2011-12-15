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

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
@Table(name = "default_answer")
public class DefaultAnswer extends Model {

    @Lob
    @Column(name = "text_data")
    public String textData;

    @Column(name = "binary_data")
    public Blob binaryData;

    @OneToMany(mappedBy = "defaultAnswer")
    public Collection<Question> questionCollection;

    public DefaultAnswer() {
        questionCollection = new ArrayList<Question>();
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.DefaultAnswers[ defaultAnswerId=" + getId() + " ]";
    }
}
