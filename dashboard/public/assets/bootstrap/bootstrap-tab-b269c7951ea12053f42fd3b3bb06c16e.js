/* ========================================================
 * bootstrap-tab.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#tabs
 * ========================================================
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
 * ======================================================== */
!function(e){"use strict";var t=function(t){this.element=e(t)};t.prototype={constructor:t,show:function(){var t,i,n=this.element,s=n.closest("ul:not(.dropdown-menu)"),a=n.attr("data-target");a||(a=n.attr("href"),a=a&&a.replace(/.*(?=#[^\s]*$)/,"")),n.parent("li").hasClass("active")||(t=s.find(".active a").last()[0],n.trigger({type:"show",relatedTarget:t}),i=e(a),this.activate(n.parent("li"),s),this.activate(i,i.parent(),function(){n.trigger({type:"shown",relatedTarget:t})}))},activate:function(t,i,n){function s(){a.removeClass("active").find("> .dropdown-menu > .active").removeClass("active"),t.addClass("active"),o?(t[0].offsetWidth,t.addClass("in")):t.removeClass("fade"),t.parent(".dropdown-menu")&&t.closest("li.dropdown").addClass("active"),n&&n()}var a=i.find("> .active"),o=n&&e.support.transition&&a.hasClass("fade");o?a.one(e.support.transition.end,s):s(),a.removeClass("in")}},e.fn.tab=function(i){return this.each(function(){var n=e(this),s=n.data("tab");s||n.data("tab",s=new t(this)),"string"==typeof i&&s[i]()})},e.fn.tab.Constructor=t,e(function(){e("body").on("click.tab.data-api",'[data-toggle="tab"], [data-toggle="pill"]',function(t){t.preventDefault(),e(this).tab("show")})})}(window.jQuery);