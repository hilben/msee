!function(e){e.fn.fcbkcomplete=function(t){return this.queue(function(){function n(){s(),l(0)}function s(){if(D=e('<ul class="holder"></ul>').width(C.width),C.attachto?"object"==typeof C.attachto?C.attachto.append(D):e(C.attachto).append(D):A.after(D),T=e('<div class="facebook-auto">').width(C.width),""!=C.complete_text){var t=C.complete_text;T.append('<div class="default">'+t+"</div>"),C.select_all_text&&T.children(".default").append(e('<a href="" class="select_all_items">'+C.select_all_text+"</a>").click(function(){return e(A).trigger("selectAll"),!1}))}T.hover(function(){M=0},function(){M=1}),j=e('<ul id="'+E+'_feed"></ul>').width(C.width),D.after(T.prepend(j)),a()}function a(){name=A.attr("name"),C.bricket&&"undefined"!=typeof name&&-1==name.indexOf("[]")&&(name+="[]");var t=e("<"+A.get(0).tagName+' name="'+name+'" id="'+E+'" multiple="multiple" class="'+A.get(0).className+' hidden">').data("cache",{});e.each(A.children("option"),function(i,n){if(n=e(n),t.data("cache")[n.val()]=n.text(),n.hasClass("selected")){var s=o(n.text(),n.val(),!0,n.hasClass("locked"));t.append('<option value="'+n.val()+'" selected="selected" id="opt_'+s+'"class="selected">'+n.text()+"</option>")}}),A.after(t),A.remove(),A=t,e(A).bind("addItem",function(e,t){o(t.title,t.value,0,0,0)}),e(A).bind("removeItem",function(e,t){var i=D.children("li[rel="+t.value+"]");i.length&&r(i)}),e(A).bind("destroy",function(){D.remove(),T.remove(),A.show()}),e(A).bind("selectAll",function(){var t=e(A).val()||[];e.each(e(A).data("cache"),function(i,n){-1===e.inArray(i,t)&&o(n,i,0,0,0)}),j.parent().hide()})}function o(t,i,n,s,a){if(!m())return!1;var o="bit-box"+(s?" locked":""),c=L(),h=document.createTextNode(x(t)),d=e('<a class="closebutton" href="#"></a>'),u=e('<li class="'+o+'" rel="'+i+'" id="pt_'+c+'"></li>').prepend(h).append(d);if(D.append(u),d.click(function(){return r(e(this).parent("li")),!1}),!n){e("#"+E+"_annoninput").remove(),l(a);var p=e('<option value="'+x(i,1)+'" id="opt_'+c+'" class="selected" selected="selected">'+x(t)+"</option>");A.append(p),C.onselect&&b(C.onselect,p),A.change()}return D.children("li.bit-box.deleted").removeClass("deleted"),w(1),c}function r(t){if(!t.hasClass("locked")){t.fadeOut("fast");var i=t.attr("id");if(C.onremove){var n=i?e("#o"+i):A.children("option[value="+t.attr("rel")+"]");b(C.onremove,n)}i?e("#o"+i).remove():A.children('option[value="'+t.attr("rel")+'"]').remove(),t.remove(),A.change(),P=0}}function l(t){var i=e('<li class="bit-input" id="'+E+'_annoninput">'),n=e('<input type="text" class="maininput" size="'+C.input_min_size+'" autocomplete="off">');C.input_tabindex>0&&n.attr("tabindex",C.input_tabindex),""!=C.input_name&&n.attr("name",C.input_name),D.append(i.append(n)),n.focus(function(){N=!0,m()&&T.fadeIn("fast")}),n.blur(function(){N=!1,M?T.fadeOut("fast"):n.focus()}),D.click(function(){C.input_min_size<0&&j.length&&k(_(n.val(),1)),n.focus(),j.length&&n.val().length>C.input_min_size?j.show():(w(1),T.children(".default").show())}),n.keypress(function(e){if(e.keyCode==H.enter)return!1;var t=C.input_min_size>n.val().length?C.input_min_size:n.val().length+1;n.attr("size",t).width(parseInt(n.css("font-size"))*t)}),n.keyup(function(t){var i=_(n.val(),1);if(t.keyCode==H.backspace&&0==i.length&&(w(1),!D.children("li.bit-box:last").hasClass("locked"))){if(0==D.children("li.bit-box.deleted").length)return D.children("li.bit-box:last").addClass("deleted"),!1;if(P)return;P=1,D.children("li.bit-box.deleted").fadeOut("fast",function(){return r(e(this)),!1})}t.keyCode!=H.downarrow&&t.keyCode!=H.uparrow&&t.keyCode!=H.leftarrow&&t.keyCode!=H.rightarrow&&i.length>C.input_min_size&&(k(i),T.children(".default").hide(),j.show())}),C.oncreate&&b(C.oncreate,n),t&&setTimeout(function(){n.focus(),T.children(".default").show()},1)}function c(t,i){j.html(""),C.cache||null==i||O.clear(),v(t),null!=i&&i.length&&e.each(i,function(e,t){O.set(_(t.key),_(t.value))});var n=C.maxshownitems<O.length()?C.maxshownitems:O.length(),s="";e.each(O.search(t),function(e,i){n&&(C.filter_selected&&A.children('option[value="'+i.key+'"]').hasClass("selected")||(s+='<li rel="'+i.key+'">'+x(h(i.value,t))+"</li>",S++,n--))}),j.append(s),C.firstselected&&(I=j.children("li:visible:first"),I.addClass("auto-focus")),S>C.height?j.css({height:24*C.height+"px",overflow:"auto"}):j.css("height","auto"),m()&&T.is(":hidden")&&T.show()}function h(e,t){var i=C.filter_begin?"":"(.*)",n=C.filter_begin?"<em>$1</em>$2":"$1<em>$2</em>$3",s=i+(C.filter_case?"("+t+")(.*)":"("+t.toLowerCase()+")(.*)");try{var a=new RegExp(s,C.filter_case?"g":"gi"),e=e.replace(a,n)}catch(o){}return e}function d(){j.children("li").mouseover(function(){j.children("li").removeClass("auto-focus"),I=e(this),I.addClass("auto-focus")}),j.children("li").mouseout(function(){e(this).removeClass("auto-focus"),I=null})}function u(){j.unbind("mouseover").unbind("mouseout").mousemove(function(){d(),j.unbind("mousemove")})}function p(){var t=e("#"+E+"_annoninput").children(".maininput");d(),j.children("li").unbind("mousedown").mousedown(function(){var t=e(this);o(t.text(),t.attr("rel"),0,0,1),w(1),T.hide()}),t.unbind("keydown"),t.keydown(function(t){if(t.keyCode!=H.backspace&&D.children("li.bit-box.deleted").removeClass("deleted"),(t.keyCode==H.enter||t.keyCode==H.tab||t.keyCode==H.comma)&&y()){var i=I;return o(i.text(),i.attr("rel"),0,0,1),g(t)}if((t.keyCode==H.enter||t.keyCode==H.tab||t.keyCode==H.comma)&&!y()){if(C.newel){var n=_(e(this).val());return o(n,n,0,0,1),g(t)}if((C.addontab||C.addoncomma)&&C.newel){I=j.children("li:visible:first");var i=I;return o(i.text(),i.attr("rel"),0,0,1),g(t)}}t.keyCode==H.downarrow&&f("first"),t.keyCode==H.uparrow&&f("last")})}function f(e){if(u(),null==I||0==I.length)I=j.children("li:visible:"+e),j.get(0).scrollTop="first"==e?0:parseInt(I.get(0).scrollHeight,10)*(parseInt(j.children("li:visible").length,10)-Math.round(C.height/2));else{I.removeClass("auto-focus"),I="first"==e?I.nextAll("li:visible:first"):I.prevAll("li:visible:first");var t=parseInt(I.prevAll("li:visible").length,10),i=parseInt(I.nextAll("li:visible").length,10);(("first"==e?t:i)>Math.round(C.height/2)||("first"==e?t:i)<=Math.round(C.height/2))&&"undefined"!=typeof I.get(0)&&(j.get(0).scrollTop=parseInt(I.get(0).scrollHeight,10)*(t-Math.round(C.height/2)))}j.children("li").removeClass("auto-focus"),I.addClass("auto-focus")}function g(e){return T.hide(),e.preventDefault(),I=null,!1}function m(){return 0!=C.maxitems&&D.children("li.bit-box").length<C.maxitems}function v(t){if(C.newel&&m()){if(j.children("li[fckb=1]").remove(),0==t.length)return;var i=e('<li rel="'+t+'" fckb="1">').html(x(t));j.prepend(i),S++}}function b(e,t){var n={};for(i=0;i<t.get(0).attributes.length;i++)null!=t.get(0).attributes[i].nodeValue&&(n["_"+t.get(0).attributes[i].nodeName]=t.get(0).attributes[i].nodeValue);return e.call(e,n)}function y(){return null==I||0==I.length?!1:!0}function _(e,t){if("undefined"!=typeof t){for(i=0;i<e.length;i++){var n=e.charCodeAt(i);(H.exclamation<=n&&n<=H.slash||H.colon<=n&&n<=H.at||H.squarebricket_left<=n&&n<=H.apostrof)&&(e=e.replace(e[i],escape(e[i])))}e=e.replace(/(\{|\}|\*)/i,"\\$1")}return e.replace(/script(.*)/g,"")}function x(e,t){return e=e.toString(),e=e.replace("\\",""),"undefined"!=typeof t?e:unescape(e)}function w(e){j.children().remove(),e&&j.hide()}function k(t){if(S=0,C.json_url&&m())if(C.cache&&z.get(t))c(t),p();else{$++;var i=$;setTimeout(function(){i==$&&e.getJSON(C.json_url,{tag:x(t)},function(e){N&&(c(t,e),z.set(t,1),p())})},C.delay)}else c(t),p()}var C=e.extend({json_url:null,width:512,cache:!1,height:"10",newel:!1,addontab:!1,addoncomma:!1,firstselected:!1,filter_case:!1,filter_selected:!1,filter_begin:!1,complete_text:"Start to type...",select_all_text:null,maxshownitems:30,maxitems:10,oncreate:null,onselect:null,onremove:null,attachto:null,delay:350,input_tabindex:0,input_min_size:1,input_name:"",bricket:!0},t),D=null,j=null,T=null,S=0,N=!1,I=null,P=0,M=1,A=e(this),E=A.attr("id"),$=0,z={set:function(e,t){var i=A.data("jsoncache");i[e]=t,A.data("jsoncache",i)},get:function(e){return"undefined"!=A.data("jsoncache")[e]?A.data("jsoncache")[e]:null},init:function(){A.data("jsoncache",{})}},H={enter:13,tab:9,comma:188,backspace:8,leftarrow:37,uparrow:38,rightarrow:39,downarrow:40,exclamation:33,slash:47,colon:58,at:64,squarebricket_left:91,apostrof:96},L=function(){for(var e="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz",t="",i=0;32>i;i++){var n=Math.floor(Math.random()*e.length);t+=e.substring(n,n+1)}return t},O={search:function(t){var i=new Array,n=new RegExp((C.filter_begin?"^":"")+t,C.filter_case?"g":"gi");return e.each(A.data("cache"),function(e,t){"function"==typeof t.search&&-1!=t.search(n)&&i.push({key:e,value:t})}),i},set:function(e,t){var i=A.data("cache");i[e]=t,A.data("cache",i)},get:function(e){return"undefined"!=A.data("cache")[e]?A.data("cache")[e]:null},clear:function(){A.data("cache",{})},length:function(){if("object"==typeof A.data("cache")){var e=0;for(i in A.data("cache"))e++;return e}return A.data("cache").length},init:function(){"undefined"==A.data("cache")&&A.data("cache",{})}};return n(),z.init(),O.init(),this})}}(jQuery);