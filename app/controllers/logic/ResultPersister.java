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
package controllers.logic;

import controllers.exceptions.MSMApplicationException;
import java.io.Reader;
import controllers.exceptions.ResultNotParsedException;
import models.NdgResult;
import models.Transactionlog;
import models.constants.TransactionlogConsts;
import java.util.Date;
import org.javarosa.core.model.FormDef;

import play.db.jpa.JPA;

public class ResultPersister {


    public void postResult(Reader reader, String surveyId) throws MSMApplicationException {

        NdgResult ndgResult = null;
        try {
            ResultParser parser = new ResultParser(reader);
            parser.parse();
            ndgResult = parser.getResult();

        } catch (Exception e) {
            throw new ResultNotParsedException();
        }

        JPA.em().persist(ndgResult);

        setResultReceived(ndgResult);
    }

    /**
     * Call log operation to register the result received by server.
     */
    private void setResultReceived(NdgResult result) {
        Transactionlog postResultTransaction = new Transactionlog();
        postResultTransaction.setTransactionDate(new Date());
        postResultTransaction.setTransactionStatus(TransactionlogConsts.TransactionStatus.STATUS_SUCCESS);
        postResultTransaction.setIdSurvey(result.getSurveysSurveyId());
        postResultTransaction.setIdResult(result.getResultId());

        JPA.em().persist(postResultTransaction);
    }

    private void persistAnswers(FormDef resultParsed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void persistAnswers(FormDef resultParsed, NdgResult ndgResult) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
