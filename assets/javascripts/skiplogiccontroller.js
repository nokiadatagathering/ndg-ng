
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
        $( '#' + skipObject.uiId + ' div.skipto' ).addClass( 'selected' );
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
        quesiton: question,
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
        contains : function ( keyCategory ) {return contains( keyCategory )}
    }

    function get( keyCategory ){
        var index = $.inArray( keyCategory, skipedQuestionArray );
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

    function contains( keyCategory ){
        var index = $.inArray( keyCategory, skipedQuestionArray );

        if( index != -1 ){
             return true;
        }else{
            return false;
        }
    }
}
