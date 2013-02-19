// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
function addOntologyLink2Element(e,t,n){$("#"+e).attr("action","/ontologies/"+t+"/"+n),alert($("#"+e).attr("action"))};