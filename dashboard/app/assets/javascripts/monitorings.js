// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
//= require jquery-ui

//= require jquery/jquery
//= require jquery/jquery.min


//= require jquery/jquery-ui.custom
//= require jquery/jquery-ui.custom.min
//= require jquery/jquery.dynatree-1.2.2
//= require jquery/jquery.cookie.js

//= require star-rating/jquery.rating

//= require google/monitoring-jsapi

var currentSelectedEndpoint;
var checkedEndpoints;
var currentSelectedQoSParams;

$(document).ready(function() {





    console.log("LOADED MONITORING.JS");
    currentSelectedQoSParams = ["RequestTotal"];


    //Load and render the qosParamsCheckBoxes
    $('.qosParamsCheckBoxes').load('/monitorings/setSelectedQoSParams', function() {

        //Check all current selected checkboxes
        $('input:checkbox.qoscheckbox').each(function() {
            $(this).click(function() {
                setCurrentSelectedQoSParams();
            });

            if (jQuery.inArray($(this).attr("id"), currentSelectedQoSParams) >= 0) {
                $(this).attr("checked", true);
            }

        })
    });

    $("#invokebutton").click(function () {
        $('#invocationModal').modal();
        $('input.serviceID').val(currentSelectedEndpoint);
    });


    $("#registrationbutton").click(function () {
        $('#registrationModal').modal();

        // location.reload();
    });


    $("#rankbutton").click(function() {
        console.info("Rank the following endpoints : " + checkedEndpoints);
        $('#rankingSetQoSParams').modal();
        $('.rankingrateparams').load('/monitorings/qoSParamsRanking', {
            checkedEndpoints: checkedEndpoints
        });


    });

    $("#startranking").click(function() {

        $('#rankingSetQoSParams').modal('hide');
        $('#rankingResultModal').modal();

        var paramKeys = [];
        var paramsValues = [];

        //add endpoints and values:
        $('.star').each(function() {
            if ($(this).attr("value")>=0&&$(this).attr("checked")) {
                paramKeys.push( $(this).attr("name"));
                paramsValues.push($(this).attr("value"));
            }
        });

        st = $('textarea#servicetemplate').val();
        inst = $('textarea#serviceinstances').val();

        console.info("st "+st);
        console.info("inst "+inst);

        $('.rankedendpoints').load('/monitorings/doRanking',{
            checkedEndpoints: checkedEndpoints,
            keys: paramKeys,
        values: paramsValues, servicetemplate: st, serviceinstances: inst});

    });

    $('#my-accordion').on('show hide', function() {
        $(this).css('height', 'auto');
    });

   // $('#my-accordion').collapse({ parent: true, toggle: true }); 

    $("#tree").dynatree({

        checkbox : true,
        selectMode : 2,

        title : "Lazy loading tree",
        autoFocus : false, // Set focus to first child, when expanding or lazy-loading.

        initAjax : {
            url : "/monitorings/getSubcategoriesAndServices/root",
            data : {
                mode : "funnyMode"
            }
        },
        onQuerySelect : function(select, node) {
            if (node.data.isFolder)
                return false;
            },

            onSelect : function(select, node) {
                checkedEndpoints = $.map(node.tree.getSelectedNodes(), function(node) {
                    return node.data.key;
                });
                console.log("You checked " + checkedEndpoints);
            },

            onActivate : function(node) {
                console.log("You activated " + node);
                document.getElementById('statustext').textContent = "Loading monitoring data...";
                currentSelectedEndpoint = node.data.title;


                updateEndpointDetails();

                updateGraph();
            },

            onLazyRead : function(node) {
                node.appendAjax({
                    url : "/monitorings/getSubcategoriesAndServices/" + node.data.title,
                    data : {
                        mode : "funnyMode",
                    }
                });

            }
        });

    });

    function updateEndpointDetails() {
        console.info("updateEndpointDetails");
         $('#servicedetails').load('/monitorings/getServiceDetails',{
            currentSelectedEndpoint: currentSelectedEndpoint});
    }


    function updateGraph() {
        //$('.endpointdetails').load('/monitorings/showEndpointDetails/' + currentSelectedQoSParams + '/' + node.data.title.replace("http://", ""));
        $('#table').empty();

        $.ajax({
            type: 'POST',
            url: '/monitorings/getGoogleGraphData',
            data: {
                'qos': currentSelectedQoSParams,
                'url': currentSelectedEndpoint
            },

            success:  function(data) {
                console.log("call success!");

                console.log("not parsed: " + data);

                eval("var a = " + data + ";")

                console.log("Parsed: " + a);

                console.log("Load qos Params:");
                console.log(currentSelectedQoSParams);

                console.log("Finished...");

                if (data.indexOf("Date") >= 0) {// check if at least 1 date element is inside

                    document.getElementById('statustext').textContent = "Drawing graph";
                    console.log("Loading Graph:");
                    drawLineChart(a);
                } else {
                    document.getElementById('statustext').textContent = "No monitoring data available ";
                }

            },
            error: function(xhr, error){
                console.debug(xhr);
                console.debug(error);
            }
        });
    }

    google.load('visualization', '1.0', {
        packages : ['corechart', 'table', 'controls']
    });

    function drawLineChart(jsondata) {

        var JSONObject = jsondata;

        console.log("JSONObject: " + JSONObject);
        var data = new google.visualization.DataTable(JSONObject);

        var formatter = new google.visualization.DateFormat({
            formatType : 'short'
        });
        formatter.format(data, 0);

        // Create and draw the visualization.
        visualization = new google.visualization.LineChart(document.getElementById('table')).draw(data, {
            width : 1400,
            height : 800,
        });


        document.getElementById('statustext').textContent = "Finished graph";
    }

    /*
    * Sets and updates the current selected QoSParams for the monitoring graph component
    */
    function setCurrentSelectedQoSParams() {
        var values = $('input:checkbox:checked.qoscheckbox').map(function() {
            return this.id;
        }).get();
        console.log(values);
        currentSelectedQoSParams = values;

        updateGraph();
    }

