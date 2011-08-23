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

import br.org.indt.ndg.server.persistence.NdgEntityManagerUtils;
import br.org.indt.ndg.server.persistence.structure.Survey;
import br.org.indt.ndg.server.persistence.structure.Transactionlog;
import br.org.indt.ndg.server.persistence.structure.NdgUser;
import br.org.indt.ndg.server.persistence.structure.consts.TransactionlogConsts;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import play.db.jpa.JPA;

public class DiscoveryDownloadBussinessDelegate {

    private static final Logger log = Logger.getLogger( DiscoveryDownloadBussinessDelegate.class.getName() );
    private String m_surveysServerAddress = "http://localhost:8080"; // address that should be used to download surveys
    private String mUserName = null;

    public void setPortAndAddress( String thisServerAddress ) {
        m_surveysServerAddress = thisServerAddress;
    }

    public void setUserName( String userName ) {
        mUserName = userName;
    }

    /********** Downloading OpenRosa Surveys List and specific surveys **********/
    public String getFormattedSurveyAvailableToDownloadList( String userName ) {
        String result = null;
        EntityManager em = null;
        try {
            em = JPA.em();
            NdgUser user = getUserByUserName( em, userName );

            Query transactionQuery = em.createNamedQuery( "Transactionlog.findByUserIdAndStatus" );
            transactionQuery.setParameter( "ndgUserId", user.getIdUser() );
            transactionQuery.setParameter( "transactionStatus", TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE );
            ArrayList<Transactionlog> transactions = (ArrayList<Transactionlog>) transactionQuery.getResultList();

            if ( transactions.isEmpty() ) {//no survey for given client
                return null;
            }

            ArrayList<Survey> surveys = getSurveysFromTransactions( transactions );

            OpenRosaSurveysList list = new OpenRosaSurveysList( surveys );
            result = list.toString();
        } catch ( Exception e ) {
            log.log( Level.SEVERE, "Exception", e );
        } 
        return result;
    }

    public String getFormattedSurvey( String surveyId, String userName ) {
        String result = null;
        EntityManager em = null;
        try {
            em = JPA.em();
            NdgUser user = getUserByUserName( em, userName );

            Query transactionQuery = em.createNamedQuery( "Transactionlog.findFromTransactionByUserIdAndSurveyId" );
            transactionQuery.setParameter( "ndgUserId", user.getIdUser() );
            transactionQuery.setParameter( "transactionStatus", TransactionlogConsts.TransactionStatus.STATUS_AVAILABLE );
            transactionQuery.setParameter( "surveyId", surveyId );

            Transactionlog transaction = (Transactionlog) transactionQuery.getSingleResult();

            Query surveyQuery = em.createNamedQuery( "Survey.findBySurveyId" );
            surveyQuery.setParameter( "surveyId", transaction.getIdSurvey().getSurveyId() );
            Survey survey = (Survey) surveyQuery.getSingleResult();

            SurveyXmlBuilder builder = new SurveyXmlBuilder();//TODO use survey as an entry to XMLBuilder

            StringWriter buffer = new StringWriter();
            builder.printSurveyXml( surveyId, new PrintWriter( buffer ) );
            result = buffer.toString();
                    
            //result = "Survey found: " + survey.getTitle();

            transaction.setTransactionStatus( TransactionlogConsts.TransactionStatus.STATUS_SUCCESS );//ack
            NdgEntityManagerUtils.persist( em, transaction );
        } catch ( NoResultException ex ) {//it is OK for exception from Tranasction, id throw from survey need to be checked why
            log.log( Level.INFO, "getFormattedSurvey:NoResultException", ex );
            log.log( Level.INFO, "NoSurvey for {0}and deviceId{1}", new Object[]{ surveyId, userName });
            result = null;
        } catch ( Exception ex ) {
            log.log( Level.WARNING, "getFormattedSurvey:Exception", ex );
        } 
        return result;
    }

    private NdgUser getUserByUserName( EntityManager em, String userName ) {
        Query userQuery = em.createNamedQuery( "NdgUser.findByUsername" );
        userQuery.setParameter( "username", userName );

        return (NdgUser) userQuery.getSingleResult();
    }

