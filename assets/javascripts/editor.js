
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
        
            + '<a href="#" id="executeAddButton">'
            + '<img id="add_button" src="images/btn_plus.jpg"></a>'
        
            + '<ul id="sortableList">'
            + '<div id="categories">'
            + '</div>'
            + '</ul>'  
        
            + '</div>'
            );
    
        fillEditor(id.data);
  
        $( "#categories" ).sortable();
        $( "#categories" ).disableSelection();
        
//       $( "#categories" ).accordion();
        
        $( "#executeBackButton" ).click( function(){onBackClicked();} );
        $( "#executeAddButton" ).click( function(){onAddClicked();} );
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
    
    function onAddClicked(){
        appendCategoryElement('n','New Category'); //TODO 
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
        $('#categories').append('<li id="'+ categoryId + '" class="ui-state-default">'
            + '<h3><div><a href="#">' + value + '</a>'
            + '<a href="#" id="'+ deleteId +'"><img class="deleteCategory" src="images/btn_editor_delete.jpg" title="' + LOC.get('LOC_DELETE') + '"/></a>'
            +'<div style="clear:both;"></div></div></h3>'
            + '</li>');
        
        $('#'+ deleteId).click( categoryId, function(i){onDeleteCategoryClicked(i);} );
    }
    
    function onDeleteCategoryClicked(categoryId){
//        var cat = '#' + categoryId.data
        $('#' + categoryId.data).empty().remove();
//        $('#category1').remove();
    }
    
    function fillQuestions(categoryId){
        $.getJSON('/application/questions', {'categoryId': parseInt(categoryId)},
                                                    function(data){fillQuestionList(data, categoryId);} );
    }
    
    function fillQuestionList(data, categoryId){
        
        var listId = 'questions' + categoryId;
        
        $("#category" + categoryId).append(
                    '<ul id="' + listId +'" class="question-list"></ul>'
                    );
                        
        $.each(data.questions,function(i,item) {
            appendQuestionElement(item, listId);
        });
        
        $( '#' + listId ).sortable({
            connectWith: '.' + "question-list"
        }).disableSelection();
    }
    
    function appendQuestionElement(item, listId){
        $("#" + listId).append(
                '<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>'+ item.label +'</li>');
    }
    
   

    
    
}();
