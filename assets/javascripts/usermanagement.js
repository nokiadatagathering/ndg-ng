/*
 * File encapsulates action related to Survey List view
 *
 **/


var UserManagement = function() {
    var userStartIndex = 0;
    var usersNameSortAscending = true;
    var emailSortAscending = true;
    var phoneSortAscending = true;
    var permissionSortAscending = true;
    var lastSortByColumn = "id";

    return {
        showUserManagement : function(){showUserManagement();}
    };

    function showUserManagement (){
        $('#minimalist').empty();
        $('#plusButton').unbind('mouseover');
        //todo plus button handling
        $('#sectionTitle').text('User Admin');
        $('#userManagement').unbind('click');
        $('#userManagement').text('Survey List');
        $('#userManagement').click(function() {SurveyList.showSurveyList() });
        $('#leftColumnContent' ).empty();

        $('#minimalist').append( '<thead>'
                               + '<tr>'
                               + '<th scope="col"><a href="#" id="executeSortByName">' + LOC.get('LOC_NAME') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByEmail">' + LOC.get('LOC_EMAIL') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByPhone">' + LOC.get('LOC_PHONE') + '</a></th>'
                               + '<th scope="col"><a href="#" id="executeSortByPermission">' + LOC.get('LOC_PERMISSION') + '</a></th>'
                               + '<th scope="col"></th>'
                               + '</tr>'
                               + '</thead>'
                               + '<tbody id="userListTable">'
                               + '</tbody>');

        $("#executeSortByName").click( function(){toggleSortByName();} );
        $("#executeSortByEmail").click( function(){toggleSortByEmail();} );
        $("#executeSortByPhone").click( function(){toggleSortByPhone();} );
        $("#executeSortByPermission").click( function(){toggleSortByPermission();} );
        $.getJSON('/application/listuserscount', function(data){updateTotalPages(data);});
        fillUserData( lastSortByColumn, usersNameSortAscending );
    }

    function updateTotalPages(data){
        totalPages = data.usersCount;
        updatePageNumber();
    }

    function updatePageNumber() {
        $("#pageIndexText").empty();
        $("#pageIndexText").append( '<small>' + (userStartIndex + 1) + '</small>' + '<strong> of ' + totalPages + '</strong>' );
        $("#buttonPrevious").click( function(){onPreviousClicked();} );
        $("#buttonNext").click( function(){onNextClicked();} );
    }

    function fillUserData( orderByColumn, isAscending ) {
        lastSortByColumn = orderByColumn;
        $.getJSON('/application/listusers', {
            'startIndex': userStartIndex,
            'isAscending': isAscending,
            'orderBy': orderByColumn },  function(data){
                refreshTable(data);
            });
    }


    function refreshTable(data) {
        $('#userListTable').empty();
        $.each(data.users,function(i,item) {
            fillWithData(i,item);
        });
    }

    function toggleSortByName() {
        resetColumnTitle();
        if ( usersNameSortAscending ) {
            usersNameSortAscending = false;
            $('#executeSortByName').text(LOC.get('LOC_NAME')+ CONST.get('DESC'));
        } else {
            usersNameSortAscending = true;
            $('#executeSortByName').text(LOC.get('LOC_NAME') + CONST.get('ASC'));
        }
        fillUserData( 'username', usersNameSortAscending );
    }
    function toggleSortByEmail() {
        resetColumnTitle();
        if ( emailSortAscending ) {
            emailSortAscending = false;
            $('#executeSortByEmail').text(LOC.get('LOC_EMAIL') + CONST.get('DESC'));
        } else {
            emailSortAscending = true;
            $('#executeSortByEmail').text(LOC.get('LOC_EMAIL') + CONST.get('ASC'));
        }
        fillUserData( 'email', emailSortAscending );
    }

    function toggleSortByPhone() {
        resetColumnTitle();
        if ( phoneSortAscending ) {
            phoneSortAscending = false;
            $('#executeSortByPhone' ).text(LOC.get('LOC_PHONE') + CONST.get('DESC'));
        } else {
            phoneSortAscending = true;
            $('#executeSortByPhone' ).text(LOC.get('LOC_PHONE') + CONST.get('ASC'));
        }
        fillUserData( 'phoneNumber', phoneSortAscending );
    }

    function toggleSortByPermission() {
        resetColumnTitle();
        if ( permissionSortAscending ) {
            permissionSortAscending = false;
            $('#executeSortByPermission' ).text(LOC.get('LOC_PERMISSION') + CONST.get('DESC'));
        } else {
            permissionSortAscending = true;
            $('#executeSortByPermission' ).text(LOC.get('LOC_PERMISSION') + CONST.get('ASC'));
        }
        fillUserData( 'userRoleCollection', permissionSortAscending );
    }

    function resetColumnTitle() {
        $('#executeSortByName').text(LOC.get('LOC_NAME'));
        $('#executeSortByEmail').text(LOC.get('LOC_EMAIL'));
        $('#executeSortByPhone').text(LOC.get('LOC_PHONE'));
        $('#executeSortByPermission').text(LOC.get('LOC_PERMISSION'));
    }

    function fillWithData(i, item) {
        $('#userListTable').append( '<tr id="User'+ item.id + '">'
                                    + '<td>'+ item.username + '</td>'
                                + '<td>' + item.email + '</td>'
                                + '<td>' + item.phoneNumber + '</td>'
                                + '<td>' + item.userRoleCollection[0].ndgRole.roleName + '</td>'
                                + '</tr>' );
    }

    function onPreviousClicked(e) {
        if ( --userStartIndex < 0 ) {
            userStartIndex = 0;
        } else {
            fillUserData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

    function onNextClicked(e) {
        if( ++userStartIndex >= totalPages ) {
            userStartIndex--;
        } else {
            fillUserData( lastSortByColumn, lastSortAscending );
            updatePageNumber();
        }
        e.preventDefault();
    }

     function onMouseOverNext(){
        this.bgColor='#dbe4f1';
        document.getElementById('next_button').src='images/icon_next_on.png';
    }

    function onMouseOutNext(){
        this.bgColor='#edf0f6';
        document.getElementById('next_button').src='images/icon_next_off.png';
    }

    function onMouseOverPrevious(){
        this.bgColor='#dbe4f1';
        document.getElementById('prev_button').src='images/icon_previous_on.png';
    }

    function onMouseOutPrevious(){
        this.bgColor='#edf0f6';
        document.getElementById('prev_button').src='images/icon_previous_off.png';
    }
}();