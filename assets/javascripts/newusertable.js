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
                                + 'top:' + (position.top + 39) + 'px;'});
        addUserTable.width(position.width);

        dimmedBackground = jQuery('<div></div>', {
                'class': "dimmedBackground"
            });
        //fill table
        var tableHtml;
        tableHtml = '<form id="newUserForm" method="post" action="userManager/addUser"><table class="newUserTableClass"><tr class="newUserTrClass">'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="text" name="username" title="Username" /></td>'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="text" name="firstName" title="First Name" /></td>'
                  + '<td class="newUserTdClass"><input id="newUserFakePwd" class="grayed newUserInput" type="text" name="fakepwd" value="Password" />'
                  +      '<input id="newUserPassword" class="newUserPassword" type="password" name="password" title="Password" /></td>'
                  + '<td class="newUserTdClass"><select name="role" style="width:133px;height:34px;font-size:12px;line-height:29px;vertical-align:middle;margin-top:5px;margin-left:0px;text-transform: uppercase;'
                  + 'border-bottom-color: #EEE;border-bottom-left-radius: 2px;border-bottom-right-radius: 2px;border-bottom-style: inset;border-bottom-width: 2px;border-collapse: collapse;border-left-color: #D0D1D5;border-left-style: inset;border-left-width: 1px;border-right-color: #EEE;border-right-style: inset;border-right-width: 2px;border-top-color: #D0D1D5;border-top-left-radius: 2px;border-top-right-radius: 2px;border-top-style: solid;border-top-width: 1px;">'
                  + '<option value="Admin">Admin</option><option value="Field Worker">Field Worker</option><option value="Operator">Operator</option></select></td>'
                  + '<td><input type="submit" class="submitNewUserButton" value="" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="email" name="email" title="E-Mail" /></td>'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="text" name="lastName" title="Last Name" /></td>'
                  + '<td class="newUserTdClass"><input id="newUserFakePwdConfirm" class="grayed newUserInput" type="text" name="fakepwdconfirm" value="Retype Password" />'
                  +      '<input id="newUserPasswordConfirm" class="newUserPassword" type="password" name="passwordRetype" title="Retype Password" /></td>'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="number" name="phoneNumber" title="Phone Number" /></td>'
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

            $('#newUserForm').submit(function(data){
                //todo more validation
                if( $("#newUserForm input[name=password]").val() === $("#newUserForm input[name=passwordRetype]").val()) {
                  Utils.encryptCredentials(data);
                  $("#newUserForm input[name=passwordRetype]").val("");
                  var formData = $('#newUserForm').serialize();
                    $.ajax({
                        type: "post",
                        url: "userManager/addUser",
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