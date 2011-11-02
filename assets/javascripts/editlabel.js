var EditedLabel = function( elem, callbackFun ){
    var element = elem;
    var onLabelChanged = callbackFun;
    var editBoxId;

    element.bind( 'click' , function( event ){onClicked( event );});

    function onClicked( event ){
        editBoxId = element.attr('id') + 'edit';

        element.replaceWith( '<input id="' + editBoxId + '" type="text" style="width:'+ element.width() +'px"/>' );
        $( "#" + editBoxId ).val( element.text() );
        $( "#" + editBoxId ).focus();

        $( "#" + editBoxId ).bind( 'keypress', function( e ){onEditBoxSave( e );} );
        $( "#" + editBoxId ).bind( 'click', function( e ){ e.stopPropagation(); } );
        $( "#" + editBoxId ).focusout( function(event) { onFocusLost(event) } );
        event.stopPropagation();
    }

    function onEditBoxSave( event ){
        if( event.which == 13 ){
            var newVal = $( "#" + editBoxId ).val();
            $( "#" + editBoxId ).replaceWith( element );
            element.text( newVal );

            element.bind( 'click', function( event ){ onClicked( event ); });
            onLabelChanged( newVal );
        }
    }

    function onFocusLost(event) {
        var newVal = $( "#" + editBoxId ).val();
        $( "#" + editBoxId ).replaceWith( element );
        element.text( newVal );

        element.bind( 'click', function( event ){ onClicked( event ); });
        onLabelChanged( newVal );
    }
}
