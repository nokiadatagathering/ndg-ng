
var Editor = function() {

    return {
        createEditor : function(id) {
            createEditor(id);
        }
    };

    function createEditor(id){
        $('#content').empty();
        $('#content').append(

            '<div id="editor">'
            + '<a href="#" id="executeBackButton">'
            + '<img id="back_button" src="images/btn_back.jpg"></a>'

            + '<a href="#" id="executeAddCategory">'
            + '<img id="add_button" src="images/btn_plus.jpg"></a>'

            + '<a href="#" id="executeAddQuestion">'
            + '<img id="add_button" src="images/btn_plus.jpg"></a>'

            + '<ul id="sortableList">'
            + '<div id="categories"></div>'
            + '</ul>'

            + '</div>'
            );

        fillEditor(id.data);

        $( "#categories" ).sortable();
        $( "#categories" ).disableSelection();
//       $( "#categories" ).accordion();

        $( "#executeBackButton" ).click( function(){onBackClicked();} );
        $( "#executeAddCategory" ).click( function(){onAddCategoryClicked();} );
        $( "#executeAddQuestion" ).click( function(){onAddQuestionClicked();} );
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
                    + '<div>' + value
                    + '<a href="#" id="'+ deleteId +'"><img class="deleteElement" src="images/btn_editor_delete.jpg" title="' + LOC.get('LOC_DELETE') + '"/></a>'
                    +'<div style="clear:both;"></div></div></li>');

        $('#'+ deleteId).click( questionId, function(i){onDeleteElementClicked(i);} );
    }
}();
