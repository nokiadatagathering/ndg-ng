

var SendSurvey = function() {

    var currentSurveyId = "";
    var selectedUsers = new Array();

    function showUserList(i) {
        currentSurveyId = i.data;
        $('#sendSurveyUsers').empty();
        $('#sendSurveyUsers').append( '<table><thead>'
                               + '<tr >'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-check"></th>'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-username">' + "Username" + '</th>'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-phone">' + "phone number" + '</th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="userListTable">'
                               + '</tbody></table>' );

        $.getJSON('/application/listUsers', {'orderBy': "id"}, function(i) {fillUserTable(i);} );
        $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
    }

    function fillUserTable(data) {
        $('#userListTable').empty();
        $.each(data.items,function(i,item) {
            fillWithResults(i,item);
        });
        $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'../images/empty.png'});
    }

    function fillWithResults(i, item) {
        $('#userListTable').append( '<tr class="tableSendSurvey-content">'
                                    + '<td class="tableSendSurvey-content" ><input type="checkbox" id="userCheckbox' + item.id + '"/></td>'
                                    + '<td class="tableSendSurvey-content" >'+ item.username + '</td>'
                                    + '<td class="tableSendSurvey-content" >' + item.phoneNumber + '</td>'
                                    + '</tr>' );
        $( '#userCheckbox' + item.id ).bind( 'check uncheck', item.id, function(i){userCheckboxClicked(i);} )
    }

    function onStartSendingClicked() {
        if(selectedUsers.length > 0)
        {
            $.ajax(
            {
                type: "POST",
                url: "/sendsurveys/" + currentSurveyId ,
                data: {users : selectedUsers},
                success: function(msg){
                   alert( "Surveys available");
                   selectedUsers = new Array();
                   sendSurveyDialog.dialog("close");
                   $('#sendSurveyUsers').empty();
                },
                error: function(request,error) {
                    alert("Error with connection to server");
                }
            });
        } else
        {
            alert("No users selected");
        }
    }

//    function onClearUsersClicked() {
//        $('input[id^="userCheckbox"]').attr('checked', false);
//        selectedUsers = new Array();
//    }

    function userCheckboxClicked(i) {
        if( i.currentTarget.checked ) {
            if ( -1 == jQuery.inArray( i.data, selectedUsers ) ) {
                selectedUsers.push( i.data );
            }
        } else {
            if ( -1 != jQuery.inArray( i.data, selectedUsers ) ) {
                selectedUsers.splice(jQuery.inArray( i.data, selectedUsers ), 1 );
            }
        }
    }

        return {showUserList : function(i) {showUserList(i);}
    };
}();