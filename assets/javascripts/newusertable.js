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
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_USERNAME' ) + '</td>'
                  +     '<td class="newUserTdClass"><input id="userName" class="newUserInput" type="text" name="username" maxlength="15" placeholder="johnsmith06" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_FIRST_NAME' ) + '</td>'
                  +     '<td class="newUserTdClass"><input id="formfirstName" class="newUserInput" type="text" name="firstName" maxlength="20" placeholder="John" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_LAST_NAME' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input id="formlastName" class="newUserInput" type="text" name="lastName" maxlength="20" placeholder="Smith" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_PASSWORD' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input id="newUserFakePwd" class="grayed newUserInput" type="text" maxlength="10" name="fakepwd" placeholder="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  +         '<input id="newUserPassword" class="newUserInput" type="password" maxlength="10" name="password" placeholder="' + LOC.get( 'LOC_PASSWORD' ) + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input id="newUserFakePwdConfirm" class="grayed newUserInput" type="text" name="fakepwdconfirm" placeholder="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  +         '<input id="newUserPasswordConfirm" class="newUserInput" type="password" name="passwordRetype" placeholder="' + LOC.get( 'LOC_RETYPE_PASSWORD' ) + '" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_PERMISSION' ) + '</td>'
                  +     '<td class="newUserTdClass"><select id="selectRole" name="role" class="newUserSelectInput">'
                  +         '<option value="Admin">' + LOC.get( 'LOC_ADMIN' ) + '</option>'
                  +         '<option value="Field Worker">' + LOC.get( 'LOC_FIELD_WORKER' ) + '</option>'
                  +         '<option value="Operator">' + LOC.get( 'LOC_OPERATOR' ) + '</option></select>'
                  +     '</td>'
                  + '</tr>'
                  + '<tr id="emailTr" class="newUserTrClass">'
                  +     '<td class="newUserTdClass">E-mail</td>'
                  +     '<td class="newUserTdClass">'
                  +         '<input class="newUserInput" type="email" name="email" maxlength="30" placeholder="johnsmith06@email.com" />'
                  +     '</td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td class="newUserTdClass">' + LOC.get( 'LOC_PHONE_NUMBER_LONG' ) + '</td>'
                  +     '<td class="newUserTdClass"><input id="newUserPhoneNumber" class="newUserInput" maxlength="14" type="text" name="phoneNumber" placeholder="+1234567890" /></td>'
                  + '</tr>'
                  + '<tr class="newUserTrClass">'
                  +     '<td colspan="2">'
                  +         '<input type="submit" class="submitNewUserButton large button blue" value="' + LOC.get( 'LOC_SAVE' ) + '" />'
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

            var select = document.getElementById("selectRole");
            select.onchange = function() {
                if(select.value == 'Field Worker') {
                    $('#emailTr').hide();
                }
                else {
                    $('#emailTr').show();
                }
//                var selIndex = select.selectedIndex;
//                var selValue = select.options(selIndex).innerHTML;
            }

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

    function userInputValidation() {

        var ck_name = /^[A-Za-z0-9 ]{1,30}$/;
        var sFileName1 = $('#formfirstName').val()
        var sFileName2 = $('#formlastName').val()
        var sFileName3 = $('#userName').val()


        if( $("#newUserForm input[name=username]").val() == '' || !ck_name.test(sFileName3)) {
            alertDialog.dialog( {title: LOC.get('LOC_ERROR')} );
            $('#buttonOK').text( LOC.get('LOC_OK') ); 
            $('#addUserError').text( LOC.get('LOC_USERNAME_EMPTY') ); 
            $("#buttonOK").click(function() {alertDialog.dialog("close");});     
            alertDialog.dialog({close: function(){$.unblockUI();}} )
            alertDialog.dialog("open");
            $.blockUI( {message: null} );
            return false;
        } else if( $("#newUserForm input[name=password]").val() == '' && $("#newUserForm input[name=passwordRetype]").val() == '' ) {
            alertDialog.dialog( {title: LOC.get('LOC_ERROR')} );
            $('#buttonOK').text( LOC.get('LOC_OK') ); 
            $('#addUserError').text( LOC.get('LOC_PASSWORD_EMPTY') ); 
            $("#buttonOK").click(function() {alertDialog.dialog("close");});     
            alertDialog.dialog({close: function(){$.unblockUI();}} )
            alertDialog.dialog("open");
            $.blockUI( {message: null} );
            return false;
        } else if( $("#newUserForm input[name=password]").val() != $("#newUserForm input[name=passwordRetype]").val()) {
            alertDialog.dialog( {title: LOC.get('LOC_ERROR')} );
            $('#buttonOK').text( LOC.get('LOC_OK') ); 
            $('#addUserError').text( LOC.get('LOC_MSG_PASSWORD_NOT_MATCH') ); 
            $("#buttonOK").click(function() {alertDialog.dialog("close");});     
            alertDialog.dialog({close: function(){$.unblockUI();}} )
            alertDialog.dialog("open");
            $.blockUI( {message: null} );
            return false;
        } else if( $( "#newUserPhoneNumber" ).val().length < 5 ||
                    $( "#newUserPhoneNumber" ).val() == 'Phone Number'){
            alertDialog.dialog( {title: LOC.get('LOC_ERROR')} );
            $('#buttonOK').text( LOC.get('LOC_OK') ); 
            $('#addUserError').text( LOC.get('LOC_MSG_SHORT_NUMBER') ); 
            $("#buttonOK").click(function() {alertDialog.dialog("close");});     
            alertDialog.dialog({close: function(){;$.unblockUI();}} )
            alertDialog.dialog("open");
            $.blockUI( {message: null} );
            return false;
        } else if (!ck_name.test(sFileName1) || !ck_name.test(sFileName2)){
            alertDialog.dialog( {title: LOC.get('LOC_ERROR')} );
            $('#buttonOK').text( LOC.get('LOC_OK') ); 
            $('#addUserError').text( LOC.get('LOC_TEXT') ); 
            $("#buttonOK").click(function() {alertDialog.dialog("close");});     
            alertDialog.dialog({close: function(){$.unblockUI();}} )
            alertDialog.dialog("open");
            $.blockUI( {message: null} );
            //alert( LOC.get( 'LOC_USERNAME_EMPTY' ) );
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
