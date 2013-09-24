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
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.settings.SettingValues;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.ManagerNotify;

import org.yaawp.R;
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
	    
	    initPreferences();
	}
	
	private void addOnPreferenceChangeListener( int key, Preference.OnPreferenceChangeListener listener ) {
	    
	    String _key = getKey( key );
	    Preference preference = this.findPreference ( this, _key );
	    if ( preference != null ) {
	    	preference.setOnPreferenceChangeListener(listener);
	    }
	    // listener.onPreferenceChange(preference, value);
	}
	
	/* @Override
	protected void onPause() {
	    super.onPause();
	}*/
	
	private void initPreferences() {
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
            return true;
        }
    }; 
    

    
}