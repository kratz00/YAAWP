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

package org.yaawp.preferences;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import org.yaawp.MainApplication;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;


public class Settings {

	private static final String TAG = "Settings";

	/** last application version that run on machine */
	private static final String KEY_S_APPLICATION_VERSION_LAST = "KEY_S_APPLICATION_VERSION_LAST";
	
	// GLOBAL
	/** screen highlight mode */
	public static final int VALUE_HIGHLIGHT_OFF = 0;
	public static final int VALUE_HIGHLIGHT_ONLY_GPS = 1;
	public static final int VALUE_HIGHLIGHT_ALWAYS = 2;


	// GENERAL
	/** default language */
	public static final String VALUE_LANGUAGE_DEFAULT = "default";
	public static final String VALUE_LANGUAGE_AR = "ar";
	public static final String VALUE_LANGUAGE_CZ = "cs";
	public static final String VALUE_LANGUAGE_DA = "da";
	public static final String VALUE_LANGUAGE_DE = "de";
	public static final String VALUE_LANGUAGE_EL = "el";
	public static final String VALUE_LANGUAGE_EN = "en";
	public static final String VALUE_LANGUAGE_ES = "es";
	public static final String VALUE_LANGUAGE_FI = "fi";
	public static final String VALUE_LANGUAGE_FR = "fr";
	public static final String VALUE_LANGUAGE_HU = "hu";
	public static final String VALUE_LANGUAGE_IT = "it";
	public static final String VALUE_LANGUAGE_JA = "ja";
	public static final String VALUE_LANGUAGE_KO = "ko";
	public static final String VALUE_LANGUAGE_NL = "nl";
	public static final String VALUE_LANGUAGE_PL = "pl";
	public static final String VALUE_LANGUAGE_PT = "pt";
	public static final String VALUE_LANGUAGE_PT_BR = "pt_BR";
	public static final String VALUE_LANGUAGE_RU = "ru";
	public static final String VALUE_LANGUAGE_SK = "sk";
	// public static final String DEFAULT_LANGUAGE = VALUE_LANGUAGE_DEFAULT;
	
	/** confirmation on exit */
	public static final String KEY_B_CONFIRM_ON_EXIT = "KEY_B_CONFIRM_ON_EXIT";
	public static final boolean DEFAULT_CONFIRM_ON_EXIT = true;
	/** last used index of coordinates format */
	public static final String KEY_I_GET_COORDINATES_LAST_INDEX = "KEY_I_GET_COORDINATES_LAST_INDEX";
	public static final int DEFAULT_GET_COORDINATES_LAST_INDEX = 0;
	

	// GPS & LOCATION
	/** if GPS should start automatically after application start */
	public static final String KEY_B_START_GPS_AUTOMATICALLY = "KEY_B_START_GPS_AUTOMATICALLY";
	public static final boolean DEFAULT_START_GPS_AUTOMATICALLY = true;
	/** last known latitude */
	protected static final String KEY_F_LAST_KNOWN_LOCATION_LATITUDE = "KEY_F_LAST_KNOWN_LOCATION_LATITUDE";
	protected static final float DEFAULT_LAST_KNOWN_LOCATION_LATITUDE = 50.07967f;
	/** last known longitude */
	protected static final String KEY_F_LAST_KNOWN_LOCATION_LONGITUDE = "KEY_F_LAST_KNOWN_LOCATION_LONGITUDE";
	protected static final float DEFAULT_LAST_KNOWN_LOCATION_LONGITUDE = 14.42980f;
	/** last known altitude */
	protected static final String KEY_F_LAST_KNOWN_LOCATION_ALTITUDE = "KEY_F_LAST_KNOWN_LOCATION_ALTITUDE";
	protected static final float DEFAULT_LAST_KNOWN_LOCATION_ALTITUDE = 0.0f;

	/** minimum time for notification */
	public static final String KEY_S_GPS_MIN_TIME_NOTIFICATION = "KEY_S_GPS_MIN_TIME_NOTIFICATION";
	public static final String DEFAULT_GPS_MIN_TIME_NOTIFICATION = "0";


	
	// SENSORS



	/** orientation filter */

	public static final int VALUE_SENSORS_ORIENT_FILTER_NO = 0;
	public static final int VALUE_SENSORS_ORIENT_FILTER_LIGHT = 1;
	public static final int VALUE_SENSORS_ORIENT_FILTER_MEDIUM = 2;
	public static final int VALUE_SENSORS_ORIENT_FILTER_HEAVY = 3;

