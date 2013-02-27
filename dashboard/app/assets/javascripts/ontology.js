// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

function addOntologyLink2Element(form_id, ontology_id, method) {
	$('#' + form_id).attr("action", "/ontologies/" + ontology_id + "/" + method);
	alert($('#' + form_id).attr("action"));
}