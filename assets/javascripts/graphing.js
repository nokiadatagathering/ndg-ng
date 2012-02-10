/*
 * File encapsulates action related to Graphing
 *
 **/

var Graphing = function() {

     var isIE = (function()
      {
         var div = document.createElement('div');
         div.innerHTML = '<!--[if IE]><i></i><![endif]-->';
         return (div.getElementsByTagName('i').length === 1);         
      }());

     var surveyId;
     var questionId;

     return  { graphResults : function( surveyId, selectedResults) {graphResults(surveyId,selectedResults);},
               graphAllResults : function(surveyId) {graphAllResults(surveyId);},
               getGraphData : function() {getGraphData(questionId);}
                          };


    function graphAllResults(currentSurveyId) {
        isAllResults = true;
        surveyId = currentSurveyId;
        setupGraph();
    }

    function graphResults(currentSurveyId, selectedResults) {
        surveyId = currentSurveyId;
        resultList = selectedResults;
        isAllResults = false;
        setupGraph();
    }

   

    function setupGraph(){
        $('#sectionTitle').text(LOC.get('LOC_GRAPH_VIEW'));
        $('#searchComboBox').unbind('click');
        $('#leftColumn').empty('resultListTable');
        $('#minimalist').empty();
        $('#searchComboBox').hide();
        $('#searchTextField').hide();
        $('#contentToolbar').hide();
        setupLeftColumn();
        chooseGraphType();
                        }


      function setupLeftColumn() {
       var columnContent = '<div class="resultCollectionQuantity clickableElem">';
           columnContent +='<span id ="resultCollectionQuantityString' + surveyId + '" class="buttonBack"></span>';
           columnContent +='</div>';

       $('#leftColumn').append( columnContent );
        
       $('#resultCollectionQuantityString' + surveyId ).click( surveyId, function() {
                               $('.resultListButton').hide(); 
                               $('#canvas').replaceWith('<table id="minimalist" class="resultListTable"></table>');
                               $('#searchComboBox').show();
                               $('#searchTextField').show();
                               ResultList.showResultList(surveyId)
                                                                                    });
                                }  


      function chooseGraphType() {   

        // hit the db for the raw data 
        fillGraphData(surveyId);


      function fillGraphData(surveyId){
             var getJSONQuery = $.getJSON('surveyManager/getSurvey', {'surveyId': parseInt(surveyId)}, function(data){
                                                                surveyModel = new SurveyModel(data.survey);
                                                                selectGraphType();
                                                                                                                     } );
             getJSONQuery.error(Utils.redirectIfUnauthorized);
                                      }
                                }

      function selectGraphType(){ 
                $('#sectionTitle').empty();
                $('#sectionTitle').append(surveyModel.getSurvey().title);

                $('#minimalist').addClass('resultListTable');
                $('#content').addClass('resultListTable');
         
                var content1 = '<thead><tr><th id="questionHeader" class="columnHeaderNoWrap" scope="col"><div>Question</div></th><th id="graphType" class="columnHeaderNoWrap" scope="col"><div>Graph Selected Results As</div></th><th id="null" scope="col"><div></div></th></tr></thead><tbody>';
                $('#minimalist').append(content1);
 
                $.each( surveyModel.getSurvey().categoryCollection, function( i, item ) { 
                           $.each(item.questionCollection, function(j,items) {
                                     if (items.objectName == 'ExclusiveQuestion'){ 
                                           var questionId = items.id; 
                                           var columnContentPie = '<span><td id="graphData" class="graphListButton">';
                                               columnContentPie += LOC.get('LOC_PIE_CHART');
                                               columnContentPie += '</td></span>';
                                                                                 }
                                     else                                        {
                                        var columnContentPie = '<td></td>';
                                                                                  }                    
                                     $('#minimalist').append( '<tr class="itemTextColor" id="dynamicRow' + j + '">'
                                                           + '<td>' + items.label + '</td>' + columnContentPie + '</tr>'); 
                                     if (items.objectName == 'ExclusiveQuestion'){  
                                             $('#graphData').click( function(){ getGraphData(questionId); return false;} );  
                                                                                 }
                                                                              }); 
                                                                                         });  
              $('#minimalist').append('</tbody>')                
                                  }



      function getGraphData(questionId){  
          if( isAllResults ) { 
               var contentUrl = 'listData/graphall?questionId='+questionId;
               var getJSONQuery = $.getJSON( contentUrl, function(item){
                            renderGraphData(item.data);
                                 });
               getJSONQuery.error(Utils.redirectIfUnauthorized) ;
                              }
          else                {   
               // get the selected results        
               var contentUrl = 'listData/graphselected?questionId='+questionId+'&ids=' + resultList.join(',');
               var getJSONQuery = $.getJSON( contentUrl, function(item){
                            renderGraphData(item.data);
                              });
               getJSONQuery.error(Utils.redirectIfUnauthorized) ;
                              }
                            }
      
              
      function renderGraphData(data){
               $('#minimalist').empty();
               $('#minimalist').replaceWith('<div id="canvas"></div>');

               var output = jQuery.jqplot ('canvas', [data], 
                  { 
                     seriesDefaults: {
                     // Make this a pie chart.
                     renderer: jQuery.jqplot.PieRenderer, 
                     rendererOptions:  {
                     // Put data labels on the pie slices.
                     // By default, labels show the percentage of the slice.
                     showDataLabels: true
                                       }
                                     }, 
                     legend: { show:true, location: 'e' }
                   }
                                          );  

               if (!$.jqplot.use_excanvas) {
        $('div.jqplot-target').each(function(){

            // Add a view image button
            var btn = $(document.createElement('button'));
            btn.text('View as PNG');
            btn.addClass('resultListButton');
            btn.bind('click', {chart: $(this)}, function(evt) {
               //evt.data.chart.jqplotViewImage();
               var elem = evt.data.chart.jqplotToImageElem();
               var content = '<p style="display: none"><b>Right/Control Click the Image to Download.<b><br><img src="'+elem.src+'" width="820"/></div>';
                             $('#canvas').empty();
                             $('.div.jqplot-target').empty();
                             $('.resultListButton').remove();
                             $('#canvas').prepend(content);
                             $("p").show("slow");
                                                              });

            $(this).after(btn);   
         
            // add a save image button unless its IE
            if (isIE == false){                                           
            btn = $(document.createElement('button'));
            btn.text('Save as PNG');
            btn.addClass('resultListButton');
            btn.bind('click', {chart: $(this)}, function(evt) {
            evt.data.chart.jqplotSaveImage();
                                                              });
            $(this).after(btn);
            btn = null;
                                }
                                             });
                                        }
                                    }


}();                   


