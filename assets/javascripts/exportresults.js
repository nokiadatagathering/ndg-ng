/*
 * File encapsulates action related to Export Results
 *
 **/


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
        exportDialog.dialog({title: LOC.get('LOC_EXPORT_RESULTS'),
                              open: function(){
                              $('.ui-widget-overlay').hide().fadeIn();},
                              show: 'fade',
                              hide: 'fade'});
        exportDialog.dialog( "open" );
        $('#exportResults-step_1').empty();
        $('#exportResults-step_2').empty();
        $('#exportResults-step_3').empty();

        exportXLSResults();
    }

//    function exportCSVResults() {
//        $.getJSON( '/service/surveyHasImages',
//                   { 'surveyId': surveyId },
//                   function(result) { proceedExportResults( ".CSV", result.hasImages ); } );
//    }

    function exportXLSResults() {
        var getJSONQuery = $.getJSON( '/service/surveyHasImages',
                   { 'surveyId': surveyId },
                   function(result) { proceedExportResults( ".XLS", result.hasImages ); } );
        getJSONQuery.error(Utils.redirectIfUnauthorized);
    }

    function proceedExportResults(fileFormat, hasImages) {
        $('#exportResults-step_1').empty();
        if ( hasImages ) {
            $('#exportResults-step_2').append( LOC.get('LOC_EXPORT_IMAGES')  );
            $('#exportResults-step_2').append( '<button id="buttonYES" type="button" title="' + LOC.get('LOC_YES') + '">' + LOC.get('LOC_YES') + '</button>' );
            $('#exportResults-step_2').append( '<button id="buttonNO" type="button" title="' + LOC.get('LOC_NO') + '">' + LOC.get('LOC_NO') + '</button>' );
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