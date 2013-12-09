package org.yaawp.preferences;

import org.yaawp.utils.Utils;

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
		
	public static boolean getPrefBoolean( int key ) {
		return getPrefBoolean( getKey(key), false );
	}	
	
	public static int getPrefInteger( int key ) {
		String data = getPrefString( getKey(key), "0" );
		int value = Utils.parseInt(data);
		return value;
	}
	
	public static float getPrefFloat( int key ) {
		String data = getPrefString( getKey(key), "0.0" );
		float value = Utils.parseFloat(data);
		return value;
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
	
	public static String getPrefString(String key, String def) {
		if (mContext == null) {
			return def;
		}	
		return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, def);
	}	
}
