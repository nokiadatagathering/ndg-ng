/*
 * File encapsulates action related to Export Results
 *
 **/

const LOC_CSV = 'Download in CSV format';
const LOC_XLS = 'Download in XLS format';


var ExportResults = function() {

    function exportResults(i) {
        //Get the screen height and width

        var maskHeight = $(document).height();
        var maskWidth = $(window).width();

        //Set height and width to mask to fill up the whole screen
        $('#mask').css({'width':maskWidth,'height':maskHeight});
        $('#mask').fadeTo("slow",0.8);

        drawDialog();
    }

    function drawDialog() {
        exportDialog.dialog("open");
        if ( document.getElementById( 'buttonCSV' ) == null ) {
            $('#dialog-exportResults').append( '<button id="buttonCSV" class="buttonCSV" type="button" title="' + LOC_CSV + '"/>' );
            $('#dialog-exportResults').append( '<button id="buttonXLS" class="buttonXLS" type="button" title="' + LOC_XLS + '"/>' );
            $('#dialog-exportResults').append( '<button id="cancelButton-exportResult" class="cancelButton" type="button" title="' + LOC_XLS + '"/>' );
            $('#cancelButton-exportResult').click( function() { closeExportResultDialog() } );
            $('#buttonCSV').click( function() {  } );
            $('#buttonXLS').click( function() {  } );
        }
    }

    function closeExportResultDialog() {
        exportDialog.dialog("close");
        $('#mask').hide();
    }

    return {exportResults : function(i) {exportResults(i);}
    };

}();