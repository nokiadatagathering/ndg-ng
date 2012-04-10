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
               updateContainerSize: function() {updateContainerSize();},
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

        function updateContainerSize() {
          var totalHeight = $('#container').height();
          var tableHeight = $('#minimalist').height();

             if ( (totalHeight + tableHeight) < 400 ) {
                 $('#container').height( 400 );
             } else if ( $('#container').height() < totalHeight + tableHeight ) {
                 $('#container').height( totalHeight + tableHeight + 1 );
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

                                     switch(items.questionType.typeName){
                                       case 'select1':

                                          if(!items.relevant){
                                            var questionId = items.id;
                                            var columnContent = '<span class="graphRows"><td id="graphDataPie" class="graphListButton">';
                                                 columnContent += LOC.get('LOC_PIE_CHART');
                                                 columnContent += '</td></span>';
                                            break;
                                               }else{
                                            var columnContent = '<span class="graphRows"><td></td></span>';
                                            break;
                                                    }
                                        case 'int':
                                          var questionId = items.id;
                                          var columnContent = '<span class="graphRows"><td id="graphDataInt" class="graphListButton">';
                                               columnContent += LOC.get('LOC_BAR_CHART');
                                               columnContent += '</td></span>';
                                          break;
                                       default:
                                          var columnContent = '<span class="graphRows"><td></td></span>';
                                                                           }
                                  
                                     $('#minimalist').append( '<tr class="itemTextColor graphData" id="dynamicRow' + j + '">'
                                                           + '<td>' + items.label + '</td>' + columnContent + '</tr>'); 


                                     if (items.questionType.typeName == 'select1'){
                                             var type = 'select1';  
                                             $('#graphDataPie').click( function(){ getGraphData(questionId, type, items.label); return false;} );  
                                                                                 }
                                     if (items.questionType.typeName == 'int'){ 
                                             var type = 'int';   
                                             $('#graphDataInt').click( function(){ getGraphData(questionId, type, items.label); return false;} ); 
                                                                                 }

                                                                              }); 
                                                                                         });  
              $('#minimalist').append('</tbody>') 
              updateContainerSize();              
                                  }

  



      function getGraphData(questionId, type, questionlabel ){  
          if( isAllResults ) { 
               var contentUrl = 'listData/graphall?questionId='+questionId;
               var getJSONQuery = $.getJSON( contentUrl, function(item){
                            renderGraphData(item.data, type, questionlabel);
                                 });
               getJSONQuery.error(Utils.redirectIfUnauthorized) ;
                              }
          else                {   
               // get the selected results        
               var contentUrl = 'listData/graphselected?questionId='+questionId+'&ids=' + resultList.join(',');
               var getJSONQuery = $.getJSON( contentUrl, function(item){
                            renderGraphData(item.data, type, questionlabel);
                              });
               getJSONQuery.error(Utils.redirectIfUnauthorized) ;
                              }
                            }
      
              
      function renderGraphData(data, type, questionlabel){
               $('#minimalist').empty();
               $('#minimalist').replaceWith('<div id="canvas"></div>');
               switch (type){
                  case 'select1':
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
                                         
                  break;
                  case 'int':
               
                  //for(i = 0; i < data.length; i++) data[i].sort();
                  //alert(data);
                  var output = $.jqplot('canvas', [data], {
                    // Tell the plot to stack the bars.
                    seriesDefaults:{
                     renderer: jQuery.jqplot.BarRenderer,
                     pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                     rendererOptions: {
                                       },

                                   },
                     axesDefaults: {
                              tickOptions: {
                                   showGridline: false,
                                           },
                                   },
                     axes: {
                        xaxis: {
                                renderer: jQuery.jqplot.CategoryAxisRenderer,
                                label:questionlabel,
                                showTicks: false, 
                                showTickMarks: false,
                                rendererOptions:{sortMergedLabels:true},
                               },
                        yaxis: {
                                renderer: jQuery.jqplot.CategoryAxisRenderer,
                                label:'Total Answers',
                                showTicks: false, 
                                showTickMarks: false,
                               }
                           },
     
                   });
                                         
                  break;
                  default:
                            } //end of switch
                

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


