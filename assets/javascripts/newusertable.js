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

        dimmedBackground = jQuery('<div></div>', {
                'class': "dimmedBackground"
            });
        //fill table
        var tableHtml;
        tableHtml = '<form id="newUserForm" method="post" action="userManager/addUser"><table class="newUserTableClass">'
                  + '<tr class="newUserTrClass">'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="text" name="username" title="' + LOC.get( 'LOC_USERNAME' ) + '" /></td>'
                  + '<td class="newUserTdClass"><input class="newUserInput" type="text" name="firstName" title="' + LOC.get( 'LOC_FIRST_NAME' ) + '" /></td>'
                  + '<td class="newUserTdClass">'
                  +     '<input id="newUserFakePwd" class="grayed newUserInput" type="text" name="fakepwd" value="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  +     '<input id="newUserPassword" class="newUserPassword" type="password" name="password" title="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  + '</td>'
                  + '<td class="newUserTdClass"><select name="role" style="width:133px;height:34px;font-size:12px;line-height:29px;vertical-align:middle;margin-top:5px;margin-left:0px;text-transform: uppercase;'
                  +         'border-bottom-color: #EEE;border-bottom-left-radius: 2px;border-bottom-right-radius: 2px;border-bottom-style: inset;border-bottom-width: 2px;border-collapse: collapse;border-left-color: #D0D1D5;border-left-style: inset;border-left-width: 1px;border-right-color: #EEE;border-right-style: inset;border-right-width: 2px;border-top-color: #D0D1D5;border-top-left-radius: 2px;border-top-right-radius: 2px;border-top-style: solid;border-top-width: 1px;">'
                  +     '<option value="Admin">' + LOC.get( 'LOC_ADMIN' ) + '</option>'
                  +     '<option value="Field Worker">' + LOC.get( 'LOC_FIELD_WORKER' ) + '</option>'
                  +     '<option value="Operator">' + LOC.get( 'LOC_OPERATOR' ) + '</option></select></td>'
                  + '<td><input type="submit" class="submitNewUserButton" value="" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  + '<td class="newUserTdClass">'
                  +     '<input class="newUserInput" type="email" name="email" title="E-Mail" />'
                  + '</td>'
                  + '<td class="newUserTdClass">'
                  +     '<input class="newUserInput" type="text" name="lastName" title="' + LOC.get( 'LOC_LAST_NAME' ) + '" />'
                  + '</td>'
                  + '<td class="newUserTdClass">'
                  +     '<input id="newUserFakePwdConfirm" class="grayed newUserInput" type="text" name="fakepwdconfirm" value="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  +     '<input id="newUserPasswordConfirm" class="newUserPassword" type="password" name="passwordRetype" title="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  + '</td>'
                  + '<td class="newUserTdClass"><input id="newUserPhoneNumber" class="newUserInput" type="text" name="phoneNumber" title="' + LOC.get( 'LOC_PHONE_NUMBER_LONG' ) + '" /></td>'
                  + '</tr></table></form>'
          addUserTable.append(tableHtml);


        $("body").prepend(addUserTable);
        $("#newUserPhoneNumber").mask("+?999999999999999",{placeholder:" "});


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

            $("#newUserPhoneNumber").blur(function()
            {
                if ($(this).val() == "" || $(this).val() == "+")
                {
                    $(this).addClass("grayed");
                    $(this).val($(this)[0].title);
                }
            });

            $("#newUserPhoneNumber").focus(function()
            {
                $(this).removeClass("grayed");
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

                if( !userInputValidation() ){
                    return false;
                }

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
                    error: function(result, textStatus, error) {
                       if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                          alert("ERROR!!");//todo ui spec for form validation
                        }
                    }
                });
                return false;
            });

            $('.newUserInput').blur();
        });

        isShown = true;

        $("body").prepend(dimmedBackground);
        dimmedBackground.click(function(){hide();});
    }

    function userInputValidation(){
        if( $("#newUserForm input[name=password]").val() != $("#newUserForm input[name=passwordRetype]").val()) {
            alert( LOC.get( 'LOC_MSG_PASSWORD_NOT_MATCH' ) );//todo ui spec for form validation
            return false;
        }else if( $( "#newUserPhoneNumber" ).val().length < 5 ||
                    $( "#newUserPhoneNumber" ).val() == 'Phone Number'){
            alert( LOC.get( 'LOC_MSG_SHORT_NUMBER' ) );
            return false;
        }
        return true;
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