	// GUIDING
	/** is guiding sounds enabled on compass screen */
	public static final String KEY_B_GUIDING_COMPASS_SOUNDS = "KEY_B_GUIDING_COMPASS_SOUNDS";
	public static final boolean DEFAULT_GUIDING_COMPASS_SOUNDS = false;
	/** disable gps when screen off during guiding */
	public static final String KEY_B_GUIDING_GPS_REQUIRED = "KEY_B_GUIDING_GPS_REQUIRED";
	public static final boolean DEFAULT_GUIDING_GPS_REQUIRED = true;
	/** waypoint sounds */
	public static final String KEY_S_GUIDING_WAYPOINT_SOUND = "KEY_S_GUIDING_WAYPOINT_SOUND";
	public static final int VALUE_GUIDING_WAYPOINT_SOUND_INCREASE_CLOSER = 0;
	public static final int VALUE_GUIDING_WAYPOINT_SOUND_BEEP_ON_DISTANCE = 1;
	public static final int VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND = 2;
	public static final String DEFAULT_GUIDING_WAYPOINT_SOUND = String.valueOf(VALUE_GUIDING_WAYPOINT_SOUND_BEEP_ON_DISTANCE);
	public static final String VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = "";
	/** waypoint sounds beep distance */
	public static final String KEY_S_GUIDING_WAYPOINT_SOUND_DISTANCE = "KEY_S_GUIDING_WAYPOINT_SOUND_DISTANCE";
	public static final String DEFAULT_GUIDING_WAYPOINT_SOUND_DISTANCE = "100";
	
	// UNITS PARAMETRES
	/** default latitude/longitude format */
	public static final int VALUE_UNITS_COO_LATLON_DEC = 0;
	public static final int VALUE_UNITS_COO_LATLON_MIN = 1;
	public static final int VALUE_UNITS_COO_LATLON_SEC = 2;	

	/** default length format */
	public static final int VALUE_UNITS_LENGTH_ME = 0;
	public static final int VALUE_UNITS_LENGTH_IM = 1;
	public static final int VALUE_UNITS_LENGTH_NA = 2;

	/** default height format */
	public static final int VALUE_UNITS_ALTITUDE_METRES = 0;
	public static final int VALUE_UNITS_ALTITUDE_FEET = 1;
	/** default angle format */
	public static final int VALUE_UNITS_SPEED_KMH = 0;
	public static final int VALUE_UNITS_SPEED_MILH = 1;
	public static final int VALUE_UNITS_SPEED_KNOTS = 2;
	/** default angle format */
	public static final int VALUE_UNITS_ANGLE_DEGREE = 0;
	public static final int VALUE_UNITS_ANGLE_MIL = 1;

