/* =========================================================
 * bootstrap-modal.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#modals
 * =========================================================
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
 * ========================================================= */
!function(e){"use strict";function t(){var t=this,n=setTimeout(function(){t.$element.off(e.support.transition.end),i.call(t)},500);this.$element.one(e.support.transition.end,function(){clearTimeout(n),i.call(t)})}function i(){this.$element.hide().trigger("hidden"),n.call(this)}function n(t){var i=this.$element.hasClass("fade")?"fade":"";if(this.isShown&&this.options.backdrop){var n=e.support.transition&&i;this.$backdrop=e('<div class="modal-backdrop '+i+'" />').appendTo(document.body),"static"!=this.options.backdrop&&this.$backdrop.click(e.proxy(this.hide,this)),n&&this.$backdrop[0].offsetWidth,this.$backdrop.addClass("in"),n?this.$backdrop.one(e.support.transition.end,t):t()}else!this.isShown&&this.$backdrop?(this.$backdrop.removeClass("in"),e.support.transition&&this.$element.hasClass("fade")?this.$backdrop.one(e.support.transition.end,e.proxy(s,this)):s.call(this)):t&&t()}function s(){this.$backdrop.remove(),this.$backdrop=null}function a(){var t=this;this.isShown&&this.options.keyboard?e(document).on("keyup.dismiss.modal",function(e){27==e.which&&t.hide()}):this.isShown||e(document).off("keyup.dismiss.modal")}var o=function(t,i){this.options=i,this.$element=e(t).delegate('[data-dismiss="modal"]',"click.dismiss.modal",e.proxy(this.hide,this))};o.prototype={constructor:o,toggle:function(){return this[this.isShown?"hide":"show"]()},show:function(){var t=this;this.isShown||(e("body").addClass("modal-open"),this.isShown=!0,this.$element.trigger("show"),a.call(this),n.call(this,function(){var i=e.support.transition&&t.$element.hasClass("fade");!t.$element.parent().length&&t.$element.appendTo(document.body),t.$element.show(),i&&t.$element[0].offsetWidth,t.$element.addClass("in"),i?t.$element.one(e.support.transition.end,function(){t.$element.trigger("shown")}):t.$element.trigger("shown")}))},hide:function(n){n&&n.preventDefault(),this.isShown&&(this.isShown=!1,e("body").removeClass("modal-open"),a.call(this),this.$element.trigger("hide").removeClass("in"),e.support.transition&&this.$element.hasClass("fade")?t.call(this):i.call(this))}},e.fn.modal=function(t){return this.each(function(){var i=e(this),n=i.data("modal"),s=e.extend({},e.fn.modal.defaults,i.data(),"object"==typeof t&&t);n||i.data("modal",n=new o(this,s)),"string"==typeof t?n[t]():s.show&&n.show()})},e.fn.modal.defaults={backdrop:!0,keyboard:!0,show:!0},e.fn.modal.Constructor=o,e(function(){e("body").on("click.modal.data-api",'[data-toggle="modal"]',function(t){var i,n=e(this),s=e(n.attr("data-target")||(i=n.attr("href"))&&i.replace(/.*(?=#[^\s]+$)/,"")),a=s.data("modal")?"toggle":e.extend({},s.data(),n.data());t.preventDefault(),s.modal(a)})})}(window.jQuery);