/*
 *  Copyright (C) 2010-2011  INdT - Instituto Nokia de Tecnologia
 *
 *  NDG is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  NDG is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with NDG.  If not, see <http://www.gnu.org/licenses/
 */
package br.org.indt.ndg.server.persistence.logic;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.logging.Logger;
import javax.persistence.NoResultException;

public class ProcessDownloadSurvey {

    private static final Logger log = Logger.getLogger( ProcessDownloadSurvey.class.getName() );
//  XForms protocol specific query params as specified by:
//  https://bitbucket.org/javarosa/javarosa/wiki/FormListAPI
    private static final String DEVICE_ID_PARAM = "deviceID"; // mandatory
    private static final String FORM_ID_PARAM = "formID"; // optional
//    private static String VERBOSE_PARAM = "verbose"; // optional
//    private static final String FORM_ID_PARAM = "formID"; // non-standard
    private String mUserName = null;
    private Map<String, String[]> m_parameterMap = null;
    private DiscoveryDownloadBussinessDelegate xformsDataProvider;

    public ProcessDownloadSurvey( String thisServerAddress, String userName ) {
        mUserName = userName;
        xformsDataProvider = new DiscoveryDownloadBussinessDelegate();
        xformsDataProvider.setPortAndAddress( thisServerAddress );
        xformsDataProvider.setUserName( userName );
    }

    public String processDownloadCommand( String formId ) throws NoResultException {
        String result = xformsDataProvider.getFormattedSurvey( formId, mUserName );
        if ( result == null ) {
            throw new NoResultException();
        }
        return result;
    }

    public String processListCommand() throws NoResultException {
        String result = xformsDataProvider.getFormattedSurveyAvailableToDownloadList( mUserName );
        if ( result == null ) {
            throw new NoResultException();
        }
        return result;
    }
}
