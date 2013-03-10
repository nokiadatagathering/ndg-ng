var EditUserTable = (function() {

    var isShown = false;
    var addUserTable;
    var dimmedBackground;

    return {
        show : function(position, user) {return showTable(position, user);},
        isShown : function() {return isShown}
    };

    function showTable(position, user) {
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
        tableHtml = '<form id="editUserForm" method="post" action="userManager/editUser"><table class="newUserTableClass">'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_USERNAME' ) + '</td>'
                  +     '<td class="newUserTdClass"><input class="newUserInput" type="text" name="username" maxlength="15" value="' + user.username + '" readonly="readonly" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_FIRST_NAME' ) + '</td>'
                  +     '<td class="newUserTdClass"><input class="newUserInput" type="text" name="firstName" maxlength="20" value="' + user.firstName + '" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_LAST_NAME' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input class="newUserInput" type="text" name="lastName" maxlength="20" value="' + user.lastName + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_PASSWORD' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input id="newUserFakePwd" class="grayed newUserInput" type="text" maxlength="10" name="fakepwd" value="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  +         '<input id="newUserPassword" class="newUserPassword" type="password" maxlength="10" name="password" placeholder="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input id="newUserFakePwdConfirm" class="grayed newUserInput" type="text" name="fakepwdconfirm" value="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  +         '<input id="newUserPasswordConfirm" class="newUserPassword" type="password" name="passwordRetype" placeholder="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">Role</td>'
                  +     '<td class="newUserTdClass"><select id="selectRole" name="role" class="newUserSelectInput">'
                  +         '<option value="Admin">' + LOC.get( 'LOC_ADMIN' ) + '</option>'
                  +         '<option value="Field Worker">' + LOC.get( 'LOC_FIELD_WORKER' ) + '</option>'
                  +         '<option value="Operator">' + LOC.get( 'LOC_OPERATOR' ) + '</option></select>'
                  +     '</td>'
                  + '</tr>'
                  + '<tr id="emailTr" class="newUserTrClass">'
                  +     '<td class="newUserTdClass">E-mail</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input class="newUserInput" type="email" name="email" maxlength="30" value="' + user.email + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_PHONE_NUMBER_LONG' ) + '</td>'
                  +     '<td class="newUserTdClass"><input id="newUserPhoneNumber" class="newUserInput" maxlength="14" type="text" name="phoneNumber" value="' + user.phoneNumber + '" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td colspan="2">'
                  +         '<input type="submit" class="submitNewUserButton large button blue" value="Save" />'
                  +     '</td>'
                  + '</tr>'
                  + '</table></form>'

        addUserTable.append(tableHtml);

        $("body").prepend(addUserTable);
        $("#newUserPhoneNumber").mask("+?999999999999999",{placeholder:""});

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

            $('#selectRole').val(user.userRoleCollection[0].ndgRole.roleName);

            var select = document.getElementById("selectRole");

            if(select.value == 'Field Worker') {
                $('#emailTr').hide();
            }
            else {
                $('#emailTr').show();
            }
            select.onchange = function() {
                if(select.value == 'Field Worker') {
                    $('#emailTr').hide();
                }
                else {
                    $('#emailTr').show();
                }
            }

         $('#editUserForm').submit(function(data){
                //todo more validation

                if ( !userInputValidation() ) {
                    return false;
                }
                if ($("#newUserPassword").val() != "" && $("#newUserPasswordConfirm").val() != "") {
                    Utils.encryptCredentials(data);
                }
                $("#editUserForm input[name=passwordRetype]").val("");
                var formData = $('#editUserForm').serialize();
                $.ajax({
                    type: "post",
                    url: "userManager/editUser",
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
        if( $("#editUserForm input[name=password]").val() != $("#editUserForm input[name=passwordRetype]").val()) {
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
