var Mapping = function() {

    var surveyId;

    return { mapAllResults : function(currentSurveyId) { mapAllResults(currentSurveyId); },
             mapResults : function(currentSurveyId, selectedResults) { mapResults(currentSurveyId, selectedResults); } 
           };

    function mapAllResults(currentSurveyId) {
        surveyId = currentSurveyId;
        isAllResults = true;
        showMap();
    }

    function mapResults(currentSurveyId, selectedResults) {
        surveyId = currentSurveyId;
        isAllResults = false;
        resultList = selectedResults;
        showMap();
    }

    function showMap() {
        $('#sectionTitle').text(LOC.get('LOC_MAP_VIEW'));
        $('#searchComboBox').unbind('click');
        $('#leftColumn').empty('resultListTable');
        $('#minimalist').empty();
        $('#searchComboBox').hide();
        $('#searchTextField').hide();
        $('#contentToolbar').hide();
        setupLeftColumn();
        getMapData();
    }

      function setupLeftColumn() {
        var columnContent = '<div class="resultCollectionQuantity clickableElem">';
            columnContent +='<span id ="resultCollectionQuantityString' + surveyId + '" class="buttonBack"></span>';
            columnContent +='</div>';

        $('#leftColumn').append( columnContent );

        $('#resultCollectionQuantityString' + surveyId ).click( surveyId, function() { 
                               $('.resultListButton').hide(); 
                               $('#map').replaceWith('<table id="minimalist" class="resultListTable"></table>');
                               $('#searchComboBox').show();
                               $('#searchTextField').show();
                               ResultList.showResultList(surveyId)
                                                                                    });
    }


    function getMapData() {
          if( isAllResults ) {
              var contentUrl = 'service/getAllResults?surveyId=' + surveyId;
              var getJSONQuery = $.getJSON( contentUrl, function(data) {
                                            createMap(data);
                                           });
              getJSONQuery.error(Utils.redirectIfUnauthorized);
          }
          else {
               var contentUrl = 'service/getResults?surveyId=' + surveyId + '&resultIDs=' + resultList.join(',');
               var getJSONQuery = $.getJSON( contentUrl, function(data) {
                                             createMap(data);
                                            });
               getJSONQuery.error(Utils.redirectIfUnauthorized);
          }
    }

    function createMap(data) {

        $('#datatable').height('500px');
        $('#datatable').width('810px');

        $('#datatable').empty();
       
        
        var mapDiv = '<div id="map" style="z-index: -1; width: 100%; height: 100%;"></div>'
        $('#datatable').append(mapDiv);
 
        
        nokia.Settings.set( "appId", "7s7qhRnP7jg2SK34tw7Y");
        nokia.Settings.set( "authenticationToken", "x353yEXgA8JNbPs2tDtM7g");

        var mapContainer = document.getElementById("map");

        var infoBubbles = new nokia.maps.map.component.InfoBubbles();

        var startLat = parseFloat(data.items[0].latitude);
        var startLong = parseFloat(data.items[0].longitude);


        var map = new nokia.maps.map.Display(mapContainer, {
	        center: [startLat, startLong],
        	zoomLevel: 4,
        	components: [
                infoBubbles,
		          new nokia.maps.map.component.ZoomBar(),
		          new nokia.maps.map.component.Behavior(),
		          new nokia.maps.map.component.TypeSelector(),
		          new nokia.maps.map.component.Traffic(),
		          new nokia.maps.map.component.PublicTransport(),
		          new nokia.maps.map.component.Overview(),
		          new nokia.maps.map.component.ScaleBar(),
		          new nokia.maps.positioning.component.Positioning(),
		          //new nokia.maps.map.component.ContextMenu()
            ]
        });  
        
        map.removeComponent(map.getComponentById("zoom.MouseWheel"));
       // console.log(nokia.maps.Features.getFeatureMap());
      
        $.each(data.items,function(i,item) {
            var standardMarker = new nokia.maps.map.StandardMarker([parseFloat(item.latitude), parseFloat(item.longitude)], {
                text: i + 1,
                brush: {
                    color: "#3a77ca"
                }
            });

            var TOUCH = nokia.maps.dom.Page.browser.touch,
                CLICK = TOUCH ? "tap" : "click";

            var endTime = new Date(parseInt(item.endTime));

            var html = "<div>" +
                    "<h2>Result information</h2>" +
                    "<br/><h3 style=\"color:white;\">Title: " + item.title      + "</h3>" +
                    "<br/><h3 style=\"color:white;\">End: "   + endTime.toDateString() + " " + endTime.toTimeString()+ "</h3>" +
                    "<br/><h3 style=\"color:white;\">Coord: " + item.latitude   + " " + item.longitude + "</h3></div>";

            standardMarker.addListener(
                                      CLICK,
                                      function (evt) {
                                          infoBubbles.addBubble(html, standardMarker.coordinate);
            });

            map.objects.add(standardMarker);
        });
    }

 

}();