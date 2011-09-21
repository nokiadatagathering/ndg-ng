/*
 * File encapsulates action related to Result List view
 *
 **/

var ResultList = function() {
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
        $('#executeExportAllResults').remove();
        $('#executeExportResults').remove();
        selectedResults = new Array();
        SurveyList.showSurveyList();
    }

    function fillWithResults(i, item) {
        $('#resultListTable').append( '<tr>'
                                    + '<td><input type="checkbox" id="resultCheckbox' + item.id + '"/></td>'
                                    + '<td>'+ item.resultId + '</td>'
                                    + '<td>' + item.title + '</td>'
                                    + '<td>' + new Date( item.startTime ) + '</td>'
                                    + '<td>' + item.ndgUser.username + '</td>'
                                    + '<td>' + ( item.latitude!= null ? 'OK': 'NO GPS' ) + '</td>'
                                    + '</tr>' );
        $( '#resultCheckbox' + item.id ).click( item.resultId, function(i){resultCheckboxClicked(i);} );
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
                $('#minimalist').before('<input type="button" id="executeExportResults" title="' + LOC.get('LOC_EXPORT_RESULTS') + '" value="' +  LOC.get('LOC_EXPORT_RESULTS') +'"/>');
                $('#executeExportResults').click( function() { exportResults() } );
            }
        } else {
            $('#executeExportResults').remove();
        }
    }

    function exportResults() {
        ExportResults.exportResults( currentSurveyId, selectedResults );
    }

    function exportAllResults() {
        ExportResults.exportAllResults( currentSurveyId );
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
        $('#minimalist').before('<a href="#" id="executeBackToSurveyList">' +  LOC.get('LOC_BACK_TO_SURVEY_LIST')+ '</a>');
        $('#minimalist').before('<input type="button" id="executeExportAllResults" title="' +  LOC.get('LOC_EXPORT_ALL_RESULTS') + '" value="' +  LOC.get('LOC_EXPORT_ALL_RESULTS') +'"/>');
        $('#executeBackToSurveyList').click( function(){backToSurveyList()} );
        $('#executeExportAllResults').click( function() { exportAllResults() } );
        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultId"><b>' +  LOC.get('LOC_RESULTID') + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultTitle"><b>' +  LOC.get('LOC_RESULTTITLE') + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByDateSent"><b>' +  LOC.get('LOC_DATESENT') + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByUser"><b>' +  LOC.get('LOC_USER') + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByLocation"><b>'+  LOC.get('LOC_LOCATION') + '</b></th>'
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
            document.getElementById( 'executeSortByResultId' ).text =  LOC.get('LOC_RESULTID') + CONST.get('DESC');
            resultIdAsc = false;
        } else {
            document.getElementById( 'executeSortByResultId' ).text =  LOC.get('LOC_RESULTID') + CONST.get('ASC');
            resultIdAsc = true;
        }
        fillSurveyData( 'resultId', resultIdAsc );
    }

    function toggleSortByResultTitle() {
        resetColumnTitle();
        if ( resultTitleAsc ) {
            document.getElementById( 'executeSortByResultTitle' ).text =  LOC.get('LOC_RESULTTITLE') + CONST.get('DESC');
            resultTitleAsc= false;
        } else {
            document.getElementById( 'executeSortByResultTitle' ).text =  LOC.get('LOC_RESULTTITLE') + CONST.get('ASC');
            resultTitleAsc = true;
        }
        fillSurveyData( 'title', resultTitleAsc );
    }

    function toggleSortByDateSent() {
        resetColumnTitle();
        if ( dateSentAsc ) {
            document.getElementById( 'executeSortByDateSent' ).text =  LOC.get('LOC_DATESENT') + CONST.get('DESC');
            dateSentAsc = false;
        } else {
            document.getElementById( 'executeSortByDateSent' ).text =  LOC.get('LOC_DATESENT') + CONST.get('ASC');
            dateSentAsc = true;
        }
        fillSurveyData( 'startTime', dateSentAsc );
    }

    function toggleSortByUser() {
        resetColumnTitle();
        if ( userAsc ) {
            document.getElementById( 'executeSortByUser' ).text =  LOC.get('LOC_USER') + CONST.get('DESC');
            userAsc = false;
        } else {
            document.getElementById( 'executeSortByUser' ).text =  LOC.get('LOC_USER') + CONST.get('ASC');
            userAsc = true;
        }
        fillSurveyData( 'ndgUser.username', userAsc );
    }

    function toggleSortByLocation() {
        resetColumnTitle();
        if ( locationAsc) {
            document.getElementById( 'executeSortByLocation' ).text =  LOC.get('LOC_LOCATION') + CONST.get('DESC');
            locationAsc = false;
        } else {
            document.getElementById( 'executeSortByLocation' ).text =  LOC.get('LOC_LOCATION') + CONST.get('ASC');
            locationAsc = true;
        }
        fillSurveyData( 'latitude', locationAsc );
    }

    function resetColumnTitle() {
        document.getElementById( 'executeSortByResultId' ).text =  LOC.get('LOC_RESULTID');
        document.getElementById( 'executeSortByResultTitle' ).text =  LOC.get('LOC_RESULTTITLE');
        document.getElementById( 'executeSortByDateSent' ).text =  LOC.get('LOC_DATESENT');
        document.getElementById( 'executeSortByUser' ).text =  LOC.get('LOC_USER');
        document.getElementById( 'executeSortByLocation').text =  LOC.get('LOC_LOCATION');
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
        $('#navi').append( '<a class="buttonPrevious" id="buttonPrevious"/><a class="nextPrevText">'
                         + (resultStartIndex + 1)
                         + ' of '
                         + totalPages
                         + '</a><a class="buttonNext"  id="buttonNext"/>' );

        $("#buttonPrevious").click( function(){onPreviousClicked();} );
        $("#buttonNext").click( function(){onNextClicked();} );
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