package org.yaawp.preferences;

import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;

import org.yaawp.R;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import menion.android.whereyougo.settings.Settings;

public class PreferenceFunc {
	
	public static String TAG = "PreferenceFunc";
	
    public static void setScreenFullscreen(Activity activity) {
    	try {
			boolean fullScreen = PreferenceUtils.getPrefBoolean( R.string.pref_fullscreen );
			
    		if (fullScreen) {
				activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}

    	} catch (Exception e) {
    		Logger.e(TAG, "setScreenFullScreen(" + activity + ")", e);
    	}
    }
    
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
