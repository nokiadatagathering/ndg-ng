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
        $( '#editorRight' ).hide();
        $( '#editorRight' ).append(
                '<span class="rightPanelLabel"> Question type </span>'
                + '<select class="rightPanelInput" id=qType></select>'
                + '<span class="rightPanelLabel"> Label </span>'
                + '<input class="rightPanelInput" id="qLabel" type="text"/>'
                + '<span class="rightPanelLabel"> Object id </span>'
                + '<input class="rightPanelInput" id="objId" type="text"/>'

                + '<div id="questionOptions">'
                + '<div id="optionsLabel"><span> Options </span></div> '
                + '<div id="addOption"><span title="Add option"></span></div>' //TODO localize
                + '<div style="clear:both;"></div>'
                + '</div>'

        );

        $( '#questionOptions' ).hide();

        $.each( typeList, function( idx, type ){
            $( '#qType' ).append( '<option value="'+ type.id +'">' + type.typeName + '</option>' );
        });

        $( '#qType' ).change( function(){onTypeChanged();} );
        $( '#qLabel' ).keyup( function(){onQuestionLabelChanged();} );
        $( '#objId' ).keyup( function(){onObjectIdChanged();} );
        $( '#addOption' ).click( function() {onAddOptionClicked();} );
    }

    this.fillRightPanel = function( question ){
        $( '#editorRight' ).show();
        currentQuestion = question;

        $( '#qType' ).val( question.questionType.id );
        $( '#qLabel' ).val( $.trim( question.label ) );
        $( '#objId' ).val( question.objectName );

        $( '.optionInput' ).remove();
        if(question.questionType.id == 10 || question.questionType.id == 11){
            $( '#questionOptions' ).show();
            $.each( currentQuestion.questionOptionCollection, function( i, item ){
                appendOption( item);
            });
        }else{
            $( '#questionOptions' ).hide();
        }
    }

    function appendOption( option ){
        var optionId;
        if( option.hasOwnProperty( 'id' ) ){
            optionId = 'option' + option.id;
        }else{
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
            optionId = 'option' + numRand;
        }

        option.uiId = optionId;

        $( '#questionOptions' ).append(
                '<div id="' + optionId + '" class="optionInput">'
                + '<div class="optionInputLabel"><input class="rightPanelInput" type="text"/></div>'
                + '<div class="deleteOption"><span title="' + LOC.get('LOC_DELETE') + '"></span></div>'
                + '<div style="clear:both;"></div>'
                + '</div>'
            );

        $( '#'+ optionId + ' input').val( option.label );
        $( '#'+ optionId + ' div.deleteOption' ).click( optionId, function( optId ){deleteOption( optId.data );});
        $( '#'+ optionId + ' input.rightPanelInput').keyup( optionId, function( event ){onOptionValueChanged( event.data );} );
    }

    function onOptionValueChanged( optionId ){
        var option = getOption( optionId );
        var value = $( '#'+ optionId + ' input.rightPanelInput').val();

        option.label = value;
    }

    function getOption( optionUiId ){
        var option;

        $.each( currentQuestion.questionOptionCollection , function( idx, item ){
            if( item.uiId == optionUiId ){
                option = item;
            }
        });
        return option;
    }

    function deleteOption( optId ){
        var option = getOption( optId );
        option.isDelete = 'true';

        $( '#'+ optId ).remove();
    }

    function onQuestionLabelChanged(){
        currentQuestion.label = $( '#qLabel' ).val();
        refreshQuestionList( currentQuestion.label );
    }

    function onObjectIdChanged(){
        currentQuestion.objectName = $( '#objId' ).val();
    }

    function onTypeChanged(){
        currentQuestion.questionType.id = parseInt( $( '#qType' ).val() ) ;
        if( currentQuestion.questionType.id == 10 || currentQuestion.questionType.id == 11 ){
            $( '#questionOptions' ).show();
        }
    }

    function onAddOptionClicked(){
        var option = new QuestionOption();
        currentQuestion.questionOptionCollection.push( option );
        appendOption( option )
    }

}