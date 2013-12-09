/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.yaawp.R;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.Log;

import org.yaawp.openwig.OpenWigHelper;
import org.yaawp.positioning.LocationState;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.FileSystem;
import org.yaawp.utils.Logger;
import org.yaawp.utils.StringToken;
import org.yaawp.utils.Utils;

public class MainApplication extends Application {

	private static final String TAG = "MainApplication";
	
	// application name
	public static String APP_NAME = "Yaawp";
	
    private Locale locale = null;
	// screen ON/OFF receiver
	private ScreenReceiver mScreenReceiver;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(
            		newConfig,
            		getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        
        PreferenceUtils.SetContext( this );
        
        Configuration config = getBaseContext().getResources().getConfiguration();
        String lang = PreferenceUtils.getPrefString( R.string.pref_language );

        //Logger.d(TAG, "lang:" + lang + ", system:" + config.locale.getLanguage());
        if (!lang.equals("") && !lang.equals(Settings.VALUE_LANGUAGE_DEFAULT) &&
        		!config.locale.getLanguage().equals(lang)) {
        	ArrayList<String> loc = StringToken.parse(lang, "_");
        	if (loc.size() == 1) {
        		locale = new Locale(lang);	
        	} else {
        		locale = new Locale(loc.get(0), loc.get(1));
        	}
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
            		getBaseContext().getResources().getDisplayMetrics());
        }
        
        // initialize core
        initCore();
    }

    public void onLowMemory() {
    	super.onLowMemory();
    	Log.d(TAG, "onLowMemory()");
    }
    
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminate()");
    }

	public boolean isScreenOff() {
		return mScreenOff;
	}
	
	private boolean mScreenOff = false;
	
	private class ScreenReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	        	//Logger.v(TAG, "ACTION_SCREEN_OFF");
				mScreenOff = true;
	        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
	        	//Logger.v(TAG, "ACTION_SCREEN_ON");
				LocationState.onScreenOn(false);
				mScreenOff = false;
	        }
	    }
	}

	private void initCore() {
		// register screen on/off receiver
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mScreenReceiver = new ScreenReceiver();
		registerReceiver(mScreenReceiver, filter);
		
    	// initialize root directory
		FileSystem.createRoot(APP_NAME);

		// set location state
		LocationState.init(this);
    	// initialize DPI
    	Utils.getDpPixels(this, 1.0f);
    	
    	
    	
    	// init openwig engine
    	OpenWigHelper.SetDeviceId( PreferenceUtils.getPrefString( R.string.pref_wherigo_engine_deviceid ) );
    	OpenWigHelper.SetPlatform( PreferenceUtils.getPrefString( R.string.pref_wherigo_engine_platform ) );
	}
	
    private static Timer mTimer;
    
    public static void onActivityPause() {
    	//Logger.i(TAG, "onActivityPause()");
    	if (mTimer != null) {
    		mTimer.cancel();
    	}
    	
    	mTimer = new Timer();
    	mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!Settings.existCurrentActivity())
					onAppMinimized();
				LocationState.onActivityPauseInstant(Settings.getCurrentActivity());
				mTimer = null;
			}
		}, 2000);
    }
    
	public void destroy() {
		try {
			unregisterReceiver(mScreenReceiver);
		} catch (Exception e) {
			Logger.w(TAG, "destroy(), e:" + e);
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		onAppVisibilityChange = null;
	}
    
    private static void onAppMinimized() {
    	if (onAppVisibilityChange != null)
    		onAppVisibilityChange.onAppMinimized();
    }
    
    public static void appRestored() {
    	onAppRestored();
    	if (onAppVisibilityChange != null)
    		onAppVisibilityChange.onAppRestored();
    }
    
    private static void onAppRestored() {
    	Logger.w(TAG, "onAppRestored()");
    }
    
    private static OnAppVisibilityChange onAppVisibilityChange;
    
    public static void registerVisibilityHandler(OnAppVisibilityChange handler) {
    	MainApplication.onAppVisibilityChange = handler;
    }
    
    public interface OnAppVisibilityChange {
    	
    	public void onAppMinimized();
    	
    	public void onAppRestored();
    	
    }
}
