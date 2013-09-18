package org.yaawp.preferences;

import android.content.Context;
import android.preference.PreferenceManager;


public class PreferenceUtils {

	private static Context mContext = null;
	
	public static void SetContext( Context context ) {
		mContext = context;
	}
	
    private static String getKey(final int prefKeyId) {
    	if ( mContext == null ) {
    		return "";
    	}
        return mContext.getString(prefKeyId);
    }   
	
    /* public static boolean getPrefBoolean( int key, boolean def ) {
		return getPrefBoolean( getKey(key), def );
	} */
	
	public static boolean getPrefBoolean( int key ) {
		return getPrefBoolean( getKey(key), false );
	}	
	
	public static int getPrefInteger( int key ) {
		return getPrefInteger( getKey(key), 0 );
	}
	
	public static float getPrefFloat( int key ) {
		return getPrefFloat( getKey(key), 0 );
	}
	
	public static String getPrefString( int key ) {
		return getPrefString( getKey(key), "" );
	}	
	
	public static boolean getPrefBoolean(String key, boolean def) {
		if (mContext == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, def);
	}	
	
	public static int getPrefInteger(String key, int def) {
		if (mContext == null) {
			return def;
		}		
		return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key, def);
	}	
	
	public static float getPrefFloat(String key, int def) {
		if (mContext == null) {
			return def;
		}	
		return PreferenceManager.getDefaultSharedPreferences(mContext).getFloat(key, def);
	}	
	
	public static String getPrefString(String key, String def) {
		if (mContext == null) {
			return def;
		}	
		return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, def);
	}	
}
