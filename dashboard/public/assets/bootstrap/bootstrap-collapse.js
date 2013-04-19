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
!function(e){"use strict";var t=function(t,i){this.$element=e(t),this.options=e.extend({},e.fn.collapse.defaults,i),this.options.parent&&(this.$parent=e(this.options.parent)),this.options.toggle&&this.toggle()};t.prototype={constructor:t,dimension:function(){var e=this.$element.hasClass("width");return e?"width":"height"},show:function(){var t,i=this.dimension(),n=e.camelCase(["scroll",i].join("-")),s=this.$parent&&this.$parent.find(".in");s&&s.length&&(t=s.data("collapse"),s.collapse("hide"),t||s.data("collapse",null)),this.$element[i](0),this.transition("addClass","show","shown"),this.$element[i](this.$element[0][n])},hide:function(){var e=this.dimension();this.reset(this.$element[e]()),this.transition("removeClass","hide","hidden"),this.$element[e](0)},reset:function(e){var t=this.dimension();this.$element.removeClass("collapse")[t](e||"auto")[0].offsetWidth,this.$element.addClass("collapse")},transition:function(t,i,n){var s=this,a=function(){"show"==i&&s.reset(),s.$element.trigger(n)};this.$element.trigger(i)[t]("in"),e.support.transition&&this.$element.hasClass("collapse")?this.$element.one(e.support.transition.end,a):a()},toggle:function(){this[this.$element.hasClass("in")?"hide":"show"]()}},e.fn.collapse=function(i){return this.each(function(){var n=e(this),s=n.data("collapse"),a="object"==typeof i&&i;s||n.data("collapse",s=new t(this,a)),"string"==typeof i&&s[i]()})},e.fn.collapse.defaults={toggle:!0},e.fn.collapse.Constructor=t,e(function(){e("body").on("click.collapse.data-api","[data-toggle=collapse]",function(t){var i,n=e(this),s=n.attr("data-target")||t.preventDefault()||(i=n.attr("href"))&&i.replace(/.*(?=#[^\s]+$)/,""),a=e(s).data("collapse")?"toggle":n.data();e(s).collapse(a)})})}(window.jQuery);