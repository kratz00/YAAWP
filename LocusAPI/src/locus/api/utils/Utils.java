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

package locus.api.utils;

import java.io.Closeable;
import java.lang.reflect.Field;

public class Utils {

//	private static final String TAG = "Utils";
	
    public static void closeStream(Closeable stream) {
    	try {
    		if (stream != null) {
    			stream.close();
    			stream = null;
    		}
    	} catch (Exception e) {
    		System.err.println("closeStream(" + stream + "), e:" + e);
    		e.printStackTrace();
    	}
    }
    
    public static double parseDouble(Object data) {
        return parseDouble(String.valueOf(data));
    }

    public static double parseDouble(String data) {
        try {
        	data = data.trim().replace(",", ".");
            return Double.parseDouble(data);
        } catch (Exception e) {
        	//Logger.e("Utils", "parseDouble(" + data + ")", e);
            return 0.0;
        }
    }
    
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    private static final String NEW_LINE = System.getProperty("line.separator");
    
    public static String toString(Object obj) {
    	return toString(obj, "");
    }
    
    public static String toString(Object obj, String prefix) {
    	// add base
    	StringBuilder result = new StringBuilder();
    	result.append(prefix);
    	if (obj == null) {
    		result.append(" empty object!");
    		return result.toString();
    	}
    	
    	// handle existing object
    	result.append(obj.getClass().getName()).append(" {").append(NEW_LINE);

    	// determine fields declared in this class only (no fields of superclass)
    	Field[] fields = obj.getClass().getDeclaredFields();

    	// print field names paired with their values
    	for (Field field : fields) {
    		result.append(prefix).append("    ");
    		try {
    			result.append(field.getName());
    			result.append(": ");
    			// set accessible for private fields
    			field.setAccessible(true);
    			// requires access to private field:
    			result.append(field.get(obj));
    		} catch (Exception ex) {
    			System.out.println(ex);
    		}
    		result.append(NEW_LINE);
    	}
    	result.append(prefix).append("}");
    	return result.toString();
    }
}
