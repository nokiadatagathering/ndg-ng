
var SurveyModel = function(s){
    var survey = s;

    //public methods
    this.getSurvey = function(){
        return survey;
    }

    this.getSurveyId = function(){
        return survey.id;
    }

    this.updateCategory = function ( catId, newLabel ){
        getCategory( catId).label = newLabel;
    }

    this.updateSurveyTitle = function ( newTitle ){
        survey.title = newTitle;
    }

    this.getQuestion = function( qId ){
        return getQue( qId );
    }

    this.createNewSurvey = function(){
        survey = new Survey();
        return survey;
    }

    this.newCategory = function (){
        var newCategory = new Category();
        survey.categoryCollection.push( newCategory );

        return newCategory;
    }

    this.newQuestion = function( categoryId ){
        var newQuestion = new Question();
        var category = getCategory( categoryId );

        category.questionCollection.push( newQuestion );
        return newQuestion;
    }

    this.getSurveyString = function (){
        //TODO reorginize question, set indexes
        return JSON.stringify( survey );
    }

    this.deleteCategory = function ( catId ){

        var cat = getCategory( catId );
        cat.isDelete = 'true';
    }

    this.deleteQuestion = function ( queId ){

        var que = getQue( queId );
        que.isDelete = 'true';
    }

    this.reorderCategory = function( newOrder ){
        $.each( newOrder, function( idx, item ){
            var category = getCategory( item );
            if( category != null ){
                category.categoryIndex = idx;
            }
        });
    }

    this.reorderQuestion = function ( newOrder, currentCategoryId ){
        var currentCategory = getCategory( currentCategoryId );

        $.each( newOrder, function( idx, item ){
            var question = getQue( item );
            question.questionIndex = idx;

            var index = $.inArray( question, currentCategory.questionCollection );

            if( index == -1){
                removeFromOldCategory(question);
                currentCategory.questionCollection.push( question );
            }
        });
    }

    function removeFromOldCategory( question ){
        $.each( survey.categoryCollection, function( idx, item ){
            var index = $.inArray( question, item.questionCollection );
            if( index != -1 ){
                item.questionCollection.splice( index, 1 );
            }
        });
    }

    function getQue( qId ){
        var quest;
        $.each(survey.categoryCollection, function( i, cateItem ){
            $.each(cateItem.questionCollection, function( i, qItem ){
               if( qItem.uiId == qId ){
                   quest = qItem;
               }
            });
        });
        return quest;
    }

    //private methods
    function getCategory( catId ){
        var category;
        $.each(survey.categoryCollection, function( i, item ){
           if( item.uiId == catId ){
               category = item;
               return false;
           }
           return true;
        });
        return category;
    }
}

var Category = function(){
    var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
    this.label = "New Category";
    this.objectName = "category" + numRand;
    this.questionCollection = [];
}

var Question = function(){
    var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id

    this.label = "New question";
    this.objectName = "category" + numRand;
    this.questionType = new Object();
    this.questionType.id = 1;
    this.questionOptionCollection = [];
}

var Survey = function(){
    this.title = "New survey";
    this.categoryCollection = [];
    this.categoryCollection.push( new Category() );
}

var QuestionOption = function(){
    this.label = "New option";
    this.optionValue = "val"; //TODO
}

