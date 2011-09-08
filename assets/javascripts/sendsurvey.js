

var SendSurvey = function() {

    var currentSurveyId = "";

    function showUserList(i) {
        currentSurveyId = i.data;
        $('#sendSurveyUsers').empty();
        $('#sendSurveyUsers').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultId"><b>' + "Username" + '</b></th>'
                               + '<th scope="col"><a href="#" id="executeSortByResultTitle"><b>' + "phone number" + '</b></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="userListTable">'
                               + '</tbody>' );

        $.getJSON('/application/listUsers', {'surveyId': parseInt(i.data),
                                               'startIndex': 0,
                                               'isAscending': true,
                                              'orderBy': "title"},
                                               function(i) {fillUserTable(i);} );

    }

    function fillUserTable(data) {
        $('#surveyListTable').empty();
        $.each(data.users,function(i,item) {
            fillWithResults(i,item);
        });
    }

    function fillWithResults(i, item) {
        $('#userListTable').append( '<tr>'
                                    + '<td><input type="checkbox" id="userCheckbox' + item.id + '"/></td>'
                                    + '<td>'+ item.username + '</td>'
                                    + '<td>' + item.phoneNumber + '</td>'
                                     + '</tr>' );
    }

        return {showUserList : function(i) {showUserList(i);}
    };



}();