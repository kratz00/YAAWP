	package org.yaawp.hmi.activities;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.Intent;
import android.app.Activity;

import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.openwig.OpenWigHelper;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.PreferenceValues;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.A;
import org.yaawp.utils.ManagerNotify;
import org.yaawp.utils.Utils;

import android.media.RingtoneManager;
import android.net.Uri;


public class YaawpPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

	private static String TAG = "YaawpPreferenceActivity";
	
	private static final int REQUEST_GUIDING_WPT_SOUND = 0; 
	
	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.addPreferencesFromResource(R.xml.preferences);
	    
	    int prefs[]= new int[] { 
	    	  R.string.pref_highlight
	    	, R.string.pref_wherigo_engine_deviceid
	    	, R.string.pref_wherigo_engine_platform
	    	, R.string.pref_sensors_compass_hardware
	    	, R.string.pref_sensors_compass_auto_change
	    	, R.string.pref_sensors_compass_auto_change_value
	    	, R.string.pref_cartridgelist_sorting
	    	, R.string.pref_cartridgelist_anywhere_first
		    , R.string.pref_scan_external_storage
		    , R.string.pref_exclude_android_dir
		    , R.string.pref_exclude_hidden_dirs
	    	, R.string.pref_exclude_whereyougo_dir
	    	, R.string.pref_include_dropbox_dir
	    	// , R.string.pref_guiding_compass_sounds 
	    	, R.string.pref_guiding_sound_type
	    	, R.string.pref_guiding_sound_distance
	    };
	    
	    for ( int i=0; i<prefs.length; i++ ) {
		    Preference preference = findPreference ( prefs[i] );
		    if ( preference != null ) {
		    	preference.setOnPreferenceChangeListener( this );
		    }	    	
	    }

	    
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
		
    @SuppressWarnings("deprecation")
    public Preference findPreference(final PreferenceActivity preferenceActivity, final CharSequence key) {
        return preferenceActivity.findPreference(key);
    }  

    public Preference findPreference( final int key ) {
    	String keyString = getKey( key );
        return findPreference(keyString);
    }     
    
    private static boolean isPreference(final Preference preference, int preferenceKeyId) {
        return getKey(preferenceKeyId).equals(preference.getKey());
    }
    
    private static String getKey(final int prefKeyId) {
        return A.getApp().getString(prefKeyId);
    }   
   
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
        	// TODO YaawpAppData.GetInstance().mCartridges.clear(); // TODO set flag for new file scan
        }            
        else if (isPreference(preference, R.string.pref_exclude_android_dir)) {
        	// TODO YaawpAppData.GetInstance().mCartridges.clear(); // TODO set flag for new file scan
        }
        else if (isPreference(preference, R.string.pref_exclude_hidden_dirs)) {
        	// TODO YaawpAppData.GetInstance().mCartridges.clear(); // TODO set flag for new file scan
        }
        else if (isPreference(preference, R.string.pref_exclude_whereyougo_dir)) {
        	// TODO YaawpAppData.GetInstance().mCartridges.clear(); // TODO set flag for new file scan
        }
        else if (isPreference(preference, R.string.pref_include_dropbox_dir)) {
        	// TODO YaawpAppData.GetInstance().mCartridges.clear(); // TODO set flag for new file scan
        }            
        else if (isPreference(preference, R.string.pref_guiding_sound_type)) {
            int result = Utils.parseInt(value);
            if (result == PreferenceValues.GuidingWaypointSound.CUSTOM_SOUND ) {
                // lastUsedPreference = (ListPreference) pref;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("audio/*");
                if (!Utils.isIntentAvailable(intent)) {
                	intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                }
                this.startActivityForResult(intent, REQUEST_GUIDING_WPT_SOUND);
                status = true; // don't set the preference yet
            }            	
        }
        else if (isPreference(preference, R.string.pref_guiding_sound_distance)) {
        	int distance = Utils.parseInt(value);
            if (distance <= 0) {
            	ManagerNotify.toastShortMessage(R.string.invalid_value);
                status = false;
            }	
        }            

        return status;
    }
    


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GUIDING_WPT_SOUND) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    if (uri != null) {
                    	Preference preference = findPreference( R.string.pref_guiding_sound_type );
                    	// String key = preference.getKey();
                    	// PreferenceManager.getDefaultSharedPreferences( this ).edit().putInt( key, PreferenceValues.GuidingWaypointSound.CUSTOM_SOUND );
                    	// PreferenceManager.getDefaultSharedPreferences( this ).edit().putString( key+"_uri", uri.toString() );
                    	// Settings.setPrefInt( getKey(R.string.pref_guiding_sound_type), PreferenceValues.GuidingWaypointSound.CUSTOM_SOUND );
                    	Settings.setPrefString(Settings.VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI, uri.toString() );
                    }
            }
        }
    }
    

    


}