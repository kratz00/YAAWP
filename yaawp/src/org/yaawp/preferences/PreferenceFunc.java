package org.yaawp.preferences;


import org.yaawp.R;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;
import android.content.Context;
import android.os.PowerManager;



public class PreferenceFunc {
	
	public static String TAG = "PreferenceFunc";
	    
    private static PowerManager.WakeLock wl;

    public static void enableWakeLock() {
    	try {
	    	boolean disable = false;
	    	int value = PreferenceUtils.getPrefInteger( R.string.pref_highlight );
	    	if ( value == Settings.VALUE_HIGHLIGHT_OFF) {
	    		disable = true;
	    	} else if (value == Settings.VALUE_HIGHLIGHT_ONLY_GPS) {
	    		if (!LocationState.isActuallyHardwareGpsOn()) {
	    			disable = true;
	    		}
	    	}
	    	Logger.w(TAG, "enableWakeLock(), dis:" + disable + ", wl:" + wl);
	    	if (disable && wl != null) {
	    		disableWakeLock();
	    	} else if (!disable && wl == null) {
	   			PowerManager pm = (PowerManager) A.getApp().getSystemService(Context.POWER_SERVICE);
	   			wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
	   			wl.acquire();
	    	}
	    	//Logger.w(TAG, "enableWakeLock(), res:" + wl);
    	} catch (Exception e) {
    		Logger.e(TAG, "enableWakeLock(), e:" + e.toString());
    	}
    }
    
    public static void disableWakeLock() {
    	Logger.w(TAG, "disableWakeLock(), wl:" + wl);
    	if (wl != null) {
    		wl.release();
    		wl = null;
    	}
    }    
    
}
