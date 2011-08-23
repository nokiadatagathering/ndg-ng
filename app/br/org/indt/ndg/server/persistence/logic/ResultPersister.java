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

import br.org.indt.ndg.server.exceptions.MSMApplicationException;
import java.io.Reader;
import java.util.logging.Logger;
import br.org.indt.ndg.server.exceptions.ResultNotParsedException;
import br.org.indt.ndg.server.exceptions.ResultSaveException;
import br.org.indt.ndg.server.persistence.NdgEntityManagerUtils;
import br.org.indt.ndg.server.persistence.structure.NdgResult;
import br.org.indt.ndg.server.persistence.structure.Transactionlog;
import br.org.indt.ndg.server.persistence.structure.consts.TransactionlogConsts;
import java.util.Date;
import java.util.logging.Level;
import org.javarosa.core.model.FormDef;



public class ResultPersister {

    private static final Logger log = Logger.getLogger( ResultPersister.class.getName() );
    private static final long serialVersionUID = 1L;

    public boolean postResult( Reader reader, String surveyId )
            throws MSMApplicationException {

        NdgResult ndgResult = null;
          try {
             ResultParser parser = new ResultParser(reader);
            parser.parse();
            ndgResult = parser.getResult();

        } catch ( Exception e ) {
            throw new ResultNotParsedException();
        }

        if ( !NdgEntityManagerUtils.persist( ndgResult ) ) {
            throw new ResultSaveException();
        }

        if ( setResultReceived( ndgResult ) ) {
            log.log( Level.INFO, "Result {0} successfully logged!", ndgResult.getResultId() );
        }
        return true;
    }

    /**
     * Call log operation to register the result received by server.
     */
    private boolean setResultReceived( NdgResult result ) {
        boolean valid = false;
        Transactionlog postResultTransaction = new Transactionlog();
        postResultTransaction.setTransactionDate( new Date() );
        postResultTransaction.setTransactionStatus( TransactionlogConsts.TransactionStatus.STATUS_SUCCESS );
        postResultTransaction.setIdSurvey( result.getSurveysSurveyId() );
        postResultTransaction.setIdResult( result.getResultId() );

        valid = NdgEntityManagerUtils.persist( postResultTransaction );
        if ( !valid ) {
            log.log( Level.SEVERE, "Logging result received via http id:{0}", result.getResultId() );
        }
        return valid;
    }

    private void persistAnswers(FormDef resultParsed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void persistAnswers(FormDef resultParsed, NdgResult ndgResult) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
