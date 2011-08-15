
/* Author: Ian Lawrence*/


$(document).ready(function(){

$('#toolsover').hide();
$('#minimalistover').hide()

$('#minimalist').mouseover(function(e){
$('#toolsover').show();
$('#minimalistover').show();
});

$('#minimalist').mouseout(function(e){
$('#toolsover').hide();
$('#minimalistover').hide();
});

});




























