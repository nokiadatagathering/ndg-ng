/*
 * File encapsulates action related to Result List view
 *
 **/

const LOC_RESULTID = 'ResultId';
const LOC_RESULTTITLE = 'Result Title';
const LOC_DATESENT = 'Date Sent';
const LOC_USER = 'User';
const LOC_LOCATION = 'Location';
const LOC_BACK_TO_SURVEY_LIST = 'Back To SurveyList';
const LOC_EXPORT_RESULTS = 'Export Results';

var ResultList = function() {

    const ASC = ' \u2191';
    const DESC = ' \u2193';

    var currentSurveyId;
    var resultStartIndex = 0;
    var totalPages = 1;
    var lastSortByColumn = "title";
    var lastSortAscending = true;
    var resultIdAsc = true;
    var dateSentAsc = true;
    var resultTitleAsc = true;
    var userAsc = true;
    var locationAsc = true;

    var selectedResults = new Array();

    function backToSurveyList() {
        $('#minimalist').empty();
        $('#executeBackToSurveyList').remove();
        selectedResults = new Array();
        SurveyList.showSurveyList();
    }

    function fillWithResults(i, item) {
        $('#resultListTable').append( '<tr>'
                                    + '<td><input type="checkbox" id="resultCheckbox' + item.resultId + '"/></td>'
                                    + '<td>'+ item.resultId + '</td>'
                                    + '<td>' + item.title + '</td>'
                                    + '<td>' + new Date( item.startTime ) + '</td>'
                                    + '<td>' + item.ndgUser.username + '</td>'
                                    + '<td>' + ( item.latitude!= null ? 'OK': 'NO GPS' ) + '</td>'
                                    + '</tr>' );
        $( '#resultCheckbox' + item.resultId ).click( item.resultId, function(i){resultCheckboxClicked(i);} );

    }

    function resultCheckboxClicked(i) {
        if( i.currentTarget.checked ) {
            if ( -1 == jQuery.inArray( i.data, selectedResults ) ) {
                selectedResults.push( i.data );
            }
        } else {
            if ( -1 != jQuery.inArray( i.data.toString(), selectedResults ) ) {
                selectedResults.splice(jQuery.inArray( i.data.toString(), selectedResults ), 1 );
            }
        }

        if ( selectedResults.length > 0 ) {
            if ( document.getElementById('executeExportResults') == null ) {
                $('#minimalist').before('<input type="button" id="executeExportResults" title="' + LOC_EXPORT_RESULTS + '" value="' + LOC_EXPORT_RESULTS +'"/>');
                $('#executeExportResults').click( function() { exportResults() } );
            }
        } else {
            $('#executeExportResults').remove();
        }
    }

    function exportResults() {
        //window.location.href = 'service/' + selectedResults;
        ExportResults.exportResults( selectedResults );
    }

    function fillResultsTable(data) {
        $('#surveyListTable').empty();
        $.each(data.results,function(i,item) {
            fillWithResults(i,item);
        });
    }

    function showResultList(i) {
        currentSurveyId = i.data;

        $('#minimalist').empty();
        $('#minimalist').before('<a href="#" id="executeBackToSurveyList">' + LOC_BACK_TO_SURVEY_LIST+ '</a>');
        $('#executeBackToSurveyList').click( function(){backToSurveyList()} );
        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultId"><b>' + LOC_RESULTID + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultTitle"><b>' + LOC_RESULTTITLE + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByDateSent"><b>' + LOC_DATESENT + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByUser"><b>' +LOC_USER + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByLocation"><b>'+ LOC_LOCATION + '</b></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="resultListTable">'
                               + '</tbody>' );

        $("#executeSortByResultId").click( function(){toggleSortByResultId();} );
        $("#executeSortByResultTitle").click( function(){toggleSortByResultTitle();} );
        $("#executeSortByDateSent").click( function(){toggleSortByDateSent();} );
        $("#executeSortByUser").click( function(){toggleSortByUser();} );
        $("#executeSortByLocation").click( function(){toggleSortByLocation();} );


        $.getJSON( '/application/listresultscount',
                   {'surveyId': parseInt(i.data)},
                   function(data){updateTotalPages(data);} );

        $.getJSON('/application/listResults', {'surveyId': parseInt(i.data),
                                               'startIndex': 0,
                                               'isAscending': true,
                                               'orderBy': "title"},
                                               function(i) {fillResultsTable(i);} );

    }

    function toggleSortByResultId() {
        resetColumnTitle();
        if ( resultIdAsc ) {
            document.getElementById( 'executeSortByResultId' ).text = LOC_RESULTID + DESC;
            resultIdAsc = false;
        } else {
            document.getElementById( 'executeSortByResultId' ).text = LOC_RESULTID + ASC;
            resultIdAsc = true;
        }
        fillSurveyData( 'resultId', resultIdAsc );
    }

    function toggleSortByResultTitle() {
        resetColumnTitle();
        if ( resultTitleAsc ) {
            document.getElementById( 'executeSortByResultTitle' ).text = LOC_RESULTTITLE + DESC;
            resultTitleAsc= false;
        } else {
            document.getElementById( 'executeSortByResultTitle' ).text = LOC_RESULTTITLE + ASC;
            resultTitleAsc = true;
        }
        fillSurveyData( 'title', resultTitleAsc );
    }

    function toggleSortByDateSent() {
        resetColumnTitle();
        if ( dateSentAsc ) {
            document.getElementById( 'executeSortByDateSent' ).text = LOC_DATESENT + DESC;
            dateSentAsc = false;
        } else {
            document.getElementById( 'executeSortByDateSent' ).text = LOC_DATESENT + ASC;
            dateSentAsc = true;
        }
        fillSurveyData( 'startTime', dateSentAsc );
    }

    function toggleSortByUser() {
        resetColumnTitle();
        if ( userAsc ) {
            document.getElementById( 'executeSortByUser' ).text = LOC_USER + DESC;
            userAsc = false;
        } else {
            document.getElementById( 'executeSortByUser' ).text = LOC_USER + ASC;
            userAsc = true;
        }
        fillSurveyData( 'ndgUser.username', userAsc );
    }

    function toggleSortByLocation() {
        resetColumnTitle();
        if ( locationAsc) {
            document.getElementById( 'executeSortByLocation' ).text = LOC_LOCATION + DESC;
            locationAsc = false;
        } else {
            document.getElementById( 'executeSortByLocation' ).text = LOC_LOCATION + ASC;
            locationAsc = true;
        }
        fillSurveyData( 'latitude', locationAsc );
    }

    function resetColumnTitle() {
        document.getElementById( 'executeSortByResultId' ).text = LOC_RESULTID;
        document.getElementById( 'executeSortByResultTitle' ).text = LOC_RESULTTITLE;
        document.getElementById( 'executeSortByDateSent' ).text = LOC_DATESENT;
        document.getElementById( 'executeSortByUser' ).text = LOC_USER;
        document.getElementById( 'executeSortByLocation').text = LOC_LOCATION;
    }

    function fillSurveyData( orderByColumn, isAscending ) {
        $('#resultListTable').empty();
        lastSortByColumn = orderByColumn;
        $.getJSON('/application/listResults', {'surveyId': currentSurveyId,
                                               'startIndex': resultStartIndex,
                                               'isAscending': isAscending,
                                               'orderBy': orderByColumn},
                                               function(i) { fillResultsTable(i); } );
    }

    function updateTotalPages(data){
        totalPages = data.resultsCount;
        updatePageNumber();
    }

    function updatePageNumber() {
        $('#navi').empty();
        $('#navi').append( '<a href="#" id="executePreviousButton">'
                         + '<img id="prev_button" src="images/icon_previous_off.jpg"></a>'
                         + (resultStartIndex + 1)
                         + ' of '
                         + totalPages
                         + '<a href="#" id="executeNextButton">'
                         + '<img id="next_button" src="images/icon_next_off.jpg"></a>' );

        $("#executePreviousButton").click( function(){onPreviousClicked();} );
        $("#executeNextButton").click( function(){onNextClicked();} );
    }

    function onPreviousClicked(e) {
        if ( --resultStartIndex < 0 ) {
            resultStartIndex = 0;
        } else {
            fillSurveyData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

    function onNextClicked(e) {
        if( ++resultStartIndex >= totalPages ) {
            resultStartIndex--;
        } else {
            fillSurveyData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

    return {showResultList : function(i) {showResultList(i);}
    };

}();