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


public class SurveyStatusConsts {

    public static final int BUILDING = 0;
    public static final int AVAILABLE = 1;
    public static final int CLOSED = 2;


    public static int getStatusFlag( String filter ) {
        if ( "filterBuilding".equalsIgnoreCase( filter ) ) {
            return BUILDING;
        } else if ( "filterAvailable".equalsIgnoreCase( filter ) ) {
            return AVAILABLE;
        } else if ( "filterClosed".equalsIgnoreCase( filter ) ) {
            return CLOSED;
        } else {
            return CLOSED;
        }
    }
}
