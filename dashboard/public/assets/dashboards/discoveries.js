!function(e){e.fn.fcbkcomplete=function(t){return this.queue(function(){function n(){r(),l(0)}function r(){if(N=e('<ul class="holder"></ul>').width(T.width),T.attachto?"object"==typeof T.attachto?T.attachto.append(N):e(T.attachto).append(N):$.after(N),E=e('<div class="facebook-auto">').width(T.width),""!=T.complete_text){var t=T.complete_text;E.append('<div class="default">'+t+"</div>"),T.select_all_text&&E.children(".default").append(e('<a href="" class="select_all_items">'+T.select_all_text+"</a>").click(function(){return e($).trigger("selectAll"),!1}))}E.hover(function(){L=0},function(){L=1}),S=e('<ul id="'+q+'_feed"></ul>').width(T.width),N.after(E.prepend(S)),a()}function a(){name=$.attr("name"),T.bricket&&"undefined"!=typeof name&&-1==name.indexOf("[]")&&(name+="[]");var t=e("<"+$.get(0).tagName+' name="'+name+'" id="'+q+'" multiple="multiple" class="'+$.get(0).className+' hidden">').data("cache",{});e.each($.children("option"),function(n,r){if(r=e(r),t.data("cache")[r.val()]=r.text(),r.hasClass("selected")){var i=o(r.text(),r.val(),!0,r.hasClass("locked"));t.append('<option value="'+r.val()+'" selected="selected" id="opt_'+i+'"class="selected">'+r.text()+"</option>")}}),$.after(t),$.remove(),$=t,e($).bind("addItem",function(e,t){o(t.title,t.value,0,0,0)}),e($).bind("removeItem",function(e,t){var n=N.children("li[rel="+t.value+"]");n.length&&s(n)}),e($).bind("destroy",function(){N.remove(),E.remove(),$.show()}),e($).bind("selectAll",function(){var t=e($).val()||[];e.each(e($).data("cache"),function(n,r){-1===e.inArray(n,t)&&o(r,n,0,0,0)}),S.parent().hide()})}function o(t,n,r,i,a){if(!g())return!1;var o="bit-box"+(i?" locked":""),u=R(),c=document.createTextNode(w(t)),d=e('<a class="closebutton" href="#"></a>'),f=e('<li class="'+o+'" rel="'+n+'" id="pt_'+u+'"></li>').prepend(c).append(d);if(N.append(f),d.click(function(){return s(e(this).parent("li")),!1}),!r){e("#"+q+"_annoninput").remove(),l(a);var p=e('<option value="'+w(n,1)+'" id="opt_'+u+'" class="selected" selected="selected">'+w(t)+"</option>");$.append(p),T.onselect&&v(T.onselect,p),$.change()}return N.children("li.bit-box.deleted").removeClass("deleted"),C(1),u}function s(t){if(!t.hasClass("locked")){t.fadeOut("fast");var n=t.attr("id");if(T.onremove){var r=n?e("#o"+n):$.children("option[value="+t.attr("rel")+"]");v(T.onremove,r)}n?e("#o"+n).remove():$.children('option[value="'+t.attr("rel")+'"]').remove(),t.remove(),$.change(),D=0}}function l(t){var n=e('<li class="bit-input" id="'+q+'_annoninput">'),r=e('<input type="text" class="maininput" size="'+T.input_min_size+'" autocomplete="off">');T.input_tabindex>0&&r.attr("tabindex",T.input_tabindex),""!=T.input_name&&r.attr("name",T.input_name),N.append(n.append(r)),r.focus(function(){j=!0,g()&&E.fadeIn("fast")}),r.blur(function(){j=!1,L?E.fadeOut("fast"):r.focus()}),N.click(function(){T.input_min_size<0&&S.length&&k(x(r.val(),1)),r.focus(),S.length&&r.val().length>T.input_min_size?S.show():(C(1),E.children(".default").show())}),r.keypress(function(e){if(e.keyCode==M.enter)return!1;var t=T.input_min_size>r.val().length?T.input_min_size:r.val().length+1;r.attr("size",t).width(parseInt(r.css("font-size"))*t)}),r.keyup(function(t){var n=x(r.val(),1);if(t.keyCode==M.backspace&&0==n.length&&(C(1),!N.children("li.bit-box:last").hasClass("locked"))){if(0==N.children("li.bit-box.deleted").length)return N.children("li.bit-box:last").addClass("deleted"),!1;if(D)return;D=1,N.children("li.bit-box.deleted").fadeOut("fast",function(){return s(e(this)),!1})}t.keyCode!=M.downarrow&&t.keyCode!=M.uparrow&&t.keyCode!=M.leftarrow&&t.keyCode!=M.rightarrow&&n.length>T.input_min_size&&(k(n),E.children(".default").hide(),S.show())}),T.oncreate&&v(T.oncreate,r),t&&setTimeout(function(){r.focus(),E.children(".default").show()},1)}function u(t,n){S.html(""),T.cache||null==n||P.clear(),y(t),null!=n&&n.length&&e.each(n,function(e,t){P.set(x(t.key),x(t.value))});var r=T.maxshownitems<P.length()?T.maxshownitems:P.length(),i="";e.each(P.search(t),function(e,n){r&&(T.filter_selected&&$.children('option[value="'+n.key+'"]').hasClass("selected")||(i+='<li rel="'+n.key+'">'+w(c(n.value,t))+"</li>",A++,r--))}),S.append(i),T.firstselected&&(_=S.children("li:visible:first"),_.addClass("auto-focus")),A>T.height?S.css({height:24*T.height+"px",overflow:"auto"}):S.css("height","auto"),g()&&E.is(":hidden")&&E.show()}function c(e,t){var n=T.filter_begin?"":"(.*)",r=T.filter_begin?"<em>$1</em>$2":"$1<em>$2</em>$3",i=n+(T.filter_case?"("+t+")(.*)":"("+t.toLowerCase()+")(.*)");try{var a=new RegExp(i,T.filter_case?"g":"gi"),e=e.replace(a,r)}catch(o){}return e}function d(){S.children("li").mouseover(function(){S.children("li").removeClass("auto-focus"),_=e(this),_.addClass("auto-focus")}),S.children("li").mouseout(function(){e(this).removeClass("auto-focus"),_=null})}function f(){S.unbind("mouseover").unbind("mouseout").mousemove(function(){d(),S.unbind("mousemove")})}function p(){var t=e("#"+q+"_annoninput").children(".maininput");d(),S.children("li").unbind("mousedown").mousedown(function(){var t=e(this);o(t.text(),t.attr("rel"),0,0,1),C(1),E.hide()}),t.unbind("keydown"),t.keydown(function(t){if(t.keyCode!=M.backspace&&N.children("li.bit-box.deleted").removeClass("deleted"),(t.keyCode==M.enter||t.keyCode==M.tab||t.keyCode==M.comma)&&b()){var n=_;return o(n.text(),n.attr("rel"),0,0,1),m(t)}if((t.keyCode==M.enter||t.keyCode==M.tab||t.keyCode==M.comma)&&!b()){if(T.newel){var r=x(e(this).val());return o(r,r,0,0,1),m(t)}if((T.addontab||T.addoncomma)&&T.newel){_=S.children("li:visible:first");var n=_;return o(n.text(),n.attr("rel"),0,0,1),m(t)}}t.keyCode==M.downarrow&&h("first"),t.keyCode==M.uparrow&&h("last")})}function h(e){if(f(),null==_||0==_.length)_=S.children("li:visible:"+e),S.get(0).scrollTop="first"==e?0:parseInt(_.get(0).scrollHeight,10)*(parseInt(S.children("li:visible").length,10)-Math.round(T.height/2));else{_.removeClass("auto-focus"),_="first"==e?_.nextAll("li:visible:first"):_.prevAll("li:visible:first");var t=parseInt(_.prevAll("li:visible").length,10),n=parseInt(_.nextAll("li:visible").length,10);(("first"==e?t:n)>Math.round(T.height/2)||("first"==e?t:n)<=Math.round(T.height/2))&&"undefined"!=typeof _.get(0)&&(S.get(0).scrollTop=parseInt(_.get(0).scrollHeight,10)*(t-Math.round(T.height/2)))}S.children("li").removeClass("auto-focus"),_.addClass("auto-focus")}function m(e){return E.hide(),e.preventDefault(),_=null,!1}function g(){return 0!=T.maxitems&&N.children("li.bit-box").length<T.maxitems}function y(t){if(T.newel&&g()){if(S.children("li[fckb=1]").remove(),0==t.length)return;var n=e('<li rel="'+t+'" fckb="1">').html(w(t));S.prepend(n),A++}}function v(e,t){var n={};for(i=0;i<t.get(0).attributes.length;i++)null!=t.get(0).attributes[i].nodeValue&&(n["_"+t.get(0).attributes[i].nodeName]=t.get(0).attributes[i].nodeValue);return e.call(e,n)}function b(){return null==_||0==_.length?!1:!0}function x(e,t){if("undefined"!=typeof t){for(i=0;i<e.length;i++){var n=e.charCodeAt(i);(M.exclamation<=n&&n<=M.slash||M.colon<=n&&n<=M.at||M.squarebricket_left<=n&&n<=M.apostrof)&&(e=e.replace(e[i],escape(e[i])))}e=e.replace(/(\{|\}|\*)/i,"\\$1")}return e.replace(/script(.*)/g,"")}function w(e,t){return e=e.toString(),e=e.replace("\\",""),"undefined"!=typeof t?e:unescape(e)}function C(e){S.children().remove(),e&&S.hide()}function k(t){if(A=0,T.json_url&&g())if(T.cache&&O.get(t))u(t),p();else{H++;var n=H;setTimeout(function(){n==H&&e.getJSON(T.json_url,{tag:w(t)},function(e){j&&(u(t,e),O.set(t,1),p())})},T.delay)}else u(t),p()}var T=e.extend({json_url:null,width:512,cache:!1,height:"10",newel:!1,addontab:!1,addoncomma:!1,firstselected:!1,filter_case:!1,filter_selected:!1,filter_begin:!1,complete_text:"Start to type...",select_all_text:null,maxshownitems:30,maxitems:10,oncreate:null,onselect:null,onremove:null,attachto:null,delay:350,input_tabindex:0,input_min_size:1,input_name:"",bricket:!0},t),N=null,S=null,E=null,A=0,j=!1,_=null,D=0,L=1,$=e(this),q=$.attr("id"),H=0,O={set:function(e,t){var n=$.data("jsoncache");n[e]=t,$.data("jsoncache",n)},get:function(e){return"undefined"!=$.data("jsoncache")[e]?$.data("jsoncache")[e]:null},init:function(){$.data("jsoncache",{})}},M={enter:13,tab:9,comma:188,backspace:8,leftarrow:37,uparrow:38,rightarrow:39,downarrow:40,exclamation:33,slash:47,colon:58,at:64,squarebricket_left:91,apostrof:96},R=function(){for(var e="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz",t="",n=0;32>n;n++){var r=Math.floor(Math.random()*e.length);t+=e.substring(r,r+1)}return t},P={search:function(t){var n=new Array,r=new RegExp((T.filter_begin?"^":"")+t,T.filter_case?"g":"gi");return e.each($.data("cache"),function(e,t){"function"==typeof t.search&&-1!=t.search(r)&&n.push({key:e,value:t})}),n},set:function(e,t){var n=$.data("cache");n[e]=t,$.data("cache",n)},get:function(e){return"undefined"!=$.data("cache")[e]?$.data("cache")[e]:null},clear:function(){$.data("cache",{})},length:function(){if("object"==typeof $.data("cache")){var e=0;for(i in $.data("cache"))e++;return e}return $.data("cache").length},init:function(){"undefined"==$.data("cache")&&$.data("cache",{})}};return n(),O.init(),P.init(),this})}}(jQuery),/* =============================================================
 * bootstrap-collapse.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#collapse
 * =============================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============================================================ */
