/*
 * File encapsulates action related to Survey List view
 *
 **/
const LOC_DOWNLOAD = 'Download';
const LOC_UPLOAD = 'Upload';
const LOC_EDIT = 'Edit';
const LOC_DUPLICATE = 'Duplicate';
const LOC_DELETE = 'Delete';
const LOC_SEND= 'Send';



var SurveyList = function() {

    var surveyStartIndex = 0;
    var surveyNameSortAscending = true;
    var datePublishedSortAscending = true;
    var publisherSortAscending = true;
    var resultsSortAscending = true;
    var lastSortByColumn = "title";
    var lastSortAscending = true;
    var totalPages = 1;

    return {showSurveyList : function(){showSurveyList();}
    };

    function showSurveyList (){
        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"><a href="#" id="executeSortBySurveyName"><img id="sortBySurveyName" src="images/surveyName.jpg"></a><!--<b>SURVEY NAME</b>--></th>'
                               + '<th scope="col"><a href="#" id="executeSortByDatePublished"><img id="sortByDatePublished" src="images/datePublished.jpg"></a><!--<b>DATE PUBLISHED</b>--></th>'
                               + '<th scope="col"><a href="#" id="executeSortByPublisher"><img id="sortByPublisher" src="images/publisher.jpg"></a><!--<b>PUBLISHER</b>--></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResults"><img id="sortByResults" src="images/results.jpg"></a><!--<b>RESULTS</b>--></th>'
                               + '<th scope="col"></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="surveyListTable">'
                               + '</tbody>');

        $("#executeSortBySurveyName").click( function(){toggleSortBySurveyName();} );
        $("#executeSortByDatePublished").click( function(){toggleSortByDatePublished();} );
        $("#executeSortByPublisher").click( function(){toggleSortByPublisher();} );
        $("#executeSortByResults").click( function(){toggleSortByResults();} );

        $.getJSON('/application/listsurveyscount', function(data){updateTotalPages(data);});
        fillSurveyData( lastSortByColumn, surveyNameSortAscending );
    }

    function updateTotalPages(data){
        totalPages = data.surveysCount;
        updatePageNumber();
    }

    function updatePageNumber() {
        $('#navi').empty();
        $('#navi').append( '<a href="#" id="executePreviousButton">'
                         + '<img id="prev_button" src="images/icon_previous_off.jpg"></a>'
                         + (surveyStartIndex + 1)
                         + ' of '
                         + totalPages
                         + '<a href="#" id="executeNextButton">'
                         + '<img id="next_button" src="images/icon_next_off.jpg"></a>' );
        $('#prev_button').mouseover( function(){onMouseOverPrevious();} )
        $('#prev_button').mouseout( function(){onMouseOutPrevious();} )
        $('#next_button').mouseover( function(){onMouseOverNext();} )
        $('#next_button').mouseout( function(){onMouseOutNext();} )
        $("#executePreviousButton").click( function(){onPreviousClicked();} );
        $("#executeNextButton").click( function(){onNextClicked();} );
    }

    function fillSurveyData( orderByColumn, isAscending ) {

        lastSortByColumn = orderByColumn;
        if ( 'resultCollection' != orderByColumn ) {
            $.getJSON('/application/listsurveys', {'startIndex': surveyStartIndex,
                                                   'isAscending': isAscending,
                                                    'orderBy': orderByColumn},
                                                    function(data){refreshTable(data);} );
        } else {
            $.getJSON('/application/listSurveysByResults', {'startIndex': surveyStartIndex,
                                                            'isAscending': resultsSortAscending
                                                            },
                                                            function(data) {refreshTable(data);} );
        }
    }

    function refreshTable(data) {
        $('#surveyListTable').empty();
        $.each(data.surveys,function(i,item) {
            fillWithData(i,item);
        });
    }

    function toggleSortByResults() {
        resetColumnTitle();
        if ( resultsSortAscending ) {
            document.getElementById( 'sortByResults' ).src = 'images/resultsDesc.jpg';
            lastSortAscending = resultsSortAscending = false;
        } else {
            document.getElementById( 'sortByResults' ).src = 'images/resultsAsc.jpg';
            lastSortAscending = resultsSortAscending = true;
        }
        fillSurveyData( 'resultCollection', resultsSortAscending );
    }

    function toggleSortByPublisher() {
        resetColumnTitle();
        if ( publisherSortAscending ) {
            document.getElementById( 'sortByPublisher' ).src = 'images/publisherDesc.jpg';
            lastSortAscending = publisherSortAscending = false;
        } else {
            document.getElementById( 'sortByPublisher' ).src = 'images/publisherAsc.jpg';
            lastSortAscending = publisherSortAscending = true;
        }
        fillSurveyData( 'ndgUser.username', publisherSortAscending );
    }

    function toggleSortByDatePublished() {
        resetColumnTitle();
        if ( datePublishedSortAscending ) {
            document.getElementById( 'sortByDatePublished' ).src = 'images/datePublishedDesc.jpg';
            lastSortAscending = datePublishedSortAscending = false;
        } else {
            document.getElementById( 'sortByDatePublished' ).src = 'images/datePublishedAsc.jpg';
            lastSortAscending = datePublishedSortAscending = true;
        }
        fillSurveyData( 'uploadDate', datePublishedSortAscending );
    }

    function toggleSortBySurveyName() {
        resetColumnTitle();
        if ( surveyNameSortAscending ) {
        document.getElementById( 'sortBySurveyName' ).src = 'images/surveyNameDesc.jpg';
        surveyNameSortAscending = false;
        } else {
            document.getElementById( 'sortBySurveyName' ).src = 'images/surveyNameAsc.jpg';
            surveyNameSortAscending = true;
        }
        fillSurveyData( 'title', surveyNameSortAscending );
    }

    function resetColumnTitle() {
        document.getElementById( 'sortBySurveyName' ).src = 'images/surveyName.jpg';
        document.getElementById( 'sortByDatePublished' ).src = 'images/datePublished.jpg';
        document.getElementById( 'sortByPublisher' ).src = 'images/publisher.jpg';
        document.getElementById( 'sortByResults' ).src = 'images/results.jpg';
    }

    function fillWithData(i, item) {
        $('#surveyListTable').append( '<tr id="Survey'+ item.id + '">'
                                    + '<td id="testData">'+ item.title + '<br>ID ' + item.surveyId + '</td>'
                                + '<td>' + new Date( item.uploadDate ).toLocaleDateString() + '</td>'
                                + '<td>' + item.ndgUser.username + '</td>'
                                + '<td><a href="#" id="Item'+ item.id + '">' + item.resultCollection + '</a></td>'
                                + '<td><div class="menu" id="menu' + item.id + '" >'
                                + '<button type="button" class="buttonDownload" title="' + LOC_DOWNLOAD + '"/>'
                                + '<button type="button" class="buttonUpload" title="' + LOC_UPLOAD + '"/>'
                                + '<button type="button" class="buttonSend" title="' + LOC_SEND + '"/>'
                                + '<button type="button" class="buttonEdit" title="' + LOC_EDIT + '"/>'
                                + '<button type="button" class="buttonDuplicate" title="' + LOC_DUPLICATE + '"/>'
                                + '<button type="button" class="buttonDelete" title="' + LOC_DELETE + '"/>'
                                + '</div>' +'</td>'
                                + '</tr>' );

        $( '#Survey' + item.id ).mouseover( item.id, function(i) { onMouseOverHandler(i); } );
        $( '#Survey' + item.id ).mouseout( item.id, function(i) { onMouseOutHandler(i); } );
        $( '#Item' + item.id ).click( item.id, function(i) {
            ResultList.showResultList(i);
        } );
        $('#menu' + item.id +' .buttonDownload').click( item.surveyId, function(i){onDownloadSurveyClicked(i);} );
        $('#menu' + item.id +' .buttonUpload').click( item.surveyId, function(i){onUploadSurveyClicked(i);} );
    }

    function onPreviousClicked(e) {
        if ( --surveyStartIndex < 0 ) {
            surveyStartIndex = 0;
        } else {
            fillSurveyData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

    function onNextClicked(e) {
        if( ++surveyStartIndex >= totalPages ) {
            surveyStartIndex--;
        } else {
            fillSurveyData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

    function onDownloadSurveyClicked(e) {
        window.location.href = 'management/' + e.data;
    }

    function onUploadSurveyClicked(e) {
       uploadDialog.dialog("open");
    }

    function onMouseOverHandler(e){
        $('#menu' + e.data + " button").addClass("hover");
    }

    function onMouseOutHandler(e){
        $('#menu' + e.data + " button").removeClass("hover");
    }

    function onMouseOverNext(){
        this.bgColor='#dbe4f1';
        document.getElementById('next_button').src='images/icon_next_on.jpg';
    }

    function onMouseOutNext(){
        this.bgColor='#edf0f6';
        document.getElementById('next_button').src='images/icon_next_off.jpg';
    }

    function onMouseOverPrevious(){
        this.bgColor='#dbe4f1';
        document.getElementById('prev_button').src='images/icon_previous_on.jpg';
    }

    function onMouseOutPrevious(){
        this.bgColor='#edf0f6';
        document.getElementById('prev_button').src='images/icon_previous_off.jpg';
    }
}();