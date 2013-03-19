$(function(){module("bootstrap-popover"),test("should be defined on jquery object",function(){var e=$("<div></div>");ok(e.popover,"popover method is defined")}),test("should return element",function(){var e=$("<div></div>");ok(e.popover()==e,"document.body returned")}),test("should render popover element",function(){$.support.transition=!1;var e=$('<a href="#" title="mdo" data-content="http://twitter.com/mdo">@mdo</a>').appendTo("#qunit-fixture").popover("show");ok($(".popover").length,"popover was inserted"),e.popover("hide"),ok(!$(".popover").length,"popover removed")}),test("should store popover instance in popover data object",function(){$.support.transition=!1;var e=$('<a href="#" title="mdo" data-content="http://twitter.com/mdo">@mdo</a>').popover();ok(!!e.data("popover"),"popover instance exists")}),test("should get title and content from options",function(){$.support.transition=!1;var e=$('<a href="#">@fat</a>').appendTo("#qunit-fixture").popover({title:function(){return"@fat"},content:function(){return"loves writing tests （╯°□°）╯︵ ┻━┻"}});e.popover("show"),ok($(".popover").length,"popover was inserted"),equals($(".popover .popover-title").text(),"@fat","title correctly inserted"),equals($(".popover .popover-content").text(),"loves writing tests （╯°□°）╯︵ ┻━┻","content correctly inserted"),e.popover("hide"),ok(!$(".popover").length,"popover was removed"),$("#qunit-fixture").empty()}),test("should get title and content from attributes",function(){$.support.transition=!1;var e=$('<a href="#" title="@mdo" data-content="loves data attributes (づ｡◕‿‿◕｡)づ ︵ ┻━┻" >@mdo</a>').appendTo("#qunit-fixture").popover().popover("show");ok($(".popover").length,"popover was inserted"),equals($(".popover .popover-title").text(),"@mdo","title correctly inserted"),equals($(".popover .popover-content").text(),"loves data attributes (づ｡◕‿‿◕｡)づ ︵ ┻━┻","content correctly inserted"),e.popover("hide"),ok(!$(".popover").length,"popover was removed"),$("#qunit-fixture").empty()}),test("should respect custom classes",function(){$.support.transition=!1;var e=$('<a href="#">@fat</a>').appendTo("#qunit-fixture").popover({title:"Test",content:"Test",template:'<div class="popover foobar"><div class="arrow"></div><div class="inner"><h3 class="title"></h3><div class="content"><p></p></div></div></div>'});e.popover("show"),ok($(".popover").length,"popover was inserted"),ok($(".popover").hasClass("foobar"),"custom class is present"),e.popover("hide"),ok(!$(".popover").length,"popover was removed"),$("#qunit-fixture").empty()})});