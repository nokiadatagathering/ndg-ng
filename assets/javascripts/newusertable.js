var NewUserTable = (function() {

    var isShown = false;
    var addUserTable;
    var dimmedBackground;

    return {
        show : function(position) {return showTable(position);},
        isShown : function() {return isShown}
    };

    function showTable(position) {
        if(isShown) {
            return;
        }
        addUserTable = jQuery('<div></div>', {
                                id  : 'addNewUser',
                                'class': 'newUserTable',
                                style: 'left:' + position.left + 'px;'
                                + 'top:' + position.top + 'px;'});
        addUserTable.width(position.width);
        addUserTable.height(position.height);

        dimmedBackground = jQuery('<div></div>', {
                'class': "dimmedBackground"
            });
        //fill table
        var tableHtml;
        tableHtml = '<form id="newUserForm" method="post" action="application/addUser"><table><tr>'
                  +  '<td><input class="newUserInput" type="text" name="username" title="Username" /></td>'
                  +  '<td><input id="newUserFakePwd" class="grayed" type="text" name="fakepwd" value="Password" />'
                  +       '<input id="newUserPassword" class="newUserPassword" type="password" name="password" title="Password" /></td>'
                  +  '<td><input id="newUserFakePwdConfirm" class="grayed" type="text" name="fakepwdconfirm" value="Retype Password" />'
                  +       '<input id="newUserPasswordConfirm" class="newUserPassword" type="password" name="passwordRetype" title="Retype Password" /></td>'
                  +  '<td><div><select name="role" style="width: 160px; height: 60px;" ><option value="Admin">Admin</option><option value="Field Worker">Field Worker</option><option value="Operator">Operator</option></select>'
                  + '</tr><tr>'
                  +  '<td><input class="newUserInput" type="text" name="fullName" title="Full Name" /></td>'
                  +  '<td><input class="newUserInput" type="email" name="email" title="E-Mail" /></td>'
                  +  '<td><input class="newUserInput" type="number" name="phoneNumber" title="Phone Number" /></td>'
                  +  '<td><input type="submit" style="width: 160px; height: 60px;" value="Save Changes" /></td>'
              + '</tr></table></form>'
          addUserTable.append(tableHtml);


        $("body").prepend(addUserTable);

        addUserTable.ready(function() {
            $(".newUserInput").focus(function(src)
            {
                if ($(this).val() == $(this)[0].title)
                {
                    $(this).removeClass("grayed");
                    $(this).val("");
                }
            });

            $('#newUserFakePwd').focus(function(src)
            {
                $('#newUserFakePwd').hide();
                $('#newUserPassword').show();
                $('#newUserPassword').focus();
            });

            $("#newUserFakePwdConfirm").focus(function(src)
            {
                $("#newUserFakePwdConfirm").hide();
                $('#newUserPasswordConfirm').show();
                $('#newUserPasswordConfirm').focus();
            });

            $(".newUserInput").blur(function()
            {
                if ($(this).val() == "")
                {
                    $(this).addClass("grayed");
                    $(this).val($(this)[0].title);
                }
            });
            $("#newUserPassword").blur(function()
            {
                if ($(this).val() == "")
                {
                    $(this).hide();
                    $("#newUserFakePwd").show();
                }
            });
            $("#newUserPasswordConfirm").blur(function()
            {
                if ($(this).val() == "")
                {
                    $(this).hide();
                    $("#newUserFakePwdConfirm").show();
                }
            });

            $('#newUserForm').submit(function(){
                //todo more validation
                var formData = $('#newUserForm').serialize();
                if( $("#newUserForm input[name=password]").val() === $("#newUserForm input[name=passwordRetype]").val()) {
                $.ajax({
                    type: "post",
                    url: "application/addUser",
                    data: formData,
                    success: function(result) {
                        DynamicTable.refresh();
                        hide();
                    },
                    error: function(result) {
                        alert("ERROR!!");//todo ui spec for form validation
                    }

                });
                } else {
                    alert("Password do not match");//todo ui spec for form validation
                }
            return false;
            });

            $('.newUserInput').blur();
        });

        isShown = true;

        $("body").prepend(dimmedBackground);
        dimmedBackground.click(function(){hide();});
    }

    function hide()
    {
       addUserTable.remove();
       delete(addUserTable);
       dimmedBackground.remove();
       delete(dimmedBackground)
       isShown = false;
    }

})();