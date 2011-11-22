

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

        var getJSONQuery = $.getJSON('/listData/sendSurveysUserList', {'formID': currentSurveyId}, function(i) {fillUserTable(i);} );
        getJSONQuery.error(Utils.redirectIfUnauthorized);
        sendSurveyDialog.dialog({beforeClose: function(){$('#buttonSendSurveyDone').unbind('click');}} )
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
        $('#userListTable').append( '<tr class="tableSendSurvey-row">'
                                    + '<td class="tableSendSurvey-cell" ><input type="checkbox" id="userCheckbox' + item.id + '"/></td>'
                                    + '<td class="tableSendSurvey-cell" >'+ item.username + '</td>'
                                    + '<td class="tableSendSurvey-cell" >' + item.phoneNumber + '</td>'
                                    + '</tr>' );
        $( '#userCheckbox' + item.id ).bind( 'check uncheck', item.id, function(i){userCheckboxClicked(i);} )
    }

    function onStartSendingClicked() {
        if(selectedUsers.length > 0)
        {
            $('#buttonSendSurveyDone').unbind('click');
            $.ajax(
            {
                type: "POST",
                url: "sendsurveys/" + currentSurveyId ,
                data: {users : selectedUsers},
                success: function(msg){
                   alert( "Surveys available");
                   selectedUsers = new Array();
                   sendSurveyDialog.dialog("close");
                   $('#sendSurveyUsers').empty();
                   DynamicTable.refresh();
                },
                error: function(result, textStatus, error) {
                       if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                           alert("Error with connection to server");
                           $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
                        }
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