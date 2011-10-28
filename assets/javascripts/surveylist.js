/*
 * File encapsulates action related to Survey List view
 *
 **/


var SurveyList = function() {

    var searchLabels;
    var searchIds;
    var searchDbFields = ["surveyId", "title", "ndgUser.username"];
    var searchBy = "surveyId";

    return {showSurveyList : function(){showSurveyList();},
            fillWithData : function(i, item){fillWithData(i, item);},
            searchFieldChange: function(event){searchFieldChange(event);},
            getSearchBy: function() {return searchBy;}
    };

    function showSurveyList (){

        var columnIds = ["executeSortBySurveyName", "executeSortByDatePublished", "executeSortByPublisher", "executeSortByResults",null];//null is for item toolbar
        var columnTexts = ["LOC_SURVEY_NAME", "LOC_DATE_PUBLISHED", "LOC_PUBLISHER", "LOC_RESULTS"];
        var columnDbFields = ["title","uploadDate", "ndgUser.username", "resultCollection"];

        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "surveys", SurveyList);

        $('#plusButton').bind('mouseover.surveyList', function(event) {SurveyListCombo.showSurveyMenu();});
        $('#leftColumnContent' ).append( '<h3>STATUS</h3><h4 class="labelBuilding">Building</h4><h4 class="labelAvailable">Available</h4><h4 class="labelClosed">Closed</h4>');


        $('#pageSelect').empty();
        $('#pageSelect').append('<H2 id=sectionTitle>Survey List</H2><H3 id=userManagement>User Admin</H3>');
        $('#userManagement').click(function() {UserManagement.showUserManagement() });

        $('#uploadForm').submit( function () { uploadNewSurvey(); } );
        $('#buttonSendFile').click( function() {  $('#uploadForm').submit( ) });

        $('#searchComboText').text("ID");
        $('#searchComboBox').click( function(event) {createSearchList(event);});
    }

    function createSearchList(event) {
       searchLabels = ["ID", LOC.get("LOC_SURVEY_NAME"), LOC.get("LOC_PUBLISHER")];
       searchIds = ["searchById", "searchBySurveyName", "searchByPublisher"];
       SurveyListCombo.showSearchMenu(event, searchLabels, searchIds, SurveyList);
    }

    function fillWithData(i, item) {
        var date = new Date( item.uploadDate )
        $('#dynamicListTable').append( '<tr id="dynamicRow'+ i + '">'
                                    + '<td id="surveyNameCell"><pre class="surveyNameText" >'+ item.title + '</pre><pre class="surveyIdText">ID: ' + item.surveyId + '</pre></td>'
                                + '<td>' + date.toString("dd/MM/yy") + '</td>'
                                + '<td>' + item.ndgUser.username + '</td>'
                                + '<td class="resultCollectionQuantity" id="resultCollectionQuantityString' + item.id + '"></td>'
                                + '<td class="menubar" id="menu' + i + '" >'
                                + '<span title="' + LOC.get('LOC_DOWNLOAD') + '"class="buttonDownload" id="buttonDownload" unselectable="on"></span>'
                                + '<span title="' + LOC.get('LOC_UPLOAD') + '"class="buttonUpload" id="buttonUpload" unselectable="on"></span>'
                                + '<span title="' + LOC.get('LOC_SEND') + '"class="buttonPhone" id="buttonPhone" unselectable="on"></span>'
                                + '<span title="' + LOC.get('LOC_EDIT') + '" class="buttonEdit" id="buttonEdit" unselectable="on"></span>'
                                + '<span title="' + LOC.get('LOC_DUPLICATE') + '"class="buttonDuplicate" id="buttonDuplicate" unselectable="on"></span>'
                                + '<span title="' + LOC.get('LOC_DELETE') + '"class="buttonDelete" id="buttonDelete" unselectable="on"></span>'
                                + '</td>'
                                + '</tr>' );

        if ( item.resultCollection > 0 ) {
            $('#resultCollectionQuantityString' + item.id ).text(  item.resultCollection );
        } else {
            $('#resultCollectionQuantityString' + item.id ).text( '-' );
        }

        if( item.available ) {
            $('#dynamicRow'+ i + ' td:first' ).addClass( 'markAvailable' );
            $('#dynamicRow'+ i + ' td' ).addClass( 'markAvailableText' );
        } else {
            $('#dynamicRow'+ i + ' td:first' ).addClass( 'markBuilding' );
            $('#dynamicRow'+ i + ' td' ).addClass( 'markBuildingText' );
        }

        $('#resultCollectionQuantityString' + item.id ).click( item.id, function(i) {
            ResultList.showResultList(i);
        } );
        $('#menu' + i +' #buttonDownload').click( item.surveyId, function(i){onDownloadSurveyClicked(i);} );
        $('#menu' + i +' #buttonUpload').click( item.surveyId, function(i){onUploadSurveyClicked(i);} );
        $('#menu' + i +' #buttonDelete').click( item.surveyId, function(i){onDeleteSurveyClicked(i);} );
        $('#menu' + i +' #buttonDuplicate').click( item.surveyId, function(i){onDuplicateSurveyClicked(i);} );
        $('#menu' + i +' #buttonPhone').click( item.surveyId, function(i){onSendSurveyClicked(i);} );
        $('#menu' + i +' #buttonEdit').click( item.id, function(i){onEditSurveyClicked(i);} );
    }

    function onEditSurveyClicked(e) {
        $('#plusButton').unbind('mouseover.surveyList');
        Editor.openSurvey(e.data);
    }

    function onDownloadSurveyClicked(e) {
        window.location.href = 'management/' + e.data;
    }

    function onUploadSurveyClicked(e) {
       $('#uploadSurveyForm').fileinput( { buttonText: LOC.get('LOC_SEARCH'),
                                           inputText:""});
       uploadDialog.dialog( {title: LOC.get('LOC_SURVEY_UPLOAD')});
       document.getElementById('dialog-upload-query').textContent = LOC.get('LOC_CHOOSE_SURVEY_UPLOAD');
       document.getElementById('buttonSendFile').textContent = LOC.get('LOC_SEND_FILE');

       uploadDialog.dialog({close: function(){$.unblockUI();}} )
       uploadDialog.dialog("open");

       $("#uploadSurveyId").val(e.data);
       $.blockUI( {message: null} );
    }

    function onDeleteSurveyClicked(e) {
        confirmDeleteDialog.dialog( {title: LOC.get('LOC_EXPORT_RESULTS')} );
        document.getElementById('buttonDeleteYes').textContent = LOC.get('LOC_YES');
        document.getElementById('buttonDeleteNo').textContent = LOC.get('LOC_NO');
        document.getElementById('dialog-confirmDelete-query').textContent = LOC.get('LOC_SURVEY_DELETE_CONFIRM');
        confirmDeleteDialog.dialog("open");
        $("#buttonDeleteYes").click( e.data, function(e) {
            $.post( "/delete/" + e.data, function(data) {
                confirmDeleteDialog.dialog("close");
                DynamicTable.checkIfDeletingLast();
                DynamicTable.refresh();
            });
            $("#buttonDeleteYes").unbind("click");
            $("#buttonDeleteNo").unbind("click");
        });
       $("#buttonDeleteNo").click( function() {
           $("#buttonDeleteYes").unbind("click");
           $("#buttonDeleteNo").unbind("click");
           confirmDeleteDialog.dialog("close");
       });
    }

    function onDuplicateSurveyClicked(e) {
       $.post( "/duplicate/" + e.data, function(data) {
            DynamicTable.refresh();
        });
    }

    function onSendSurveyClicked(e) {
        SendSurvey.showUserList(e);
        sendSurveyDialog.dialog( {title: LOC.get('LOC_SEND_SURVEY')} );
        document.getElementById('buttonSendSurveyDone').textContent = LOC.get('LOC_DONE');
        sendSurveyDialog.dialog({close: function(){$.unblockUI();}} )
        sendSurveyDialog.dialog("open");
        $.blockUI( {message: null} );
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

    function searchFieldChange(event) {
        var fieldId = event.currentTarget.id;
        var nameIndex = jQuery.inArray( fieldId, searchIds );
        $('#searchComboText').text(searchLabels[nameIndex]);
        searchBy = searchDbFields[nameIndex];
        $('#searchTextField').val("");
    }

}();
