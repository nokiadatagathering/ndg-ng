
var SurveyModel = function(s){
    var survey = s;

    //public methods
    this.getSurvey = function(){return survey;};

    this.updateCategory = function ( catId, newLabel ){
        getCategory( catId).label = newLabel;
    }

    this.updateSurveyTitle = function ( newTitle ){
        survey.title = newTitle;
    }

    this.getQuestion = function ( cId, qId ){
        var quest;
        $.each(getCategory(cId).questionCollection, function(i, item){
           if(item.uiId == qId){
               quest = item;
           }
        });
        return quest;
    }

    this.newCategory = function (){
        //TODO how to generate uniqe objectName
        var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
        var newCategory = new Object();
        newCategory.label = "New Category";
        newCategory.objectName = "category" + numRand;
        newCategory.questionCollection = [];
        survey.categoryCollection.push( newCategory );



        return newCategory;
    }

    this.newQuestion = function( categoryId ){
        var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id

        var newQuestion = new Object();
        newQuestion.label = "New question";
        newQuestion.objectName = "category" + numRand;
        newQuestion.questionType = new Object();
        newQuestion.questionType.id = 1;


        var category = getCategory( categoryId );
        category.questionCollection.push( newQuestion );

        return newQuestion;
    }

    //private methods
    function getCategory( catId ){
        var category;
        $.each(survey.categoryCollection, function( i, item ){
           if( item.uiId == catId ){
               category = item;
           }
        });
        return category;
    }
}


//TODO remove this
var Category = function(lab, catIdx){
    this.label = lab;
    this.categoryIndex = catIdx;
    this.questionCollection = [];
}

var Question = function(label, objectName){
    this.label = label;
    this.objectName = objectName;
    this.questionType = new QuestionType('1');//TODO fix this
}

var QuestionType = function(type){
    this.type = type;
}

