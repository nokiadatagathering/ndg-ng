var QuestionPanel = function ( callback ){

    var currentQuestion;
    var refreshQuestionList = callback;

    this.createQuestionPanel = function(){
        $.getJSON( '/application/questionType', function( data ){
                                                   typeList = data.types;
                                                   createRightPanel();
                                                } );
    }

    function createRightPanel(){
        $('#editorRight').hide();
        $('#editorRight').append(
                '<span class="rightPanelLabel"> Question type </span>'
                + '<select class="rightPanelInput" id=qType></select>'
                + '<span class="rightPanelLabel"> Label </span>'
                + '<input class="rightPanelInput" id="qLabel" type="text"/>'
                + '<span class="rightPanelLabel"> Object id </span>'
                + '<input class="rightPanelInput" id="objId" type="text"/>'
        );
        $.each(typeList, function(idx, type){
            $('#qType').append('<option value="'+ type.id +'">' + type.typeName + '</option>');
        });

        $( '#qType' ).change( function(){ onTypeChanged();} );
        $( '#qLabel' ).keyup( function(){ onQuestionLabelChanged();} );
        $( '#objId' ).keyup( function(){ onObjectIdChanged();} );
    }

    this.fillRightPanel = function( question ){
        $( '#editorRight' ).show();
        currentQuestion = question;

        $( '#qType' ).val( question.questionType.id );
        $( '#qLabel' ).val( $.trim( question.label ) );
        $( '#objId' ).val( question.objectName );


    }

    function onQuestionLabelChanged(){
        currentQuestion.label = $( '#qLabel' ).val();
        refreshQuestionList( currentQuestion.label );
    }

    function onObjectIdChanged(){
        currentQuestion.objectName = $( '#objId' ).val();
    }

    function onTypeChanged(){
        currentQuestion.questionType.id = $( '#qType' ).val();
    }
}