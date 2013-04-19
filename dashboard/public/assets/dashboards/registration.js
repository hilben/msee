/* =============================================================
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
!function(e){"use strict";var t=function(t,n){this.$element=e(t),this.options=e.extend({},e.fn.collapse.defaults,n),this.options.parent&&(this.$parent=e(this.options.parent)),this.options.toggle&&this.toggle()};t.prototype={constructor:t,dimension:function(){var e=this.$element.hasClass("width");return e?"width":"height"},show:function(){var t,n=this.dimension(),r=e.camelCase(["scroll",n].join("-")),i=this.$parent&&this.$parent.find(".in");i&&i.length&&(t=i.data("collapse"),i.collapse("hide"),t||i.data("collapse",null)),this.$element[n](0),this.transition("addClass","show","shown"),this.$element[n](this.$element[0][r])},hide:function(){var e=this.dimension();this.reset(this.$element[e]()),this.transition("removeClass","hide","hidden"),this.$element[e](0)},reset:function(e){var t=this.dimension();this.$element.removeClass("collapse")[t](e||"auto")[0].offsetWidth,this.$element.addClass("collapse")},transition:function(t,n,r){var i=this,a=function(){"show"==n&&i.reset(),i.$element.trigger(r)};this.$element.trigger(n)[t]("in"),e.support.transition&&this.$element.hasClass("collapse")?this.$element.one(e.support.transition.end,a):a()},toggle:function(){this[this.$element.hasClass("in")?"hide":"show"]()}},e.fn.collapse=function(n){return this.each(function(){var r=e(this),i=r.data("collapse"),a="object"==typeof n&&n;i||r.data("collapse",i=new t(this,a)),"string"==typeof n&&i[n]()})},e.fn.collapse.defaults={toggle:!0},e.fn.collapse.Constructor=t,e(function(){e("body").on("click.collapse.data-api","[data-toggle=collapse]",function(t){var n,r=e(this),i=r.attr("data-target")||t.preventDefault()||(n=r.attr("href"))&&n.replace(/.*(?=#[^\s]+$)/,""),a=e(i).data("collapse")?"toggle":r.data();e(i).collapse(a)})})}(window.jQuery);