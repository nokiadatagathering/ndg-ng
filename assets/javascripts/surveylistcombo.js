/*
 * File encapsulates action related to Survey List comboboxes
 *
 **/


var SurveyListCombo = function() {

    return {showMenu : function(){showMenu();},
            showSelectionMenu : function(event){showSelectionMenu(event);}
    };

    function showMenu(){
        document.onclick=closeMenu;
        if ( document.getElementById('popup-context').innerHTML.trim() != "" ) {
            return;
        }

        $('#popup-context').append( '<a id="newSurveyAction" href="#">' + LOC.get( 'LOC_NEW_SURVEY' ) + '</a>' );
        var pos = objectPosition(document.getElementById("plusButtonImage"));

        $('#newSurveyAction').click( function(){alert( "!!!" );});

        document.getElementById("popup-context").style.left = pos[0] + document.getElementById("plusButtonImage").clientWidth + "px";
        document.getElementById("popup-context").style.top = pos[1] + "px";
        document.getElementById("popup-context").style.visibility="visible";
    }

    function showSelectionMenu( event ){
        document.onclick=closeSelectionMenu;
        if ( document.getElementById('popup-selection-context').innerHTML.trim() != "" ) {
            return;
        }


        $('#popup-selection-context').append( '<a id="selectAllVisibleAction" href="#">' + LOC.get( 'LOC_ALL' ) + '</a>'
                                            + '<a id="selectAllPagesAction" href="#">' + LOC.get( 'LOC_ALL_PAGES' ) + '</a>'
                                            + '<a id="unselectAll" href="#">' + LOC.get( 'LOC_NONE' ) + '</a>' );
        var pos = objectPosition(document.getElementById("comboBoxSelection"));

        $('#selectAllVisibleAction').click( function(){ ResultList.selectAllVisibleResults(); } );
        $('#selectAllPagesAction').click( function(){ ResultList.selectAllResults(); });
        $('#unselectAll').click( function(){ResultList.unselectAllResults();});

        document.getElementById("popup-selection-context").style.left = pos[0] + "px" ;
        document.getElementById("popup-selection-context").style.top = pos[1] - document.getElementById("comboBoxSelection").clientHeight - 40 + "px";
        document.getElementById("popup-selection-context").style.visibility = "visible";

        if (!event) {
            var event = window.event;
        }
        event.cancelBubble = true;
    if (event.stopPropagation) {
            event.stopPropagation();
        }
    }

    function objectPosition( object ) {
        var curleft = 0;
        var curtop = 0;
        if ( object.offsetParent ) {
            do {
                curleft += object.offsetLeft;
                curtop += object.offsetTop;
            } while ( ( object = object.offsetParent ) );
        }
        return [curleft,curtop];
    }


    function closeMenu() {
        document.getElementById('popup-context').style.visibility="hidden";
        document.getElementById('popup-context').innerHTML = "";
    }

    function closeSelectionMenu() {
        document.getElementById('popup-selection-context').style.visibility="hidden";
        document.getElementById('popup-selection-context').innerHTML = "";
    }
}();