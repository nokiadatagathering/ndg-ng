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
import controllers.exceptions.ResultSaveException;
import java.io.Reader;
import java.util.ArrayList;
import models.NdgResult;
import models.TransactionLog;
import models.constants.TransactionlogConsts;
import java.util.Date;
import models.Answer;
import models.Survey;

public class ResultPersister {

    public void postResult(Reader reader, String surveyId) throws MSMApplicationException {


        NdgResult ndgResult = createResult(surveyId);


        ResultParser parser = new ResultParser(reader, ndgResult, surveyId);
        parser.parse();

        ndgResult.save();
        setResultReceived(ndgResult);
    }

    private NdgResult createResult(String surveyId) throws ResultSaveException {
        Survey survey = Survey.find("bySurveyId", surveyId).first();
        if (survey == null) {
            throw new ResultSaveException(ResultSaveException._ERROR_CODE_NO_SURVEY);
        }

        NdgResult retval = new NdgResult();
        retval.survey = survey;
        if (retval.answerCollection == null) {
            retval.answerCollection = new ArrayList<Answer>();
        }
        return retval;
    }

    /**
     * Call log operation to register the result received by server.
     */
    private void setResultReceived(NdgResult result) {
        TransactionLog postResultTransaction = new TransactionLog();
        postResultTransaction.transactionDate = new Date();
        postResultTransaction.transactionStatus = TransactionlogConsts.TransactionStatus.STATUS_SUCCESS;
        postResultTransaction.survey = result.survey;
        postResultTransaction.idResult = result.resultId;
        postResultTransaction.ndgUser = result.ndgUser;

        postResultTransaction.save();
    }
}
