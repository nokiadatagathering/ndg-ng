var EditedLabel = function( elem, callbackFun ){
    var element = elem;
    var onLabelChanged = callbackFun;
    var editBoxId;
    var editingStart = false;

    element.bind( 'click' , function( event ){onClicked( event );});

    function onClicked( event ){
        editBoxId = element.attr('id') + 'edit';

        var width = element.width();
        width+=50;

        element.replaceWith( '<input style="min-width:100px;max-width:450px; width:' + width +'px;" id="' + editBoxId + '" type="text" />' );
        $( "#" + editBoxId ).val( element.text() );
        textfield = document.getElementById( editBoxId );
        if (textfield.setSelectionRange) {
            textfield.setSelectionRange(textfield.value.length, textfield.value.length);
        } else if (textfield.createTextRange) {
            var range = textfield.createTextRange();
            range.collapse(true);
            range.moveEnd('character', textfield.value.length);
            range.moveStart('character', textfield.value.length);
            range.select();
        }
        textfield.focus();

        $( "#" + editBoxId ).bind( 'keypress', function( e ){onEditBoxSave( e );} );
        $( "#" + editBoxId ).bind( 'click', function( e ){e.stopPropagation();} );
        $( "#" + editBoxId ).focusout( function(event) {onFocusLost(event)} );
        //event.stopPropagation();
        editingStart = true;
        document.onclick = finishEditOnDocumentClick;
    }

    function onEditBoxSave( event ){
        if( event.which == 13 ){
            onFocusLost(event)
        }
    }

    function onFocusLost(event) {
        var newVal = $( "#" + editBoxId ).val();

        newVal = $.trim( newVal );
        if( newVal.length < 1 ) {
            newVal = element.text();
        }
        $( "#" + editBoxId ).replaceWith( element );
        element.text( newVal );

        element.bind( 'click', function( event ){onClicked( event );});
        onLabelChanged( newVal );
    }

    function finishEditOnDocumentClick( event ) {
        if ( editingStart ) {
            editingStart = false;
        } else {
            document.onclick -= finishEditOnDocumentClick;
            onFocusLost( event );
        }
    }
}
