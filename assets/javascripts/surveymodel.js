
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

    this.updateQuestionTitle = function( qId, newLabel ){
        getQue( qId ).label = newLabel;
    }

    this.updateQuestionType = function( qId, type ){
        getQue( qId ).questionType.id = parseInt( type );
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

        var category;
        if( categoryId != null ){
            category = getCategory( categoryId );
        }else{
            category = survey.categoryCollection[0];
        }

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
                category.categoryIndex = idx + 1;
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

    this.duplicateQuestion = function( queId, category ){
        var newQuestion = new Question( getQue( queId ) );
        category.questionCollection.push( newQuestion );
        return newQuestion;
    }

    this.getCategoryForQuestion = function( qId ){
        var category;
        $.each(survey.categoryCollection, function( i, cateItem ){
            $.each(cateItem.questionCollection, function( i, qItem ){
               if( qItem.uiId == qId ){
                   category = cateItem;
               }
            });
        });
        return category;
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

function Question( question ){

    var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id

    if( question != undefined ){
        this.label = question.label;
        this.objectName = "question" + numRand;
        this.questionType = question.questionType;
        this.questionOptionCollection = [];

        for( var idx = 0; idx < question.questionOptionCollection.length; idx++){
            this.questionOptionCollection.push( new QuestionOption( question.questionOptionCollection[idx] ) );
        }

    }else{
        this.label = "New question";
        this.objectName = "question" + numRand;
        this.questionType = new Object();
        this.questionType.id = 1;
        this.questionOptionCollection = [];
    }
}

var Survey = function(){
    this.title = "New Survey";
    this.categoryCollection = [];
    this.categoryCollection.push( new Category() );
}

var QuestionOption = function( option ){
    if( option != undefined ){
        this.label = option.label;
        this.optionValue = option.optionValue;
    }else{
        this.label = "New option";
        this.optionValue = "val"; //TODO edit val
    }

}

