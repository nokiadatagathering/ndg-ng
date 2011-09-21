/*
 * File encapsulates action related to Survey List view
 *
 **/


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
                               + '<th scope="col"><a href="#" id="executeSortBySurveyName">' + LOC.get('LOC_SURVEY_NAME') + '</th>'
                               + '<th scope="col"><a href="#" id="executeSortByDatePublished">' + LOC.get('LOC_DATE_PUBLISHED') + '</th>'
                               + '<th scope="col"><a href="#" id="executeSortByPublisher">' + LOC.get('LOC_PUBLISHER') + '</th>'
                               + '<th scope="col"><a href="#" id="executeSortByResults">' + LOC.get('LOC_RESULTS') + '</th>'
                               + '<th scope="col"></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="surveyListTable">'
                               + '</tbody>');

        $("#executeSortBySurveyName").click( function(){toggleSortBySurveyName();} );
        $("#executeSortByDatePublished").click( function(){toggleSortByDatePublished();} );
        $("#executeSortByPublisher").click( function(){toggleSortByPublisher();} );
        $("#executeSortByResults").click( function(){toggleSortByResults();} );
        $('#uploadForm').submit(function () {uploadNewSurvey();} );
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
            document.getElementById( 'executeSortByResults' ).text = LOC.get('LOC_RESULTS')+ CONST.get('DESC');
            lastSortAscending = resultsSortAscending = false;
        } else {
            document.getElementById( 'executeSortByResults' ).text = LOC.get('LOC_RESULTS') + CONST.get('ASC');
            lastSortAscending = resultsSortAscending = true;
        }
        fillSurveyData( 'resultCollection', resultsSortAscending );
    }

    function toggleSortByPublisher() {
        resetColumnTitle();
        if ( publisherSortAscending ) {
            document.getElementById( 'executeSortByPublisher' ).text = LOC.get('LOC_PUBLISHER') + CONST.get('DESC');
            lastSortAscending = publisherSortAscending = false;
        } else {
            document.getElementById( 'executeSortByPublisher' ).text = LOC.get('LOC_PUBLISHER') + CONST.get('ASC')
            lastSortAscending = publisherSortAscending = true;
        }
        fillSurveyData( 'ndgUser.username', publisherSortAscending );
    }

    function toggleSortByDatePublished() {
        resetColumnTitle();
        if ( datePublishedSortAscending ) {
            document.getElementById( 'executeSortByDatePublished' ).text = LOC.get('LOC_DATE_PUBLISHED') + CONST.get('DESC');
            lastSortAscending = datePublishedSortAscending = false;
        } else {
            document.getElementById( 'executeSortByDatePublished' ).text= LOC.get('LOC_DATE_PUBLISHED') + CONST.get('ASC');
            lastSortAscending = datePublishedSortAscending = true;
        }
        fillSurveyData( 'uploadDate', datePublishedSortAscending );
    }

    function toggleSortBySurveyName() {
        resetColumnTitle();
        if ( surveyNameSortAscending ) {
            document.getElementById( 'executeSortBySurveyName' ).text = LOC.get('LOC_SURVEY_NAME') + CONST.get('DESC');
            surveyNameSortAscending = false;
        } else {
            document.getElementById( 'executeSortBySurveyName' ).text = LOC.get('LOC_SURVEY_NAME') + CONST.get('ASC');
            surveyNameSortAscending = true;
        }
        fillSurveyData( 'title', surveyNameSortAscending );
    }

    function resetColumnTitle() {
        document.getElementById( 'executeSortBySurveyName' ).text = LOC.get('LOC_SURVEY_NAME');
        document.getElementById( 'executeSortByDatePublished' ).text = LOC.get('LOC_DATE_PUBLISHED');
        document.getElementById( 'executeSortByPublisher' ).text = LOC.get('LOC_PUBLISHER');
        document.getElementById( 'executeSortByResults' ).text = LOC.get('LOC_RESULTS');
    }

    function fillWithData(i, item) {
        $('#surveyListTable').append( '<tr id="Survey'+ item.id + '">'
                                    + '<td id="testData">'+ item.title + '<br>ID ' + item.surveyId + '</td>'
                                + '<td>' + new Date( item.uploadDate ).toLocaleDateString() + '</td>'
                                + '<td>' + item.ndgUser.username + '</td>'
                                + '<td><a href="#" id="Item'+ item.id + '">' + item.resultCollection + '</a></td>'
                                + '<td><div class="menu" id="menu' + item.id + '" >'
                                + '<button type="button" class="buttonDownload" title="' + LOC.get('LOC_DOWNLOAD') + '"/>'
                                + '<button type="button" class="buttonUpload" title="' + LOC.get('LOC_UPLOAD') + '"/>'
                                + '<button type="button" class="buttonSend" title="' + LOC.get('LOC_SEND') + '"/>'
                                + '<button type="button" class="buttonEdit" title="' + LOC.get('LOC_EDIT') + '"/>'
                                + '<button type="button" class="buttonDuplicate" title="' + LOC.get('LOC_DUPLICATE') + '"/>'
                                + '<button type="button" class="buttonDelete" title="' + LOC.get('LOC_DELETE') + '"/>'
                                + '</div>' +'</td>'
                                + '</tr>' );

        $( '#Survey' + item.id ).mouseover( item.id, function(i) { onMouseOverHandler(i); } );
        $( '#Survey' + item.id ).mouseout( item.id, function(i) { onMouseOutHandler(i); } );
        $( '#Item' + item.id ).click( item.id, function(i) {
            ResultList.showResultList(i);
        } );
        $('#menu' + item.id +' .buttonDownload').click( item.surveyId, function(i){onDownloadSurveyClicked(i);} );
        $('#menu' + item.id +' .buttonUpload').click( item.surveyId, function(i){onUploadSurveyClicked(i);} );
        $('#menu' + item.id +' .buttonDelete').click( item.surveyId, function(i){onDeleteSurveyClicked(i);} );
        $('#menu' + item.id +' .buttonDuplicate').click( item.surveyId, function(i){onDuplicateSurveyClicked(i);} );
        $('#menu' + item.id +' .buttonSend').click( item.surveyId, function(i){onSendSurveyClicked(i);} );
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
       $("#uploadSurveyId").val(e.data);
    }

    function onDeleteSurveyClicked(e) {
        confirmDeleteDialog.dialog("open");
        $("#buttonDeleteYes").click( e.data, function(e){
            $.post( "/delete/" + e.data, function(data) {
            confirmDeleteDialog.dialog("close");
            $('#surveyListTable').empty();
            fillSurveyData( lastSortByColumn, surveyNameSortAscending );
        });
        $("#buttonDeleteYes").unbind("click");
        $("#buttonDeleteNo").unbind("click");
        });
       $("#buttonDeleteNo").click( function(){
           $("#buttonDeleteYes").unbind("click");
           $("#buttonDeleteNo").unbind("click");
           confirmDeleteDialog.dialog("close");
       });
    }

    function onDuplicateSurveyClicked(e) {
       $.post( "/duplicate/" + e.data, function(data) {
            $('#surveyListTable').empty();
            fillSurveyData( lastSortByColumn, surveyNameSortAscending );
        });
    }

    function onSendSurveyClicked(e) {
        SendSurvey.showUserList(e);
        sendSurveyDialog.dialog("open");
    }

    function uploadNewSurvey() {
        var resultFrame = $('#uploadSurveyResult').load(function ()
        {
           var response = resultFrame.contents().find('body').find('pre').html();
            if(response === "success") {
                alert("success");
               } else {
                alert("failure")
               }

           resultFrame.unbind('load');
           uploadDialog.dialog("close");
           $(':file', '#uploadForm' ).val('');
        });
        return true;
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