    /*
 * File encapsulates action related to Survey List view
 *
 **/


var DynamicTable = function() {
    var elementStartIndex = 0;
    var elementEndIndex = 10;
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
    var scrollReady = false;

    return {showList : function(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams){showList(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams);},
            refresh: function() {refresh();},
            checkIfDeletingLast: function() {checkIfDeletingLast();}
      };

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').unbind('click');
        if(elementEndIndex < totalItems){
            $('#contentToolbar').removeClass('backgroundHide');
            $('#contentToolbar').append('<span class="toolbarText">Click here for more items</span>');
            $('#contentToolbar').click(function() {
                $('#contentToolbar').unbind('click');
                scrollDownList();
            });
          } else {
              $('#contentToolbar').addClass('backgroundHide');
          }
          $('#contentToolbar').animate({top: $('#dynamicListTable').position().top + $('#dynamicListTable').height()});
    }

    function scrollDownList() {
        if(scrollReady)
        {
            $('#contentToolbar span').text("Loading...");
            scrollReady = true;
            var diff = totalItems - elementEndIndex;
            if( diff > 0 ){
                elementStartIndex = elementEndIndex;
                if(diff > 5){
                    elementEndIndex += 5;
                } else{
                    elementEndIndex += diff;
                }
            loadMoreData();
            }
        }
    }

    function showLoadingNewRows() {

    }

    function showList (_columnIds, _columnTexts, _columnDbFields, remoteAction, _contentHandler, _ajaxParams){
        initVariables(_columnIds, _columnTexts, _columnDbFields, _contentHandler, _ajaxParams);

        contentHandler.prepareLayout(createTableHtml());

        addHandlers (remoteAction);

    }

    function initVariables(_columnIds, _columnTexts, _columnDbFields, _contentHandler, _ajaxParams){
        elementStartIndex = 0;
        elementEndIndex = 10;
        totalItems = 0;
        scrollReady = false;
        columnIds= _columnIds;
        columnTexts = _columnTexts;
        contentHandler = _contentHandler;
        columnDbFields = _columnDbFields;
        lastSortByColumn = undefined;
        lastSortAscending = true;
        ajaxParams = _ajaxParams;
    }

    function addHandlers(remoteAction) {
        $('.sortHeader' ).click( function(event){toggleSortByColumn(event);} );

        contentUrl = 'listData/' + remoteAction;

        $('#searchTextField').unbind('keyup');
        $('#searchTextField').keyup( function(event){performSearch(event)});

        fillListData();

        $(window).unbind('scroll');
        $(window).scroll(function(){
        if  ($(window).scrollTop() == $(document).height() - $(window).height()){
           scrollDownList();
        }
        });
    }

    function createTableHtml() {
    var htmlContent = '';
    htmlContent += '<thead>' + '<tr>';
    $.each(columnIds,function(i,item) {
        htmlContent += '<th scope="col" id="' + item + '" ';
        if(i == 0 && item != null) {
            htmlContent +='class = "columnHeaderNoWrap sortHeader firstColumnHeader ' + item + 'Width"';
        } else if( item != null ) {
            htmlContent +='class = "columnHeaderNoWrap sortHeader ' + item + 'Width"';
        }
        htmlContent += '><div>';
        if(LOC.get(columnTexts[i]) != null) {
            htmlContent += LOC.get(columnTexts[i]);
            htmlContent += '<span class="sortIndicatorPlaceholder"/>';
        }
        htmlContent +='</div></th>';
    });
    htmlContent += '</tr></thead>'
                 + '<tbody id="dynamicListTable">'
                 + '</tbody>';
    return htmlContent;
    }

    function performSearch(event) {
        if ( event.which == 13 ) {
            event.preventDefault();
        }
        elementStartIndex = 0;
        DynamicTable.refresh();
    }

    function toggleSortByColumn(event)
    {
        var columnId = event.currentTarget.id;
        var columnIndex = jQuery.inArray( columnId, columnIds );
        resetColumnTitle();
        if ( columnSortAscending[columnIndex] ) {
            $('#' + columnId + ' span' ).addClass('sortDescending');
            lastSortAscending = columnSortAscending[columnIndex] = false;
        } else {
            $('#' + columnId + ' span' ).addClass('sortAscending');
            lastSortAscending = columnSortAscending[columnIndex] = true;
        }
        lastSortByColumn = columnDbFields[columnIndex];
        elementStartIndex = 0;
        fillListData();
    }

    function resetColumnTitle() {
        $.each(columnIds,function(i,item) {
            $('#' + item + ' span').removeClass('sortAscending');
            $('#' + item + ' span').removeClass('sortDescending');
        });
    }

    function checkIfDeletingLast() {
        if( (totalItems - 1) % CONST.get('TABLE_ROW_COUNT') == 0 && elementStartIndex > 0)
            {
                elementStartIndex -= CONST.get('TABLE_ROW_COUNT');
            }
    }

    function refresh() {
        elementStartIndex = 0;
        fillListData( );
    }

    function loadMoreData() {
        var getJSONQuery = $.getJSON( contentUrl, prepareGetContentQuery(), function(data){renderTableData(data);});
        getJSONQuery.error(Utils.redirectIfUnauthorized) ;
    }

    function fillListData( ) {
        var getJSONQuery = $.getJSON( contentUrl, prepareGetContentQuery(), function(data){
            $('#dynamicListTable').empty();
            renderTableData(data);});
        getJSONQuery.error(Utils.redirectIfUnauthorized) ;
    }

    function prepareGetContentQuery() {
        var params = {'startIndex': elementStartIndex,
                  'endIndex' : elementEndIndex,
                  'isAscending': lastSortAscending,
                  'orderBy': lastSortByColumn};
        jQuery.extend(params, ajaxParams);
        var searchText = $('#searchTextField').val();
        if( searchText != "") {
        var searchParams = {
            'searchField' : contentHandler.getSearchBy(),
            'searchText' : escape(searchText)
        };
        jQuery.extend(params, searchParams);
        }
        return params;
    }

    function renderTableData(data) {
        totalItems = data.totalSize;
        if($('#container').height() < elementEndIndex * CONST.get('TABLE_ROW_HEIGHT_TOTAL') + 280){
            $('#container').height(elementEndIndex * CONST.get('TABLE_ROW_HEIGHT_TOTAL') + 280)
        }
        $.each(data.items,function(i,item) {
            var offset = elementStartIndex + i;
            contentHandler.fillWithData(offset,item);
            $('#menu' +  offset + ' span').mousedown( function() {onButtonMouseDownHandler($(this));} );
            $( '#dynamicRow' + offset ).mouseover( offset, function(offset) {onMouseOverHandler(offset);} );
            $( '#dynamicRow' + offset ).mouseout( offset, function(offset) {onMouseOutHandler(offset);} );
        });
        if(contentHandler.loadingFinished) {
            contentHandler.loadingFinished();
        }
        prepareContentToolbar();
        scrollReady = true;
    }

    function onButtonMouseDownHandler(source) {
        source.addClass('pushed');
        $(document).bind('mouseup.menubutton',function() {
            $('.pushed').removeClass('pushed');
            $(document).unbind('mouseup.menubutton');
            return false;});
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
        //$('#dynamicRow'+ e.data).addClass("hoverRow");
    }

    function onMouseOutHandler(e){
        $('#menu' + e.data+ " span" ).removeClass("hover");
        //$('#dynamicRow'+ e.data).removeClass("hoverRow");
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