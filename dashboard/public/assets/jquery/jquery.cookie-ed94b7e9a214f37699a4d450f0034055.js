/**
 * Cookie plugin
 *
 * Copyright (c) 2006 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
jQuery.cookie=function(e,t,i){if("undefined"==typeof t){var s=null;if(document.cookie&&""!=document.cookie)for(var n=document.cookie.split(";"),o=0;o<n.length;o++){var a=jQuery.trim(n[o]);if(a.substring(0,e.length+1)==e+"="){s=decodeURIComponent(a.substring(e.length+1));break}}return s}i=i||{},null===t&&(t="",i=$.extend({},i),i.expires=-1);var r="";if(i.expires&&("number"==typeof i.expires||i.expires.toUTCString)){var l;"number"==typeof i.expires?(l=new Date,l.setTime(l.getTime()+1e3*60*60*24*i.expires)):l=i.expires,r="; expires="+l.toUTCString()}var h=i.path?"; path="+i.path:"",c=i.domain?"; domain="+i.domain:"",u=i.secure?"; secure":"";document.cookie=[e,"=",encodeURIComponent(t),r,h,c,u].join("")};