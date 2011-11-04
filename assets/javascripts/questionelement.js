var QuestionUiElement = function( questionModel ){
    var question = questionModel;

    this.init = function(){
        $( '#'+ question.uiId + ' .typeSelect' ).val( question.questionType.id );
        $( '#'+ question.uiId + ' .typeSelect' ).live( 'change', function( ){onQuestionTypeChanged();} );
       appendDetails();
    }

    function appendDetails ()   {
        $( '#'+ question.uiId + ' div.questionDetails' ).empty();
        $( '#'+ question.uiId + ' div.additionalButtons' ).empty();

        var type = question.questionType.id;

        switch( +type ){
        case 1:
            createDescriptive();
            break;
        case 2:
            createNumeric();
            break;
        case 3:
            createNumeric();
            break;
        case 4:
            createDate();
            break;
        case 6:
            createImage();
            break;
        case 10:
            createSelect( true );
            break;
        case 11:
            createSelect( false );
            break;
        default:

        }
    }

    function createDescriptive(){
        var elem = $(
                '<div>'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="descriptiveDefault detailInputText" type="text" name="descriptiveDefault" />'
            +   '<span class="detailLabel">LENGTH:</span><input class="descriptiveLength detailInputText" type="text" name="length" />' // TODO localize
            +   '</div>'
            );//TODO length can be only numeric

        $( '#'+ question.uiId + ' div.questionDetails' ).append( elem );
    }

    function createNumeric(){
        var elem = $(
                '<div>'
            +   '</div>'
            +   '<div>'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="numericDefault detailInputText" type="text" name="numericDefault" />'
            +   '<input class="rangeCheckMin" type="checkbox" name="minRangeCheckBox" />'
            +   '<span class="detailLabel">MIN.RANGE</span>'
            +   '<input class="rangeInputMin detailInputText" type="text" name="minRangeInput" />' // TODO localize

            +   '<input class="rangeCheckMax" type="checkbox" name="maxRangeCheckBox" />'
            +   '<span class="detailLabel">MAX.RANGE</span>'
            +   '<input class="rangeInputMax detailInputText" type="text" name="maxRange" />' // TODO localize
            +   '</div>'
            );

        $( '#' + question.uiId + ' div.questionDetails' ).append( elem );


        $( '#' + question.uiId + ' input.rangeCheckMin').change( function (){onCheckBoxMinChange();} );
        $( '#' + question.uiId + ' input.rangeCheckMax').change( function (){onCheckBoxMaxChange();} );
        onCheckBoxMinChange();
        onCheckBoxMaxChange();
    }

    function onCheckBoxMinChange(){
        if( $( '#' + question.uiId + ' input.rangeCheckMin' ).attr( 'checked' ) ){
            $( '#' + question.uiId + ' input.rangeInputMin' ).removeAttr( 'disabled' );
        }else{
            $( '#' + question.uiId + ' input.rangeInputMin' ).attr( 'disabled', 'disabled' );
        }
    }

    function onCheckBoxMaxChange(){
        if( $( '#' + question.uiId + ' input.rangeCheckMax' ).attr( 'checked' ) ){
            $( '#' + question.uiId + ' input.rangeInputMax' ).removeAttr( 'disabled' );
        }else{
            $( '#' + question.uiId + ' input.rangeInputMax' ).attr( 'disabled', 'disabled' );
        }
    }

    function createDate(){
        return $( '<span>Date element<span>' );
    }

    function createImage(){
        return $( '<span>Image element<span>' );
    }

    function createSelect( isExclusive ){

        $( '#'+ question.uiId + ' div.additionalButtons' ).append(
                    '<div class="importOption"></div>'
                +   '<div class="addOption" ></div>'
        );

        var elem = $(
                '<div class="options">'
            +   '</div>'
            );

        $( '#' + question.uiId + ' div.questionDetails' ).append( elem );
        $( '#' + question.uiId + ' div.addOption').bind('click', +isExclusive, function( event ) {onAddOptionClicked( event );} );

        $.each( question.questionOptionCollection, function( i, item ){
            if( !item.hasOwnProperty( 'isDelete' ) ){
                appendOption( item, isExclusive );
            }
        });
    }


    function appendOption( option, isExclusive ){
        var optionId;
        if( option.hasOwnProperty( 'id' ) ){
            optionId = 'option' + option.id;
        }else{
            var numRand = Math.floor( Math.random() * 10000 ); //TODO maybe exist better way to get rundom id
            optionId = 'option' + numRand;
        }
        option.uiId = optionId;

        var optionType;
        if( isExclusive ){
            optionType = 'radio';
        }else{
            optionType = 'checkbox';
        }

        var labelId = optionId + 'label'
        $( '#' + question.uiId + ' div.options' ).append(
                    '<div id="' + optionId + '" class="optionInput">'
                +   '<input type="' + optionType + '" class="optionCheckBox" />'
                +   '<span id="' + labelId + '" class="detailLabel optionLabel">' + option.label + '</span>'
                +   '<div class="deleteOption"></div>'
                +   '</div>'
            );

        new EditedLabel( $( "#" + labelId ), function( newLabel ){
            onOptionLabelChanged( optionId, newLabel );});

        $( '#'+ optionId + ' span.deleteOption' ).click( optionId, function( optId ){deleteOption( optId.data );});
    }

    function onAddOptionClicked( event ){
        var option = new QuestionOption();
        question.questionOptionCollection.push( option );
        appendOption( option, event.data );
    }

    function deleteOption( optId ){
        var option = getOption( optId );
        option.isDelete = 'true';

        $( '#'+ optId ).remove();
    }

    function onOptionLabelChanged( optionId, newLabel ){
        var option = getOption( optionId );
        option.label = newLabel;
    }

    function onQuestionTypeChanged(){
        appendDetails();
    }


    function getOption( optionUiId ){
        var option;

        $.each( question.questionOptionCollection , function( idx, item ){
            if( item.uiId == optionUiId ){
                option = item;
            }
        });
        return option;
    }
}
