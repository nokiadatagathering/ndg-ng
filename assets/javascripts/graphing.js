/*
 * File encapsulates action related to Graphing
 *
 **/

var Graphing = function() {

    return {setupGraph : function() {setupGraph();}

                          };

    function setupGraph(){
        $('#sectionTitle').text(LOC.get('LOC_GRAPH_VIEW'));
        $('#searchComboBox').unbind('click');
        $('#leftColumn').empty('resultListTable');
        $('#minimalist').empty();
        $('#search').empty();
        $('#contentToolbar').hide();
        setupLeftColumn();
        prepareGraph();
                        }

     function prepareGraph() {
        $('#minimalist').empty();
        $('#minimalist').height('400px');
        $('#minimalist').width('820px');

        // data
    var data = [
        { label: "Question 1",  data: 10},
        { label: "Question 2",  data: 30},
        { label: "Question 3",  data: 90},
        { label: "Question 4",  data: 70},
        { label: "Question 5",  data: 80},
        { label: "Question 6",  data: 110}
    ];

    var data = [];
    var series = Math.floor(Math.random()*10)+1;
    for( var i = 0; i<series; i++)
    {
        data[i] = { label: "Question "+(i+1), data: Math.floor(Math.random()*100)+1 }
    }


        // draw the graph
         $.plot($("#minimalist"), data,
    {
        series: {
            pie: {
                show: true
            }
        }
    });

        // add some extra content
        var extraGraphContent = '<div class="itemTextColor">extra graph content</div>';
        $('#minimalist').append(extraGraphContent);
                             }


    function setupLeftColumn() {
        var columnContent = '<div class="graphLeftMenu">';
        columnContent += '<span id ="backHome" class="buttonBack"></span>';
        columnContent +=  '</div>';

        $('#leftColumn').append( columnContent );
        $('#backHome').click(function(){ alert("bACK"); } );

                               }

}();
