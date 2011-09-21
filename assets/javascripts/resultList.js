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
        $('#resultListTable').append( '<tr id="Result' + item.id + '">'
                                    + '<td><input type="checkbox" id="resultCheckbox' + item.id + '"/></td>'
                                    + '<td>'+ item.resultId + '</td>'
                                    + '<td>' + item.title + '</td>'
                                    + '<td>' + new Date( item.startTime ).toString("dd/MM/yy") + '</td>'
                                    + '<td>' + item.ndgUser.username + '</td>'
                                    + '<td>' + ( item.latitude!= null ? 'OK': 'NO GPS' ) + '</td>'
                                    + '</tr>' );

        $( '#Result' + item.id ).mouseover( item.id, function(i) {onMouseOverHandler(i);} );
        $( '#Result' + item.id ).mouseout( item.id, function(i) {onMouseOutHandler(i);} );
        $( '#resultCheckbox' + item.id ).bind( 'check uncheck', i, function(i){resultCheckboxClicked(i);} )
    }

    function resultCheckboxClicked(i) {
        if( i.currentTarget.checked ) {
            if ( -1 == jQuery.inArray( i.data, selectedResults ) ) {
                selectedResults.push( i.data );
            }
        } else {
            if ( -1 != jQuery.inArray( i.data, selectedResults ) ) {
                selectedResults.splice(jQuery.inArray( i.data, selectedResults ), 1 );
            }
        }

//        if ( selectedResults.length > 0 ) {
//            if ( document.getElementById('executeExportResults') == null ) {
//                $('#minimalist').before('<input type="button" id="executeExportResults" title="' + LOC.get('LOC_EXPORT_RESULTS') + '" value="' +  LOC.get('LOC_EXPORT_RESULTS') +'"/>');
//                $('#executeExportResults').click( function() { exportResults() } );
//            }
//        } else {
//            $('#executeExportResults').remove();
//        }
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

        $('input:checkbox:not([safari])').checkbox({cls:'customCheckbox', empty:'../images/empty.png'});
    }

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').append( '<span class="buttonNext"  id="buttonNext" unselectable="on"></span>'
                                   + '<span class="buttonPrevious" id="buttonPrevious" unselectable="on"></span>'
                                   + '<span id="pageIndexText"><small>0</small> <strong>of 0</strong></span>' );
        $('#contentToolbar').append( '<span class="comboBoxSelection" id="comboBoxSelection" unselectable="on"/></span>');
        $('#contentToolbar').append( '<span class="buttonExportExcel" id="buttonExportExcel" unselectable="on"/></span>');
        $('#contentToolbar').append( '<span class="buttonExportKML" id="buttonExportKML" unselectable="on"/></span>');
        $('#contentToolbar').append( '<span class="buttonExportExternal" id="buttonExportExternal" unselectable="on"/></span>');
        $('#contentToolbar span').mousedown(function() { onButtonMouseDownHandler($(this));} );
    }

    function showResultList(i) {
        currentSurveyId = i.data;

        $('#leftColumnContent' ).empty();
        $('#leftColumnContent' ).append( '<a href="#"><img id ="backButtonImage" src="images/back.png"></a>');
        $('#backButtonImage').click( function(){backToSurveyList()} );

        prepareContentToolbar();

        $('#minimalist').empty();
        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col" class="checkColumn" ></th>'
                               + '<th scope="col" class="resultIdColumn"><a href="#" id="executeSortByResultId">' +  LOC.get('LOC_RESULTID') + '</th>'
                               + '<th scope="col" class="resultTitleColumn"><a href="#" id="executeSortByResultTitle">' +  LOC.get('LOC_RESULTTITLE') + '</th>'
                               + '<th scope="col" class="dataSentColumn"><a href="#" id="executeSortByDateSent">' +  LOC.get('LOC_DATESENT') + '</th>'
                               + '<th scope="col" class="userColumn"><a href="#" id="executeSortByUser">' +  LOC.get('LOC_USER') + '</th>'
                               + '<th scope="col" class="locationColumn"><a href="#" id="executeSortByLocation">'+  LOC.get('LOC_LOCATION') + '</th>'
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

    function onButtonMouseDownHandler(source) {
        source.addClass('pushed');
        $(document).mouseup(function() {
            $('.pushed').removeClass('pushed');
            $('body').unbind('mouseup');
            return false; });
        return false;
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

    function updateTotalPages(data) {
        totalPages = data.resultsCount;
        updatePageNumber();
    }

    function updatePageNumber() {
        $("#pageIndexText").empty();
        $("#pageIndexText").append( '<small>' + (resultStartIndex + 1) + '</small>' + '<strong> of ' + totalPages + '</strong>' );

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

    function onMouseOverHandler(e){
        $('#Result'+ e.data).addClass("hoverRow");
    }

    function onMouseOutHandler(e){
        $('#Result'+ e.data).removeClass("hoverRow");
    }

    return {showResultList : function(i) {showResultList(i);}
    };

}();