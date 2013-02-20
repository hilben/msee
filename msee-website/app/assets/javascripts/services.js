// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
//= require bootstrap/bootstrap-collapse

function addServiceLink2Element(form_id, service_id, method) {
	$('#' + form_id).attr("action", "/services/" + service_id + "/" + method);
	alert($('#' + form_id).attr("action"));
}
