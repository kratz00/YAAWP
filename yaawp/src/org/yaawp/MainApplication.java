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

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.yaawp.R;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.StatFs;
import android.util.Log;

import org.yaawp.app.YaawpAppData;
import org.yaawp.openwig.OpenWigHelper;
import org.yaawp.positioning.LocationState;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.A;
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

	// create directories during startup
	protected static String[] DIRS = new String[] {
		FileSystem.CACHE
	};	
	
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
        
        
		// register screen on/off receiver
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mScreenReceiver = new ScreenReceiver();
		registerReceiver(mScreenReceiver, filter);
		
    	// initialize root directory
		FileSystem.createRoot(APP_NAME);
		
		// 
		A.registerApp((MainApplication) this);
		
		if ( !(DIRS == null || DIRS.length == 0) ) {
			
			// --- check file system
	   		if (FileSystem.createRoot(MainApplication.APP_NAME)) {
	   			//Logger.w(TAG, "FileSystem succesfully created!");
	   	    	// fileSystem created successfully
	   	    	for (int i = 0; i < DIRS.length; i++) {
	   	    		(new File(FileSystem.ROOT + DIRS[i])).mkdirs();
	   	    	}	   		
	   	    	YaawpAppData.GetInstance().mFileSystemCheck = true;
	    	} else {
	    		YaawpAppData.GetInstance().mFileSystemCheck = false;
	    		//Logger.w(TAG, "FileSystem cannot be created!");
	    		
	    		/*
				UtilsGUI.showDialogError(CustomMain.this,
						R.string.filesystem_cannot_create_root,
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showDialogFinish(FINISH_EXIT_FORCE);
					}
				});
				*/
				return;
				
	    	}		
	   		
	   		// --- check free space
   	    	// check disk space (at least 5MB)
   	    	StatFs stat = new StatFs(FileSystem.ROOT);
   	    	long bytesFree = (long) stat.getBlockSize() *(long) stat.getAvailableBlocks();
   	    	long megFree = bytesFree / 1048576;
   	    	//Logger.d(TAG, "megAvailable:" + megAvail + ", free:" + megFree);
   	    	if (megFree > 0 && megFree < 5) {
   	    		YaawpAppData.GetInstance().mFileSystemSpace = false;
   	    		/*
   	    		UtilsGUI.showDialogError(CustomMain.this,
   	    				getString(R.string.not_enough_disk_space_x, FileSystem.ROOT, megFree + "MB"),
   	    				new DialogInterface.OnClickListener() {

   	    			@Override
   	    			public void onClick(DialogInterface dialog, int which) {
   	    				showDialogFinish(FINISH_EXIT_FORCE);
   	    			}
   	    		});
   	    		*/
   	    		
   	    		return;
   	    		
   	    	} else {
   	    		YaawpAppData.GetInstance().mFileSystemSpace = true;
   	    	}
	   	       		
		}
		
		// set location state
		LocationState.init(this);		
		
    	// start gps
        if (Utils.isPermissionAllowed(Manifest.permission.ACCESS_FINE_LOCATION) &&
        		Settings.getPrefBoolean(Settings.KEY_B_START_GPS_AUTOMATICALLY,
        		Settings.DEFAULT_START_GPS_AUTOMATICALLY)) {
        	LocationState.setGpsOn(this);
        } else {
        	LocationState.setGpsOff(this);
        	// TODO create a message in the list
        }		

    	// initialize DPI
    	Utils.getDpPixels(this, 1.0f);
    	
    	// init openwig engine
    	OpenWigHelper.SetDeviceId( PreferenceUtils.getPrefString( R.string.pref_wherigo_engine_deviceid ) );
    	OpenWigHelper.SetPlatform( PreferenceUtils.getPrefString( R.string.pref_wherigo_engine_platform ) );

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

/*
public static final int FINISH_NONE = -1;
public static final int FINISH_EXIT = 0;
public static final int FINISH_EXIT_FORCE = 1;
public static final int FINISH_RESTART = 2;
public static final int FINISH_RESTART_FORCE = 3;
public static final int FINISH_RESTART_FACTORY_RESET = 4;


protected int finishType = FINISH_NONE;

public void showDialogFinish2(final int typeOfFinish) {
//Logger.d(TAG, "showFinishDialog(" + typeOfFinish + ")");
	if (typeOfFinish == FINISH_NONE)
		return;
	
	this.finishType = typeOfFinish;
	
	runOnUiThread(new Runnable() { 
		public void run() {
			String title = I18N.get(R.string.question);
	    	String message = "";
	    	boolean cancelable = (
	    			finishType == FINISH_RESTART_FORCE ||
	    			finishType == FINISH_RESTART_FACTORY_RESET ||
	    			finishType == FINISH_EXIT_FORCE) ? false : true; 
	    	switch (finishType) {
	    	case FINISH_EXIT:
	    		message = I18N.get(R.string.do_you_really_want_to_exit);
	    		break;
	    	case FINISH_EXIT_FORCE:
	    		title = I18N.get(R.string.info);
	    		message = I18N.get(R.string.you_have_to_exit_app_force);
	    		break;
			case FINISH_RESTART:
				message = I18N.get(R.string.you_have_to_restart_app_recommended);
				break;
			case FINISH_RESTART_FORCE:
				title = I18N.get(R.string.info);
				message = I18N.get(R.string.you_have_to_restart_app_force);
				break;
			case FINISH_RESTART_FACTORY_RESET:
				title = I18N.get(R.string.info);
				message = I18N.get(R.string.you_have_to_restart_app_force);
				break;
	    	}
	    	
	    	AlertDialog.Builder b = new AlertDialog.Builder(CustomMain.this);
	    	b.setCancelable(cancelable);
	    	b.setTitle(title);
	    	b.setIcon(R.drawable.ic_question_alt);
	    	b.setMessage(message);
	    	b.setPositiveButton(R.string.ok, 
	    			new DialogInterface.OnClickListener() {
				
	    		@Override
	    		public void onClick(DialogInterface dialog, int which) {
	    			if (finishType == FINISH_EXIT || finishType == FINISH_EXIT_FORCE) {
	    				CustomMain.this.finish();
	    			} else if (finishType == FINISH_RESTART || finishType == FINISH_RESTART_FORCE ||
	    					finishType == FINISH_RESTART_FACTORY_RESET) {
			    		// Setup one-short alarm to restart my application in 3 seconds - TODO need use another context
//			    		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//			    		Intent intent = new Intent(APP_INTENT_MAIN);
//			    		PendingIntent pi = PendingIntent.getBroadcast(CustomMain.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//			    		alarmMgr.set(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + 3000, pi); 
	    				CustomMain.this.finish();
	    			} 
	    		}
	    	});
	    	if (cancelable) {
	    		b.setNegativeButton(R.string.cancel, null);
	    	}
	    	b.show();
	    }
	});
}
*/
