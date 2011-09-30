/*
 * File encapsulates action related to Survey List view
 *
 **/


var UserManagement = function() {

    return {
        showUserManagement : function(){showUserManagement();},
        fillWithData : function(i, item){fillWithData(i, item);}
    };

    function showUserManagement (){

        var columnIds = ["executeSortByName", "executeSortByEmail", "executeSortByPhone", "executeSortByPermission"];
        var columnTexts = ["LOC_NAME", "LOC_EMAIL", "LOC_PHONE", "LOC_PERMISSION"];
        var columnDbFields = ["username","email", "phoneNumber", "userRoleCollection"];

        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "users", UserManagement);

        //todo plus button handling
        $('#sectionTitle').text('User Admin');
        $('#userManagement').text('Survey List');
        $('#userManagement').click(function() {SurveyList.showSurveyList() });

    }

    function fillWithData(i, item) {
        $('#dynamicListTable').append( '<tr id="dynamicRow'+ i + '">'
                                    + '<td>'+ item.username + '</td>'
                                + '<td>' + item.email + '</td>'
                                + '<td>' + item.phoneNumber + '</td>'
                                + '<td>' + item.userRoleCollection[0].ndgRole.roleName + '</td>'
                                + '</tr>' );
     }

}();