
var Editor = function() {

    var surveyId;
    var surveyModel;

    var currentSelectionId;
    var questionPanel;

    return {
        openSurvey : function(id) {openSurvey(id);},
        createSurvey : function() {createSurvey();},
        addCategory : function(id) {addCategory(id);},
        addQuestion : function(id) {addQuestion(id);}
    };

    function createSurvey(){
        createEditor();
        surveyModel = new SurveyModel();
        surveyModel.createNewSurvey();

        fillCategoryList();
    }

    function openSurvey( id ){
        createEditor();
        fillEditor( id );
    }


    function createEditor(){

        $('#sectionTitle').text( 'Survey Editor' ); //TODO localize
        $('#userManagement').remove();

        $('#content').empty();

        $('#leftColumnContent' ).empty();
        $('#leftColumnContent' ).append(
             '<div class="leftMenuButtonBlock">'
            + '<span id="executeBackButton" class="leftMenuButton" />'
            + '</div>'
            + '<div class="leftMenuButtonBlock">'
            + '<span id="executeSave" class="leftMenuButton" />'
            + '</div>'
        );

        $('#content').append(
            '<div id="editor">'
            + '<h3 id="surveyHeader"><span id="surveyTitle"></span><span class="edit">Edit</span></h3>'
            + '<div id="editorMain">'
            + '<ul id="sortableList">'
            + '<div id="categories"></div>'
            + '</ul>'
            + '</div>'
            + '<div id="editorRight">'
            + '</div>'
            +'<div style="clear:both;"></div>'
            + '</div>'
            );


        $( "#plusButton").unbind('mouseover');
        $( "#plusButton").mouseover( function(event) {SurveyListCombo.showEditorMenu(event);});
        $( "#executeBackButton" ).click( function(){onBackClicked();} );
        $( "#executeSave" ).click( function(){onSaveClicked();} );

        questionPanel = new QuestionPanel( function(label){refreshCurrentQuestionLabel(label);});
        questionPanel.createQuestionPanel();

        $( "#categories" ).sortable( {delay: 50} );
        $( "#surveyHeader span.edit" ).bind( 'click.edit', function(){onEditTitleClick();} );
    }

    function onEditTitleClick(){
        var oldTitle = $( "#surveyTitle" ).text();

        $( "#surveyTitle" ).replaceWith( '<input id="titleEdit" type="text"/>' );
        $( "#titleEdit" ).val( oldTitle );

        $( "#surveyHeader span.edit" ).text( "Save" );
        $( "#surveyHeader span.edit" ).unbind( 'click.edit' );
        $( "#surveyHeader span.edit" ).bind( 'click.save', function(e){onTitleSaveClicked(e);} );
    }

    function onTitleSaveClicked(){
        var editedVal = $( "#titleEdit" ).val();

        $( "#titleEdit" ).replaceWith( '<span id="surveyTitle">' + editedVal + '</span>' );
        $( "#surveyHeader span.edit" ).text( "Edit" ); //TODO localize
        $( "#surveyHeader span.edit" ).unbind( 'click.save' );

        $( "#surveyHeader span.edit" ).bind( 'click.edit', function(){onEditTitleClick();} );
        surveyModel.updateSurveyTitle( editedVal );

    }

    function onSaveClicked(){
        updateSurveyIndexes();
        $.ajax(
        {
            type: "POST",
            url: "/saveSurvey",
            data: {surveyData : surveyModel.getSurveyString()},
            success: function( msg ){
               refresh( msg );
//               alert( "Survey saved successfully "); //TODo localize
            },
            error: function( request, error ) {
                alert("Problem with saving survey. Original error: " + error);
            }
        });
    }


    function refresh( id ){
        openSurvey( id );
    }

    function updateSurveyIndexes(){
        var catOrder = $( "#categories" ).sortable('toArray');
        $.each(catOrder, function(i, catId){
            var queOrder = $( '#' + catId + ' .listQuestion').sortable('toArray');
            surveyModel.reorderQuestion( queOrder, catId );
        });

        surveyModel.reorderCategory( catOrder );
    }


    function onBackClicked(){
         $('#content').empty();
         SurveyList.showSurveyList();
    }

    function addCategory(){
        var category = surveyModel.newCategory();
        appendCategoryElement( category );
    }

    function addQuestion(){
        var children = $( "#categories" ).find( ".listCategory" );
        $( "#combobox" ).empty();

        $.each(children, function(i, item){
            var categoryId = item.id;
            var catName = $( '#' + item.id + ' span.categoryName').text();

            $( "#combobox" ).append('<option value="'+ categoryId +'">' + catName + '</option>');
        });

        $( '#confirmCategoryButton' ).empty();
        $( '#confirmCategoryButton' ).append( '<button id="confirmCategory">Confirm</button>' );
        $( '#confirmCategory').click( function(){onConfirmCategory();} );
        switchCategoryDialog.dialog( "open" );
    }

    function onConfirmCategory(){
        var selectElem = $( "#combobox" ).val();

        var question = surveyModel.newQuestion( selectElem );
        appendQuestionElement( question, selectElem );

        showCategory( selectElem );
        $( '#' + question.uiId ).trigger( 'click' );

        switchCategoryDialog.dialog( "close" );
    }

    function fillEditor(id){
        $.getJSON('/application/getSurvey', {'surveyId': parseInt(id)},
                                                    function(data){
                                                        surveyModel = new SurveyModel(data.survey);
                                                        fillCategoryList();
                                                    } );
    }

    function fillCategoryList(){
        $( '#surveyTitle' ).text( surveyModel.getSurvey().title );
        $.each( surveyModel.getSurvey().categoryCollection, function( i, item ) {
            appendCategoryElement( item );
            fillQuestions( item.questionCollection, item.uiId );
        });
    }

    function appendCategoryElement( category ){

        if( !category.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
            category.uiId = "category" + numRand;
        }else{
            category.uiId = "category" + category.id;
        }

        //TODO localize
        $("#categories").append(
             '<li id="'+ category.uiId + '" class="ui-state-default listCategory">'
            + '<h3>'
            + '<span class="expandIcon"></span>'
            + '<span class="categoryName">' + category.label + '</span><span class="edit">Edit</span>'
            + '<span class="deleteElement" title="' + LOC.get('LOC_DELETE') + '"/>'
            + '<div style="clear:both;"></div>'
            + '</h3>'
            + '<ul class="listQuestion"></ul>'
            + '</li>');

        $( '#' + category.uiId + ' span.deleteElement').click( category.uiId, function(i){onDeleteCategoryClicked(i);} );

        setListParams( category.uiId );
        hideCatogry ( category.uiId );
    }

    function hideCatogry( categoryId ){
        $( '#' + categoryId + ' .listQuestion' ).hide();
    }

    function showCategory( categoryId ){
        $( '#' + categoryId + ' .listQuestion' ).show();
    }

    function setListParams( categoryId ){
        var listRef = '#' + categoryId + ' .listQuestion';
        var catHeaderRef = "#" + categoryId + " h3";
        var expandIconElem = "#" + categoryId + ' span.expandIcon';

        $( "#" + categoryId + " span.edit" ).bind('click.edit',categoryId, function(e){onCategoryEditClicked(e);});

        //allows sort question
        $( listRef ).sortable({
            connectWith: " .listQuestion",
            delay: 50
        }).disableSelection();

        //hide and show questions in category
        $( catHeaderRef ).bind('click', function(){
            $( listRef ).toggle('fast');

            if($(expandIconElem).hasClass('expanded')){
                $(expandIconElem).removeClass('expanded');
            }else{
                $(expandIconElem).addClass('expanded');
            }
            return false;
        });

        //allows droping question on category
        $( catHeaderRef).droppable({
            accept: ".listQuestion li",
            drop: function( event, ui ) {
                var $item = $( this ).parent();
                var $list = $item.find( ".listQuestion" );

                ui.draggable.hide( "slow", function() {
                    $( this ).appendTo( $list ).show( "fast" );
                });
            }
        });
    }

    function onCategoryEditClicked(event){
        var categoryId = event.data;
        var catLabel = $("#" + categoryId + " span.categoryName").text();

        $( "#" + categoryId + " span.categoryName" ).replaceWith( '<input class="labelEdit" type="text"/>' );
        $( "#" + categoryId + " input.labelEdit" ).val( catLabel );

        $( "#" + categoryId + " span.edit" ).text( "Save" );
        $( "#" + categoryId + " span.edit" ).unbind( 'click.edit' );
        $( "#" + categoryId + " span.edit" ).bind( 'click.save', categoryId, function(e){onCategorySaveClicked(e);});

        $( "#" + categoryId + " h3").unbind('click');
    }

    function fillQuestions( data, categoryId ){
        $.each(data, function(i,item) {
            appendQuestionElement(item, categoryId);
        });
    }

    function appendQuestionElement( question, categoryId ){

        if( !question.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
            question.uiId = "question" + numRand;
        }else{
            question.uiId = "question" + question.id;
        }

        $( '#' + categoryId + ' ul.listQuestion' ).append(
                      '<li id="' + question.uiId +'" class="ui-state-default listItemQuestion"><div>'
                    + '<div class="questionText"> <span> ' + question.label + '</span></div>'
                    + '<div class="deleteElement">'
                    + '<span class="deleteElement" title="' + LOC.get('LOC_DELETE') + '"/>'
                    + '</div>'
                    + '<div style="clear:both;"></div>'
                    + '</li>');

        $( '#'+ question.uiId + ' span.deleteElement' ).click( question.uiId, function( i ){onDeleteQuestionClicked ( i );} );
        $( '#'+ question.uiId ).click( question.uiId, function( i ){onQuestionClicked(i);} );
    }

    function onCategorySaveClicked(event){
        var categoryId = event.data;
        var editedVal = $("#" + categoryId + " input.labelEdit").val();

        $( "#" + categoryId + " input.labelEdit").replaceWith( '<span class="categoryName">' + editedVal + '</span>' );
        $( "#" + categoryId + " span.edit" ).text( "Edit" ); //TODO localize
        $( "#" + categoryId + " span.edit" ).unbind('click.save');

        surveyModel.updateCategory( categoryId, editedVal ); ///.replace( "category", "" )

        setListParams(categoryId);
        event.stopPropagation();
    }

    function removePreviousSelction(){
        $( '#' + currentSelectionId ).removeClass( 'elementSelected' );
    }

    function onDeleteCategoryClicked( event ){
        $( '#' + event.data ).empty().remove();
        surveyModel.deleteCategory( event.data );
    }

    function onDeleteQuestionClicked( event ){
        $( '#' + event.data ).empty().remove();
        surveyModel.deleteQuestion( event.data );
    }

    function onQuestionClicked( event ){
        removePreviousSelction();
        currentSelectionId = event.data;

        $( '#' + currentSelectionId ).addClass( 'elementSelected' );

        var currenQuestion = surveyModel.getQuestion( currentSelectionId );
        questionPanel.fillRightPanel( currenQuestion );
    }

    function refreshCurrentQuestionLabel( label ){
        $( '#' + currentSelectionId + ' .questionText span' ).text( label );
    }

}();
