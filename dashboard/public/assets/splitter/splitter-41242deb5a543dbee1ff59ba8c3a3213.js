!function(e){e.fn.splitter=function(t){return t=t||{},this.each(function(){function i(t){c.outline&&(r=r||g.clone(!1).insertAfter(u)),d.css("-webkit-user-select","none"),g.addClass(c.activeClass),u._posSplit=u[0][c.pxSplit]-t[c.eventPos],e(document).bind("mousemove",n).bind("mouseup",s)}function n(e){var t=u._posSplit+e[c.eventPos];c.outline?(t=Math.max(0,Math.min(t,h._DA-g._DA)),g.css(c.origin,t)):o(t)}function s(t){g.removeClass(c.activeClass);var i=u._posSplit+t[c.eventPos];c.outline&&(r.remove(),r=null,o(i)),d.css("-webkit-user-select","text"),e(document).unbind("mousemove",n).unbind("mouseup",s)}function o(t){t=Math.max(u._min,h._DA-p._max,Math.min(t,u._max,h._DA-g._DA-p._min)),g._DA=g[0][c.pxSplit],g.css(c.origin,t).css(c.fixed,h._DF),u.css(c.origin,0).css(c.split,t).css(c.fixed,h._DF),p.css(c.origin,t+g._DA).css(c.split,h._DA-g._DA-t).css(c.fixed,h._DF),e.browser.msie||d.trigger("resize")}function a(e){for(var t=0,i=1;i<arguments.length;i++)t+=Math.max(parseInt(e.css(arguments[i]))||0,0);return t}var r,l=(t.splitHorizontal?"h":t.splitVertical?"v":t.type)||"v",c=e.extend({activeClass:"active",pxPerKey:8,tabIndex:0,accessKey:""},{v:{keyLeft:39,keyRight:37,cursor:"e-resize",splitbarClass:"vsplitbar",outlineClass:"voutline",type:"v",eventPos:"pageX",origin:"left",split:"width",pxSplit:"offsetWidth",side1:"Left",side2:"Right",fixed:"height",pxFixed:"offsetHeight",side3:"Top",side4:"Bottom"},h:{keyTop:40,keyBottom:38,cursor:"n-resize",splitbarClass:"hsplitbar",outlineClass:"houtline",type:"h",eventPos:"pageY",origin:"top",split:"height",pxSplit:"offsetHeight",side1:"Top",side2:"Bottom",fixed:"width",pxFixed:"offsetWidth",side3:"Left",side4:"Right"}}[l],t),h=e(this).css({position:"relative"}),d=e(">*",h[0]).css({position:"absolute","z-index":"1","-moz-outline-style":"none"}),u=e(d[0]),p=e(d[1]),f=e('<a href="javascript:void(0)"></a>').attr({accessKey:c.accessKey,tabIndex:c.tabIndex,title:c.splitbarClass}).bind(e.browser.opera?"click":"focus",function(){this.focus(),g.addClass(c.activeClass)}).bind("keydown",function(e){var t=e.which||e.keyCode,i=t==c["key"+c.side1]?1:t==c["key"+c.side2]?-1:0;i&&o(u[0][c.pxSplit]+i*c.pxPerKey,!1)}).bind("blur",function(){g.removeClass(c.activeClass)}),g=e(d[2]||"<div></div>").insertAfter(u).css("z-index","100").append(f).attr({"class":c.splitbarClass,unselectable:"on"}).css({position:"absolute","user-select":"none","-webkit-user-select":"none","-khtml-user-select":"none","-moz-user-select":"none"}).bind("mousedown",i);/^(auto|default|)$/.test(g.css("cursor"))&&g.css("cursor",c.cursor),g._DA=g[0][c.pxSplit],h._PBF=e.boxModel?a(h,"border"+c.side3+"Width","border"+c.side4+"Width"):0,h._PBA=e.boxModel?a(h,"border"+c.side1+"Width","border"+c.side2+"Width"):0,u._pane=c.side1,p._pane=c.side2,e.each([u,p],function(){this._min=c["min"+this._pane]||a(this,"min-"+c.split),this._max=c["max"+this._pane]||a(this,"max-"+c.split)||9999,this._init=c["size"+this._pane]===!0?parseInt(e.curCSS(this[0],c.split)):c["size"+this._pane]});var m=u._init;if(isNaN(p._init)||(m=h[0][c.pxSplit]-h._PBA-p._init-g._DA),c.cookie){e.cookie||alert("jQuery.splitter(): jQuery cookie plugin required");var v=parseInt(e.cookie(c.cookie));isNaN(v)||(m=v),e(window).bind("unload",function(){var t=String(g.css(c.origin));e.cookie(c.cookie,t,{expires:c.cookieExpires||365,path:c.cookiePath||document.location.pathname})})}isNaN(m)&&(m=Math.round((h[0][c.pxSplit]-h._PBA-g._DA)/2)),c.anchorToWindow?(h._hadjust=a(h,"borderTopWidth","borderBottomWidth","marginBottom"),h._hmin=Math.max(a(h,"minHeight"),20),e(window).bind("resize",function(){var t=h.offset().top,i=e(window).height();h.css("height",Math.max(i-t-h._hadjust,h._hmin)+"px"),e.browser.msie||h.trigger("resize")}).trigger("resize")):c.resizeToWidth&&!e.browser.msie&&e(window).bind("resize",function(){h.trigger("resize")}),h.bind("resize",function(e,t){e.target==this&&(h._DF=h[0][c.pxFixed]-h._PBF,h._DA=h[0][c.pxSplit]-h._PBA,h._DF<=0||h._DA<=0||o(isNaN(t)?c.sizeRight||c.sizeBottom?h._DA-p[0][c.pxSplit]-g._DA:u[0][c.pxSplit]:t))}).trigger("resize",[m])})}}(jQuery);