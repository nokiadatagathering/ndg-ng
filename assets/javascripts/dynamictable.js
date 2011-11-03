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
            checkIfDeletingLast: function() {checkIfDeletingLast();},
            showList2 : function(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams){showList2(columnIds, columnTexts, columnDbFields, remoteAction, contentHandler, ajaxParams);}
    };

    function prepareContentToolbar() {
        $('#contentToolbar').empty();
        $('#contentToolbar').unbind('click');
        if(elementEndIndex < totalItems){
            $('#contentToolbar').css('background-color', '#20264a');
            $('#contentToolbar').append('<span class="toolbarText">Click here for more items</span>');
            $('#contentToolbar').click(function() {
                $('#contentToolbar').unbind('click');
                scrollDownList();
            });
          } else {
              $('#contentToolbar').css('background-color', '#edf0f6');
          }
          $('#contentToolbar').animate({top: $('#dynamicListTable').position().top + $('#dynamicListTable').height()});
//
//        $('#contentToolbar').append( '<span class="buttonNext"  id="buttonNext"></span>'
//                                   + '<span class="buttonPrevious" id="buttonPrevious"></span>' +
//                                     '<span class="toolbarText" id="itemRangeLabel"></span>');
//        $('#buttonPrevious').click( function(event){onPreviousClicked(event);} );
//        $('#buttonNext').click(  function(event){onNextClicked(event);} );
    }

    function scrollDownList() {
        if(scrollReady)
        {
            $('#contentToolbar span').text("Loading...");
            scrollReady = true;
            var diff = totalItems - elementEndIndex;
            if( diff > 0 ){
                if(diff > 5){
                    elementEndIndex += 5;
                } else{
                    elementEndIndex += diff;
                }
            DynamicTable.refresh();
            }
        }
    }

    function showLoadingNewRows() {

    }

    function showList (_columnIds, _columnTexts, _columnDbFields, remoteAction, _contentHandler, _ajaxParams){
        elementStartIndex = 0;
        elementEndIndex = 10;
        totalItems = 0;
        scrollReady = false;
        columnIds= _columnIds;
        columnTexts = _columnTexts;
        contentHandler = _contentHandler;
        columnDbFields = _columnDbFields;
        lastSortByColumn = undefined;
        ajaxParams = _ajaxParams;

        recreateContainers();

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
            htmlContent += '<th scope="col" id="' + item + '" ';
            if(i == 0 && item != null) {
                htmlContent +='class = "sortHeader firstColumnHeader ' + item + 'Width"';
            } else if( item != null ) {
                htmlContent +='class = "sortHeader ' + item + 'Width"';
            }
            htmlContent += '><div>';
            if(LOC.get(columnTexts[i]) != null) {
                htmlContent += LOC.get(columnTexts[i]);
            }
            htmlContent +='<span class="sortIndicatorPlaceholder"/></div></th>';
        });
        htmlContent += '</tr></thead>'
                     + '<tbody id="dynamicListTable">'
                     + '</tbody>';
        $('#minimalist').append(htmlContent);
                           var i = 0;

        $('.sortHeader' ).click( function(event){toggleSortByColumn(event);} );

        contentUrl = 'listData/' + remoteAction;

        $('#searchTextField').unbind('keyup');
        $('#searchTextField').keyup( function(event){performSearch(event)});

        fillListData( lastSortByColumn, true );

        $(window).scroll(function(){
        if  ($(window).scrollTop() == $(document).height() - $(window).height()){
           scrollDownList();
        }
        });

    }

    function showList2 (_columnIds, _columnTexts, _columnDbFields, remoteAction, _contentHandler, _ajaxParams){
        elementStartIndex = 0;
        elementEndIndex = 10;
        totalItems = 0;
        scrollReady = false;
        columnIds= _columnIds;
        columnTexts = _columnTexts;
        contentHandler = _contentHandler;
        columnDbFields = _columnDbFields;
        lastSortByColumn = undefined;
        ajaxParams = _ajaxParams;

        $('#minimalist_layout2_2').empty();

        var htmlContent = '';
        htmlContent += '<thead>' + '<tr>';
        $.each(columnIds,function(i,item) {
            htmlContent += '<th scope="col" id="' + item + '" '
            if(i == 0 && item != null) {
                htmlContent +='class = "sortHeader firstColumnHeader ' + item + 'Width"';
            } else if(item != null) {
                htmlContent +='class = "sortHeader ' + item + 'Width"';
            }
            htmlContent += '><div>';
            if(LOC.get(columnTexts[i]) != null) {
                htmlContent += LOC.get(columnTexts[i]);
            }
            htmlContent +='<span class="sortIndicatorPlaceholder"/></div></th>'
        });

        htmlContent += '</thead>'
                     + '<tbody id="dynamicListTable">'
                     + '</tbody>';
        $('#minimalist_layout2_2').append(htmlContent);
                           var i = 0;

        $('.sortHeader' ).click( function(event){toggleSortByColumn(event);} );

        contentUrl = 'listData/' + remoteAction;

        $('#searchTextField').unbind('keyup');
        $('#searchTextField').keyup( function(event){performSearch(event)});

        fillListData( lastSortByColumn, true );

        $(window).scroll(function(){
        if  ($(window).scrollTop() == $(document).height() - $(window).height()){
           scrollDownList();
        }
        });

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
        if( document.getElementById( "leftColumn_layout2" ) != null ) {
            $("#leftColumn_layout2").remove();
            $("#middleColumn_layout2").remove();
            $("#rightColumn_layout2").remove();

            var layout = "";
            layout += "<div id='leftcolumn'>"
                    + "<span class='plusButton' id='plusButton' >"
                    + "<a href='#'><img id ='plusButtonImage' src='images/plus.png'></a>"
                    + "</span>"
                    + "<div id=filter>"
                    + "<span id='leftColumnContent'>"
                    + "</span>"
                    + "</div>"
                    + "</div><!--END of leftcolumn-->"
                    + "<div id='content'>"
                    + "<div id='contentMain'>"
                    + "<div id='datatable'>"
                    + "<table id='minimalist'></table>"
                    + "</div>"
                    + "<div id='contentToolbar' ></div>"
                    + "</div>"
                    + "</div>"
            $( '#container' ).append( layout );
        }
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
        $('#container').height('715px');
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
        fillListData( lastSortByColumn, columnSortAscending[columnIndex] );
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
        fillListData( lastSortByColumn, lastSortAscending );
    }

    function fillListData( orderByColumn, isAscending ) {

        lastSortByColumn = orderByColumn;
        var params = {'startIndex': elementStartIndex,
                      'endIndex' : elementEndIndex,
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
        totalItems = data.totalSize;
        if($('#container').height() < elementEndIndex * CONST.get('TABLE_ROW_HEIGHT_TOTAL') + 280){
            $('#container').height(elementEndIndex * CONST.get('TABLE_ROW_HEIGHT_TOTAL') + 280)
        }
        $('#dynamicListTable').empty();
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
