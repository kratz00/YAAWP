	package org.yaawp.hmi.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.content.SharedPreferences;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.gui.extension.CustomMain;
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.ManagerNotify;

import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.openwig.OpenWigHelper;
import org.yaawp.preferences.PreferenceFunc;

import cz.matejcik.openwig.WherigoLib;

public class YaawpPreferenceActivity extends PreferenceActivity {

	private static String TAG = "YaawpPreferenceActivity";
	
	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.addPreferencesFromResource(R.xml.preferences);
	    
	    addOnPreferenceChangeListener( R.string.pref_highlight, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_wherigo_engine_deviceid, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_wherigo_engine_platform, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_sensors_compass_hardware, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_sensors_compass_auto_change, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_sensors_compass_auto_change_value, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_cartridgelist_sorting, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_cartridgelist_anywhere_first, VALUE_CHANGE_LISTENER );
	    
	    addOnPreferenceChangeListener( R.string.pref_scan_external_storage, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_exclude_android_dir, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_exclude_hidden_dirs, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_exclude_whereyougo_dir, VALUE_CHANGE_LISTENER );
	    addOnPreferenceChangeListener( R.string.pref_include_dropbox_dir, VALUE_CHANGE_LISTENER );
	    
	}
	
	/*
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			CustomActivity.customOnCreate(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onCreate()", e);
		}
	}
	
	public void onStart() {
		try {
			super.onStart();
			CustomActivity.customOnStart(this);
			
			needRestart = false;
			needRestartFactoryReset = false;
			needGpsRestart = false;
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onStart()", e);
		}
	}
	
	protected void onResume() {
		try {
			super.onResume();
			CustomActivity.customOnResume(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onResume()", e);
		}
	}
	
	protected void onPause() {
		try {
			super.onPause();
			CustomActivity.customOnPause(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onPause()", e);
		}
	}
	
	public void onDestroy() {
		try {
			super.onDestroy();
			if (needRestartFactoryReset) {
	    		A.getMain().showDialogFinish(CustomMain.FINISH_RESTART_FACTORY_RESET);
	    	} else if (needRestart) {
	    		A.getMain().showDialogFinish(CustomMain.FINISH_RESTART);
	    	}
			
			if (needGpsRestart) {
				if (LocationState.isActuallyHardwareGpsOn()) {
					LocationState.setGpsOff(CustomPreferenceActivity.this);
					LocationState.setGpsOn(CustomPreferenceActivity.this);
				}
			}
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onDestroy()", e);
		}
	}
	 */
	
	/*
	 *  ----
	 */
	
	private void addOnPreferenceChangeListener( int key, Preference.OnPreferenceChangeListener listener ) {
	    
	    String _key = getKey( key );
	    Preference preference = this.findPreference ( this, _key );
	    if ( preference != null ) {
	    	preference.setOnPreferenceChangeListener(listener);
	    }
	    // listener.onPreferenceChange(preference, value);
	}
	
    @SuppressWarnings("deprecation")
    public static Preference findPreference(final PreferenceActivity preferenceActivity, final CharSequence key) {
        return preferenceActivity.findPreference(key);
    }  
    
    private static boolean isPreference(final Preference preference, int preferenceKeyId) {
        return getKey(preferenceKeyId).equals(preference.getKey());
    }
    
    private static String getKey(final int prefKeyId) {
        return A.getApp().getString(prefKeyId);
    }   

    private static final Preference.OnPreferenceChangeListener VALUE_CHANGE_LISTENER = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, final Object value) {
            boolean status = true;
            
            if (isPreference(preference, R.string.pref_highlight)) {
            	PreferenceFunc.enableWakeLock();
            	// TODO preference.setSummary( stringValue );
            }
            else if (isPreference(preference, R.string.pref_wherigo_engine_deviceid)) {
            	OpenWigHelper.SetDeviceId(value.toString());           	
            }
            else if (isPreference(preference, R.string.pref_wherigo_engine_platform)) {
            	OpenWigHelper.SetPlatform(value.toString());         	
            }
            else if (isPreference(preference, R.string.pref_sensors_compass_hardware)) {
            	// TODO check if new value already persisted
            	A.getRotator().manageSensors();
            }
            else if (isPreference(preference, R.string.pref_sensors_compass_auto_change)) {
            	// TODO check if new value already persisted
            	A.getRotator().manageSensors();
            }
            else if (isPreference(preference, R.string.pref_sensors_compass_auto_change_value)) {
				if ( Utils.parseInt(value) <= 0) {
					ManagerNotify.toastShortMessage(R.string.invalid_value);
					status = false;
				}
            }
            else if (isPreference(preference, R.string.pref_language)) {
				// TODO activity.needRestart = true;
				return true;
            }
            else if (isPreference(preference, R.string.pref_cartridgelist_sorting)) {
            	YaawpAppData.GetInstance().mRefreshCartridgeList = true;
            }
            else if (isPreference(preference, R.string.pref_cartridgelist_anywhere_first)) {
            	YaawpAppData.GetInstance().mRefreshCartridgeList = true;
            }            
            else if (isPreference(preference, R.string.pref_scan_external_storage)) {
            	YaawpAppData.GetInstance().mCartridges.clear();
            }            
            else if (isPreference(preference, R.string.pref_exclude_android_dir)) {
            	YaawpAppData.GetInstance().mCartridges.clear();
            }
            else if (isPreference(preference, R.string.pref_exclude_hidden_dirs)) {
            	YaawpAppData.GetInstance().mCartridges.clear();
            }
            else if (isPreference(preference, R.string.pref_exclude_whereyougo_dir)) {
            	YaawpAppData.GetInstance().mCartridges.clear();
            }
            else if (isPreference(preference, R.string.pref_include_dropbox_dir)) {
            	YaawpAppData.GetInstance().mCartridges.clear();
            }            
           
            
            return true;
        }
    }; 
    

    
}