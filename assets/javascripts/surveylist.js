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

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').append( '<span class="buttonNext"  id="buttonNext"></span>'
                                   + '<span class="buttonPrevious" id="buttonPrevious"></span>'
                                   + '<span id="pageIndexText"><small>0</small> <strong>of 0</strong></span>' );
    }

    function showSurveyList (){
        prepareContentToolbar();

        $('#plusButton').mouseover( function(event) {SurveyListCombo.showMenu();});
        $('#leftColumnContent' ).empty();
        $('#leftColumnContent' ).append( '<h3>STATUS</h3><h4 class="markBuilding">Building</h4><h4 class="markAvailable">Available</h4>');

        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"><a href="#" id="executeSortBySurveyName">' + LOC.get('LOC_SURVEY_NAME') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByDatePublished">' + LOC.get('LOC_DATE_PUBLISHED') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByPublisher">' + LOC.get('LOC_PUBLISHER') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResults">' + LOC.get('LOC_RESULTS') + '</a></th>'
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
        $("#pageIndexText").empty();
        $("#pageIndexText").append( '<small>' + (surveyStartIndex + 1) + '</small>' + '<strong> of ' + totalPages + '</strong>' );
        $("#buttonPrevious").click( function(){onPreviousClicked();} );
        $("#buttonNext").click( function(){onNextClicked();} );
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
        var date = new Date( item.uploadDate )
        $('#surveyListTable').append( '<tr id="Survey'+ item.id + '">'
                                    + '<td id="surveyNameCell">'+ item.title + '<br>ID: ' + item.surveyId + '</td>'
                                + '<td>' + date.toString("dd/MM/yy") + '</td>'
                                + '<td>' + item.ndgUser.username + '</td>'
                                + '<td id="resultCollectionQuantityString' + item.id + '"></td>'
                                + '<td class="menubar" id="menu' + item.id + '" >'
                                + '<span title="' + LOC.get('LOC_DOWNLOAD') + '"class="buttonDownload" id="buttonDownload" unselectable="on"/></span>'
                                + '<span title="' + LOC.get('LOC_UPLOAD') + '"class="buttonUpload" id="buttonUpload" unselectable="on"/></span>'
                                + '<span title="' + LOC.get('LOC_SEND') + '"class="buttonPhone" id="buttonPhone" unselectable="on"/></span>'
                                + '<span title="' + LOC.get('LOC_EDIT') + '" class="buttonEdit" id="buttonEdit" unselectable="on"/></span>'
                                + '<span title="' + LOC.get('LOC_DUPLICATE') + '"class="buttonDuplicate" id="buttonDuplicate" unselectable="on"/></span>'
                                + '<span title="' + LOC.get('LOC_DELETE') + '"class="buttonDelete" id="buttonDelete" unselectable="on"/></span>'
                                + '</td>'
                                + '</tr>' );
        $('#menu' + item.id + ' span').mousedown(function() { onButtonMouseDownHandler($(this));} );
        if ( item.resultCollection > 0 ) {
            $('#resultCollectionQuantityString' + item.id ).append( '<a href="#" id="Item'+ item.id + '">' + item.resultCollection + '</a>' );
        } else {
            $('#resultCollectionQuantityString' + item.id ).append( '-' );
        }

        if( item.available ) {
            $('#Survey'+item.id + ' td:first' ).addClass( 'markAvailable' );
        } else {
            $('#Survey'+item.id + ' td:first' ).addClass( 'markBuilding' );
        }

        $( '#Survey' + item.id ).mouseover( item.id, function(i) {onMouseOverHandler(i);} );
        $( '#Survey' + item.id ).mouseout( item.id, function(i) {onMouseOutHandler(i);} );
        $( '#Item' + item.id ).click( item.id, function(i) {
            ResultList.showResultList(i);
        } );
        $('#menu' + item.id +' #buttonDownload').click( item.surveyId, function(i){onDownloadSurveyClicked(i);} );
        $('#menu' + item.id +' #buttonUpload').click( item.surveyId, function(i){onUploadSurveyClicked(i);} );
        $('#menu' + item.id +' #buttonDelete').click( item.surveyId, function(i){onDeleteSurveyClicked(i);} );
        $('#menu' + item.id +' #buttonDuplicate').click( item.surveyId, function(i){onDuplicateSurveyClicked(i);} );
        $('#menu' + item.id +' #buttonPhone').click( item.surveyId, function(i){onSendSurveyClicked(i);} );
        $('#menu' + item.id +' #buttonEdit').click( item.id, function(i){onEditSurveyClicked(i);} );
    }

function onButtonMouseDownHandler(source)
{
    source.addClass('pushed');
    $(document).mouseup(function() {
        $('.pushed').removeClass('pushed');
        $('body').unbind('mouseup');
        return false; });
    return false;
}

    function onEditSurveyClicked(e) {
        Editor.createEditor(e);
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
        $('#menu' + e.data + " span" ).addClass("hover");
        $('#Survey'+ e.data).addClass("hoverRow");
    }

    function onMouseOutHandler(e){
        $('#menu' + e.data+ " span" ).removeClass("hover");
        $('#Survey'+ e.data).removeClass("hoverRow");
    }

    function onMouseOverNext(){
        this.bgColor='#dbe4f1';
        document.getElementById('next_button').src='images/icon_next_on.png';
    }

    function onMouseOutNext(){
        this.bgColor='#edf0f6';
        document.getElementById('next_button').src='images/icon_next_off.png';
    }

    function onMouseOverPrevious(){
        this.bgColor='#dbe4f1';
        document.getElementById('prev_button').src='images/icon_previous_on.png';
    }

    function onMouseOutPrevious(){
        this.bgColor='#edf0f6';
        document.getElementById('prev_button').src='images/icon_previous_off.png';
    }
}();