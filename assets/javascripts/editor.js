
var Editor = function() {

    var surveyId;
    var jsonSurvey;

    return {
        newSurvey : function() {newSurvey();},
        createEditor : function(id) {createEditor(id);},
        addCategory : function(id) {addCategory(id);},
        addQuestion : function(id) {addQuestion(id);}
    };

    function newSurvey(){
        $.getJSON('/application/createSurvey', {'surveyName': 'New survey'},// TODO localize
                                                    function(data){
                                                        surveyId = data.survey.id;
                                                        createEditor(surveyId);
                                                        addCategory();
                                                    } );
    }

    function createEditor(id){
        surveyId = id;

        $('#sectionTitle').text('Survey Editor'); //TODO localize
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
            + '<ul id="sortableList">'
            + '<div id="categories"></div>'
            + '</ul>'
            + '</div>'
            );

        fillEditor(surveyId);

        $( "#plusButton").unbind('mouseover');
        $( "#plusButton").mouseover( function(event) {SurveyListCombo.showEditorMenu(event);});
        $( "#executeBackButton" ).click( function(){onBackClicked();} );
        $( "#executeSave" ).click( function(){onSaveClicked();} );
    }

    function onSaveClicked(){
        $.ajax(
        {
            type: "POST",
            url: "/saveSurvey",
            data: {surveyData : prepereSurveyJSON(), id : surveyId},
            success: function(msg){
               alert( "Success");
            },
            error: function(request,error) {
                alert("Failure" + error);
            }
        });
    }

    function prepereSurveyJSON(){
        var categoryList = [];
        var catElemList = $( "#categories" ).find(".listCategory");

        $.each(catElemList, function(i, item){
            var catName = $( '#' + item.id ).find('.categoryName');
            categoryList[i] = new Category(catName[0].text, i);
            categoryList[i].questionCollection = prepereQuestions(item);
        });


        return JSON.stringify(categoryList);
    }

    function prepereQuestions(catItem){
        var questionElemList = $( '#' + catItem.id ).find(".listItemQuestion");
        var questionList = [];
        $.each( questionElemList, function( i, item ){
            var questionLabel = $( '#' + item.id ).find('.questionText');
            questionList[i] = new Question(questionLabel[0].innerText, item.id);
        } );
        return questionList;
    }

    function onBackClicked(){
         $('#content').empty();
         SurveyList.showSurveyList();
    }

    function addCategory(){
        var numRand = Math.floor(Math.random()*10000); //TODO maybe exist better way to get rundom id
        appendCategoryElement(parseInt(numRand),'New Category'); //TODO localize
        setAccordion();
    }

    function addQuestion(){
        var children = $( "#categories" ).find(".listCategory");

        $( "#combobox" ).empty();

        $.each(children, function(i, item){
            var catName = $( '#' + item.id ).find('.categoryName');
            var listId = $( '#' + item.id ).find('.listQuestion');
            $( "#combobox" ).append('<option value="'+ listId[0].id +'">' + catName[0].text + '</option>'); //TODO label
        });

        $('#confirmCategoryButton').empty();
        $('#confirmCategoryButton').append('<button id="confirmCategory">Confirm</button>');
        $('#confirmCategory').click( function(){onConfirmCategory();} );
        switchCategoryDialog.dialog("open");
    }

    function onConfirmCategory(){
        var selectElem = $( "#combobox" ).val();
        var numRand = Math.floor(Math.random()*10000); //TODO maybe exist better way to get rundom id
        appendQuestionElement(numRand, "New question", selectElem); //TODO localize

        switchCategoryDialog.dialog("close");
    }

    function fillEditor(id){
        $.getJSON('/listData/categories', {'surveyId': parseInt(id)},
                                                    function(data){
                                                        jsonSurvey = data;
                                                        fillCategoryList(data);
                                                    } );
    }

    function setAccordion(){
        $( "#categories" )
                .accordion("destroy");

        $( "#categories" )
                .accordion({
                        header: "h3",
                        collapsible: true,
                        active: false,
                        autoHeight: false,
                        icons: false
                })
                .sortable().disableSelection();

        $( ".listCategory h3" ).droppable({
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

    function fillCategoryList(data){
        $.each(data.categories,function(i,item) {
            appendCategoryElement(item.id, item.label);
            fillQuestions(item.questionCollection, item.id );
        });
        setAccordion();
    }

    function appendCategoryElement(id, value){
        var categoryId = "category" + id;
        var deleteId = "executeDeleteCategory" + id;
        var listId = 'questions' + id;



        $("#categories").append(
            '<li id="'+ categoryId + '" class="ui-state-default listCategory">'
            +'<h3><a href="#" class="categoryName">' + value + '</a>'
            +'<span id="'+ deleteId +'" class="deleteElement" title="' + LOC.get('LOC_DELETE') + '"/>'
            +'<div style="clear:both;"></div>'
            + '</h3>'
            +'<ul id="' + listId +'" class="listQuestion"></ul>'
            +'</li>');

        $( '#' + deleteId).click( categoryId, function(i){onDeleteElementClicked(i);} );

        $( '#' + listId ).sortable({
            connectWith: + ".listQuestion"
        }).disableSelection();
    }

    function onDeleteElementClicked(itemId){
        $('#' + itemId.data).empty().remove();
    }

    function fillQuestions(data, categoryId ){
        var listId = 'questions' + categoryId;
        $.each(data, function(i,item) {
            appendQuestionElement(item.id, item.label, listId);
        });
    }

    function appendQuestionElement(id, value, listId){
        var deleteId = 'executeDeleteQuestion' + id;
        var questionId = 'question' + id;
        $("#" + listId).append(
                      '<li id="' + questionId +'"class="ui-state-default listItemQuestion"><div>'
                    + '<div class="questionText"> <span> ' + value + '</span></div>'
                    + '<div class="deleteElement">'
                    + '<span id="'+ deleteId +'" class="deleteElement" title="' + LOC.get('LOC_DELETE') + '"/>'
                    + '</div>'
                    + '<div style="clear:both;"></div>'
                    + '</li>');

        $('#'+ deleteId).click( questionId, function(i){onDeleteElementClicked(i);} );
    }
}();
