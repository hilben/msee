/*
 * jQuery.splitter.js - two-pane splitter window plugin
 *
 * version 1.51 (2009/01/09) 
 * 
 * Dual licensed under the MIT and GPL licenses: 
 *   http://www.opensource.org/licenses/mit-license.php 
 *   http://www.gnu.org/licenses/gpl.html 
 */
/**
 * The splitter() plugin implements a two-pane resizable splitter window.
 * The selected elements in the jQuery object are converted to a splitter;
 * each selected element should have two child elements, used for the panes
 * of the splitter. The plugin adds a third child element for the splitbar.
 * 
 * For more details see: http://methvin.com/splitter/
 *
 *
 * @example $('#MySplitter').splitter();
 * @desc Create a vertical splitter with default settings 
 *
 * @example $('#MySplitter').splitter({type: 'h', accessKey: 'M'});
 * @desc Create a horizontal splitter resizable via Alt+Shift+M
 *
 * @name splitter
 * @type jQuery
 * @param Object options Options for the splitter (not required)
 * @cat Plugins/Splitter
 * @return jQuery
 * @author Dave Methvin (dave.methvin@gmail.com)
 */
(function(e){e.fn.splitter=function(t){return t=t||{},this.each(function(){function r(t){f.outline&&(n=n||v.clone(!1).insertAfter(h)),c.css("-webkit-user-select","none"),v.addClass(f.activeClass),h._posSplit=h[0][f.pxSplit]-t[f.eventPos],e(document).bind("mousemove",i).bind("mouseup",s)}function i(e){var t=h._posSplit+e[f.eventPos];f.outline?(t=Math.max(0,Math.min(t,l._DA-v._DA)),v.css(f.origin,t)):o(t)}function s(t){v.removeClass(f.activeClass);var r=h._posSplit+t[f.eventPos];f.outline&&(n.remove(),n=null,o(r)),c.css("-webkit-user-select","text"),e(document).unbind("mousemove",i).unbind("mouseup",s)}function o(t){t=Math.max(h._min,l._DA-p._max,Math.min(t,h._max,l._DA-v._DA-p._min)),v._DA=v[0][f.pxSplit],v.css(f.origin,t).css(f.fixed,l._DF),h.css(f.origin,0).css(f.split,t).css(f.fixed,l._DF),p.css(f.origin,t+v._DA).css(f.split,l._DA-v._DA-t).css(f.fixed,l._DF),e.browser.msie||c.trigger("resize")}function u(e,t){var n=0;for(var r=1;r<arguments.length;r++)n+=Math.max(parseInt(e.css(arguments[r]))||0,0);return n}var n,a=(t.splitHorizontal?"h":t.splitVertical?"v":t.type)||"v",f=e.extend({activeClass:"active",pxPerKey:8,tabIndex:0,accessKey:""},{v:{keyLeft:39,keyRight:37,cursor:"e-resize",splitbarClass:"vsplitbar",outlineClass:"voutline",type:"v",eventPos:"pageX",origin:"left",split:"width",pxSplit:"offsetWidth",side1:"Left",side2:"Right",fixed:"height",pxFixed:"offsetHeight",side3:"Top",side4:"Bottom"},h:{keyTop:40,keyBottom:38,cursor:"n-resize",splitbarClass:"hsplitbar",outlineClass:"houtline",type:"h",eventPos:"pageY",origin:"top",split:"height",pxSplit:"offsetHeight",side1:"Top",side2:"Bottom",fixed:"width",pxFixed:"offsetWidth",side3:"Left",side4:"Right"}}[a],t),l=e(this).css({position:"relative"}),c=e(">*",l[0]).css({position:"absolute","z-index":"1","-moz-outline-style":"none"}),h=e(c[0]),p=e(c[1]),d=e('<a href="javascript:void(0)"></a>').attr({accessKey:f.accessKey,tabIndex:f.tabIndex,title:f.splitbarClass}).bind(e.browser.opera?"click":"focus",function(){this.focus(),v.addClass(f.activeClass)}).bind("keydown",function(e){var t=e.which||e.keyCode,n=t==f["key"+f.side1]?1:t==f["key"+f.side2]?-1:0;n&&o(h[0][f.pxSplit]+n*f.pxPerKey,!1)}).bind("blur",function(){v.removeClass(f.activeClass)}),v=e(c[2]||"<div></div>").insertAfter(h).css("z-index","100").append(d).attr({"class":f.splitbarClass,unselectable:"on"}).css({position:"absolute","user-select":"none","-webkit-user-select":"none","-khtml-user-select":"none","-moz-user-select":"none"}).bind("mousedown",r);/^(auto|default|)$/.test(v.css("cursor"))&&v.css("cursor",f.cursor),v._DA=v[0][f.pxSplit],l._PBF=e.boxModel?u(l,"border"+f.side3+"Width","border"+f.side4+"Width"):0,l._PBA=e.boxModel?u(l,"border"+f.side1+"Width","border"+f.side2+"Width"):0,h._pane=f.side1,p._pane=f.side2,e.each([h,p],function(){this._min=f["min"+this._pane]||u(this,"min-"+f.split),this._max=f["max"+this._pane]||u(this,"max-"+f.split)||9999,this._init=f["size"+this._pane]===!0?parseInt(e.curCSS(this[0],f.split)):f["size"+this._pane]});var m=h._init;isNaN(p._init)||(m=l[0][f.pxSplit]-l._PBA-p._init-v._DA);if(f.cookie){e.cookie||alert("jQuery.splitter(): jQuery cookie plugin required");var g=parseInt(e.cookie(f.cookie));isNaN(g)||(m=g),e(window).bind("unload",function(){var t=String(v.css(f.origin));e.cookie(f.cookie,t,{expires:f.cookieExpires||365,path:f.cookiePath||document.location.pathname})})}isNaN(m)&&(m=Math.round((l[0][f.pxSplit]-l._PBA-v._DA)/2)),f.anchorToWindow?(l._hadjust=u(l,"borderTopWidth","borderBottomWidth","marginBottom"),l._hmin=Math.max(u(l,"minHeight"),20),e(window).bind("resize",function(){var t=l.offset().top,n=e(window).height();l.css("height",Math.max(n-t-l._hadjust,l._hmin)+"px"),e.browser.msie||l.trigger("resize")}).trigger("resize")):f.resizeToWidth&&!e.browser.msie&&e(window).bind("resize",function(){l.trigger("resize")}),l.bind("resize",function(e,t){if(e.target!=this)return;l._DF=l[0][f.pxFixed]-l._PBF,l._DA=l[0][f.pxSplit]-l._PBA;if(l._DF<=0||l._DA<=0)return;o(isNaN(t)?!f.sizeRight&&!f.sizeBottom?h[0][f.pxSplit]:l._DA-p[0][f.pxSplit]-v._DA:t)}).trigger("resize",[m])})}})(jQuery);