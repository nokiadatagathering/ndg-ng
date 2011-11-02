var NewGroupTable = (function() {

    var isShown = false;
    var addGroupTable;
    var dimmedBackground;

    return {
        show : function(position) {return showTable(position);},
        isShown : function() {return isShown}
    };

    function showTable(position) {
        if(isShown) {
            return;
        }
        addGroupTable = jQuery('<div></div>', {
                                id  : 'addNewUser',
                                'class': 'newGroupTable',
                                style: 'left:' + position.left + 'px;'
                                + 'top:' + (position.top + 39) + 'px;'});
        addGroupTable.width(position.width);
        addGroupTable.height(position.height);

        dimmedBackground = jQuery('<div></div>', {
                'class': "dimmedBackground"
            });
        //fill table
        var tableHtml;
        tableHtml = '<form id="newGroupForm" method="post" action="application/addGroup"><table><tr>'
                  + '<td><input class="newGroupInput" type="text" name="groupname" title="Groupname" /></td>'
                  + '<td><input type="submit" class="submitNewUserButton" value="" /></td>'
                  + '</tr></table></form>'
        addGroupTable.append(tableHtml);


        $("body").prepend(addGroupTable);

        addGroupTable.ready(function() {
            $(".newGroupInput").focus(function(src)
            {
                if ($(this).val() == $(this)[0].title)
                {
                    $(this).removeClass("grayed");
                    $(this).val("");
                }
            });

            $(".newGroupInput").blur(function()
            {
                if ($(this).val() == "")
                {
                    $(this).addClass("grayed");
                    $(this).val($(this)[0].title);
                }
            });

            $('#newGroupForm').submit(function(){
                //todo more validation
                var formData = $('#newGroupForm').serialize();
                $.ajax({
                    type: "post",
                    url: "application/addGroup",
                    data: formData,
                    success: function(result) {
                        UserManagement.refresh();
                        hide();
                    },
                    error: function(result) {
                        alert("ERROR!!");//todo ui spec for form validation
                    }

                });

            return false;
            });

            $('.newGroupInput').blur();
        });

        isShown = true;

        $("body").prepend(dimmedBackground);
        dimmedBackground.click(function(){hide();});
    }

    function hide()
    {
       addGroupTable.remove();
       delete(addGroupTable);
       dimmedBackground.remove();
       delete(dimmedBackground)
       isShown = false;
    }

})();