

var SendSMS = function() {
    var currentUser;
    var currentGroup;

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

   function showSendGroupSMS(groupDetails) {
        currentGroup = groupDetails;
        $('#sendSMSGroups').empty();
        $('#sendSMSGroups').append( '<div class="tableSendSmsWidth">'
                                    + '<div class="userFullName-cell">'
                                    + '<span class="userFullName-To">' + LOC.get('LOC_TO') + ':</span>'
                                    + '<span class="userFullName-FullName">' + currentGroup.groupName + '</span>'
                                    + '</div>'
                                    + '<div class="smsMessage-cell"><textarea id="messageTextarea" class="smsMessage-textarea" name="userMessage">'
                                    + ''
                                    + '</textarea></div>'
                                    + '</div>' );


        sendGroupSMSDialog.dialog({beforeClose: function(){$('#buttonSendGroupSMSDone').unbind('click');}} )
        $('#buttonSendGroupSMSDone').click( currentGroup, function(event){onStartSendingGroupClicked(event);} );
    }


    function onStartSendingGroupClicked(event) { 
                 sendUserSMSDialog.dialog("close");
                 var haikus=[];
                 var smsMessage = $.trim( document.getElementById( 'messageTextarea' ).value );

                 if ( smsMessage != null && smsMessage.length > 0) {
                                $('#buttonSendSMSDone').unbind('click');
                 }else{
                                alert("Empty message");
                 }
                 var groupName = event.data.groupName;  
                 var contentUrl = 'listData/users?startIndex=0&endIndex=100&isAscending=true&groupName='+ groupName;
                 var getJSONQuery = $.ajax({
                       url: contentUrl,
                       async: false,
                       dataType: 'json',
                       success: function (json) {
                       haikus = json.items;
                         }
                        });
                 getJSONQuery.error(Utils.redirectIfUnauthorized) ;

                 if ( smsMessage != null && smsMessage.length > 0 ) {
                       $('#buttonSendSMSDone').unbind('click');
                       
                            for (var i = 0; i < haikus.length; i++){
 
                                  $.ajax(
                                        {
                                        type: "POST",
                                        url: "smsmessage/sendsms",
                                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                        data: {phoneNumber : haikus[i].phoneNumber,
                                        message: smsMessage
                                              },
                                  error:   function(result, textStatus, error) {
                                         if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                                              console.log(result, textStatus, error);
                                              return false;
                                                                                                       }
                                                                               }                                                 
                                        });
                      
                                                                   } // end of for
                              sendGroupSMSDialog.dialog("close");
                                                                    } //end of if
                                               } //end of function
                 


    function onStartSendingClicked(event) {
        var smsMessage = $.trim( document.getElementById( 'messageTextarea' ).value );
        alert(smsMessage);
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
                               $('#buttonSendSMSDone').click( currentUser, function(event){onStartSendingClicked(event);} );
                                                                                         }
                                                           }
            });
                                                          } else{
            alert("Empty message");
             }
    }

        return {showSendUserSMS : function(i) {showSendUserSMS(i);},
                showSendGroupSMS : function( i ) {showSendGroupSMS(i);}
    };
}();