	public static boolean getPrefBoolean(Context context, String key, boolean def) {
//		Logger.v(TAG, "getPrefBoolean(" + key + ", " + def + ")");
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, def);
	}
	public static void setPrefBoolean(Context context, String key, boolean value) {
//		Logger.v(TAG, "setPrefBoolean(" + key + ", " + value + ")");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
	} 
	public static int getPrefInt(Context context, String key, int def) {
//		Logger.v(TAG, "getPrefInt(" + key + ", " + def + ")");
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, def);
	}
	public static void setPrefInt(Context context, String key, int value) {
//		Logger.v(TAG, "setPrefInt(" + key + ", " + value + ")");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
	}
	public static float getPrefFloat(Context context, String key, float def) {
//		Logger.v(TAG, "getPrefFloat(" + key + ", " + def + ")");
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, def);
	}
	public static void setPrefFloat(Context context, String key, float value) {
//		Logger.v(TAG, "setPrefFloat(" + key + ", " + value + ")");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key, value).commit();
	}
	public static String getPrefString(Context context, String key, String def) {
//		Logger.v(TAG, "getPrefString(" + key + ", " + def + ")");
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, def);
	}
	public static void setPrefString(Context context, String key, String value) {
//		Logger.v(TAG, "setPrefString(" + key + ", " + value + ")");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
	}
	
	public static boolean getPrefBoolean(String key, boolean def) {
		if (A.getApp() == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getBoolean(key, def);
	}

	public static void setPrefBoolean(String key, boolean value) {
		if (A.getApp() == null) {
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().putBoolean(key, value).commit();
	} 
	public static int getPrefInt(String key, int def) {
		if (A.getApp() == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getInt(key, def);
	}
	public static void setPrefInt(String key, int value) {
		if (A.getApp() == null) {
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().putInt(key, value).commit();
	}
	public static float getPrefFloat(String key, float def) {
		if (A.getApp() == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getFloat(key, def);
	}
	public static void setPrefFloat(String key, float value) {
		if (A.getApp() == null) {
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().putFloat(key, value).commit();
	}
	public static String getPrefString(String key, String def) {
		if (A.getApp() == null) {
			return def;
		}
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).getString(key, def);
	}
	public static void setPrefString(String key, String value) {
		if (A.getApp() == null) {
			return;
		}
		PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().putString(key, value).commit();
	}
	
	/* APPLICATION VERSION */
	public static int getApplicationVersionLast() {
		return PreferenceManager.getDefaultSharedPreferences(A.getApp()).
		getInt(KEY_S_APPLICATION_VERSION_LAST, 0);
	}
	public static int getApplicationVersionActual() {
		try {
			return A.getApp().getPackageManager().getPackageInfo(A.getApp().getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Logger.e(TAG, "getApplicationVersionActual()", e);
			return 0;
		}
	}
	public static void setApplicationVersionLast(int lastVersion) {
		PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().
		putInt(KEY_S_APPLICATION_VERSION_LAST, lastVersion).
		commit();
	}
	public static String getApplicationVersionActualName() {
		try {
			return A.getApp().getPackageManager().getPackageInfo(A.getApp().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Logger.e(TAG, "getApplicationVersionActual()", e);
			return "";
		}
	}

	/* LANGUAGE */
	private static String loca = null;
    public static String getLanguageCode() {
    	if (loca == null) {
    		//String lang = getPrefString(KEY_S_LANGUAGE, Locale.getDefault().getLanguage());
    		String lang = Locale.getDefault().getLanguage();
Logger.w(TAG, "getLanguageCode() - " + lang);
    		if (lang == null)
    			return VALUE_LANGUAGE_EN;
    		if (lang.equals(VALUE_LANGUAGE_CZ)) {
    			loca = VALUE_LANGUAGE_CZ;
    		} else {
    			loca = VALUE_LANGUAGE_EN;
    		}
    	}
    	return loca;
    }
	/* LAST KNOW LOCATION */
    /** last known location */
    public static Location lastKnownLocation;
    public static Location getLastKnownLocation(Context c) {
    	if (lastKnownLocation == null) {
    		lastKnownLocation = new Location(TAG);
    		lastKnownLocation.setLatitude(getPrefFloat(c, KEY_F_LAST_KNOWN_LOCATION_LATITUDE,
    				DEFAULT_LAST_KNOWN_LOCATION_LATITUDE));
    		lastKnownLocation.setLongitude(getPrefFloat(c, KEY_F_LAST_KNOWN_LOCATION_LONGITUDE,
    				DEFAULT_LAST_KNOWN_LOCATION_LONGITUDE));
    		lastKnownLocation.setAltitude(getPrefFloat(c, KEY_F_LAST_KNOWN_LOCATION_ALTITUDE,
    				DEFAULT_LAST_KNOWN_LOCATION_ALTITUDE));
    	}
    	return lastKnownLocation;
    }
	public static void setLastKnownLocation() {
		try {
			PreferenceManager.getDefaultSharedPreferences(A.getApp()).edit().
			putFloat(KEY_F_LAST_KNOWN_LOCATION_LATITUDE, 
					(float) LocationState.getLocation().getLatitude()).
			putFloat(KEY_F_LAST_KNOWN_LOCATION_LONGITUDE,
					(float) LocationState.getLocation().getLongitude()).
			putFloat(KEY_F_LAST_KNOWN_LOCATION_ALTITUDE,
					(float) LocationState.getLocation().getAltitude()).
			commit();
		} catch (Exception e) {
			Logger.e(TAG, "setLastKnownLocation()", e);
		}
	}

	// setted from onResume();
    private static Activity currentActivity;
    public static boolean existCurrentActivity() {
    	return currentActivity != null;
    }
    
    public static Activity getCurrentActivity() {
    	return currentActivity == null ? A.getMain() : currentActivity;
    }
    
    public static void setCurrentActivity(Activity activity) {
    	if (Settings.currentActivity == null && activity != null)
    		MainApplication.appRestored();
    	Settings.currentActivity = activity;
    }   

}
