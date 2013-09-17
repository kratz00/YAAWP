package org.yaawp.preferences;

import android.content.Context;
import android.preference.PreferenceManager;
import menion.android.whereyougo.utils.A;

public class PreferenceUtils {

    private static String getKey(final int prefKeyId) {
        return A.getApp().getString(prefKeyId);
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
	

	public static boolean getPrefBoolean(String key, boolean def) {
		if (A.getApp() == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getBoolean(key, def);
	}	
	
	public static int getPrefInteger(String key, int def) {
		if (A.getApp() == null) {
			return def;
		}		
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getInt(key, def);
	}	
}
