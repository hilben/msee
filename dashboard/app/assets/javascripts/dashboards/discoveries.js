// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
//= require autocomplete/jquery.fcbkcomplete.js
//= require bootstrap/bootstrap-collapse

$(document).ready(function(){
	$("#categoryList").fcbkcomplete({
		json_url: "/dashboards/discovery/categories",
		addontab: true,
		maxitems: 10,
		input_min_size: 0,
		height: 10,
		cache: true,
		newel: true,
		select_all_text: "add all categories"

	});
	
	var method = getUrlVar(window.location.href)["method"];	
    $("#collapse_" + method).collapse('show');    
});
