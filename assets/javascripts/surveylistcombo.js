/*
 * File encapsulates action related to Survey List comboboxes
 *
 **/


var SurveyListCombo = function() {

    return {showMenu : function(){showMenu();}
    };

    function showMenu(){
        document.onclick=closeMenu;
        if ( document.getElementById('popup-context').innerHTML.trim() != "" ) {
            return;
        }

        $('#popup-context').append( '<a id="newSurveyAction" href="#">' + LOC.get( 'LOC_NEW_SURVEY' ) + '</a>' );
        var pos = objectPosition(document.getElementById("plusButtonImage"));

        $('#newSurveyAction').click( function(){ alert( "!!!" );});

        document.getElementById("popup-context").style.left = pos[0] + document.getElementById("plusButtonImage").clientWidth + "px";
        document.getElementById("popup-context").style.top = pos[1] + "px";
        document.getElementById("popup-context").style.visibility="visible";
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
}();