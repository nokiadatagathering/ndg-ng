/*
 * File encapsulates action related to Survey List view
 *
 **/


var SurveyList = function() {

    var searchLabels;
    var searchIds;
    var searchDbFields = ["title", "surveyId", "ndgUser.username"];
    var searchBy = "title";
    var selectedFilterName = "";

    var columnIds = ["executeSortBySurveyName", "executeSortByDatePublished", "executeSortByPublisher", "executeSortByResults",null];//null is for item toolbar
    var columnTexts = ["LOC_SURVEY_NAME", "LOC_DATE_PUBLISHED", "LOC_PUBLISHER", "LOC_RESULTS"];
    var columnDbFields = ["title","uploadDate", "ndgUser.username", "resultCollection"];

    return {showSurveyList : function(){showSurveyList();},
            fillWithData : function(i, item){fillWithData(i, item);},
            searchFieldChange: function(event){searchFieldChange(event);},
            getSearchBy: function() {return searchBy;},
            prepareLayout: function(tableHtml){prepareLayout(tableHtml);},
            additionalAjaxParams: function() {return additionalAjaxParams();}
    };


      function Validate(oForm) {    
          var ck_name = /^[A-Za-z0-9._]{5,30}$/;
          var sFileName = $('#uploadSurveyInput').val()
          var toTest = sFileName.substr(12, sFileName.length);
          if (!ck_name.test(toTest)) {
                           alert("Sorry, that name is invalid. Please try again");
                           window.location.href = '/';
                                     }
          return true
     }



    function showSurveyList (){

        recreateContainers();

        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "surveys", SurveyList);

        $('#plusButton').bind( 'click', function(event) {Editor.createSurvey();} );
        $('#plusButton').attr( 'title', LOC.get( 'LOC_NEW_SURVEY' ) );
        $('#leftColumnContent' ).append( '<h3>' + LOC.get( 'LOC_STATUS' ) + '</h3>'
                                       + '<h4 id="filterBuilding" class="labelBuilding filterable clickableElem">' + LOC.get( 'LOC_BUILDING' ) + '</h4>'
                                       + '<h4 id="filterAvailable" class="labelAvailable filterable clickableElem">' + LOC.get( 'LOC_AVAILABLE' ) + '</h4>'
                                       + '<h4 id="filterAll" class="labelClosed clickableElem">' + LOC.get( 'LOC_ALL' ) + '</h4>' );

        bindFilter();

        $('#pageSelect').empty();
        $('#pageSelect').append('<H2 id=sectionTitle></H2>');
        if(hasAdminPermission) {
            $('#pageSelect').append('<H3 id=userManagement class="clickableElem"></H3>');
            $('#userManagement').click(function() {UserManagement.showUserManagement()});
        }

        $('#uploadForm').unbind('submit');
        $('#uploadForm').submit( function () {
                          uploadNewSurvey();
                                            });

        $('#buttonSendFile').unbind( 'click' );
        $('#buttonSendFile').click( function() {
            sanitized = Validate($('#uploadForm'));
            if (true){
                $('#uploadForm').submit( );
                     }                      
        });

        $('#searchComboText').text(LOC.get("LOC_SURVEY_NAME"));
        $('#searchComboBox').click( function(event) {createSearchList(event);});

        $( '#sectionTitle' ).text( LOC.get('LOC_SURVEY_LIST') );
        $( '#userManagement' ).text( LOC.get('LOC_USER_ADMIN') );

        if( selectedFilterName != null && selectedFilterName.length > 0 ) {
            $.each( $('.filterable'), function( i, item ){selectFilter( i, item );});
        }

        $('#leftColumn' ).click( function(event) {clearFilter(event);});
    }

    function clearFilter( event ) {
        $.each( $('.filterable'), function( i, item ){removeFilterSelection( i, item );} );
        if ( selectedFilterName !="" ) {
            selectedFilterName="";
            DynamicTable.refresh();
        }
    }

    function bindFilter() {
        $('.filterable').click( function(event) {onFilterClicked( event );} );
    }

    function selectFilter(i, item) {
        if ( selectedFilterName == item.id ) {
            $( "#" + item.id ).addClass( "selectedFilter" );
        }
    }

    function onFilterClicked(event) {
        var clickedElem = event.currentTarget.id;
        if( $( "#" + clickedElem ).hasClass( "selectedFilter" ) ) {

            $( "#" + clickedElem ).removeClass( "selectedFilter" );
            selectedFilterName = "";
        } else {
            $.each( $('.filterable'), function(i,item) {removeFilterSelection(i, item);} );

            $( "#" + clickedElem ).addClass( "selectedFilter" );
            selectedFilterName = clickedElem;
        }
        DynamicTable.refresh();
        event.stopPropagation();
    }

    function additionalAjaxParams() {
        return {'filter': selectedFilterName};
    }

    function removeFilterSelection(i, item ) {
        if( item.classList != null ) {
            if( item.classList.contains("selectedFilter") ) {
                item.classList.remove("selectedFilter");
                                                            }
                                     }
                                             }

    function filterSurveys() {
        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "surveys", SurveyList);
        DynamicTable.refresh();
    }

    function prepareLayout(tableHtml) {
        $('#minimalist').empty();
        $('#leftColumnContent' ).empty();
        $('#plusButton').unbind('mouseover');
        $('#userManagement').unbind('click');

        $('#searchComboBox').unbind('click');
        $('#searchComboText').empty();

        $('#searchTextField').val("");

       $('#minimalist').append(tableHtml);
    }

    function recreateContainers() {
        if( $("#leftColumn_layout2" ).length ) {
            $("#leftColumn_layout2").remove();
            $("#middleColumn_layout2").remove();
            $("#rightColumn_layout2").remove();
        }
        $( '#leftColumn' ).remove();
        $( '#content' ).remove();
        var layout = "";
        layout += "<div id='leftColumn' title='" + LOC.get('LOC_CLICK_TO_DISABLE_FILTER') + "'>"
                + "<div class='plusButton clickableElem' id='plusButton' >"
                + "<div id ='plusButtonImage'></div>"
                + "</div>"
                + "<div id=filter>"
                + "<span id='leftColumnContent'>"
                + "</span>"
                + "</div>"
                + "</div><!--END of leftColumn-->"
                + "<div id='content'>"
                + "<div id='contentMain'>"
                + "<div id='datatable'>"
                + "<table id='minimalist'></table>"
                + "</div>"
                + "<div id='contentToolbar' ></div>"
                + "</div>"
                + "</div>"
        $( '#container' ).append( layout );

        $('.resultListTable').removeClass('resultListTable');
        $('.userManagement').removeClass('userManagement');

        if( !$('#datatable').length ) {
            $('#content').append( '<div id="contentMain">'
                                + '<div id="datatable">'
                                + '<table id="minimalist">'
                                + '</table>'
                                + '</div>'
                                + '</div>');
        }

        if( !$('#contentToolbar').length ) {
            $('#content').append('<div id=contentToolbar></div>');
        }
        $('#container').height('715px');
    }

    function createSearchList(event) {
       searchLabels = [LOC.get("LOC_SURVEY_NAME"), "ID", LOC.get("LOC_PUBLISHER")];
       searchIds = ["searchBySurveyName", "searchById", "searchByPublisher"];
       ContextComboBox.showSearchMenu(event, searchLabels, searchIds, SurveyList);
    }

    function fillWithData(i, item) {
        var date = new Date( item.uploadDate )
        $('#dynamicListTable').append( '<tr id="dynamicRow'+ i + '">'
                                    + '<td id="surveyNameCell"><pre class="surveyNameText" >'+ item.title + '</pre><pre class="surveyIdText">ID: ' + item.surveyId + '</pre></td>'
                                + '<td>' + date.toString("dd/MM/yy") + '</td>'
                                + '<td>' + item.ndgUser.username + '</td>'
                                + '<td class="resultCollectionQuantity clickableElem" id="resultCollectionQuantityString' + item.id + '"></td>'
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
            $('#resultCollectionQuantityString' + item.id ).text( item.resultCollection );
            $('#resultCollectionQuantityString' + item.id ).click( item.id, function(i) {
                ResultList.showResultList(i);
            } );
            $('#resultCollectionQuantityString' + item.id ).addClass('underLine');
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

        $('#menu' + i +' #buttonDownload').click( item.surveyId, function(i){onDownloadSurveyClicked(i);} );
        $('#menu' + i +' #buttonUpload').click( item, function(i){onUploadSurveyClicked(i);} );
        $('#menu' + i +' #buttonDelete').click( item.surveyId, function(i){onDeleteSurveyClicked(i);} );
        $('#menu' + i +' #buttonDuplicate').click( item.surveyId, function(i){onDuplicateSurveyClicked(i);} );
        $('#menu' + i +' #buttonPhone').click( item.surveyId, function(i){onSendSurveyClicked(i);} );
        $('#menu' + i +' #buttonEdit').click( item, function(i){onEditSurveyClicked(i);} );
    }

    function onEditSurveyClicked(e) {

        if( e.data.available == SurveyAvailable.BUILDING ) {
            $(window).unbind('scroll');
            Editor.openSurvey(e.data.id);
        } else {
            alert( LOC.get( 'LOC_CANNOT_EDIT_SURVEY' ) );
        }
    }

    function onDownloadSurveyClicked(e) {
        window.location.href = 'surveyManager/get/' + e.data;
    }

    function onUploadSurveyClicked(e) {
        if( e.data.available != SurveyAvailable.BUILDING ){
            alert( LOC.get( 'LOC_CANNOT_EDIT_SURVEY' ) );
            return;
        }

       $('#uploadSurveyFormButton').text(LOC.get('LOC_SEARCH'));
       $('#uploadSurveyInput').unbind('change');
       $('#uploadSurveyInput').change( function() {
           $('#uploadSurveyFakeInput').text($(this).val())
       } );

       uploadDialog.dialog( {title: LOC.get('LOC_SURVEY_UPLOAD')});
       $('#dialog-upload-query').text(LOC.get('LOC_CHOOSE_SURVEY_UPLOAD'));
       $('#buttonSendFile').text(LOC.get('LOC_SEND_FILE'));

       uploadDialog.dialog({close: function(){$.unblockUI();}} )
       uploadDialog.dialog("open");

       $("#uploadSurveyId").val(e.data.surveyId);
       $.blockUI( {message: null} );
    }

    function onDeleteSurveyClicked(e) {
        confirmDeleteDialog.dialog( {title: LOC.get('LOC_DELETE')} );
        $('#buttonDeleteYes').text( LOC.get('LOC_YES') );
        $('#buttonDeleteNo').text( LOC.get('LOC_NO') );
        $('#dialog-confirmDelete-query').text( LOC.get('LOC_SURVEY_DELETE_CONFIRM') );
        confirmDeleteDialog.dialog("open");
        $("#buttonDeleteYes").click( e.data, function(e) {

        $.ajax( {url: 'surveyManager/delete',
                  data: {formID: e.data},
                  error: function(data, textStatus, jqXHR) {
                      if (!Utils.redirectIfUnauthorized(data, textStatus, jqXHR)) {
                          alert("error");
                      }
                  },
                  success: function(data, textStatus, jqXHR) {
                      confirmDeleteDialog.dialog("close");
                      DynamicTable.checkIfDeletingLast();
                      DynamicTable.refresh();
                  }
                } );

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
        $('#dynamicListTable').empty();
        $.ajax( {url: 'surveyManager/duplicate',
                  data: {formID: e.data},
                  error: function(data, textStatus, jqXHR) {
                      if (!Utils.redirectIfUnauthorized(data, textStatus, jqXHR)) {
                          alert("error");
                      }
                      },
                  success: function(data, textStatus, jqXHR) {
                      DynamicTable.refresh();
                  }
                } );
    }

    function onSendSurveyClicked(e) {
        SendSurvey.showUserList(e);
        sendSurveyDialog.dialog( {title: LOC.get('LOC_SEND_SURVEY')} );
        $('#buttonSendSurveyDone').text( LOC.get('LOC_DONE') );
        sendSurveyDialog.dialog({close: function(){$.unblockUI();}} )
        sendSurveyDialog.dialog("open");
        $.blockUI( {message: null} );
    }

    function uploadNewSurvey() {
        var resultFrame = $('#uploadSurveyResult').load(function ()
        {
           var response = resultFrame.contents().find('body').find('pre').html();
            if(response === "success") {
                DynamicTable.refresh();
                alert("success");
            } else {
                alert("failure")
            }

           resultFrame.unbind('load');
           uploadDialog.dialog("close");
           $(':file', '#uploadForm' ).val('');
           $('#uploadSurveyFakeInput').text('');
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
