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
    var totalPages = 1;
    var contentUrl;
    var contentHandler;
    var ajaxParams;

    return {showList : function(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams){showList(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams);},
            refresh: function() {refresh();}
    };

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').append( '<span class="buttonNext"  id="buttonNext"></span>'
                                   + '<span class="buttonPrevious" id="buttonPrevious"></span>'
                                   + '<span id="pageIndexText"><small>0</small> <strong>of 0</strong></span>' );
    }

    function showList (_columnIds, _columnTexts, _columnDbFields, remoteAction, _contentHandler, _ajaxParams){
        columnIds= _columnIds;
        columnTexts = _columnTexts;
        contentHandler = _contentHandler;
        columnDbFields = _columnDbFields;
        lastSortByColumn = undefined;
        ajaxParams = _ajaxParams;

        prepareContentToolbar();
        $('#minimalist').empty();
        $('#leftColumnContent' ).empty();
        $('#plusButton').unbind('mouseover');
        $('#userManagement').unbind('click');
        var htmlContent;
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

        contentUrl = '/application/' + remoteAction;
        $.getJSON(contentUrl + 'count', function(data){updateTotalPages(data);});
        fillListData( lastSortByColumn, true );
    }

    function updateTotalPages(data){
        totalPages = data.itemsCount;
        updatePageNumber();
    }

    function updatePageNumber() {
        $("#pageIndexText").empty();
        $("#pageIndexText").append( '<small>' + (elementStartIndex + 1) + '</small>' + '<strong> of ' + totalPages + '</strong>' );
        $("#buttonPrevious").click( function(event){onPreviousClicked(event);} );
        $("#buttonNext").click(  function(event){onNextClicked(event);} );
    }

    function toggleSortByColumn(event)
    {
        var columnId = event.currentTarget.id;
        var columnIndex = jQuery.inArray( columnId, columnIds );
        resetColumnTitle();
        if ( columnSortAscending[columnIndex] ) {
            $('#' + columnId).text = LOC.get(columnTexts[columnIndex])+ CONST.get('DESC');
            lastSortAscending = columnSortAscending[columnIndex] = false;
        } else {
            $('#' + columnId).text = LOC.get(columnTexts[columnIndex]) + CONST.get('ASC');
            lastSortAscending = columnSortAscending[columnIndex] = true;
        }
        lastSortByColumn = columnDbFields[columnIndex];
        fillListData( lastSortByColumn, columnSortAscending[columnIndex] );
    }

    function resetColumnTitle() {
        $.each(columnIds,function(i,item) {
            $('#' + item).text = LOC.get(columnTexts[i]);
        });
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
        $.getJSON(contentUrl, params, function(data){refreshTable(data);} );
    }

    function refreshTable(data) {
        $('#dynamicListTable').empty();
        $.each(data.items,function(i,item) {
                contentHandler.fillWithData(i,item);
            $('#menu' + i+ ' span').mousedown(function() { onButtonMouseDownHandler($(this));} );
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
        if ( --elementStartIndex < 0 ) {
            elementStartIndex = 0;
        } else {
            refresh();
            updatePageNumber();
        }
        e.preventDefault();
    }

    function onNextClicked(e) {
        if( ++elementStartIndex >= totalPages ) {
            elementStartIndex--;
        } else {
            refresh();
            updatePageNumber();
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