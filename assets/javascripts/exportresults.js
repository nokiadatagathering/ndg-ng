/*
 * File encapsulates action related to Export Results
 *
 **/

const LOC_CSV = 'Download in CSV format';
const LOC_XLS = 'Download in XLS format';
const LOC_CLOSE = 'Close';
const LOC_EXPORTRESULTS = 'Export Results';
const LOC_EXPORT_IMAGES = 'The survey you want to export has images in its results. Do you want to export the images as well?'
const LOC_EXPORT_FORMAT = 'Please, select below the file format to export results.'
const LOC_YES = 'Yes';
const LOC_NO = 'No';


var ExportResults = function() {

    var isAllResults;
    var surveyId;
    var resultList;

    function exportAllResults(currentSurveyId) {
        isAllResults = true;
        surveyId = currentSurveyId;
        drawDialog();
    }

    function exportResults(currentSurveyId, selectedResults) {
        surveyId = currentSurveyId
        resultList = selectedResults;
        isAllResults = false;
        drawDialog();
    }

    function drawDialog() {
        exportDialog.dialog({title: LOC_EXPORTRESULTS,
                              open: function(){
                              $('.ui-widget-overlay').hide().fadeIn();},
                              show: 'fade',
                              hide: 'fade'});
        exportDialog.dialog( "open" );
        $('#exportResults-step_1').empty();
        $('#exportResults-step_2').empty();
        $('#exportResults-step_3').empty();

        $('#exportResults-step_1').append( LOC_EXPORT_FORMAT );
        $('#exportResults-step_1').append( '<button id="buttonCSV" class="buttonCSV" type="button" title="' + LOC_CSV + '"/>' );
        $('#exportResults-step_1').append( '<button id="buttonXLS" class="buttonXLS" type="button" title="' + LOC_XLS + '"/>' );
        $('#buttonCSV').click( function() {exportCSVResults()} );
        $('#buttonXLS').click( function() {exportXLSResults()} );
    }

    function exportCSVResults() {
        $.getJSON( '/service/surveyHasImages',
                   { 'surveyId': surveyId },
                   function(result) { proceedExportResults( ".CSV", result.hasImages ); } );
    }

    function exportXLSResults() {
        $.getJSON( '/service/surveyHasImages',
                   { 'surveyId': surveyId },
                   function(result) { proceedExportResults( ".XLS", result.hasImages ); } );
    }

    function proceedExportResults(fileFormat, hasImages) {
        $('#exportResults-step_1').empty();
        if ( hasImages ) {
            $('#exportResults-step_2').append( LOC_EXPORT_IMAGES  );
            $('#exportResults-step_2').append( '<button id="buttonYES" type="button" title="' + LOC_YES + '">' + LOC_YES + '</button>' );
            $('#exportResults-step_2').append( '<button id="buttonNO" type="button" title="' + LOC_NO + '">' + LOC_NO + '</button>' );
            $('#buttonYES').click( function(i) { includeImages( fileFormat,i ) } );
            $('#buttonNO').click( function(i) { includeImages( fileFormat, i ) } );
        } else {
            getFile( fileFormat, false );
        }
    }

    function includeImages(fileFormat,i) {
        getFile( fileFormat, (i.currentTarget.id == "buttonYES") );
    }

    function getFile( fileFormat ,isWithImages ) {
        $('#exportResults-step_2').empty();
        $('#exportResults-step_3').append( '<img src="images/POPUP_ICON_LOADING.gif" />');
        $('#exportResults-step_3').append( '<b>Exporting survey results to' + fileFormat + ' file...</>' );

        if( isAllResults ) {
            window.location.href = "/service/prepare?surveyId=" + surveyId + "&fileFormat=" + fileFormat + "&exportWithImages=" + isWithImages;//'exportWithImages': false//TODO handle additional question
        } else {
            window.location.href = "/service/prepareselected?ids=" + resultList.join(',') + "&fileFormat=" + fileFormat + "&exportWithImages=" + isWithImages;//'exportWithImages': false//TODO handle additional question
        }
        exportDialog.dialog("close");
    }

    return { exportResults : function( surveyId, selectedResults) {exportResults(surveyId,selectedResults);},
             exportAllResults : function(surveyId) {exportAllResults(surveyId);}
    };
}();