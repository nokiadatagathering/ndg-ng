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

package models.constants;

public class TransactionlogConsts {

    public class TransactionType {

        public static final String TYPE_SEND_SURVEY = "SS"; //Send Survey to Client
        public static final String TYPE_RECEIVE_SURVEY = "RS"; //Received Survey from Editor
        public static final String TYPE_RECEIVE_RESULT = "RR"; //Received Result from Client
        public static final String TYPE_SEND_RESULT = "SR"; //Send Result to Client
        public static final String SEND_ALERT = "SA"; //Send Alert by SMS to Client
        public static final String DOWNLOAD_CLIENT = "DC"; //Download Client
        public static final String NEW_USER_ADMIN = "NU"; //register new user by Request Access
    }

    public class TransactionMode {

        public static final String MODE_CABLE = "CABLE";
        public static final String MODE_SMS = "SMS";
        public static final String MODE_HTTP = "HTTP";
        public static final String MODE_GPRS = "GPRS";
    }

    public class TransactionStatus {

        public static final String STATUS_SUCCESS = "SUCCESS";
        public static final String STATUS_AVAILABLE = "AVAILABLE";
        public static final String STATUS_PENDING = "PENDING";
        public static final String STATUS_ERROR = "ERROR";
    }
}
