/* ===========================================================
 * bootstrap-tooltip.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#tooltips
 * Inspired by the original jQuery.tipsy by Jason Frame
 * ===========================================================
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
 * ========================================================== */
!function(e){"use strict";var t=function(e,t){this.init("tooltip",e,t)};t.prototype={constructor:t,init:function(t,i,n){var s,a;this.type=t,this.$element=e(i),this.options=this.getOptions(n),this.enabled=!0,"manual"!=this.options.trigger&&(s="hover"==this.options.trigger?"mouseenter":"focus",a="hover"==this.options.trigger?"mouseleave":"blur",this.$element.on(s,this.options.selector,e.proxy(this.enter,this)),this.$element.on(a,this.options.selector,e.proxy(this.leave,this))),this.options.selector?this._options=e.extend({},this.options,{trigger:"manual",selector:""}):this.fixTitle()},getOptions:function(t){return t=e.extend({},e.fn[this.type].defaults,t,this.$element.data()),t.delay&&"number"==typeof t.delay&&(t.delay={show:t.delay,hide:t.delay}),t},enter:function(t){var i=e(t.currentTarget)[this.type](this._options).data(this.type);i.options.delay&&i.options.delay.show?(i.hoverState="in",setTimeout(function(){"in"==i.hoverState&&i.show()},i.options.delay.show)):i.show()},leave:function(t){var i=e(t.currentTarget)[this.type](this._options).data(this.type);i.options.delay&&i.options.delay.hide?(i.hoverState="out",setTimeout(function(){"out"==i.hoverState&&i.hide()},i.options.delay.hide)):i.hide()},show:function(){var e,t,i,n,s,a,o;if(this.hasContent()&&this.enabled){switch(e=this.tip(),this.setContent(),this.options.animation&&e.addClass("fade"),a="function"==typeof this.options.placement?this.options.placement.call(this,e[0],this.$element[0]):this.options.placement,t=/in/.test(a),e.remove().css({top:0,left:0,display:"block"}).appendTo(t?this.$element:document.body),i=this.getPosition(t),n=e[0].offsetWidth,s=e[0].offsetHeight,t?a.split(" ")[1]:a){case"bottom":o={top:i.top+i.height,left:i.left+i.width/2-n/2};break;case"top":o={top:i.top-s,left:i.left+i.width/2-n/2};break;case"left":o={top:i.top+i.height/2-s/2,left:i.left-n};break;case"right":o={top:i.top+i.height/2-s/2,left:i.left+i.width}}e.css(o).addClass(a).addClass("in")}},setContent:function(){var e=this.tip();e.find(".tooltip-inner").html(this.getTitle()),e.removeClass("fade in top bottom left right")},hide:function(){function t(){var t=setTimeout(function(){i.off(e.support.transition.end).remove()},500);i.one(e.support.transition.end,function(){clearTimeout(t),i.remove()})}var i=this.tip();i.removeClass("in"),e.support.transition&&this.$tip.hasClass("fade")?t():i.remove()},fixTitle:function(){var e=this.$element;(e.attr("title")||"string"!=typeof e.attr("data-original-title"))&&e.attr("data-original-title",e.attr("title")||"").removeAttr("title")},hasContent:function(){return this.getTitle()},getPosition:function(t){return e.extend({},t?{top:0,left:0}:this.$element.offset(),{width:this.$element[0].offsetWidth,height:this.$element[0].offsetHeight})},getTitle:function(){var e,t=this.$element,i=this.options;return e=t.attr("data-original-title")||("function"==typeof i.title?i.title.call(t[0]):i.title),e=e.toString().replace(/(^\s*|\s*$)/,"")},tip:function(){return this.$tip=this.$tip||e(this.options.template)},validate:function(){this.$element[0].parentNode||(this.hide(),this.$element=null,this.options=null)},enable:function(){this.enabled=!0},disable:function(){this.enabled=!1},toggleEnabled:function(){this.enabled=!this.enabled},toggle:function(){this[this.tip().hasClass("in")?"hide":"show"]()}},e.fn.tooltip=function(i){return this.each(function(){var n=e(this),s=n.data("tooltip"),a="object"==typeof i&&i;s||n.data("tooltip",s=new t(this,a)),"string"==typeof i&&s[i]()})},e.fn.tooltip.Constructor=t,e.fn.tooltip.defaults={animation:!0,delay:0,selector:!1,placement:"top",trigger:"hover",title:"",template:'<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'}}(window.jQuery);