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

        if(question.defaultAnswer != null ){
            $( '#' + question.uiId + ' .defaultAnswer ').val( question.defaultAnswer.textData );
        }
        //TODO move to question function
        $( '#' + question.uiId + ' .defaultAnswer ').keyup( function(){onDefaultChanged()});

    }

    function setDefaultAnswer( newDefValue ){
        if(question.defaultAnswer == null){
            question.defaultAnswer = new DefaultAnswer();
        }
        question.defaultAnswer.textData = newDefValue;
    }

    function onDefaultChanged(){
        setDefaultAnswer( $( '#' + question.uiId + ' .defaultAnswer ').val() );
    }

    function createDescriptive(){
        var elem = $(
                '<div>'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="defaultAnswer descriptiveDefault detailInputText" type="text" name="descriptiveDefault" />'
            +   '<span class="detailLabel">LENGTH:</span><input class="descriptiveLength detailInputText" type="text" name="length" />' // TODO localize
            +   '</div>'
            );//TODO length can be only numeric

        $( '#'+ question.uiId + ' div.questionDetails' ).append( elem );
        $( '#'+ question.uiId + ' .descriptiveLength' ).val( question.constraintMax );
        $( '#'+ question.uiId + ' .descriptiveLength' ).keyup( function (){onQuestionLengthChanged()});
    }

    function onQuestionLengthChanged(){
        question.constraintMax = $( '#'+ question.uiId + ' .descriptiveLength' ).val();
    }

    function createNumeric(){
        var elem = $(
                '<div>'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="defaultAnswer numericDefault detailInputText" type="text" name="numericDefault" />'
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

        $( '#' + question.uiId + ' .rangeInputMin').keyup( function (){onRangeInputMinChanged();} );
        $( '#' + question.uiId + ' .rangeInputMax').keyup( function (){onRangeInputMaxChanged();} );

        $( '#'+ question.uiId + ' .rangeInputMax' ).val( question.constraintMax );
        $( '#'+ question.uiId + ' .rangeInputMin' ).val( question.constraintMin );

        onCheckBoxMinChange();
        onCheckBoxMaxChange();
    }

    function onRangeInputMaxChanged(){
        question.constraintMax = $( '#'+ question.uiId + ' .rangeInputMax' ).val();
    }

    function onRangeInputMinChanged(){
        question.constraintMin = $( '#'+ question.uiId + ' .rangeInputMin' ).val();
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
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span><input class="defaultAnswer detailInputText dateInput" type="text" name="numericDefault" />'
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
        $( '#' + question.uiId + ' .defaultAnswer' ).datepicker({
                        showOn: "button",
                        buttonImage: "images/Calendar-icon.png",
                        buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true,
                        onClose: function(dateText, inst) {onDefaultChanged();}
        });

        $( '#' + question.uiId + ' .rangeInputMin' ).datepicker({
                        showOn: "button",
                        buttonImage: "images/Calendar-icon.png",
                        buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true,
                        onClose: function(dateText, inst) {onRangeInputMinChanged();}
        });

        $( '#' + question.uiId + ' .rangeInputMax' ).datepicker({
                    showOn: "button",
                    buttonImage: "images/Calendar-icon.png",
                    buttonImageOnly: true,
                    changeMonth: true,
                    changeYear: true,
                    onClose: function(dateText, inst) {onRangeInputMaxChanged();}
        });


        $( '#' + question.uiId + ' input.rangeCheckMin').change( function (){onCheckBoxMinChange();} );
        $( '#' + question.uiId + ' input.rangeCheckMax').change( function (){onCheckBoxMaxChange();} );
        onCheckBoxMinChange();
        onCheckBoxMaxChange();

        $( '#' + question.uiId + ' .rangeInputMin').keyup( function (){onRangeInputMinChanged();} );
        $( '#' + question.uiId + ' .rangeInputMax').keyup( function (){onRangeInputMaxChanged();} );

        $( '#'+ question.uiId + ' .rangeInputMax' ).val( question.constraintMax );
        $( '#'+ question.uiId + ' .rangeInputMin' ).val( question.constraintMin );

    }

    function createImage(){
        return $( '<span>Image element<span>' );
    }

    function createTime(){
         var elem = $(
               '<div class="timeDefault">'
            +   '<span class="detailLabel defaultLabel">DEFAULT:</span>'
            +   '<input class="defaultAnswer detailInputText timeInput" type="text" name="numericDefault" />'
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

        $( '#' + question.uiId + ' .optionCheckBox' ).change( question.uiId, function( event ){onSelectChanged( event.data )} );
    }

    function onSelectChanged( questionUiId ){
        var defValue = '';
        var value = $("#" + question.uiId + ' input[@name=' + questionUiId + ']:checked');
        $.each( value, function (idx, item){
            defValue += $(this).val() + ' ';
        });

        setDefaultAnswer( defValue )
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
                +   '<input name="' + question.uiId + '" value="' + option.optionValue + '" type="' + optionType + '" class="optionCheckBox" />'
                +   '<span id="' + labelId + '" class="detailLabel optionLabel">' + option.label + '</span>'
                +   '<div class="skipto optionIcon"></div>'
                +   '<div class="deleteOption optionIcon"></div>'
                +   '</div>'
            );

        if( !isExclusive ){
            $( '#' + question.uiId + ' .skipto' ).hide();
        }else{
            $( '#' + question.uiId + ' .skipto' ).draggable({
                    start: function(){
                        $( '#' + optionId.uiId + ' .optionIcon' ).addClass( 'iconVisibleDrag' );
                        Editor.updateContainerSize();
                    },
                    stop: function(){
                        $( '#' + optionId.uiId + ' .optionIcon' ).removeClass( 'iconVisibleDrag' );
                        $( '#' + optionId.uiId + ' .optionIcon' ).removeClass( 'iconVisible' );
                    },
                    revert: 'invalid',
                    helper: 'clone'
                });
        }

        if( question.defaultAnswer != null &&
            question.defaultAnswer.textData.indexOf( option.optionValue ) != -1 ){
            $( '#' + optionId + ' input').attr('checked', 'checked' );
        }

        new EditedLabel( $( "#" + labelId ), function( newLabel ){
            onOptionLabelChanged( optionId, newLabel );});

        $( '#' + optionId + ' .deleteOption' ).click( optionId, function( optId ){deleteOption( optId.data );});
        $( '#' + optionId  ).hover(
            function(){
                $( '#' + optionId + ' .optionIcon' ).addClass( 'iconVisible' );
            },
            function(){
                $( '#' + optionId + ' .optionIcon' ).removeClass( 'iconVisible' );
            }
        );

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
        var optionVal = newLabel.toLowerCase().split(' ').join('');

        $( '#'+ optionId + ' input' ).attr('value', optionVal );

        option.label = newLabel;
        option.optionValue = optionVal;
    }

    function onQuestionTypeChanged(){
        appendDetails();
    }


    function getOption( optionUiId ){// TODO move to model
        var option;

        $.each( question.questionOptionCollection , function( idx, item ){
            if( item.uiId == optionUiId ){
                option = item;
            }
        });
        return option;
    }
}
