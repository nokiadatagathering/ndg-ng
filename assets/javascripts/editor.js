
var Editor = function() {

    var surveyId;

    return {
        createEditor : function(id) {
            createEditor(id);
        }
    };

    function createEditor(id){
        surveyId = id.data;
        $('#content').empty();
        $('#content').append(

            '<div id="editor">'
            + '<a href="#" id="executeBackButton">'
            + '<img id="back_button" src="images/btn_back.jpg"></a>'

            + '<a href="#" id="executeAddCategory">'
            + '<img id="add_button" src="images/btn_plus.jpg"></a>'

            + '<a href="#" id="executeAddQuestion">'
            + '<img id="add_button" src="images/btn_plus.jpg"></a>'

            + '<a href="#" id="executeSave">'
            + '<img id="save_button" src="images/check1.png"></a>'

            + '<ul id="sortableList">'
            + '<div id="categories"></div>'
            + '</ul>'

            + '</div>'
            );

        fillEditor(surveyId);

        $( "#categories" ).sortable();
        $( "#categories" ).disableSelection();
//       $( "#sortableList" ).accordion();

        $( "#executeBackButton" ).click( function(){onBackClicked();} );
        $( "#executeAddCategory" ).click( function(){onAddCategoryClicked();} );
        $( "#executeAddQuestion" ).click( function(){onAddQuestionClicked();} );
        $( "#executeSave" ).click( function(){onSaveClicked();} );
    }

    function onSaveClicked(){
//        prepereSurveyJSON();
        $.ajax(
        {
            type: "POST",
            url: "/saveSurvey",
            data: {surveyData : prepereSurveyJSON(), id : surveyId}, //TODO
            success: function(msg){
               alert( "Success");
            },
            error: function(request,error) {
                alert("Failure" + error);
            }
        });
    }

    function prepereSurveyJSON(){
//        var category1 = new Category("test", 2);
//        var jsonCategory = JSON.stringify(category1);


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
         $('#content').append(
         '<div id="datatable">'
                +'<table id="minimalist">'
                +'</table>'
            +'</div>');//TODO refactor this

        SurveyList.showSurveyList();
    }

    function onAddCategoryClicked(){
        var numRand = Math.floor(Math.random()*10000); //TODO maybe exist better way to get rundom id
        appendCategoryElement(parseInt(numRand),'New Category'); //TODO localize
    }

    function onAddQuestionClicked(){
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
        $.getJSON('/application/categoryList', {'surveyId': parseInt(id)},
                                                    function(data){fillCategoryList(data);} );
    }

    function fillCategoryList(data){
        $.each(data.categories,function(i,item) {
            appendCategoryElement(item.id, item.label);
            fillQuestions(item.id);
        });
    }

    function appendCategoryElement(id, value){
        var categoryId = "category" + id;
        var deleteId = "executeDeleteCategory" + id;
        var listId = 'questions' + id;

        $('#categories').append('<li id="'+ categoryId + '" class="ui-state-default listCategory">'
            +'<div>'
            +'<a id="test" href="#" class="categoryName">' + value + '</a>'
            +'<a href="#" id="'+ deleteId +'"><img class="deleteElement" src="images/btn_editor_delete.jpg" title="' + LOC.get('LOC_DELETE') + '"/></a>'
            +'<div style="clear:both;"></div>'
            + '<ul id="' + listId +'" class="listQuestion"></ul>'
            +'</div>'
            +'</li>');

        $('#'+ deleteId).click( categoryId, function(i){onDeleteElementClicked(i);} );

        $( '#' + listId ).sortable({
            connectWith: '.' + "listQuestion"
        }).disableSelection();
    }

    function onDeleteElementClicked(itemId){
        $('#' + itemId.data).empty().remove();
    }

    function fillQuestions(categoryId){
        var listId = 'questions' + categoryId;
        $.getJSON('/application/questions', {'categoryId': parseInt(categoryId)},
                                                    function(data){fillQuestionList(data, listId);} );
    }

    function fillQuestionList(data, listId){
        $.each(data.questions,function(i,item) {
            appendQuestionElement(item.id, item.label, listId);
        });
    }

    function appendQuestionElement(id, value, listId){
        var deleteId = 'executeDeleteQuestion' + id;
        var questionId = 'question' + id;
        $("#" + listId).append(
                '<li id="' + questionId +'"class="ui-state-default listItemQuestion">'
//                    + '<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>'
                    + '<div class="questionText">' + value
                    + '<a href="#" id="'+ deleteId +'"><img class="deleteElement" src="images/btn_editor_delete.jpg" title="' + LOC.get('LOC_DELETE') + '"/></a>'
                    +'<div style="clear:both;"></div></div></li>');

        $('#'+ deleteId).click( questionId, function(i){onDeleteElementClicked(i);} );
    }
}();
