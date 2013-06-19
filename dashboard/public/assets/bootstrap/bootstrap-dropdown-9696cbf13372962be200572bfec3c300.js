/* ============================================================
 * bootstrap-dropdown.js v2.3.0
 * http://twitter.github.com/bootstrap/javascript.html#dropdowns
 * ============================================================
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
!function(e){"use strict";function t(){e(n).each(function(){i(e(this)).removeClass("open")})}function i(t){var i,n=t.attr("data-target");return n||(n=t.attr("href"),n=n&&/#/.test(n)&&n.replace(/.*(?=#[^\s]*$)/,"")),i=n&&e(n),i&&i.length||(i=t.parent()),i}var n="[data-toggle=dropdown]",s=function(t){var i=e(t).on("click.dropdown.data-api",this.toggle);e("html").on("click.dropdown.data-api",function(){i.parent().removeClass("open")})};s.prototype={constructor:s,toggle:function(){var n,s,a=e(this);if(!a.is(".disabled, :disabled"))return n=i(a),s=n.hasClass("open"),t(),s||n.toggleClass("open"),a.focus(),!1},keydown:function(t){var s,a,o,r,l;if(/(38|40|27)/.test(t.keyCode)&&(s=e(this),t.preventDefault(),t.stopPropagation(),!s.is(".disabled, :disabled"))){if(o=i(s),r=o.hasClass("open"),!r||r&&27==t.keyCode)return 27==t.which&&o.find(n).focus(),s.click();a=e("[role=menu] li:not(.divider):visible a",o),a.length&&(l=a.index(a.filter(":focus")),38==t.keyCode&&l>0&&l--,40==t.keyCode&&l<a.length-1&&l++,~l||(l=0),a.eq(l).focus())}}};var a=e.fn.dropdown;e.fn.dropdown=function(t){return this.each(function(){var i=e(this),n=i.data("dropdown");n||i.data("dropdown",n=new s(this)),"string"==typeof t&&n[t].call(i)})},e.fn.dropdown.Constructor=s,e.fn.dropdown.noConflict=function(){return e.fn.dropdown=a,this},e(document).on("click.dropdown.data-api",t).on("click.dropdown.data-api",".dropdown form",function(e){e.stopPropagation()}).on(".dropdown-menu",function(e){e.stopPropagation()}).on("click.dropdown.data-api",n,s.prototype.toggle).on("keydown.dropdown.data-api",n+", [role=menu]",s.prototype.keydown)}(window.jQuery);