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

package controllers.exceptions;

public class ResultSaveException extends MSMApplicationException {

    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "MSM_CORE_MSG_RESULT_NOT_SAVED";
    public static final String ERROR_CODE_NO_SURVEY = "SURVEY_NOT_FOUND";
    public static final String ERROR_CODE_SURVEY_NOT_SEND_TO_USER = "USER_CANT_USE_THIS_SURVEY";

    public ResultSaveException(String errorCode) {
        super();
        setErrorCode( errorCode );
    }
    
    public ResultSaveException() {
        super();
        setErrorCode( ERROR_CODE );
    }

    public ResultSaveException( Exception e ) {
        super( e );
        setErrorCode( ERROR_CODE );
    }
}
