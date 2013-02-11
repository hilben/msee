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

//= require bootstrap/bootstrap-dropdown
//= require bootstrap/bootstrap-transition
//= require bootstrap/bootstrap.min

//= require google/monitoring-jsapi



var currentSelectedEndpoint;
var checkedEndpoints;
var currentSelectedQoSParams;

$(document).ready(function() {

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

    $("#rankbutton").click(function() {
        // alert("Rank the following endpoints : " + checkedEndpoints);

        $('#rankingSetQoSParams').modal();
        $('.rankingrateparams').load('/monitorings/qoSParamsRanking');
        $('.ratetest').load('/monitorings/qoSParamsRanking');

    });

    $("#startranking").click(function() { 
        
        $('#rankingSetQoSParams').modal('hide');
        $('#rankingResultModal').modal();
       // $('.rankedendpoints').load('/monitorings/getRankedEndpoints/ResponseTime,PayloadSizeResponse/0x4,7x1/' + checkedEndpoints);


        var querystring='';
        var qosParamsCounts=0;
        var qosRatingCounts=0;
        //add endpoints:
        $('.star').each(function() {
            if ($(this).attr("value")>=0&&$(this).attr("checked")) {

                
                if (querystring!='') {
                    querystring+=',';
                }

                querystring = querystring + $(this).attr("name");
                
                qosParamsCounts+=1;
            }
        });
        
        querystring += "/";

        //add values:
        $('.star').each(function() {
            if ($(this).attr("value")>=0&&$(this).attr("checked")) {
                
                if (qosRatingCounts>0) {
                    querystring+=',';
                }

                querystring = querystring + $(this).attr("value")+"x0";
                
                qosRatingCounts+=1;
            }
        });


        if (qosRatingCounts!=qosParamsCounts||qosParamsCounts<1) {
            alert("Error with the Ranking. Did you selected Parameters?" + qosRatingCounts+" "+qosParamsCounts);
        } else {
        $('.rankedendpoints').load('/monitorings/getRankedEndpoints/'+querystring+'/' + checkedEndpoints);
        }
    });    




       

    $('#accordion').collapse().height('auto');

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
            $('.endpointdetails').load('/monitorings/showEndpointDetails/' + currentSelectedQoSParams + '/' + node.data.title.replace("http://", ""));

        },

        onLazyRead : function(node) {
            node.appendAjax({
                url : "/monitorings/getSubcategoriesAndServices/" + node.data.title,
                data : {
//                    key: node.data.key,
                    mode : "funnyMode",
                }
            });

        }
    });

});

google.load('visualization', '1.0', {
    packages : ['corechart', 'table', 'controls']
});

function drawLineChart(jsondata) {

    var JSONObject = jsondata;

    // Create and populate the data table.
    var data = new google.visualization.DataTable(JSONObject);

    var formatter = new google.visualization.DateFormat({
        formatType : 'short'
    });
    formatter.format(data, 0);
    // newgraphtest(data);

    // Create and draw the visualization.
    visualization = new google.visualization.LineChart(document.getElementById('table')).draw(data, {
        width : 800,
        height : 600,
    });

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
    $('.endpointdetails').load('/monitorings/showEndpointDetails/' + currentSelectedQoSParams + '/' + currentSelectedEndpoint.replace("http://", ""));
}

function ajaxCallSucceed(json) {
    console.log("call success!");

    var a = eval(json);

    console.log("Eval: " + a);

    console.log("Load qos Params:");
    console.log(currentSelectedQoSParams);

    console.log("Finished...");

    if (a.rows.length > 0) {

        document.getElementById('statustext').textContent = "Drawing graph";
        console.log("Loading Graph:");
        drawLineChart(a);
    } else {
        document.getElementById('statustext').textContent = "No monitoring data available ";
    }
}

