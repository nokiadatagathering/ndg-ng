var ConfirmCover = (function() {

    var visible = false;

    return {
        show : function(position, userId) {return showConfirm(position, userId);},
        isShown : function() {return visible},
        close : function() { close(); }
    };

    function showConfirm(position, userId) {
        var stopBreakPoint = 0;
        var coverDiv = jQuery('<div></div>', {
                                id  : 'confirmDeleteBar',
                                'class': 'configDeleteBar',
                                style: 'left:' + position.left + 'px;'
                                + 'top:' + position.top + 'px;'});
        coverDiv.width(position.width);
        coverDiv.height(position.height);
        coverDiv.append( '<p>' + LOC.get('LOC_CONFIRM_DELETE') +  '</p>');
        $("body").prepend(coverDiv);
        coverDiv.click(userId, function(event){
            $.post( "application/deleteUser?userId=" + event.data, function(data) {
                $('#confirmDeleteBar').remove();
                DynamicTable.refresh();
            });
        });
        coverDiv.mouseleave( function(){
            close();
        })
        visible = true;
    }

    function close() {
       $('#confirmDeleteBar').remove();
       visible = false;
    }

})();