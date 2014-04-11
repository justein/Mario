/**
 *  Copyright (C) 2006 zhangbo (freeeob@gmail.com)
 *
 *  This product is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 * 
 *  This product is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 *
 *  author:zhangbo
 *  Email:jsjava@gmail.com
 */


/**  The Short class references to java.lang.Short of J2SE1.4 */
 
/**
 * constructor
 * @param value
 */
function Short(value){
	this.jsjava_class="jsjava.lang.Short";
    this.value=value;
}
Short.MIN=-Math.pow(2,15);
Short.MAX=Math.pow(2,15)-1;
Short.MIN_VALUE=-Math.pow(2,15);
Short.MAX_VALUE=Math.pow(2,15)-1;

/**
 * check whether the input value is a short value
 * @param s
 */
Short.checkValid=function(s){
	if(isNaN(s)){
		return false;
	}
	if(typeof(s)=="number"){
		if(Math.floor(s)!=s){
			return false;
		}
	}else{
		if(s.indexOf(".")!=-1){
			return false;
		}
	}
	s=parseInt(s);
	if(s<=Short.MAX&&s>=Short.MIN){
    	return true;
    }
    return false;
};

/**
 * return the short value parsed from str
 * @param str
 */
Short.parseShort=function(str){
    if(isNaN(str)){
		throw new NumberFormatException(NumberFormatException.NOT_NUMBER,"Not a number Exception!");
	}
    var s=parseInt(str);
    if(!Short.checkValid(s)){
        return;
    }
    return s;
};

/**
 * compare whether this object value is equals to input Short object value
 * @param b input Short object
 */
Short.prototype.compareTo=function(b){
    if(b==undefined){
        return -1; 
    }
    if(this.value>b.value){
        return 1; 
    }else if(this.value==b.value){
        return 0; 
    }else{
        return -1;  
    }
};

/**
 * return the object's short value
 */
Short.prototype.shortValue=function(){
    return this.value; 
};

/**
 * return a string description
 */
Short.prototype.toString=function(){
    return this.value; 
};

/**
 * compare whether this object is equal to input object o
 * @param o
 */
Short.prototype.equals=function(o){
    if(o==undefined){
        return false; 
    }
    if(o.jsjava_class&&o.jsjava_class=="jsjava.lang.Short"){
        return this.value==o.value; 
    } 
    return false;
};