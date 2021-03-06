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
import java.util.Iterator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

import models.constants.QuestionTypesConsts;

/**
 *
 * @author dominik.frankowski
 */
@Entity
@Table( name = "ndg_user" )
@NamedQuery (name = "findUserSendingSurvey",
             query = "SELECT user FROM NdgUser user WHERE user.userAdmin = :userAdmin AND user.id " +
                     "NOT IN ( SELECT log.ndgUser.id " +
                                "FROM TransactionLog log " +
                                "WHERE log.survey.surveyId = :surveyId " +
                                "AND log.transactionStatus = 'AVAILABLE')")

public class NdgUser extends Model {
    @Required
    @Column( nullable=false )
    public String password;

    @Required
    @Column( unique = true, nullable=false )
    public String username;

    @Required
    @Column(nullable = false )
    public String email;

    @Required
    @Column(nullable = false, name = "first_name")
    public String firstName;

    @Required
    @Column(nullable = false, name = "last_name")
    public String lastName;

    @Column(name = "country_code")
    public String countryCode;

    @Column(name = "area_code")
    public String areaCode;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Required
    @Column( nullable = false, name = "user_admin" )
    public String userAdmin;

    @Required
    @Column( nullable = false, name = "user_validated" )
    public char userValidated;

    @Required
    @Column( nullable = false, name = "email_preferences" )
    public char emailPreferences;

    @Column( name = "validation_key")
    public String validationKey;

    @Required
    @Column( nullable = false, name = "has_full_permissions" )
    public char hasFullPermissions;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<UserBalance> userBalanceCollection;

    @ManyToOne( optional = false )
    public Company company;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<TransactionLog> transactionlogCollection;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<NdgResult> resultsCollection;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser" )
    public Collection<Survey> surveysCollection;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "ndgUser")
    public Collection<UserRole> userRoleCollection;

    @ManyToOne( optional = true )
    public NdgGroup ndg_group;

    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }

    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }

    public NdgUser() {
    }

    public boolean hasRole(String role)
    {
        boolean retval = false;
        for (Iterator<UserRole> it = userRoleCollection.iterator(); it.hasNext();) {
            UserRole userRole = it.next();
            if(userRole.ndgRole.roleName.equals(role)) {
                retval = true;
                break;
            }
        }
        return retval;
    }

    public NdgUser( String password, String username, String email, String firstName, String lastName, String phoneNumber,String userAdmin, char userValidated, char emailPreferences, char hasFullPermissions ) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAdmin = userAdmin;
        this.phoneNumber = phoneNumber;
        this.userValidated = userValidated;
        this.emailPreferences = emailPreferences;
        this.hasFullPermissions = hasFullPermissions;
    }

    @Override
    public void _delete() {
        super._delete();
        for (NdgResult current : resultsCollection) {
            Collection<Answer> answers = current.answerCollection;
            Answer answer = answers.iterator().next();

            if ( answer.question.questionType.typeName.equalsIgnoreCase( QuestionTypesConsts.IMAGE ) ) {
                answer.binaryData.getFile().delete();
            }
        }
    }

    @Override
    public String toString() {
        return "br.org.indt.ndg.server.persistence.structure.Users[ userId=" + getId() + " ]";
    }


}
