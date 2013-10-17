/*  
 * Copyright 2012, Asamm Software, s. r. o.
 * 
 * This file is part of LocusAPI.
 * 
 * LocusAPI is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *  
 * LocusAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *  
 * You should have received a copy of the Lesser GNU General Public
 * License along with LocusAPI. If not, see 
 * <http://www.gnu.org/licenses/lgpl.html/>.
 */

package org.yaawp.extra;

@Deprecated
public class Log {

	public static void d(String tag, String msg) {
		System.out.println(tag + " - " + msg);
	}
	
	public static void w(String tag, String msg) {
		System.out.println(tag + " - " + msg);
	}
	
	public static void e(String tag, String msg) {
		System.err.println(tag + " - " + msg);
	}
	
	public static void e(String tag, String msg, Throwable e) {
		System.err.println(tag + " - " + msg + ", e:" + e.toString());
		e.printStackTrace();
	}
}
