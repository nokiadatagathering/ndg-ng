/*
 * File encapsulates action related to Survey List view
 *
 **/


var UserManagement = function() {

    var searchLabels;
    var searchIds;
    var searchDbFields = ["username", "email", "phoneNumber"];
    var searchBy = "username";

    return {
        showUserManagement : function(){showUserManagement();},
        fillWithData : function(i, item){fillWithData(i, item);},
        searchFieldChange: function(event){searchFieldChange(event);},
        getSearchBy: function() {return searchBy;}
    };

    function showUserManagement (){

        var columnIds = ["executeSortByUsername", "executeSortByName", "executeSortByEmail", "executeSortByPhone", "executeSortByPermission"];
        var columnTexts = ["LOC_USERNAME", "LOC_NAME", "LOC_EMAIL", "LOC_PHONE", "LOC_PERMISSION"];
        var columnDbFields = ["username", "firstName", "email", "phoneNumber", "userRoleCollection"];

        DynamicTable.showList(columnIds, columnTexts, columnDbFields, "users", UserManagement);

        //todo plus button handling
        $('#sectionTitle').text('User Admin');
        $('#userManagement').text('Survey List');
        $('#userManagement').click(function() {SurveyList.showSurveyList();});

        $('#searchComboBox').click( function(event) {createSearchList(event);});
        $('#searchComboText').text(LOC.get("LOC_NAME"));

    }

    function createSearchList(event) {
       searchLabels = [LOC.get("LOC_NAME"), LOC.get("LOC_EMAIL"), LOC.get("LOC_PHONE")];
       searchIds = ["searchByName", "searchByEmail", "searchByPhone"];
       SurveyListCombo.showSearchMenu(event, searchLabels, searchIds, UserManagement);
    }

    function fillWithData(i, item) {
        $('#dynamicListTable').append( '<tr id="dynamicRow'+ i + '">'
                                    + '<td>'+ item.username + '</td>'
                                +  '<td>'+ item.firstName + '</td>'
                                + '<td>' + item.email + '</td>'
                                + '<td>' + item.phoneNumber + '</td>'
                                + '<td>' + item.userRoleCollection[0].ndgRole.roleName + '</td>'
                                + '<td class="menubar users" id="menu' + i + '" >'
                                + '<span title="' + LOC.get('LOC_DELETE') + '"class="buttonDelete" id="buttonDelete" unselectable="on"></span>'
                                + '</td>'
                                + '</tr>' );
        $('#menu' + i +' #buttonDelete').click( {index: i, id: item.id}, function(event){onDeleteUserClicked(event);} );
     }
     
     function onDeleteUserClicked(event) {
         var currentRow = $('#dynamicRow' + event.data.index);
         var rect = new Object();
         rect.top = currentRow.position().top;
         rect.left = currentRow.position().left;
         rect.width = currentRow.width();
         rect.height = currentRow.height();
         ConfirmCover.show(rect, event.data.id);
     }

    function searchFieldChange(event) {
        var fieldId = event.currentTarget.id;
        var nameIndex = jQuery.inArray( fieldId, searchIds );
        $('#searchComboText').text(searchLabels[nameIndex]);
        searchBy = searchDbFields[nameIndex];
        $('#searchTextField').val("");
    }

}();