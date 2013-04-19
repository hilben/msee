/* =============================================================
 * bootstrap-scrollspy.js v2.0.1
 * http://twitter.github.com/bootstrap/javascript.html#scrollspy
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
 * ============================================================== */
!function(e){"use strict";function t(t,i){var n,s=e.proxy(this.process,this),a=e(t).is("body")?e(window):e(t);this.options=e.extend({},e.fn.scrollspy.defaults,i),this.$scrollElement=a.on("scroll.scroll.data-api",s),this.selector=(this.options.target||(n=e(t).attr("href"))&&n.replace(/.*(?=#[^\s]+$)/,"")||"")+" .nav li > a",this.$body=e("body").on("click.scroll.data-api",this.selector,s),this.refresh(),this.process()}t.prototype={constructor:t,refresh:function(){this.targets=this.$body.find(this.selector).map(function(){var t=e(this).attr("href");return/^#\w/.test(t)&&e(t).length?t:null}),this.offsets=e.map(this.targets,function(t){return e(t).position().top})},process:function(){var e,t=this.$scrollElement.scrollTop()+this.options.offset,i=this.offsets,n=this.targets,s=this.activeTarget;for(e=i.length;e--;)s!=n[e]&&t>=i[e]&&(!i[e+1]||i[e+1]>=t)&&this.activate(n[e])},activate:function(e){var t;this.activeTarget=e,this.$body.find(this.selector).parent(".active").removeClass("active"),t=this.$body.find(this.selector+'[href="'+e+'"]').parent("li").addClass("active"),t.parent(".dropdown-menu")&&t.closest("li.dropdown").addClass("active")}},e.fn.scrollspy=function(i){return this.each(function(){var n=e(this),s=n.data("scrollspy"),a="object"==typeof i&&i;s||n.data("scrollspy",s=new t(this,a)),"string"==typeof i&&s[i]()})},e.fn.scrollspy.Constructor=t,e.fn.scrollspy.defaults={offset:10},e(function(){e('[data-spy="scroll"]').each(function(){var t=e(this);t.scrollspy(t.data())})})}(window.jQuery);