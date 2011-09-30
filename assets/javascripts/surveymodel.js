
var SurveyModel = function(s){
    var survey = s;


    //public methods
    this.getSurvey = function(){ return survey; };

    this.updateCategory = function ( catId, newLabel ){
        getCategory(catId).label = newLabel;
    }

    this.updateSurveyTitle = function ( newTitle ){
        survey.title = newTitle;
    }

    this.getQuestion = function ( cId, qId ){
        var quest;
        $.each(getCategory(cId).questionCollection, function(i, item){
           if(item.id == qId){
               quest = item;
           }
        });
        return quest;
    }

    //private methods
    function getCategory(catId){
        var category;
        $.each(survey.categoryCollection, function( i, item ){
           if( item.id == catId ){
               category = item;
           }
        });
        return category;
    }
}

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

