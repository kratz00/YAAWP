package org.yaawp.openwig;


import android.util.Log;
import cz.matejcik.openwig.WherigoLib;

public class OpenWigHelper {

	private static String TAG ="OpenWigHelper";
	
	public static void SetDeviceId( String value ) {
		Log.i( TAG, "set WherigoLib.DEVICE_ID = "+ value );
		WherigoLib.env.put(WherigoLib.DEVICE_ID, value );		
	}
	
	public static void SetPlatform( String value) {
		Log.i( TAG, "set WherigoLib.PLATFORM = "+ value );
		WherigoLib.env.put(WherigoLib.PLATFORM, value );       			
	}
	
	
}
