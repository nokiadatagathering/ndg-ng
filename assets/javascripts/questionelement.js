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
        case QuestionType.DESCRIPTIVE:
            createDescriptive();
            break;
        case QuestionType.INTEGER:
            createNumeric();
            break;
        case QuestionType.DECIMAL:
            createNumeric();
            break;
        case QuestionType.DATE:
            createDate();
            break;
        case QuestionType.IMAGE:
            createImage();
            break;
        case QuestionType.TIME:
            createTime();
            break;
        case QuestionType.EXCLUSIVE:
            createSelect( true );
            break;
        case QuestionType.CHOICE:
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
        var inputElem = $( '#' + question.uiId + ' input.rangeInputMin' );
        if( $( '#' + question.uiId + ' input.rangeCheckMin' ).attr( 'checked' ) ){
            inputElem.removeAttr( 'disabled' );
        }else{
            inputElem.attr( 'disabled', 'disabled' );
        }
    }

    function onCheckBoxMaxChange(){
        var inputElem = $( '#' + question.uiId + ' input.rangeInputMax' );
        if( $( '#' + question.uiId + ' input.rangeCheckMax' ).attr( 'checked' ) ){
            inputElem.removeAttr( 'disabled' );
        }else{
            inputElem.attr( 'disabled', 'disabled' );
        }
    }

    function createDate(){
         var elem = $(
                '<div class="dateDefault">'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="detailInputText dateInput" type="text" name="numericDefault" />'
            +   '</div>'

            +   '<div>'
            +   '<input class="rangeCheckMin" type="checkbox" name="minRangeCheckBox" />'
            +   '<span class="detailLabel">MIN.RANGE</span>'
            +   '<input class="dateInput rangeInputMin detailInputText" type="text" name="minRange" />' // TODO localize
            +   '<input class="rangeCheckMax" type="checkbox" name="maxRangeCheckBox" />'
            +   '<span class="detailLabel">MAX.RANGE</span>'
            +   '<input class="dateInput rangeInputMax detailInputText" type="text" name="maxRange" />' // TODO localize
            +   '</div>'
            );
        

        $( '#' + question.uiId + ' div.questionDetails' ).append( elem );
        $( '#' + question.uiId + ' .dateInput' ).datepicker({
            		showOn: "button",
			buttonImage: "images/Calendar-icon.png",
			buttonImageOnly: true
        });
        
        
        $( '#' + question.uiId + ' input.rangeCheckMin').change( function (){onCheckBoxMinChange();} );
        $( '#' + question.uiId + ' input.rangeCheckMax').change( function (){onCheckBoxMaxChange();} );
        onCheckBoxMinChange();
        onCheckBoxMaxChange();
    }

    function createImage(){
        return $( '<span>Image element<span>' );
    }

    function createTime(){
         var elem = $(
               '<div class="timeDefault">'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span>'
            +   '<input class="detailInputText timeInput" type="text" name="numericDefault" />'
            +   '<input class="amPmRadio" type="radio" name="amPm" value="ampm" /><span class="amPmLabel">AM/PM</span>'
            +   '<input class="amPmRadio" checked="checked" type="radio" name="amPm" value="24hrs" /><span class="amPmLabel">24 HRS</span>'
            +   '</div>'
            );

        $( '#' + question.uiId + ' div.questionDetails' ).append( elem );
        $( '#' + question.uiId + ' .timeInput' ).timeEntry({
                                                spinnerImage: '', 
                                                show24Hours: true
                                            });
                                
        $( '#' + question.uiId + ' .amPmRadio' ).click(function(){
            var value = $("#" + question.uiId + " input[@name=amPm]:checked").attr('value');
            if( value == 'ampm' ){
                $('#' + question.uiId + ' .timeInput' ).timeEntry('change', {show24Hours: false}); 
            }else{
                $('#' + question.uiId + ' .timeInput' ).timeEntry('change', {show24Hours: true});
            }
        });
        
        $( '#' + question.uiId + ' .timeInput' ).val( );
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

        var labelId = optionId + 'label';
        
        $( '#' + question.uiId + ' div.options' ).append(
                    '<div id="' + optionId + '" class="optionInput">'
                +   '<input name="' + question.uiId + '" type="' + optionType + '" class="optionCheckBox" />'
                +   '<span id="' + labelId + '" class="detailLabel optionLabel">' + option.label + '</span>'
                +   '<div class="deleteOption"></div>'
                +   '</div>'
            );

        new EditedLabel( $( "#" + labelId ), function( newLabel ){
            onOptionLabelChanged( optionId, newLabel );});

        $( '#'+ optionId + ' .deleteOption' ).click( optionId, function( optId ){deleteOption( optId.data );});
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
