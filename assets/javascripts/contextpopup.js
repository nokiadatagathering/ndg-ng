/*
 * File encapsulates action related to Survey List comboboxes
 *
 **/


var SurveyListCombo = function() {

    return {showSurveyMenu : function(){showSurveyMenu();},
            showResultSelectionMenu : function(event){showResultSelectionMenu(event);},
            showEditorMenu : function(event){showEditorMenu(event);},
            showSearchMenu : function(event, searchLabels, searchIds, contentHandler){showSearchMenu(event, searchLabels, searchIds, contentHandler)}
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
        showMenu(pos.left, pos.top, $('#plusButtonImage').width(), 0);
    }

    function showEditorMenu(event){
        document.onclick=closeMenu;
        if(menuBeingShown()) {
            return;
        }

        $('#popup-context').append( '<a id="newCategoryAction" href="#">' + 'New Category' + '</a>' );
        $('#popup-context').append( '<a id="newQuestionAction" href="#">' + 'New Question' + '</a>' );

        $('#popup-context').addClass("popup-context");

        $('#newCategoryAction').click( function(){Editor.addCategory();} );
        $('#newQuestionAction').click( function(){Editor.addQuestion();});

        var pos = $('#plusButtonImage').position();
        showMenu(pos.left, pos.top, $('#plusButtonImage').width(), 0) ;
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
        showMenu(pos.left, pos.top, 0, $('#searchComboBox').height());
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