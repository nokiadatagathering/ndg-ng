
var SkipLogicController = function ( model ){

    var surveyModel = model;
    var skipLogicMap = new SkipLogicMap();

    return{
        add: function ( category, skipOption ) { add( category, skipOption ) },
        getRelevantString : function ( category ) { return getRelevantString( category ) },
        contains : function ( category ) { return contains( category ) },
        addSkipLogic : function ( skipedCategory, relevantStr ) { addSkipLogic( skipedCategory, relevantStr ) },
        optionDeleted : function ( optionId) { optionDeleted(optionId);},
        questionDeleted: function(questionId) {questionDeleted(questionId);},
        categoryDeleted: function(categoryId) {categoryDeleted(categoryId);}
    };

    function add( skippedQuestion, skipObject ){
        if(skipLogicMap.contains( skippedQuestion ) ){
            var oldSkipOptionObj = skipLogicMap.get( skippedQuestion );
            $( '#' + oldSkipOptionObj.uiId + ' div.skipto.selected' ).removeClass( 'selected' );
        }
        skipLogicMap.add( skippedQuestion,  skipObject );
        $( '#' + skipObject.option.uiId + ' div.skipto' ).addClass( 'selected' );
        $( '#' + skipObject.option.uiId + ' div.skipto' ).unbind( 'click' );
        $( '#' + skipObject.option.uiId + ' div.skipto' ).click( function(){selectedSkipClicked($(this));});
    }

    function selectedSkipClicked(source) {
        var optionId = source.parents('.optionInput').attr('id');
        var question = $('#' + skipLogicMap.getSkipTarget(optionId).uiId);
        var categoryToExpand = $(question).parents('.listCategory')[0];
        Editor.showCategory(categoryToExpand.getAttribute('id'));
        question.addClass('highlightRelevant');
        scroll(0, question[0].offsetTop);
        setTimeout( function() {question.removeClass('highlightRelevant'); }, 750);
    }

    function contains( question ){
        return skipLogicMap.contains( question );
    }

    function getRelevantString( question ){
        return skipLogicMap.get( question ).getRelevantString();
    }


    function addSkipLogic( skipedQuestion, relevantStr ){

        relevantStr = relevantStr.replace( new RegExp("'", "g"), "" );
        relevantStr = relevantStr.replace( new RegExp("/", "g"), " " ).replace( "=", " " );
        relevantStr = $.trim( relevantStr );
        var expr = relevantStr.split( " " );

        var category = surveyModel.findCategoryByObjectName( $.trim( expr[1] ) );
        var question = surveyModel.findQuestionByObjectName( category, $.trim( expr[2] ) ) ;
        var option = surveyModel.findOptionByValue( question, $.trim( expr[3] ) );

        add( skipedQuestion, new SkipObject( option, question, category ) );
    }

    function optionDeleted(optionId) {
        skipLogicMap.removeOption(optionId);
    }

    function questionDeleted(questionId) {
        skipLogicMap.removeQuestion(questionId);
    }

    function categoryDeleted(categoryId) {
        skipLogicMap.removeCategory(categoryId);
    }
}

var SkipObject = function(  opt, que, cat ){
    var option = opt;
    var question = que;
    var category = cat;


    return {
        getRelevantString : function (){return getRelevantString()},
        option : option,
        question: question,
        category: category
    }

    function getRelevantString(){
        return  '/data/' + category.objectName + '/' + question.objectName + "='" + option.optionValue + "'";
    }
};

var SkipLogicMap = function (){
    var skippedQuestionArray = [];
    var skipObjectArray = [];


    return {
        add : function ( keyCategory, valueSkipObj ){add( keyCategory, valueSkipObj )},
        get : function ( keyCategory ) {return get( keyCategory )},
        contains : function ( keyCategory ) {return contains( keyCategory )},
        removeOption : function(optionId){return removeOption(optionId)},
        removeQuestion : function(questionId){return removeQuestion(questionId)},
        removeCategory : function(categoryId){return removeCategory(categoryId)},
        getSkipTarget : function(optionId) {return getSkipTarget(optionId)}
    }

    function get( keyQuestion ){
        var index = $.inArray( keyQuestion, skippedQuestionArray );
        return skipObjectArray[ index ];
    }

    function add( keyQuestion, valueSkipObj ){

        var index = $.inArray( keyQuestion, skippedQuestionArray );
        if( index != -1 ){
            skippedQuestionArray[index] = keyQuestion;
            skipObjectArray[index] = valueSkipObj;
        }else{
            skippedQuestionArray.push( keyQuestion );
            skipObjectArray.push( valueSkipObj );
        }
    }

    function contains( keyQuestion ){
        var index = $.inArray( keyQuestion, skippedQuestionArray );

        if( index != -1 ){
             return true;
        }else{
            return false;
        }
    }

    function getSkipTarget(optionId) {
        var question = undefined;
        $.each(skipObjectArray, function(i, current){
            if(current.option.uiId == optionId) {
                question = skippedQuestionArray[i];
                return false;
            }
        });  
        return question;
    }

    function removeOption(optionId) {
        $.each(skipObjectArray, function(i, current){
            if(current.option.uiId == optionId) {
                skippedQuestionArray.splice(i, 1);
                skipObjectArray.splice(i, 1);
                return false;
            }
        });
    }

    function removeQuestion(questionId) {
        $.each(skipObjectArray, function(i, current){
            if(current.question.uiId == questionId) {
                skippedQuestionArray.splice(i, 1);
                skipObjectArray.splice(i, 1);
                if(i >= skipObjectArray.length) {
                    return false;
                }
            }
        });
    }

    function removeCategory(categoryId) {
        $.each(skipObjectArray, function(i, current){
            if(current.category.uiId == categoryId) {
                skippedQuestionArray.splice(i, 1);
                skipObjectArray.splice(i, 1);
                if(i >= skipObjectArray.length) {
                    return false;
                }
            }
        });
    }
}
