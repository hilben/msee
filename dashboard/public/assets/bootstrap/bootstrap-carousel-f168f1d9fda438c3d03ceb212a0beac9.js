/* ==========================================================
 * bootstrap-carousel.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#carousel
 * ==========================================================
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
!function(e){"use strict";var t=function(t,i){this.$element=e(t),this.options=e.extend({},e.fn.carousel.defaults,i),this.options.slide&&this.slide(this.options.slide)};t.prototype={cycle:function(){return this.interval=setInterval(e.proxy(this.next,this),this.options.interval),this},to:function(t){var i=this.$element.find(".active"),n=i.parent().children(),s=n.index(i),a=this;if(!(t>n.length-1||0>t))return this.sliding?this.$element.one("slid",function(){a.to(t)}):s==t?this.pause().cycle():this.slide(t>s?"next":"prev",e(n[t]))},pause:function(){return clearInterval(this.interval),this.interval=null,this},next:function(){return this.sliding?void 0:this.slide("next")},prev:function(){return this.sliding?void 0:this.slide("prev")},slide:function(t,i){var n=this.$element.find(".active"),s=i||n[t](),a=this.interval,o="next"==t?"left":"right",r="next"==t?"first":"last",l=this;if(s.length)return this.sliding=!0,a&&this.pause(),s=s.length?s:this.$element.find(".item")[r](),!e.support.transition&&this.$element.hasClass("slide")?(this.$element.trigger("slide"),n.removeClass("active"),s.addClass("active"),this.sliding=!1,this.$element.trigger("slid")):(s.addClass(t),s[0].offsetWidth,n.addClass(o),s.addClass(o),this.$element.trigger("slide"),this.$element.one(e.support.transition.end,function(){s.removeClass([t,o].join(" ")).addClass("active"),n.removeClass(["active",o].join(" ")),l.sliding=!1,setTimeout(function(){l.$element.trigger("slid")},0)})),a&&this.cycle(),this}},e.fn.carousel=function(i){return this.each(function(){var n=e(this),s=n.data("carousel"),a="object"==typeof i&&i;s||n.data("carousel",s=new t(this,a)),"number"==typeof i?s.to(i):"string"==typeof i||(i=a.slide)?s[i]():s.cycle()})},e.fn.carousel.defaults={interval:5e3},e.fn.carousel.Constructor=t,e(function(){e("body").on("click.carousel.data-api","[data-slide]",function(t){var i,n=e(this),s=e(n.attr("data-target")||(i=n.attr("href"))&&i.replace(/.*(?=#[^\s]+$)/,"")),a=!s.data("modal")&&e.extend({},s.data(),n.data());s.carousel(a),t.preventDefault()})})}(window.jQuery);