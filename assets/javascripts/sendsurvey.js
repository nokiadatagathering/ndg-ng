

var SendSurvey = function() {

    var currentSurveyId = "";
    var selectedUsers = new Array();

    function showUserList(i) {
        currentSurveyId = i.data;
        $('#sendSurveyUsers').empty();
        $('#sendSurveyUsers').append( '<p><b>Select survey receipents</b></p>');
        $('#sendSurveyUsers').append( '<table><thead>'
                               + '<tr>'
                               + '<th scope="col"></th>'
                               + '<th scope="col"><b>' + "Username" + '</b></th>'
                               + '<th scope="col"><b>' + "phone number" + '</b></th>'
                               + '<th scope="col"><b>' + "email" + '</b></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="userListTable">'
                               + '</tbody></table>' );

        $('#sendSurveyUsers').append( '<div><button id="startSendingButton">Send Surveys</button><button id="clearUsersButton">Clear</button></div>');
        $.getJSON('/application/listUsers', {'orderBy': "id"}, function(i) {fillUserTable(i);} );
        $('#startSendingButton').click( function(){onStartSendingClicked();} );
        $('#clearUsersButton').click( function(){onClearUsersClicked();} );

    }

    function fillUserTable(data) {
        $('#userListTable').empty();
        $.each(data.users,function(i,item) {
            fillWithResults(i,item);
        });
    }

    function fillWithResults(i, item) {
        $('#userListTable').append( '<tr>'
                                    + '<td><input type="checkbox" id="userCheckbox' + item.id + '"/></td>'
                                    + '<td>'+ item.username + '</td>'
                                    + '<td>' + item.phoneNumber + '</td>'
                                    + '<td>' + item.email + '</td>'
                                    + '</tr>' );
        $( '#userCheckbox' + item.id ).click( item.id, function(i){userCheckboxClicked(i);} );
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

    function onClearUsersClicked() {
      $('input[id^="userCheckbox"]').attr('checked', false);
      selectedUsers = new Array();
    }

    function userCheckboxClicked(i) {
        if( i.currentTarget.checked ) {
            if ( -1 == jQuery.inArray( i.data, selectedUsers ) ) {
                selectedUsers.push( i.data );
            }
        } else {
            if ( -1 != jQuery.inArray( i.data.toString(), selectedUsers ) ) {
                selectedUsers.splice(jQuery.inArray( i.data.toString(), selectedUsers ), 1 );
            }
        }
    }

        return {showUserList : function(i) {showUserList(i);}
    };



}();