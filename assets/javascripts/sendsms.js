

var SendSMS = function() {
    var currentUser;

    function showSendUserSMS(userDetails) {
        currentUser = userDetails;
        $('#sendSMSUsers').empty();
        $('#sendSMSUsers').append( '<div class="tableSendSmsWidth">'
                                    + '<div class="userFullName-cell">'
                                    + '<span class="userFullName-To">' + LOC.get('LOC_TO') + ':</span>'
                                    + '<span class="userFullName-FullName">' + currentUser.fullName+ '</span>'
                                    + '</div>'
                                    + '<div class="userPhoneNumber-cell">'
                                    + '<span class="userPhoneNumber-To">' + LOC.get('LOC_PHONE_NUMBER') + ':</span>'
                                    + '<span class="userPhoneNumber-PhoneNo">' + currentUser.phoneNumber + '</span>'
                                    + '</div>'
                                    + '<div class="smsMessage-cell"><textarea id="messageTextarea" class="smsMessage-textarea" name="userMessage">'
                                    + ''
                                    + '</textarea></div>'
                                    + '</div>' );


        sendUserSMSDialog.dialog({beforeClose: function(){$('#buttonSendSMSDone').unbind('click');}} )
        $('#buttonSendSMSDone').click( currentUser, function(event){onStartSendingClicked(event);} );
    }

    function onStartSendingClicked(event) {
        var smsMessage = $.trim( document.getElementById( 'messageTextarea' ).value )
        if ( smsMessage != null && smsMessage.length > 0) {
            $('#buttonSendSMSDone').unbind('click');
            $.ajax(
            {
                type: "POST",
                url: "smsmessage/sendsms",
                data: {phoneNumber : event.data.phoneNumber,
                       message: smsMessage},
                success: function(msg){
                             sendUserSMSDialog.dialog("close");
                },
                error: function(result, textStatus, error) {
                           if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                               alert("Error with connection to server");
                               $('#buttonSendSMSDone').click( function(){onStartSendingClicked();} );
                           }
                }
            });
        } else
        {
            alert("Empty message");
        }
    }

        return {showSendUserSMS : function(i) {showSendUserSMS(i);}
    };
}();