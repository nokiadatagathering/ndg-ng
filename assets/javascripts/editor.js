
var Editor = function() {

var surveyModel;
    var typeList;
    var currentSelectionId;

    return {
        openSurvey : function(id) {openSurvey(id);},
        createSurvey : function() {createSurvey();},
        addCategory : function(id) {addCategory(id);},
        addQuestion : function(id) {addQuestion(id);},
        setQlistConfig : function() {setQlistConfig();},
        removeQlistConfig: function() {removeQlistConfig();},
        setCatListConfig: function() {setCatListConfig();},
        removeCatListConfig: function() {removeCatListConfig();}
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

        $.getJSON( '/surveyManager/questionType', function( data ){
                                                   typeList = data.types;
                                                } );

        $('#userManagement').remove();
        prepareLayout();
        $('#content').append(
            '<div id="editor">'
            + '<div id="editorMain">'
            + '<ul class="categoryList" id="sortableList">'
            + '<div id="categories"></div>'
            + '</ul>'
            + '</div>'
        );

        new EditedLabel( $( "#sectionTitle" ), function( newTitle ){
                                                    surveyModel.updateSurveyTitle( newTitle );
                                                } );

        $( "#plusButton" ).unbind( 'mouseover' );
        $( "#plusButton" ).mouseover( function( event ) {SurveyListCombo.showEditorMenu( event );});
        $( "#editorBackButton" ).click( function(){onBackClicked();} );
        $( "#executeSave" ).click( function(){onSaveClicked();} );

        $( "#categories" ).sortable( {
                delay: 50,
                revert: true,
                forcePlaceholderSize: true,
                forceHelperSize : true,
                placeholder : 'categoryPlaceholder',
                stop: function( event, ui ) {
                    if( ui.item.hasClass( 'drag' ) ){
                        var newCategory = surveyModel.newCategory();
                        ui.item.replaceWith( createCategoryElement( newCategory ) );
                        setCategoryUiOptions( newCategory.uiId );
                    }
                }
        });
    }

    function prepareLayout() {
        $('#content').width( 865 );
        $('#leftcolumn').width( 80 );
        $('#plusButton').removeClass('plusButton');
        $('#plusButton').addClass('plusButton_layout3');
        $('#content').empty();
        $('#plusButton').before( '<div class="leftMenuButtonBlock" id="editorBackButton"/>');
        $('#leftColumnContent' ).empty();
    }

    function setCatListConfig(){
        $( "#categories" ).sortable( "option", "placeholder", 'categoryPlaceholder newCategory' );
    }

    function removeCatListConfig(){
        $( "#categories" ).sortable( "option", "placeholder", 'categoryPlaceholder' );
    }

    function setQlistConfig(){
        setHoverConfig();
        $( '.listQuestion' ).sortable( "option", "placeholder", 'questionPlaceholder newQuestion' );
    }

    function removeQlistConfig(){
        removeHoverConfig();
        $( '.listQuestion' ).sortable( "option", "placeholder", 'questionPlaceholder' );
    }

    function setHoverConfig(){
        $( '.listCategory' ).droppable({
            greedy: false,
            over: function(){
                var id = $( this ).attr('id');
                var t = setTimeout( function(){showCategory( id );}, 500 );//TODO change to 2000
                $( this ).data('timeout', t);
            },
            out: function(){
                clearTimeout( $( this ).data( 'timeout' ));
            }
        });
    }

    function removeHoverConfig(){
        $( '.listCategory' ).droppable( 'destroy' );
    }

    function onSaveClicked(){
        updateSurveyIndexes();
        $.ajax(
        {
            type: "POST",
            url: "/surveyManager/saveSurvey",
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
        onSaveClicked();
        restoreLayout()
        SurveyList.showSurveyList();
    }

    function restoreLayout() {
        $('#content').empty();
        $('#content').width( 820 );
        $('#leftcolumn').width( 125 );
        $('#plusButton').removeClass('plusButton_layout3');
        $('#plusButton').addClass('plusButton');
        $('#editorBackButton').detach();
    }

    function addCategory(){
        var category = surveyModel.newCategory();
        appendCategoryElement( createCategoryElement( category ), category );
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
        appendQuestionElement( createQuestionElement( question ), question, selectElem );

        showCategory( selectElem );
//        $( '#' + question.uiId ).trigger( 'click' );

//        switchCategoryDialog.dialog( "close" );
    }

    function fillEditor(id){
        $.getJSON('/surveyManager/getSurvey', {'surveyId': parseInt(id)}, function(data){
                                                                surveyModel = new SurveyModel(data.survey);
                                                                fillCategoryList();
                                                            } );
    }

    function fillCategoryList(){
        $( '#sectionTitle' ).text( surveyModel.getSurvey().title );
        $.each( surveyModel.getSurvey().categoryCollection, function( i, item ) {
            appendCategoryElement( createCategoryElement( item ), item );
            fillQuestions( item.questionCollection, item.uiId );
        });
    }

    function createCategoryElement( category ){

        if( !category.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO probably exist better way to get rundom id
            category.uiId = "category" + numRand;
        }else{
            category.uiId = "category" + category.id;
        }

        var categoryLabelId = category.uiId + 'label';
        //TODO localize
        var categoryElem = $('<li id="'+ category.uiId + '" class="ui-state-default listCategory">'
                + '<h3>'
                + '<span class="expandIcon"></span>'
                + '<span class="categoryName" id="' + categoryLabelId + '">' + category.label + '</span>'
                + '<div class="deleteElement" title="' + LOC.get( 'LOC_DELETE' ) + '"></div>'
                + '<div style="clear:both;"></div>'
                + '</h3>'
                + '<ul class="listQuestion"></ul>'
                + '</li>');

        return categoryElem;
    }

    function appendCategoryElement( categoryElem, category ){
        $( "#categories" ).append( categoryElem );
        setCategoryUiOptions( category.uiId );
    }

    function hideCategory( categoryId ){
        var expandIconElem = "#" + categoryId + ' span.expandIcon';
        $( '#' + categoryId + ' .listQuestion' ).hide( 'fast', function(){
            refreshLists();
        });
        $( expandIconElem ).removeClass( 'expanded' );
    }

    function showCategory( categoryId ){
        var expandIconElem = "#" + categoryId + ' span.expandIcon';
        $( '#' + categoryId + ' .listQuestion' ).show( 'fast', function(){
            refreshLists();
        });
        $( expandIconElem ).addClass( 'expanded' );
    }

    function refreshLists(){
        $( '.listQuestion' ).sortable( 'refresh' );
        $( "#categories" ).sortable( 'refresh' );
    }

    function setCategoryUiOptions( categoryId, bVal ){

        $( '#' + categoryId + ' .deleteElement').click( categoryId, function(i){onDeleteCategoryClicked(i);} );

        new EditedLabel( $( '#' + categoryId + 'label' ), function( newTitle ){
                                    surveyModel.updateCategory( categoryId, newTitle );
                                } );

        var listRef = '#' + categoryId + ' .listQuestion';
        var catHeaderRef = "#" + categoryId + " h3";
        var expandIconElem = "#" + categoryId + ' span.expandIcon';

        //allows sort question
        $( listRef ).sortable({
            revert: true,
            forcePlaceholderSize: true,
            forceHelperSize : true,
            connectWith: " .listQuestion",
            placeholder : 'questionPlaceholder',
            delay: 50,
            start : function(){
                setHoverConfig();
            },
            stop: function( event, ui ) {
                if( ui.item.hasClass( 'drag' ) ){
                    var newQuestion = surveyModel.newQuestion();
                    ui.item.replaceWith( createQuestionElement( newQuestion ));
                    setQuestionUiOptions( newQuestion );
                }
                removeHoverConfig();
            }
        }).disableSelection();

        //hide and show questions in category
        $( catHeaderRef ).bind( 'click' , function(){
            if($(expandIconElem).hasClass('expanded')){
                hideCategory( categoryId );
            }else{
                showCategory( categoryId );
            }
            return false;
        });

        hideCategory ( categoryId );
    }

    function fillQuestions( data, categoryId ){
        $.each(data, function(i,item) {
            var questioneElement = createQuestionElement( item, categoryId );
            appendQuestionElement(questioneElement, item, categoryId);
        });
    }

    function createQuestionElement( question ){

        if( !question.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
            question.uiId = "question" + numRand;
        }else{
            question.uiId = "question" + question.id;
        }

        var questionLabelId = question.uiId + 'label';
        var qElement = $(
                      '<li id="' + question.uiId +'" class="ui-state-default listItemQuestion"><div>'
                    + '<div class="questionText"> '
                    + '<span id="' + questionLabelId + '"> ' + question.label + '</span>'
                    + '</div>'

                    + '<div class="additionalButtons"></div>'
                    + ''

                    + '<div class="qIcon deleteQuestion" title="' + LOC.get('LOC_DELETE') + '"></div>'
                    + '<div class="qIcon duplicateQuestion"></div>'
                    + '<div class="qType"><span><select class="typeSelect"></select></span></div>'
                    + '<div style="clear:both;"></div>'
                    + '<div class="questionDetails"></div>'
                    + '</li>');

        return qElement;
    }

    function appendQuestionElement( questionElement, question, categoryId ){
        $( '#' + categoryId + ' ul.listQuestion' ).append( questionElement );
        setQuestionUiOptions( question );
    }

    function setQuestionUiOptions( question ){
        fillTypeCombo( question.uiId );

        $( '#'+ question.uiId + ' .typeSelect' ).val( question.questionType.id );
        $( '#'+ question.uiId + ' .typeSelect' ).bind( 'change', question.uiId, function( i ){onQuestionTypeChanged( i );} );


        $( '#'+ question.uiId + ' .deleteQuestion' ).bind( 'click', question.uiId, function( i ){onDeleteQuestionClicked ( i );} );
        $( '#'+ question.uiId + ' .duplicateQuestion' ).bind( 'click', question.uiId, function( i ){onDuplicateQuestionClicked ( i );} );
        $( '#'+ question.uiId ).bind( 'click', question.uiId, function( i ){onQuestionClicked( i );} );

        new EditedLabel( $( "#" + question.uiId + 'label' ), function( newTitle ){
                                                    surveyModel.updateQuestionTitle( question.uiId, newTitle );
                                                } );
        hideQuestion( question.uiId );

        var uiDetails = new QuestionUiElement( question );
        uiDetails.init();
    }

    function hideQuestion( qId ){
         $( '#'+ qId + ' .questionDetails' ).hide();
//         $( '#'+ qId + ' .qType' ).hide();
         $( '#'+ qId + ' .additionalButtons' ).hide();
    }


    function showQuestion( qId ){
         $( '#'+ qId + ' .questionDetails' ).show();
//         $( '#'+ qId + ' .qType' ).show();
         $( '#'+ qId + ' .additionalButtons' ).show();
    }

    function fillTypeCombo( qId ){

        $.each( typeList, function( idx, type ){
            $( '#'+ qId + ' .typeSelect' ).append( '<option value="'+ type.id +'">' + type.typeName + '</option>' );
        });
    }

    function onQuestionTypeChanged( event ){
        surveyModel.updateQuestionType( event.data , $( '#'+ event.data + ' .typeSelect' ).val() );
    }

    function onDeleteCategoryClicked( event ){
        $( '#' + event.data ).empty().remove();
        surveyModel.deleteCategory( event.data );
    }

    function onDeleteQuestionClicked( event ){
        $( '#' + event.data ).empty().remove();
        surveyModel.deleteQuestion( event.data );
    }

    function onDuplicateQuestionClicked ( event ){
        var category = surveyModel.getCategoryForQuestion( event.data );
        var newQuestion = surveyModel.duplicateQuestion( event.data, category );
        appendQuestionElement(  createQuestionElement( newQuestion ),
                                newQuestion,
                                category.uiId );

    }

    function onQuestionClicked( event ){

        hideQuestion( currentSelectionId );
        currentSelectionId = event.data;

        showQuestion( currentSelectionId );
    }
}();
