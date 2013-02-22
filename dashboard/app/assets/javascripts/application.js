// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
// or vendor/assets/javascripts of plugins, if any, can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// the compiled file.
//
// WARNING: THE FIRST BLANK LINE MARKS THE END OF WHAT'S TO BE PROCESSED, ANY BLANK LINE SHOULD
// GO AFTER THE REQUIRES BELOW.
//
//= require jquery
//= require jquery_ujs
//= require bootstrap/bootstrap-modal
//= require bootstrap/bootstrap-dropdown
//= require bootstrap/bootstrap-alert
//= require prettify/prettify
//= require star-rating/jquery.rating


function getUrlVar(url) {
	var vars = [], hash;
	var hashes;

	if ( url.indexOf("#!") >= 0 ) {	
		hashes = url.slice(url.indexOf('?') + 1, url.indexOf('#!')).split("&");
		var array = url.slice(url.indexOf('#!') + 2).split("&");				
		hashes = hashes.concat(array);	
	} else {
		hashes = url.slice(url.indexOf('?') + 1).split('&'); 
	}	
	for (var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}	
	
	return vars;
}