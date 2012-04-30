var Mapping = function() {

    var surveyId;

    return { mapAllResults : function(currentSurveyId) { mapAllResults(currentSurveyId); },
             mapResults : function(currentSurveyId, selectedResults) { mapResults(currentSurveyId, selectedResults); } };

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
        $('#minimalist').height('500px');
        $('#minimalist').width('810px');

        var mapDiv = '<div id="map" style="width:100%; height:100%;"></div>';
        $('#minimalist').append(mapDiv);

        nokia.Settings.set( "appId", "7s7qhRnP7jg2SK34tw7Y");
        nokia.Settings.set( "authenticationToken", "x353yEXgA8JNbPs2tDtM7g");

        var mapContainer = document.getElementById("map");

        var infoBubbles = new nokia.maps.map.component.InfoBubbles();

        var map = new nokia.maps.map.Display(mapContainer, {
	        center: [parseFloat(data.items[0].latitude), parseFloat(data.items[0].longitude)],
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
		        new nokia.maps.map.component.ContextMenu()
            ]
        });

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

    function setupLeftColumn() {
        var columnContent = '<div class="resultListLeftMenu">';
            columnContent += '<span id ="backResultsFromMap" class="buttonBack"></span>';
            columnContent +='</div>';

        $('#leftColumn').append( columnContent );

        $('#backResultsFromMap').click( function() {
                               $('#searchComboBox').show();
                               $('#searchTextField').show();
                               ResultList.showResultList(surveyId);
        });
    }

}();
