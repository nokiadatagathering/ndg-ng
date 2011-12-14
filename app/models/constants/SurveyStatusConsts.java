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