!function(e){"use strict";var t=function(t,n){this.$element=e(t),this.options=e.extend({},e.fn.collapse.defaults,n),this.options.parent&&(this.$parent=e(this.options.parent)),this.options.toggle&&this.toggle()};t.prototype={constructor:t,dimension:function(){var e=this.$element.hasClass("width");return e?"width":"height"},show:function(){var t,n=this.dimension(),r=e.camelCase(["scroll",n].join("-")),i=this.$parent&&this.$parent.find(".in");i&&i.length&&(t=i.data("collapse"),i.collapse("hide"),t||i.data("collapse",null)),this.$element[n](0),this.transition("addClass","show","shown"),this.$element[n](this.$element[0][r])},hide:function(){var e=this.dimension();this.reset(this.$element[e]()),this.transition("removeClass","hide","hidden"),this.$element[e](0)},reset:function(e){var t=this.dimension();this.$element.removeClass("collapse")[t](e||"auto")[0].offsetWidth,this.$element.addClass("collapse")},transition:function(t,n,r){var i=this,a=function(){"show"==n&&i.reset(),i.$element.trigger(r)};this.$element.trigger(n)[t]("in"),e.support.transition&&this.$element.hasClass("collapse")?this.$element.one(e.support.transition.end,a):a()},toggle:function(){this[this.$element.hasClass("in")?"hide":"show"]()}},e.fn.collapse=function(n){return this.each(function(){var r=e(this),i=r.data("collapse"),a="object"==typeof n&&n;i||r.data("collapse",i=new t(this,a)),"string"==typeof n&&i[n]()})},e.fn.collapse.defaults={toggle:!0},e.fn.collapse.Constructor=t,e(function(){e("body").on("click.collapse.data-api","[data-toggle=collapse]",function(t){var n,r=e(this),i=r.attr("data-target")||t.preventDefault()||(n=r.attr("href"))&&n.replace(/.*(?=#[^\s]+$)/,""),a=e(i).data("collapse")?"toggle":r.data();e(i).collapse(a)})})}(window.jQuery),$(document).ready(function(){$("#collapse_discover").collapse("show"),$("#categoryList").fcbkcomplete({json_url:"/dashboards/discovery/categories",addontab:!0,maxitems:10,input_min_size:0,height:10,cache:!0,newel:!0,select_all_text:"add all categories"});var e=getUrlVar(window.location.href).method;$("#collapse_"+e).collapse("show")});