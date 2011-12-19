
var SkipLogicController = function ( model ){

    var surveyModel = model;
    var skipLogicMap = new SkipLogicMap();

    return{
        add: function ( category, skipOption ) { add( category, skipOption ) },
        getRelevantString : function ( category ) { return getRelevantString( category ) },
        contains : function ( category ) { return contains( category ) },
        addSkipLogic : function ( skipedCategory, relevantStr ) { addSkipLogic( skipedCategory, relevantStr ) }
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
        skipLogicMap.removeOption(optionId);
        source.removeClass('selected');
        source.unbind('click');
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
    var skipedQuestionArray = [];
    var skipObjectArray = [];


    return {
        add : function ( keyCategory, valueSkipObj ){add( keyCategory, valueSkipObj )},
        get : function ( keyCategory ) {return get( keyCategory )},
        contains : function ( keyCategory ) {return contains( keyCategory )},
        removeOption : function(optionId){return removeOption(optionId)}
    }

    function get( keyQuestion ){
        var index = $.inArray( keyQuestion, skipedQuestionArray );
        return skipObjectArray[ index ];
    }

    function add( keyQuestion, valueSkipObj ){

        var index = $.inArray( keyQuestion, skipedQuestionArray );
        if( index != -1 ){
            skipedQuestionArray[index] = keyQuestion;
            skipObjectArray[index] = valueSkipObj;
        }else{
            skipedQuestionArray.push( keyQuestion );
            skipObjectArray.push( valueSkipObj );
        }
    }

    function contains( keyQuestion ){
        var index = $.inArray( keyQuestion, skipedQuestionArray );

        if( index != -1 ){
             return true;
        }else{
            return false;
        }
    }

    function removeOption(optionId) {
        $.each(skipObjectArray, function(i, current){
            if(current.option.uiId == optionId) {
                skipedQuestionArray.splice(i);
                skipObjectArray.splice(i);
                return false;
            }
        });
    }
}
