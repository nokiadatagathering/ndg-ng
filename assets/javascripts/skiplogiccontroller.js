
var SkipLogicController = function ( model ){

    var surveyModel = model;
    var skipLogicMap = new SkipLogicMap();

    return{
        add: function ( category, skipOption ) { add( category, skipOption ) },
        getRelevantString : function ( category ) { return getRelevantString( category ) },
        contains : function ( category ) { return contains( category ) },
        addSkipLogic : function ( skipedCategory, relevantStr ) { addSkipLogic( skipedCategory, relevantStr ) }
    };

    function add( skipedCategory, skipObject ){
        if(skipLogicMap.contains( skipedCategory ) ){
            var oldSkipOptionObj = skipLogicMap.get( skipedCategory );
            $( '#' + oldSkipOptionObj.quesiton.uiId + ' div.skipto.selected' ).removeClass( 'selected' );
        }
        skipLogicMap.add( skipedCategory,  skipObject );
        $( '#' + skipObject.option.uiId + ' div.skipto' ).addClass( 'selected' );
    }

    function contains( category ){
        return skipLogicMap.contains( category );
    }

    function getRelevantString( category ){
//        var str = skipLogicMap.get( category ).getRelevantString();
//        addSkipLogic( str );
        return skipLogicMap.get( category ).getRelevantString();
    }


    function addSkipLogic( skipedCategory, relevantStr ){
        var test = relevantStr.replace( new RegExp("'", "g"), "" ).replace( "/", " " ).replace( "=", " " ).split( " " );

        var category = surveyModel.findCategoryByObjectName( $.trim( test[0] ) );
        var question = surveyModel.findQuestionByObjectName( category, $.trim( test[1] ) ) ;
        var option = surveyModel.findOptionByValue( question, $.trim( test[2] ) );

        add( skipedCategory, new SkipObject( option, question, category ) );
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
        return category.objectName + '/' + question.objectName + "='" + option.optionValue + "'";
    }
};

var SkipLogicMap = function (){
    var skipedCategoryArray = [];
    var skipObjectArray = [];


    return {
        add : function ( keyCategory, valueSkipObj ){add( keyCategory, valueSkipObj )},
        get : function ( keyCategory ) {return get( keyCategory )},
        contains : function ( keyCategory ) {return contains( keyCategory )}
    }

    function get( keyCategory ){
        var index = $.inArray( keyCategory, skipedCategoryArray );
        return skipObjectArray[ index ];
    }

    function add( keyCategory, valueSkipObj ){

        var index = $.inArray( keyCategory, skipedCategoryArray );
        if( index != -1 ){
            skipedCategoryArray[index] = keyCategory;
            skipObjectArray[index] = valueSkipObj;
        }else{
            skipedCategoryArray.push( keyCategory );
            skipObjectArray.push( valueSkipObj );
        }
    }

    function contains( keyCategory ){
        var index = $.inArray( keyCategory, skipedCategoryArray );

        if( index != -1 ){
             return true;
        }else{
            return false;
        }
    }
}
