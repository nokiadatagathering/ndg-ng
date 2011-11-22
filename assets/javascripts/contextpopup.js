

var ContextComboBox = function() {

    return {showSurveyMenu : function(){showSurveyMenu();},
            showResultSelectionMenu : function(event){showResultSelectionMenu(event);},
            showEditorMenu : function(event){showEditorMenu(event);},
            showSearchMenu : function(event, searchLabels, searchIds, contentHandler){showSearchMenu(event, searchLabels, searchIds, contentHandler)},
            showUserManagementMenu: function(event){showUserManagementMenu(event)},
            showExportResultsMenu: function(event){showExportResultsMenu(event)}
    };

    function showSurveyMenu(){
        document.onclick=closeMenu;
        if(menuBeingShown()) {
            return;
        }

        $('#popup-context').append( '<a id="newSurveyAction" href="#">' + LOC.get( 'LOC_NEW_SURVEY' ) + '</a>' );

        $('#popup-context').addClass("popup-context");

        $('#newSurveyAction').click( function(){Editor.createSurvey();});
        var pos = $('#plusButtonImage').position();
        showMenu(pos.left, pos.top, $('#plusButtonImage').width() + 5, 3);
    }

    function showUserManagementMenu(event){
        document.onclick=closeMenu;

        if(menuBeingShown()) {
            return;
        }

        $('#popup-context').append( '<a id="newUserAction" href="#">' + LOC.get( 'LOC_NEW_USER' ) + '</a>'
                                  + '<a id="newGroupAction" href="#">' + LOC.get( 'LOC_NEW_GROUP' ) + '</a>' );

        $('#popup-context').addClass("popup-context");

        var currentItem = $('#minimalist_layout2_2');
        var rect = new Object();
        rect.top = currentItem.position().top;
        rect.left = currentItem.position().left + 14;//14 margin
        rect.width = currentItem.width();
        $('#newUserAction').click( function(){
            if(!NewUserTable.isShown()) {
                NewUserTable.show(rect);
            }
        });


        var groupItem = $('#minimalist_layout2');
        var rectGroup = new Object();
        rectGroup.top = groupItem.position().top;
        rectGroup.left = groupItem.position().left;
        rectGroup.width = groupItem.width();
        rectGroup.height = 49;
        $('#newGroupAction').click( function(){
            if(!NewGroupTable.isShown()) {
                NewGroupTable.show(rectGroup);
            }
        });

        var pos = $('#plusButtonImage').position();
        showMenu(pos.left, pos.top, $('#plusButtonImage').width() + 5, 3);
        stopEvent(event);
    }

    function showEditorMenu(event){
        document.onclick=closeMenu;
        if(menuBeingShown()) {
            return;
        }

        $('#popup-context').append( '<div id="newCategoryAction"><span>' + 'New Category' + '</span></div>' ); //TODO localize
        $('#popup-context').append( '<div id="newQuestionAction"><span>' + 'New Question' + '</span></div>' );

        $('#popup-context').addClass("popup-context");

        $('#newCategoryAction').click( function(){Editor.addCategory();} );
        $('#newQuestionAction').click( function(){Editor.addQuestion();});

        var pos = $('#plusButtonImage').position();
        showMenu(pos.left, pos.top, $('#plusButtonImage').width() + 5, 3) ;

        $( "#newCategoryAction" ).addClass( 'drag' ); //TODO refactor - move to editor.js
        $( "#newCategoryAction" ).draggable({
            connectToSortable: "#categories",
            helper: function(event) {
                var dragging = $('<div><img id ="plusButtonImage" src="images/plus.png"></div>')
                return dragging;
            },
            revert: 'invalid',
            start: Editor.setCatListConfig,
            stop: Editor.removeCatListConfig
        });

        $( "#newQuestionAction" ).addClass( 'drag' ); //TODO refactor - move to editor.js
        $( "#newQuestionAction" ).draggable({
            connectToSortable: " .listQuestion",
            helper: function(event) {
                var dragging = $('<div><img id ="plusButtonImage" src="images/plus.png"></div>')
                return dragging;
            },
            revert: 'invalid',
            start: Editor.setQlistConfig,
            stop: Editor.removeQlistConfig
        });
    }

    function showSearchMenu(event, searchLabels, searchIds, contentHandler) {
        document.onclick=closeMenu;
        if ( menuBeingShown() ) {
            return;
        }

        $('#popup-context').addClass("popup-search-context");
        $.each(searchIds,function(i,item) {
            $('#popup-context').append( '<a id="' + item + '" class="searchBy" href="#">' + searchLabels[i] + '</a>' );
        } );

        $('.searchBy').click( function(event){
            contentHandler.searchFieldChange(event);
        });

        var pos = $('#searchComboBox').offset();
        showMenu(pos.left, pos.top, 1, $('#searchComboBox').height());
        stopEvent(event);
    }

     function showResultSelectionMenu( event ){
        document.onclick=closeMenu;
        if ( menuBeingShown() ) {
            return;
        }

        $('#popup-context').addClass("popup-selection-context");

        $('#popup-context').append( '<a id="selectAllVisibleAction" href="#">' + LOC.get( 'LOC_ALL' ) + '</a>'
                                  + '<a id="selectAllPagesAction" href="#">' + LOC.get( 'LOC_ALL_PAGES' ) + '</a>'
                                  + '<a id="unselectAll" href="#">' + LOC.get( 'LOC_NONE' ) + '</a>' );

        $('#selectAllVisibleAction').click( function(){ResultList.selectAllVisibleResults();} );
        $('#selectAllPagesAction').click( function(){ResultList.selectAllResults();});
        $('#unselectAll').click( function(){ResultList.unselectAllResults();});

        var pos = $('#comboBoxSelection').position();

        showMenu(pos.left, pos.top, 0, - $('#comboBoxSelection').height() - 40);

        stopEvent(event);
    }

    function showExportResultsMenu(event) {
        document.onclick=closeMenu;
        if ( menuBeingShown() ) {
            return;
        }

        $('#popup-context').addClass("popup-export-results");

        $('#popup-context').append( '<div id="exportExcel"><span>' + 'Excel' + '</span></div>');
        $('#popup-context').append( '<div id="exportKml"><span>' + 'KML' + '</span></div>');
        $('#popup-context').append( '<div id="exportExternal"><span>' + LOC.get('LOC_EXTERNAL_SERVICE') + '</span></div>');

        var pos = $('#exportContextMenu').offset();

        $('#exportExcel').click(ResultList.exportResults);
        $('#exportKml').click(function(){alert('not supported')});
        $('#exportExternal').click(function(){alert('not supported')});

        showMenu(pos.left, pos.top, 0, $('#exportContextMenu').height() );
        stopEvent(event);
    }

    function menuBeingShown() {
        return ( $.trim($('#popup-context').html()) != "" );
    }

    function showMenu(left, top, offsetX, offsetY){
        $('#popup-context').css("left", left + offsetX + "px" );
        $('#popup-context').css("top", top + offsetY +  "px");
        $('#popup-context').show();
    }

    function stopEvent(event) {
        if (!event) {
            event = window.event;
        }
        event.cancelBubble = true;
        if (event.stopPropagation) {
            event.stopPropagation();
        }
    }

    function closeMenu() {
        $('#popup-context').hide();
        $('#popup-context').removeClass();
        $('#popup-context').addClass("popup-base");
        $('#popup-context').html("");
    }

}();