    /********** Helpers **********/
    private String getSurveyDownloadUrl( String formId ) {
        return m_surveysServerAddress + "/ndg-server/ReceiveSurvey/download?formID=" + formId;
    }

    private ArrayList<Survey> getSurveysFromTransactions( ArrayList<Transactionlog> transactions ) {
        ArrayList<Survey> results = new ArrayList<Survey>();
        EntityManager em = null;
        try {
            em = JPA.em();
            for ( int i = 0; i < transactions.size(); i++ ) {
                Query transactionQuery = em.createNamedQuery( "Survey.findBySurveyId" );
                transactionQuery.setParameter( "surveyId", transactions.get( i ).getIdSurvey().getSurveyId() );
                try {
                    results.add( (Survey) transactionQuery.getSingleResult() );
                } catch ( NoResultException ex ) {
                    log.log( Level.INFO, "Missing Survey with given Id", ex );
                }
            }
        } catch ( Exception ex ) {
            log.log( Level.SEVERE, "Exception:getSurveysFromTransactions", ex );
        } 
        return results;
    }

    class OpenRosaSurveysList {

        private final Document m_surveysXml;

        public OpenRosaSurveysList( ArrayList<Survey> surveysSet ) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch ( ParserConfigurationException e ) {
                e.printStackTrace();
            }
            DOMImplementation impl = builder.getDOMImplementation();
            m_surveysXml = impl.createDocument( "http://openrosa.org/xforms/xformsList", "xforms", null );
            parseSurveyList( surveysSet );
        }

        private void parseSurveyList( ArrayList<Survey> surveysSet ) {
            Element root = m_surveysXml.getDocumentElement();
            Iterator<Survey> it = surveysSet.iterator();
            Survey currentSurvey = null;
            try {
                while ( it.hasNext() ) {
                    currentSurvey = it.next();
                    try {
                        Element xformElement = m_surveysXml.createElementNS( null, "xform" );
                        Element surveIdElement = m_surveysXml.createElementNS( null, "formID" );
                        Element nameElement = m_surveysXml.createElementNS( null, "name" );
                        Element majorMinorVersionElement = m_surveysXml.createElementNS( null, "majorMinorVersion" );
                        Element downloadUrlElement = m_surveysXml.createElementNS( null, "downloadUrl" );
                        surveIdElement.appendChild( m_surveysXml.createTextNode( currentSurvey.getSurveyId() ) );
                        nameElement.appendChild( m_surveysXml.createTextNode( currentSurvey.getSurveyId() ) ); //set to the same as id
                        majorMinorVersionElement.appendChild( m_surveysXml.createTextNode( "1.0" ) ); // fake
                        downloadUrlElement.appendChild( m_surveysXml.createTextNode( getSurveyDownloadUrl( currentSurvey.getSurveyId() ) ) );
                        xformElement.appendChild( surveIdElement );
                        xformElement.appendChild( nameElement );
                        xformElement.appendChild( majorMinorVersionElement );
                        xformElement.appendChild( downloadUrlElement );
                        root.appendChild( xformElement );
                    } catch ( DOMException domEx ) { // skipping element
                        log.log( Level.SEVERE, "parseSurveyList:DOMException", domEx );
                    }
                }
            } catch ( Exception ex ) {
                log.log( Level.WARNING, "parseSurveyList:Exception", ex );
            }
        }

        @Override
        public String toString() {
            String surveyXml = null;
            try {
                Source source = new DOMSource( m_surveysXml );
                StringWriter stringWriter = new StringWriter();
                Result result = new StreamResult( stringWriter );
                TransformerFactory transformFactory = TransformerFactory.newInstance();
                Transformer transformer = transformFactory.newTransformer();
                transformer.transform( source, result );
                surveyXml = stringWriter.getBuffer().toString();
            } catch ( TransformerConfigurationException ex ) {
                ex.printStackTrace();
            } catch ( TransformerException ex ) {
                ex.printStackTrace();
            }
            return surveyXml;
        }
    }
}
