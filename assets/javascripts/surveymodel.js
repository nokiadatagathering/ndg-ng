
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

