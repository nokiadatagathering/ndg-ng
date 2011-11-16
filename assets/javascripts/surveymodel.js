
var SurveyModel = function(s){
    var survey = s;
    var currentQuestionIndex = 0;

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
        currentQuestionIndex = 0;
    }

    this.reorderQuestion = function ( newOrder, currentCategoryId ){
        var currentCategory = getCategory( currentCategoryId );

        $.each( newOrder, function( idx, item ){
            var question = getQue( item );
            question.questionIndex = currentQuestionIndex;

            var index = $.inArray( question, currentCategory.questionCollection );

            if( index == -1){
                removeFromOldCategory(question);
                currentCategory.questionCollection.push( question );
            }
            currentQuestionIndex++;
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

    this.setSkipCategory = function ( queId, optionId, dropCategoryId ){
        var skipOption;
        var question = getQue( queId );
        var skipCategory = getCategory( dropCategoryId );
        $.each( question.questionOptionCollection , function( idx, item ){
            if( item.uiId == optionId ){
                item.skipCategory = skipCategory;
                skipOption = item;
            }else{
                item.skipCategory = null;
            }
        });

        if( skipCategory.skip != undefined && skipCategory != null ){
            var oldQue = skipCategory.skip[0];
            skipCategory.skip[1].skipCategory = null;
            $( '#' + oldQue.uiId + ' div.skipto.selected' ).removeClass( 'selected' );
        }

        skipCategory.skip = [ question, skipOption ]; //TODO do we need this???
    }

    function getOpt( question, optionId ){
        var option;
        $.each( question.questionOptionCollection , function( idx, item ){
            if( item.uiId == optionId ){
                option = item;
            }
        });
        return option;
    }
};

var Category = function(){
    var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
    this.label = "New Category";
    this.objectName = "category" + numRand;
    this.questionCollection = [];
}

var Question = function( question ){

    var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id

    if( question != undefined ){
        this.label = question.label;
        this.objectName = "question" + numRand;
        this.questionType = question.questionType;
        this.questionOptionCollection = [];

        for( var idx = 0; idx < question.questionOptionCollection.length; idx++){
            this.questionOptionCollection.push( new QuestionOption( question.questionOptionCollection[idx] ) );
        }
        this.defaultAnswer = new DefaultAnswer( question.defaultAnswer );

    }else{
        this.label = "New question";
        this.objectName = "question" + numRand;
        this.questionType = new Object();
        this.questionType.id = QuestionType.DESCRIPTIVE;
        this.questionOptionCollection = [];
        this.defaultAnswer = new DefaultAnswer();
    }
};

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
        this.optionValue = "newoption";
    }
}

var DefaultAnswer = function( answer ){
    if( answer != undefined ){
        this.textData = answer.textData;
    }else{
        this.textData = "";
    }
}

