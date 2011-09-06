/*
 * File encapsulates action related to Export Results
 *
 **/

const LOC_CSV = 'Download in CSV format';
const LOC_XLS = 'Download in XLS format';
const LOC_CLOSE = 'Close';
const LOC_EXPORTRESULTS = 'Export Results';


var ExportResults = function() {

    var resultList;

    function exportResults(i) {
        resultList = i;
        exportDialog.dialog({ title: LOC_EXPORTRESULTS,
                              open: function(){
                              $('.ui-widget-overlay').hide().fadeIn();},
                              show: 'fade',
                              hide: 'fade' });
        exportDialog.dialog( "open" );
        drawDialog();
    }

    function drawDialog() {
        $('#exportResults-step_1').empty();
        $('#exportResults-step_2').empty();
        $('#exportResults-step_3').empty();

        $('#exportResults-step_1').append( '<button id="buttonCSV" class="buttonCSV" type="button" title="' + LOC_CSV + '"/>' );
        $('#exportResults-step_1').append( '<button id="buttonXLS" class="buttonXLS" type="button" title="' + LOC_XLS + '"/>' );
        $('#buttonCSV').click( function() { exportCSVResults() } );
        $('#buttonXLS').click( function() { exportXLSResults() } );
    }

    function exportCSVResults() {
        $('#exportResults-step_1').empty();
        $('#exportResults-step_2').append( '<img src="images/POPUP_ICON_LOADING.gif" />');
        $('#exportResults-step_2').append( '<b>Exporting survey resutls to CSV file...</>' );

        $.getJSON( '/service/preparezip',{ 'ids': resultList.join(','),
                                           'fileFormat': 'CSV'
                                         }, function(data){ updateDialogWithLink(data); } );
    }

    function updateDialogWithLink(data) {
        $('#exportResults-step_2').empty();
        $('#exportResults-step_3').append( '<b>Link ready' + data.surveysCount + '</b>' );
    }

    function exportXLSResults() {
        $('#exportResults-step_1').empty();
        $('#exportResults-step_2').append( '<img src="images/POPUP_ICON_LOADING.gif" />');
        $('#exportResults-step_2').append( '<b>Exporting survey resutls to XLS file...</>' );
    }

    return {exportResults : function(i) {exportResults(i);}
    };

}();