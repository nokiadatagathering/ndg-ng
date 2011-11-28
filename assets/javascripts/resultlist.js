/*
 * File encapsulates action related to Result List view
 *
 **/

var ResultList = function() {

    var currentSurveyId;
    var allResultSelected = false;
    var selectedResults = new Array();
    var searchLabels;
    var searchIds;
    var searchDbFields = ["title", "latitude", "startTime", "ndgUser.username"];
    var searchBy = "title";

    return {showResultList : function(i) {showResultList(i);},
            selectAllResults : function(){selectAllResults();},
            selectAllVisibleResults : function() {selectAllVisibleResults();},
            unselectAllResults : function(){unselectAllResults();},
            fillWithData: function(i, item) {fillWithData(i, item);},
            loadingFinished: function() {loadingFinished();},
            searchFieldChange: function(event){searchFieldChange(event);},
            getSearchBy: function() {return searchBy;},
            prepareLayout: function(tableHtml){prepareLayout(tableHtml);},
            exportResults: function(){exportResults();}
    };

    function backToSurveyList() {
        SurveyList.showSurveyList();
    }

    function showResultList(i) {
        selectedResults = new Array();
        currentSurveyId = i.data;

        var columnIds = ["checkboxColumnId", "executeSortByResultTitle", "executeSortByDateSent", "executeSortByUser", "executeSortByLocation", null];
        var columnTexts = [null, "LOC_RESULTTITLE", "LOC_DATESENT", "LOC_USER", "LOC_LOCATION"];
        var columnDbFields = [null, "title", "startTime", "ndgUser.username", "latitude"];
        var ajaxParams = {surveyId: currentSurveyId};

        $('#container').height('715px');
        setupLeftColumn();

        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "results", ResultList, ajaxParams);

        var selectAllCheckbox = '<input type="checkbox" class="resultCheckboxClass" id="selectAllResults" "/>';
        $('#checkboxColumnId').append(selectAllCheckbox);
        $('#selectAllResults').bind( 'check uncheck', $(this), function(data){selectAllResultsClicked(data);} )

        $('#sectionTitle').text(LOC.get('LOC_RESULT_LIST'));
        $('#userManagement').text('');

        $('#searchComboBox').click( function(event) {createSearchList(event);});
        $('#searchComboText').text(LOC.get("LOC_RESULTTITLE"));

    }

    function prepareLayout(tableHtml) {
        $('#minimalist').empty();
        $('#searchComboBox').unbind('click');
        $('#minimalist').append(tableHtml);
        $('#minimalist').addClass('resultListTable');
        $('#content').addClass('resultListTable');
        $('#contentToolbar').addClass('resultListTable');
    }

    function setupLeftColumn() {
        $('#leftColumn').empty('resultListTable');
        $('#leftColumn').addClass('resultListTable');
        var columnContent = '<div class="resultListLeftMenu">';
        columnContent += '<span id ="backResults" class="buttonBack"></span>';
        columnContent += '<span id="mapView" class="buttonMapView resultListButton">';
        columnContent += LOC.get('LOC_MAP_VIEW');
        columnContent +=  '</span>';
        columnContent += '<div id="exportContextMenu" class="buttonExport resultListButton"><span>';
        columnContent += LOC.get('LOC_EXPORT_TO');
        columnContent +=  '</span></div>';
        columnContent +=  '</div>';

        $('#leftColumn').append( columnContent );
        $('#backResults').click( function(){backToSurveyList()} );
        $('#mapView').click( function(){ alert("Not supported"); } );
        $('#exportContextMenu').click(function(event){ContextComboBox.showExportResultsMenu(event);});
    }

    function createSearchList(event) {
       searchLabels = [LOC.get("LOC_RESULTTITLE"), LOC.get("LOC_LOCATION"), LOC.get("LOC_DATESENT"), LOC.get("LOC_USER")];
       searchIds = [ "searchByTitle", "searchByLocation", "searchByDate", "searchByUser"];
       ContextComboBox.showSearchMenu(event, searchLabels, searchIds, ResultList);
    }

    function fillWithData(i, item) {
        $('#dynamicListTable').append( '<tr class="itemTextColor" id="dynamicRow' + i + '">'
                                    + '<td class="resultCheckboxPaddingClass"><input type="checkbox" class="resultCheckboxClass" id="resultCheckbox' + item.id + '"/></td>'
                                    + '<td>' + item.title + '</td>'
                                    + '<td>' + new Date( item.startTime ).toString("dd/MM/yy") + '</td>'
                                    + '<td>' + item.ndgUser.username + '</td>'
                                    + '<td>' + ( item.latitude!= null ? 'OK': 'NO GPS' ) + '</td>'
                                    + '<td class="resultListTable menubar" id="menu' + i + '" >'
                                    + '<span title="' + LOC.get('LOC_DELETE') + '"class="buttonDelete" id="buttonDelete" unselectable="on"></span>'
                                    + '</td>'
                                    + '</tr>' );

        $( '#resultCheckbox' + item.id ).bind( 'check uncheck', item.id, function(i){resultCheckboxClicked(i);} )
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
    }

    function selectAllResultsClicked(data) {
        if(data.currentTarget.checked) {
            selectAllResults();
        } else {
            unselectAllResults();
        }
    }

    function exportResults() {
        if( allResultSelected ) {
            ExportResults.exportAllResults( currentSurveyId );
        } else if(selectedResults.length) {
            ExportResults.exportResults( currentSurveyId, selectedResults );
        } else {
            alert("no results selected");
        }
    }


    function onButtonMouseDownHandler(source) {
        source.addClass('pushed');
        $(document).bind('mouseup.resultBar', function() {
            $('.pushed').removeClass('pushed');
            $('body').unbind('mouseup.resultBar');
            return false;});
        return false;
    }

    function selectAllVisibleResults() {
        var checkboxes = $(".resultCheckboxClass");

        $.each( checkboxes ,function( i, item ) {
            item.checked = true;
        });
    }

    function selectAllVisibleResults() {
        doSelectAllVisibleResults();
        allResultSelected = false;
    }

    function doSelectAllVisibleResults() {
        var checkboxes = $(".resultCheckboxClass");

        $.each( checkboxes ,function( i, item ) {
            item.checked = true;
        });
    }

    function selectAllResults() {
        selectAllVisibleResults();
        allResultSelected = true;
    }

    function unselectAllResults() {
        var checkboxes = $(".resultCheckboxClass");

        $.each( checkboxes ,function( i, item ) {
            item.checked = false;
        });
        allResultSelected = false;
    }

    function loadingFinished() {
         $('input:checkbox:not([safari])').checkbox({cls:'customCheckbox', empty:'images/empty.png'});
         if( allResultSelected ) {
            doSelectAllVisibleResults();
         }
    }

    function searchFieldChange(event) {
        var fieldId = event.currentTarget.id;
        var nameIndex = jQuery.inArray( fieldId, searchIds );
        $('#searchComboText').text(searchLabels[nameIndex]);
        searchBy = searchDbFields[nameIndex];
        $('#searchTextField').val("");
    }

}();