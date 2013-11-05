

var SendSurvey = function() {

    // sets variables
    var currentSurveyId = "";
    var selectedUsers = new Array();
    var selectedGroups = new Array();
    var allUsersSelected = false;
    //var allGroupsSelected = false;
    //var selectedGroupsUsers = new Array();

    // shows the interface 
    function showUserList(i) {
        currentSurveyId = i.data;
        $('#sendSurveyUsers').empty();
        
        var params = {'isAscending': true, 'endIndex': 200, 'startIndex': 0
                     };

        var selectAllCheckbox = '<input type="checkbox" class="userCheckboxClass" id="userCheckboxAll" />';
        //var selectAllGroupCheckbox = '<input type="checkbox" class="groupCheckboxClass" id="groupCheckboxAll"/>';
        $('#sendSurveyUsers').append( '<div id="tableContainer" style="height:400px; overflow:auto;"><table align="left"><thead>'
                               + '<tr >'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-check">'+ selectAllCheckbox + '</th>'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-username">' + "Username" + '</th>'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-phone">' + "Phone Number" + '</th>'    
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="userListTable">'
                               + '</tbody>'
                               + '</table>'
                               + '<table><thead>'
                               + '<tr>'
                               + '<th scope="col" class="tableSendSurvey-header-check" id="sendSurvey-groupcheck"></th>'
                               + '<th scope="col" class="tableSendSurvey-header" id="sendSurvey-group">' + "Group" + '</th>'     
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="groupListTable">'
                               + '</tbody>'
                               + '</table>'
                               + '</div>');

        var getJSONQuery = $.getJSON('listData/sendSurveysUserList', {'formID': currentSurveyId}, function(i) {fillUserTable(i);$.data(document,"allData",i);$.data(document,"totalItemsAllUsers",i.items.length);} );
        getJSONQuery.error(Utils.redirectIfUnauthorized);
        var getJSONQuery1 = $.getJSON( 'listData/groups', params, function(i){fillGroupTable(i);} );
        getJSONQuery1.error(Utils.redirectIfUnauthorized);
        $('#userCheckboxAll').bind( 'check uncheck', $(this), function(data){selectAllUsersClicked(data);} )
        //$('#groupCheckboxAll').bind( 'check uncheck', $(this), function(data){selectAllGroupsClicked(data);} )
        sendSurveyDialog.dialog({beforeClose: function(){$('#buttonSendSurveyDone').unbind('click');$("#dialog-sendSurvey").parent().find("span.ui-dialog-title").css("text-decoration", "none");}} )
        $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
    }

     function selectAllUsersClicked(data) {
        if(data.currentTarget.checked) {
            selectAllUsers();
        } else if(selectedUsers) {
            unselectAllUsers();
        }
    }

    /*function selectAllGroupsClicked (data) {
       if(data.currentTarget.checked) {
            selectAllGroups();
        } else if(selectedUsers) {
            unselectAllGroups();
        }
    
    }*/

      function selectAllUsers() {
        selectAllVisibleUsers();
        allUsersSelected  = true;
    }

    /*function selectAllGroups() {
        selectAllVisibleGroups();
        allGroupsSelected  = true;
    }*/

    function selectAllVisibleUsers() {
        doSelectAllVisibleResults();
        allUsersSelected = false;
                                     }

    /*function selectAllVisibleGroups() {
        doSelectAllVisibleGroupResults();
        allGroupsSelected = false;
                                     }*/


    function unselectAllUsers() {
        var checkboxes = $(".userCheckboxClass");

        $.each( checkboxes ,function( i, item ) {
            item.checked = false;
        });
        allUsersSelected = false;
    }

    /*function unselectAllGroups() {
        var checkboxes = $(".groupCheckboxClass");

        $.each( checkboxes ,function( i, item ) {
            item.checked = false;
        });
        allGroupsSelected = false;
    }*/

  
    function fillUserTableAll(data) {
        $('#userListTable').empty();
        for  (var i=0;i<data.items.length;i++){
                fillWithResults(i, data.items[i]);
                                     }
        $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'images/empty.png'});

        $( '#dialog-sendSurvey' ).unblock();
    }


    function fillGroupTable(data) {
      $('#groupListTable').empty();
        for  (var i=0;i<data.items.length;i++){
                //console.log(data.items[i].groupName);
                fillWithGroupResults(i, data.items[i]);
                                     }
        $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'images/empty.png'});

        $( '#dialog-sendSurvey' ).unblock();
           
    }

   
    function doSelectAllVisibleResults() {
        var checkboxes = $(".userCheckboxClass");   
        
        if($.data(document, "totalItemsAllUsers") > 10){
        $("#dialog-sendSurvey").parent().find("span.ui-dialog-title").css("text-decoration", "underline");
        var newtitle= 'Click to select all ' + $.data(document, "totalItemsAllUsers") + ' users';
        $("#dialog-sendSurvey").parent().find("span.ui-dialog-title").html(newtitle);
        $(".ui-dialog-titlebar").click( function(){
                                                  $( '#dialog-sendSurvey' ).block({
                                                     message: 'Loading Users..',
                                                     css: { border: '2px solid #3a77ca' }
                                                                                         });
                                                  $("#tableContainer").unbind("scroll");
                                                   fillUserTableAll($.data(document, "allData")); 
                                                   selectAllUsers();
                                                  });  }  

        $.each( checkboxes ,function( i, item ) {
            item.checked = true;
        });
    }  
    
     /*function doSelectAllVisibleGroupResults() {
        var checkboxes = $(".groupCheckboxClass");   
        

        $.each( checkboxes ,function( i, item ) {
            item.checked = true;
        });
    }*/

    $.fn.isNearTheEnd = function() {
        console.log(this[0].scrollTop);
        return this[0].scrollTop + this.height() >= this[0].scrollHeight;
                                      };

    $.fn.scrollTo = function( target, options, callback ){
  if(typeof options == 'function' && arguments.length == 2){ callback = options; options = target; }
  var settings = $.extend({
    scrollTarget  : target,
    offsetTop     : 50,
    duration      : 500,
    easing        : 'swing'
  }, options);
  return this.each(function(){
    var scrollPane = $(this);
    var scrollTarget = (typeof settings.scrollTarget == "number") ? settings.scrollTarget : $(settings.scrollTarget);
    var scrollY = (typeof scrollTarget == "number") ? scrollTarget : scrollTarget.offset().top + scrollPane.scrollTop() - parseInt(settings.offsetTop);
    scrollPane.animate({scrollTop : scrollY }, parseInt(settings.duration), settings.easing, function(){
      if (typeof callback == 'function') { callback.call(this); }
    });
  });
}


    function fillUserTable(data) {
    $('#userListTable').empty();
    var totalItemsDefault = data.items.length;
    if( totalItemsDefault < 10 ){
      for  (var i=0;i<totalItemsDefault;i++){
                fillWithResults(i, data.items[i]);
                                     }
                        }else{
        var elementStartIndex = 0;
        var elementEndIndex = 10;        
        var totalItems = data.items.length;
       

        for  (var i=elementStartIndex;i<elementEndIndex;i++){
                  fillWithResults(i, data.items[i]);
                            }

       $("#tableContainer").bind("scroll", function() {
               if ($(this).isNearTheEnd() ) {
               var tempScrollTop = $("#tableContainer")[0].scrollTop;

               var diff = totalItems - elementEndIndex;
               if( diff > 0 ) {
                   elementStartIndex = elementEndIndex;
                               }
               if(diff > 10) {
                   elementEndIndex += 10;
                              } 
               if(diff < 10) {
                    elementEndIndex += diff;
                             }
               
               for  (var i=elementStartIndex;i<elementEndIndex;i++){
                        if (diff==0)
                                   {
                                   break;
                                   }
                        
                        fillWithResults(i, data.items[i]);
                        
                                                                   }
               $('#tableContainer').scrollTo(tempScrollTop);

                                                  }
              
             
                                                      });
                                           
                        }
    $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'images/empty.png'});
                             }
                                  
                                             
                                                         

    function fillWithResults(i, item) {
        $('#userListTable').append( '<tr class="tableSendSurvey-row">'
                                    + '<td class="tableSendSurvey-cell" ><input type="checkbox" class="userCheckboxClass" id="userCheckbox' + item.id + '"/></td>'
                                    + '<td class="tableSendSurvey-cell" >'+ item.username + '</td>'
                                    + '<td class="tableSendSurvey-cell" >' + item.phoneNumber + '</td>'
                                    + '</tr>' );
        $( '#userCheckbox' + item.id ).bind( 'check uncheck', item.id, function(i){userCheckboxClicked(i);} )
        $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'images/empty.png'});
        $('#buttonSendSurveyDone').show();
    }

     function fillWithGroupResults(i, item) {
        $('#groupListTable').append( '<tr class="tableSendSurvey-row">'
                                        + '<td class="tableSendSurvey-cell" ><input type="checkbox" class="groupCheckboxClass" id="groupCheckbox' + item.id + '"/></td>'
                                        + '<td class="tableSendSurvey-cell" >'+ item.groupName + '</td>'
                                        + '<tr>');
        $( '#groupCheckbox' + item.id ).bind( 'check uncheck', {index: i,groupName: item.groupName,userCollection: item.userCollection}, function(i) {
                            if( $(this).is(':checked' ) ){ 
                            groupCheckboxClicked(i);
                                                        }else{                            
                            fillUserTableAll($.data(document, "allData"));                      
                                                        }
                                                                                                                                                      } );
        $('input:checkbox:not([safari])').checkbox({cls:'sendSurvey-customCheckbox', empty:'images/empty.png'});
        $('#buttonSendSurveyDone').show();
    }

    function onStartSendingClicked() {

        if(selectedUsers.length > 0)
        {
            //console.log(selectedUsers);
            $('#buttonSendSurveyDone').unbind('click');
            $.ajax(
            {
                type: "POST",
                url: "sendsurveys/" + currentSurveyId ,
                data: {users : selectedUsers},
                success: function(msg){        
                   selectedUsers = new Array();
                   sendSurveyDialog.dialog("close");
                   doneIt("Surveys are now available for the selected users");
                   DynamicTable.refresh();
                },
                error: function(result, textStatus, error) {
                       if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                           doneIt("Error with the connection to the server");
                           $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
                        }
                }
            });
       }
        /*else if (selectedGroups.length > 0) {

           for(i=0; i < selectedGroups.length; i++){
            var contentUrl = 'listData/users?groupName='+ selectedGroups[i];
           

            var getJSONQuery2 = $.ajax({
                       url: contentUrl,
                       dataType: 'json',
                       success: function (data) {
                            for(i=0;i<data.items.length;i++){
                               selectedGroupsUsers.push( data.items[i].id );
                                                            }
                                 sendToAllUsers(selectedGroupsUsers);
                  
                                 selectedGroupsUsers = new Array();
                                 selectedGroups = new Array();
                                 
                                 sendSurveyDialog.dialog("close");
                                 DynamicTable.refresh()
                                                },
                         error: function(result, textStatus, error) {
                          if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                           doneIt("Error with the connection to the server");
                           $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
                                                                                        }
                                                                    }
                                     });
            getJSONQuery2.error(Utils.redirectIfUnauthorized) ;


                                                    }
                  
        } */else
        {
            doneIt("No users selected");
        }
    }


    function doneIt(message){
       sendSurveyDoneDialog.dialog( {title: LOC.get('LOC_SEND_SURVEY')} );
       $('#sendSurveysDone').empty();
       $('#sendSurveysDone').append(message);
       sendSurveyDoneDialog.dialog("open");
       $('#buttonsendSurveysDone').text( LOC.get('LOC_CLOSE') );
       $("#buttonsendSurveysDone").click( function() {
            sendSurveyDoneDialog.dialog("close");
        });
    }

    /*function sendToAllUsers(selectedGroupsUsers){
      if (typeof selectedGroupsUsers[0] !== 'undefined' && selectedGroupsUsers[0] !== null) {
        $.ajax(
            {
                type: "POST",
                url: "sendsurveys/" + currentSurveyId ,
                data: {users : selectedGroupsUsers},
                success: function(msg){
                  doneIt("Surveys are now available for the selected groups");
                },
                error: function(result, textStatus, error) {
                       if(!Utils.redirectIfUnauthorized(result, textStatus, error) ) {
                           alert("Error with connection to server when sending surveys");
                           $('#buttonSendSurveyDone').click( function(){onStartSendingClicked();} );
                        }
                }
              });
                                                                                              }

                                                   }*/

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

    function groupCheckboxClicked(event) {
        currentGroup = event.data;
        //selectedGroups.push( currentGroup.groupName);
        $('#userListTable').empty();
        var getJSONQueryUsersPerGroup = $.getJSON('listData/users', {'groupName': currentGroup.groupName, 'endIndex': 200, 'startIndex': 0}, function(i) {fillUserTable(i);} );
        getJSONQueryUsersPerGroup.error(Utils.redirectIfUnauthorized);

    }

        return {showUserList : function(i) {showUserList(i);}
    };
}();