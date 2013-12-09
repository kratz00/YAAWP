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

package org.yaawp.hmi.gui.extension;

import java.io.File;
import java.io.StringReader;

import org.yaawp.MainApplication;
import org.yaawp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.StatFs;

import org.yaawp.hmi.activities.CustomActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.positioning.LocationState;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.A;
import org.yaawp.utils.AssetHelper;
import org.yaawp.utils.FileSystem;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;

public abstract class CustomMain extends CustomActivity {

	private static final String TAG = "CustomMain";


	// create directories during startup
	protected static String[] DIRS = new String[] {
		FileSystem.CACHE
	};
	
	private static boolean callSecondInit;
	private static boolean callRegisterOnly;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        A.registerMain(this);

        callSecondInit = false;
        callRegisterOnly = false;
        if (A.getApp() == null) { // first app run
//Logger.w(TAG, "onCreate() - init new");
        	A.registerApp((MainApplication) getApplication());

        	// not test some things
            if (testFileSystem() && testFreeSpace()) {
    	        // set last known location
    	        if (Utils.isPermissionAllowed(Manifest.permission.ACCESS_FINE_LOCATION) &&
    	        		Settings.getPrefBoolean(Settings.KEY_B_START_GPS_AUTOMATICALLY,
    	        		Settings.DEFAULT_START_GPS_AUTOMATICALLY)) {
    	        	LocationState.setGpsOn(CustomMain.this);
    	        } else {
    	        	LocationState.setGpsOff(CustomMain.this);
    	        }

        		eventFirstInit();
        		Settings.setScreenBasic(this);
        		eventCreateLayout();
        		callSecondInit = true;
            } else {
            	// do nothing, just close APP
            }
        } else {
//Logger.w(TAG, "onCreate() - only register");
        	Settings.setScreenBasic(this);
        	eventCreateLayout();
        	callRegisterOnly = true;
        }
    }
    
    public void onResumeExtra() {
    	if (callSecondInit) {
    		callSecondInit = false;
    		eventSecondInit();
    	}
    	if (callRegisterOnly) {
    		callRegisterOnly = false;
    		eventRegisterOnly();
    	}
    }

    private boolean testFileSystem() {
    	if (DIRS == null || DIRS.length == 0)
    		return true;
    	
   		if (FileSystem.createRoot(MainApplication.APP_NAME)) {
//Logger.w(TAG, "FileSystem succesfully created!");
    	} else {
//Logger.w(TAG, "FileSystem cannot be created!");
			UtilsGUI.showDialogError(CustomMain.this,
					R.string.filesystem_cannot_create_root,
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showDialogFinish(FINISH_EXIT_FORCE);
				}
			});
			return false;
    	}
    	
    	// fileSystem created successfully
    	for (int i = 0; i < DIRS.length; i++) {
    		(new File(FileSystem.ROOT + DIRS[i])).mkdirs();
    	}
        return true;
    }
    
    private boolean testFreeSpace() {
    	if (DIRS == null || DIRS.length == 0)
    		return true;
    	
    	// check disk space (at least 5MB)
    	StatFs stat = new StatFs(FileSystem.ROOT);
    	long bytesFree = (long) stat.getBlockSize() *(long) stat.getAvailableBlocks();
    	long megFree = bytesFree / 1048576;
//Logger.d(TAG, "megAvailable:" + megAvail + ", free:" + megFree);
    	if (megFree > 0 && megFree < 5) {
    		UtilsGUI.showDialogError(CustomMain.this,
    				getString(R.string.not_enough_disk_space_x, FileSystem.ROOT, megFree + "MB"),
    				new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				showDialogFinish(FINISH_EXIT_FORCE);
    			}
    		});
    		return false;
    	}
    	return true;
    }
    
    @Override
    public void onDestroy() {
		if (finish) {
			// stop debug if any forgotten
			Debug.stopMethodTracing();
			// remember value before A.getApp() exist
			boolean clearPackageAllowed = Utils.isPermissionAllowed(Manifest.permission.KILL_BACKGROUND_PROCESSES); 

			// call individual app close
			eventDestroyApp();

			// disable highlight
			PreferenceFunc.disableWakeLock();
			// save last known location
			Settings.setLastKnownLocation();
			// disable GPS modul
			LocationState.destroy(CustomMain.this);

			// destroy static references
			A.destroy();
			// call native destroy
			super.onDestroy();

			// remove app from memory			
			if (clearPackageAllowed) {
				clearPackageFromMemory(); // XXX not work on 2.2 and higher!!!
			}
		} else {
			super.onDestroy();
		}
    }
    
    public static final int FINISH_NONE = -1;
    public static final int FINISH_EXIT = 0;
    public static final int FINISH_EXIT_FORCE = 1;
    public static final int FINISH_RESTART = 2;
    public static final int FINISH_RESTART_FORCE = 3;
    public static final int FINISH_RESTART_FACTORY_RESET = 4;
    
    private boolean finish = false;
    protected int finishType = FINISH_NONE;
    
    public void showDialogFinish(final int typeOfFinish) {
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
		    				finish = true;
		    				CustomMain.this.finish();
		    			} else if (finishType == FINISH_RESTART || finishType == FINISH_RESTART_FORCE ||
		    					finishType == FINISH_RESTART_FACTORY_RESET) {
				    		// Setup one-short alarm to restart my application in 3 seconds - TODO need use another context
//				    		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//				    		Intent intent = new Intent(APP_INTENT_MAIN);
//				    		PendingIntent pi = PendingIntent.getBroadcast(CustomMain.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//				    		alarmMgr.set(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + 3000, pi); 
		    				finish = true;
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
    
    public void finishForceSilent() {
    	finish = true;
		CustomMain.this.finish();
    }
    
    private void clearPackageFromMemory() {
    	new Thread(new Runnable() {
			public void run() {
				try {
					ActivityManager aM = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);

					Thread.sleep(1250);
					aM.killBackgroundProcesses(getPackageName());
				} catch (Exception e) {
					Logger.e(TAG, "clearPackageFromMemory()", e);
				}
			}
		}).start();
    }
    


    
    public static String getNewsFromTo(int lastVersion, int actualVersion) {
//Logger.d(TAG, "getNewsFromTo(" + lastVersion + ", " + actualVersion + "), file:" + 
//		"news_" + (Const.isPro() ? "pro" : "free") + ".xml");
   		String versionInfo = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
      	String data = AssetHelper.loadAssetString("news.xml");
      	if (data == null || data.length() == 0)
      		data = AssetHelper.loadAssetString("news.xml");
    	if (data != null && data.length() > 0) {
            XmlPullParser parser;
            try {
            	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            	parser = factory.newPullParser();
            	parser.setInput(new StringReader(data));

            	int event;
                String tagName;

                boolean correct = false;
                while (true) {
                    event = parser.nextToken();
                    if (event == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                        	String name = parser.getAttributeValue(null, "name");
                        	int id = Utils.parseInt(parser.getAttributeValue(null, "id"));
                        	if (id > lastVersion && id <= actualVersion) {
                        		correct = true;
                        		versionInfo += ("<h4>" + name + "</h4><ul>");
                        	} else {
                        		correct = false;
                        	}
                        } else if (tagName.equalsIgnoreCase("li")) {
                        	if (correct) {
                        		versionInfo += ("<li>" + parser.nextText() + "</li>");
                        	}
                        }
                    } else if (event == XmlPullParser.END_TAG) {
                    	tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                        	if (correct) {
                        		correct = false;
                        		versionInfo += "</ul>";
                        	}
                        } else if (tagName.equals("document")) {
                        	break;
                        }
                    }
                }
            } catch (Exception e) {
            	Logger.e(TAG, "getNews()", e);
            }
    	}
   		
		versionInfo += "</body></html>";
    	return versionInfo;
    }
    
	/**
	 * This is called only once after application start. It's
	 * called in onCreate method before layout is placed
	 */
    protected abstract void eventFirstInit();
    
    /**
     * Method that create layout for actual activity. This is
     * called everytime, onCreate method is called
     */
    protected abstract void eventCreateLayout();
    
	/**
	 * This is called only once after application start. It's
	 * called in onResume method
	 */
    protected abstract void eventSecondInit();
    
	/**
	 * This is called everytime except first run. It's
	 * called in onResume method
	 */
    protected abstract void eventRegisterOnly();
    
    /**
     * This is called only when application really need to be destroyed,
     * so in this method is suggested to clear all variables
     */
    protected abstract void eventDestroyApp();
    

}
