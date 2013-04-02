// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
//= require autocomplete/jquery.fcbkcomplete.js
//= require bootstrap/bootstrap-collapse

$(document).ready(function(){
	$("#categoryList").fcbkcomplete({
		select_all_text: "add all categories"
		json_url: "/dashboards/discovery/categories",
		addontab: true,
		maxitems: 10,
		input_min_size: 0,
		height: 10,
		cache: true,
		newel: true,
	});
	
	var method = getUrlVar(window.location.href)["method"];	
    $("#collapse_" + method).collapse('show');    
});
