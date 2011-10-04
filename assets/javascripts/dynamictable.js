    /*
 * File encapsulates action related to Survey List view
 *
 **/


var DynamicTable = function() {
    var elementStartIndex = 0;
    var columnSortAscending = new Array();// todo as what this was for before
    var columnIds;
    var columnTexts;
    var columnDbFields;
    var lastSortByColumn;
    var lastSortAscending = true;
    var contentUrl;
    var contentHandler;
    var ajaxParams;
    var totalItems = 0;

    return {showList : function(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams){showList(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams);},
            refresh: function() {refresh();},
            checkIfDeletingLast: function() {checkIfDeletingLast();}
    };

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').append( '<span class="buttonNext"  id="buttonNext"></span>'
                                   + '<span class="buttonPrevious" id="buttonPrevious"></span>' +
                                     '<span class="toolbarText" id="itemRangeLabel"></span>');
        $('#buttonPrevious').click( function(event){onPreviousClicked(event);} );
        $('#buttonNext').click(  function(event){onNextClicked(event);} );
    }

    function showList (_columnIds, _columnTexts, _columnDbFields, remoteAction, _contentHandler, _ajaxParams){
        elementStartIndex = 0;
        totalItems = 0;
        columnIds= _columnIds;
        columnTexts = _columnTexts;
        contentHandler = _contentHandler;
        columnDbFields = _columnDbFields;
        lastSortByColumn = undefined;
        ajaxParams = _ajaxParams;

        recreateContainers();

        prepareContentToolbar();
        $('#minimalist').empty();
        $('#leftColumnContent' ).empty();
        $('#plusButton').unbind('mouseover');
        $('#userManagement').unbind('click');

        $('#searchComboBox').unbind('click');
        $('#searchComboText').empty();

        $('#searchTextField').val("");

        var htmlContent = '';
        htmlContent += '<thead>' + '<tr>';
        $.each(columnIds,function(i,item) {
        htmlContent += '<th scope="col">'
                    + '<a href="#"' ;
        if(item != null) {
            htmlContent +='class = "sortHeader" id="'
                        + item
                        + '"';
                    }
        htmlContent += '" >';
        if(LOC.get(columnTexts[i]) != null) {
            htmlContent += LOC.get(columnTexts[i]);
            }
        htmlContent +='</a></th>';
        });
        htmlContent +=  '<th scope="col"></th>'
                       + '</tr>'
                       + '</thead>'
                       + '<tbody id="dynamicListTable">'
                       + '</tbody>';
        $('#minimalist').append(htmlContent);
                           var i = 0;

        $('.sortHeader' ).click( function(event){toggleSortByColumn(event);} );

        contentUrl = 'listData/' + remoteAction;

        $('#searchTextField').unbind('keyup');
        $('#searchTextField').keyup( function(event){performSearch(event)});

        fillListData( lastSortByColumn, true );
    }

    function performSearch(event) {
        if ( event.which == 13 ) {
            event.preventDefault();
        }
        elementStartIndex = 0;
        DynamicTable.refresh();
    }

    function recreateContainers()
    {
        if( !$('#datatable').length ){
            $('#content').append( '<div id="contentMain">'
                                + '<div id="datatable">'
                                + '<table id="minimalist">'
                                + '</table>'
                                + '</div>'
                                + '</div>');
        }

        if( !$('#contentToolbar').length ){
            $('#content').append('<div id=contentToolbar></div>');
        }
    }

    function updateToolbar(startIndex, totalCount) {
        var endIndex = startIndex + CONST.get('TABLE_ROW_COUNT') < totalCount ?
                       startIndex + CONST.get('TABLE_ROW_COUNT') : totalCount ;
        totalItems = totalCount;
        $('#itemRangeLabel').empty();
        $('#itemRangeLabel').append( '<strong>' + (startIndex + 1) + '-' + endIndex + '</strong> <small> of </small>');
        $('#itemRangeLabel').append( '<strong>' + totalCount + '</strong>');
    }

    function toggleSortByColumn(event)
    {
        var columnId = event.currentTarget.id;
        var columnIndex = jQuery.inArray( columnId, columnIds );
        resetColumnTitle();
        if ( columnSortAscending[columnIndex] ) {
            $('#' + columnId).text( LOC.get(columnTexts[columnIndex])+ CONST.get('DESC') );
            lastSortAscending = columnSortAscending[columnIndex] = false;
        } else {
            $('#' + columnId).text( LOC.get(columnTexts[columnIndex]) + CONST.get('ASC') );
            lastSortAscending = columnSortAscending[columnIndex] = true;
        }
        lastSortByColumn = columnDbFields[columnIndex];
        fillListData( lastSortByColumn, columnSortAscending[columnIndex] );
    }

    function resetColumnTitle() {
        $.each(columnIds,function(i,item) {
            $('#' + item).text( LOC.get(columnTexts[i]) );
        });
    }

    function checkIfDeletingLast() {
        if( (totalItems - 1) % CONST.get('TABLE_ROW_COUNT') == 0 && elementStartIndex > 0)
            {
                elementStartIndex -= CONST.get('TABLE_ROW_COUNT');
            }
    }

    function refresh() {
        fillListData( lastSortByColumn, lastSortAscending );
    }

    function fillListData( orderByColumn, isAscending ) {

        lastSortByColumn = orderByColumn;
        var params = {'startIndex': elementStartIndex,
                                                   'isAscending': isAscending,
                                                    'orderBy': orderByColumn};
        jQuery.extend(params, ajaxParams);
        var searchText = $('#searchTextField').val();
        if( searchText != "") {
            var searchParams = {
                'searchField' : contentHandler.getSearchBy(),
                'searchText' : escape(searchText)
            };
            jQuery.extend(params, searchParams);
        }
        $.getJSON( contentUrl, params, function(data){refreshTable(data);} );
    }

    function refreshTable(data) {
        $('#dynamicListTable').empty();
        updateToolbar(data.startIndex, data.totalSize);
        $.each(data.items,function(i,item) {
                contentHandler.fillWithData(i,item);
            $('#menu' + i+ ' span').mousedown( function() {onButtonMouseDownHandler($(this));} );
            $( '#dynamicRow' + i ).mouseover( i, function(i) {onMouseOverHandler(i);} );
            $( '#dynamicRow' + i ).mouseout( i, function(i) {onMouseOutHandler(i);} );
        });
        if(contentHandler.loadingFinished)
            {
            contentHandler.loadingFinished();
            }
    }

    function onButtonMouseDownHandler(source) {
        source.addClass('pushed');
        $(document).bind('mouseup.menubutton',function() {
            $('.pushed').removeClass('pushed');
            $(document).unbind('mouseup.menubutton');
            return false; });
        return false;
    }


    function onPreviousClicked(e) {
        if ( elementStartIndex - CONST.get('TABLE_ROW_COUNT') >= 0 ) {
            elementStartIndex -= CONST.get('TABLE_ROW_COUNT')
            refresh();
        }
        e.preventDefault();
    }

    function onNextClicked(e) {
        if( elementStartIndex + CONST.get('TABLE_ROW_COUNT') < totalItems )
        {
            elementStartIndex += CONST.get('TABLE_ROW_COUNT');
            refresh();
        }
        e.preventDefault();
    }

    function onMouseOverHandler(e){
        $('#menu' + e.data + " span" ).addClass("hover");
        $('#dynamicRow'+ e.data).addClass("hoverRow");
    }

    function onMouseOutHandler(e){
        $('#menu' + e.data+ " span" ).removeClass("hover");
        $('#dynamicRow'+ e.data).removeClass("hoverRow");
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