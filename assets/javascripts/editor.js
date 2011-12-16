
var Editor = function() {

    var surveyModel;
    var typeList;
    var optionsTypeListHtml = null;
    var currentSelectionId;

    return {
        openSurvey : function(id) {openSurvey(id);},
        createSurvey : function() {createSurvey();},
        updateContainerSize: function() {updateContainerSize();},
        setHoverConfig : function() {setHoverConfig();},
        removeHoverConfig : function() {removeHoverConfig();}
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
        $(window).unbind('scroll');
        $('#container').height('715px');
        var getJSONQuery = $.getJSON( 'surveyManager/questionType', function( data ){
                                                   typeList = data.types;
                                                   optionsTypeListHtml = "";
                                                   $.each( typeList, function( idx, type ){
                                                       optionsTypeListHtml += '<option value="'+ type.id +'">' + LOC.get(type.typeName) + '</option>';
                                                   });
                                                } );
        getJSONQuery.error(Utils.redirectIfUnauthorized);

        $('#userManagement').remove();
        restoreLayout();
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

        $('#plusButton').draggable({
            connectToSortable: "#categories",
            helper: 'clone',
            revert: 'invalid'
        });

        $( "#addQuestion" ).draggable({
            connectToSortable: " .listQuestion",
            helper: 'clone',
            revert: 'invalid',
            start: function () {setHoverConfig();},
            stop: function () {removeHoverConfig();}
        });


        $( "#editorBackButton" ).click( function(){
            onBackClicked();
        } );

        $( "#categories" ).sortable( {
                start: function(event, ui) {
                    if( ui.item.hasClass( 'drag' ) ){
                        ui.placeholder.append(
                            '<span class="placeholderText">' + LOC.get( 'LOC_DROP_CATEGORY' ) + '</span>')
                    }
                    updateContainerSize();
                    unbindToggleCategory();
                },
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
                    bindToggleCategory();
                }
        });
    }

    function updateContainerSize() {
        var totalHeight = $('#categories').height();
        var headerHeight = $('#header').height();

        if ( (totalHeight + headerHeight) < 715 ) {
            $('#container').height( 715 );
        } else if ( $('#container').height() < totalHeight + headerHeight ) {
            $('#container').height( totalHeight + headerHeight + 1 );
        }
    }

    function prepareLayout() {
        $('#content').width( 865 );
        $('#leftColumn').width( 80 );
        $('#plusButton').removeClass('plusButton');
        $('#plusButton').attr( 'title', LOC.get( 'LOC_DRAG_NEW_CATEGORY' ) );
        $('#plusButton').addClass('plusButton_layout3');
        $('#plusButton').unbind( 'click' );
        $('#content').empty();
        $('#plusButton').before( '<div class="editorButton" id="editorBackButton"/>');
        $('#plusButton').after( '<div class="editorButton addQuestionButton clickableElem" id="addQuestion"/>');
        $('#addQuestion').attr( 'title', LOC.get( 'LOC_DRAG_NEW_QUESTION' ) );
        $('#leftColumnContent' ).empty();

        $('#plusButton').addClass( 'drag' );
        $('#addQuestion').addClass( 'drag' );

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
            url: "surveyManager/saveSurvey",
            data: {surveyData : surveyModel.getSurveyString()},
            success: function( msg ){
                backToSurveyList();
            },
            error: function( request, status, errorThrown ) {
                if(!Utils.redirectIfUnauthorized(request, status, errorThrown))
                    {
                    alert("Problem with saving survey. Original error: " + status);
                    backToSurveyList();
                    }
            }
        });
    }

    function backToSurveyList(){
        confirmSaveSurveyDialog.dialog( "close" );
        restoreLayout()
        SurveyList.showSurveyList();
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

        $( '#buttonDeleteSurveyYes' ).unbind('click');
        $( '#buttonDeleteSurveyNo' ).unbind('click');
        $( '#buttonDeleteSurveyCancel' ).unbind('click');


        $( '#dialogConfirmSaveSurveyQuestion' ).text( LOC.get( 'LOC_SAVE_SURVEY' ) );
        $( '#buttonDeleteSurveyYes' ).text( LOC.get( 'LOC_YES' ) );
        $( '#buttonDeleteSurveyNo' ).text( LOC.get( 'LOC_NO' ) );
        $( '#buttonDeleteSurveyCancel' ).text( LOC.get( 'LOC_CANCEL' ) );

        confirmSaveSurveyDialog.dialog( {title: LOC.get( 'LOC_SAVESURVEY' )} );

        $( '#buttonDeleteSurveyYes' ).click(function(){onSaveClicked();});
        $( '#buttonDeleteSurveyNo' ).click(function(){backToSurveyList();});
        $( '#buttonDeleteSurveyCancel' ).click(function(){confirmSaveSurveyDialog.dialog("close");});

        confirmSaveSurveyDialog.dialog("open");
    }

    function restoreLayout() {
        $('#content').empty();
        $('#content').width( 820 );
        $('#leftColumn').width( 125 );
        $('#plusButton').removeClass('plusButton_layout3');
        $('#plusButton').removeClass('drag');
        $('#plusButton').addClass('plusButton');
        $('#editorBackButton').detach();
        $('#addQuestion').detach();
        scroll(0,0);
    }

    function addCategory(){
        var category = surveyModel.newCategory();
        appendCategoryElement( createCategoryElement( category ), category );
    }

    function fillEditor(id){
        var getJSONQuery = $.getJSON('surveyManager/getSurvey', {'surveyId': parseInt(id)}, function(data){
                                                                surveyModel = new SurveyModel(data.survey);
                                                                fillCategoryList();
                                                                surveyModel.addSkipLogic();
                                                            } );
       getJSONQuery.error(Utils.redirectIfUnauthorized);
    }

    function fillCategoryList(){
        $( '#sectionTitle' ).text( surveyModel.getSurvey().title );
        $.each( surveyModel.getSurvey().categoryCollection, function( i, item ) {
            appendCategoryElement( createCategoryElement( item ), item );
            fillQuestions( item.questionCollection, item.uiId );
            bindToggleQuestions();
        });
        updateContainerSize();
        bindToggleCategory(); // allows to show and hide questions in category
    }

    function createCategoryElement( category ){

        if( !category.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO probably exist better way to get random id
            category.uiId = "category" + numRand;
        }else{
            category.uiId = "category" + category.id;
        }

        var categoryLabelId = category.uiId + 'label';
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
            updateContainerSize();
        });
        $( expandIconElem ).removeClass( 'expanded' );
    }

    function showCategory( categoryId ){
        var expandIconElem = "#" + categoryId + ' span.expandIcon';
        $( '#' + categoryId + ' .listQuestion' ).show( 'fast', function(){
            updateContainerSize();
            $( '#' + categoryId + ' .listQuestion' ).sortable( 'refresh' );
        });
        $( expandIconElem ).addClass( 'expanded' );
    }

    function setCategoryUiOptions( categoryId, bVal ){

        $( '#' + categoryId + ' .deleteElement').click( categoryId, function(i){onDeleteCategoryClicked(i);} );

        new EditedLabel( $( '#' + categoryId + 'label' ),
                         function( newTitle ) {
                            surveyModel.updateCategory( categoryId, newTitle );
                         } );

        var listRef = '#' + categoryId + ' .listQuestion';
        var catHeaderRef = "#" + categoryId + " h3";

        //allows sort question
        $( listRef ).sortable({
            revert: true,
            forcePlaceholderSize: true,
            forceHelperSize : true,
            connectWith: " .listQuestion",
            placeholder : 'questionPlaceholder',
            delay: 50,
            start : function( event, ui ){
                if( ui.item.hasClass( 'drag' ) ){
                        ui.placeholder.append(
                        '<span class="placeholderText">' + LOC.get( 'LOC_DROP_QUESTION') + '</span>');
                        updateContainerSize();
                }
                setHoverConfig();
                unbindToggleQuestions();
            },
            stop: function( event, ui ) {
                if( ui.item.hasClass( 'drag' ) ){
                    var newQuestion = surveyModel.newQuestion();
                    ui.item.replaceWith( createQuestionElement( newQuestion ));
                    setQuestionUiOptions( newQuestion );
                }
                removeHoverConfig();
                updateContainerSize();
                bindToggleQuestions();
            }
        });//.disableSelection();

        hideCategory ( categoryId );
    }

    function bindToggleCategory(){
        unbindToggleCategory();
        $( ".listCategory h3" ).bind( 'click.showCategory' , function(){
            var catId = $(this).parents('.listCategory')[0].id;
            var expandIconElem = '#' + catId + ' span.expandIcon';
            if($(expandIconElem).hasClass('expanded')){
                hideCategory( catId );
            }else{
                showCategory( catId );
            }
            return false;
        });
    }

    function unbindToggleCategory(){
        $( ".listCategory h3" ).unbind( 'click.showCategory' );
    }

    function fillQuestions( data, categoryId ){
        $.each(data, function(i,item) {
            var questioneElement = createQuestionElement( item, categoryId );
            appendQuestionElement(questioneElement, item, categoryId);
        });
    }

    function createQuestionElement( question ){

        if( !question.hasOwnProperty( 'id' ) ){
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get random id
            question.uiId = "question" + numRand;
        }else{
            question.uiId = "question" + question.id;
        }

        var questionLabelId = question.uiId + 'label';
        var qElement = $(
                      '<li id="' + question.uiId +'" class="ui-state-default listItemQuestion"><div>'
                    + '<div>'
                    + '<div class="questionText">'
                    + '<span id="' + questionLabelId + '"> ' + question.label + '</span>'
                    + '</div>'
                    + '<div class="additionalButtons"></div>'
                    + ''

                    + '<div class="qIcon deleteQuestion" title="' + LOC.get('LOC_DELETE') + '"></div>'
                    + '<div class="qIcon duplicateQuestion"></div>'
                    + '<div class="qType typeComboboxSelection"><span><select style="width:125px;" class="typeSelect"></select></span></div>'
                    + '<div style="clear:both;"></div>'
                    + '</div>'
                    + '<div class="questionDetails"></div>'
                    + '</li>');

       qElement.droppable({
            accept: '.skipto',
            drop: function( event, ui ){
                  var dragQuestionId = $( $( ui.draggable ).parents( '.listItemQuestion' )[0] ).attr( 'id' );
                  var dropQuestionId = $( this ).attr( 'id' );
                  var dragCategoryId = $( $( ui.draggable ).parents( '.listCategory' )[0] ).attr( 'id' ); 
                    if( dragQuestionId == dropQuestionId ){
                        return;
                    }

                    var optionId = $( $( ui.draggable ).parents( '.optionInput' )[0] ).attr( 'id' );

                    surveyModel.setSkipQuestion( dragCategoryId, dragQuestionId, optionId, dropQuestionId );
            }
        });

        return qElement;
    }

    function appendQuestionElement( questionElement, question, categoryId ){
        $( '#' + categoryId + ' ul.listQuestion' ).append( questionElement );
        setQuestionUiOptions( question );
        bindToggleQuestions();
    }

    function setQuestionUiOptions( question ){
        fillTypeCombo( question.uiId );

        $( '#'+ question.uiId + ' .typeSelect' ).val( question.questionType.id );
        $( '#'+ question.uiId + ' .typeSelect' ).bind( 'change', question.uiId, function( i ){onQuestionTypeChanged( i );} );


        $( '#'+ question.uiId + ' .deleteQuestion' ).bind( 'click', question.uiId, function( i ){onDeleteQuestionClicked ( i );} );
        $( '#'+ question.uiId + ' .duplicateQuestion' ).bind( 'click', question.uiId, function( i ){onDuplicateQuestionClicked ( i );} );

        new EditedLabel( $( "#" + question.uiId + 'label' ), function( newTitle ){
                                                    surveyModel.updateQuestionTitle( question.uiId, newTitle );
                                                } );
        hideQuestion( question.uiId );

        var uiDetails = new QuestionUiElement( question );
        uiDetails.init();
    }

    function bindToggleQuestions(){
        unbindToggleQuestions();
        $( '.listItemQuestion' ).bind( 'click.toggleQuestion', function(){
            onQuestionClicked( this.id );
        });
    }

    function unbindToggleQuestions(){
        $( '.listItemQuestion' ).unbind( 'click.toggleQuestion' );
    }

    function hideQuestion( qId ){
         $( '#'+ qId + ' .questionDetails' ).hide();
         $( '#'+ qId + ' .additionalButtons' ).hide();
         $( '#'+ qId + ' .deleteQuestion' ).hide();
         $( '#'+ qId + ' .duplicateQuestion' ).hide();
         $( '#'+ qId + ' .typeComboboxSelection' ).hide();
    }

    function showQuestion( qId ){
         $( '#'+ qId + ' .questionDetails' ).show();
         $( '#'+ qId + ' .additionalButtons' ).show();
         $( '#'+ qId + ' .deleteQuestion' ).show();
         $( '#'+ qId + ' .duplicateQuestion' ).show();
         $( '#'+ qId + ' .typeComboboxSelection' ).show();
         updateContainerSize();
    }

    function fillTypeCombo( qId ){
        $( '#'+ qId + ' .typeSelect' ).append( optionsTypeListHtml );
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
        var catId = $( '#' + event.data ).parents( '.listCategory' )[0].id;
        var newQuestion = surveyModel.duplicateQuestion( event.data, catId );
        appendQuestionElement(  createQuestionElement( newQuestion ),
                                newQuestion,
                                catId );

    }

    function onQuestionClicked( questionId ){

        hideQuestion( currentSelectionId );
        currentSelectionId = questionId;

        showQuestion( currentSelectionId );
    }
}();
