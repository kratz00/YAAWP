/*
  * This file is part of WhereYouGo.
  *
  * WhereYouGo is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * WhereYouGo is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with WhereYouGo.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2012 Menion <whereyougo@asamm.cz>
  */ 

package menion.android.whereyougo.gui;

import org.yaawp.R;
import menion.android.whereyougo.gui.extension.CustomPreferenceActivity;
import menion.android.whereyougo.settings.SettingItems;
import menion.android.whereyougo.settings.UtilsSettings;
import menion.android.whereyougo.utils.Logger;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingScreens extends CustomPreferenceActivity {

	private static String TAG = "SettingScreens";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.settings);
		
		try {
			setPreferenceScreen(createPreferences(SettingScreens.this));
			UtilsSettings.setDependecies(this);
		} catch (Exception e) {
			Logger.e(TAG, "onCreate()", e);
		}
	}
	
	public static PreferenceScreen createPreferences(CustomPreferenceActivity activity) {
		PreferenceScreen root = activity.getPreferenceManager().createPreferenceScreen(activity);
		root.addPreference(createPrefGps(activity, activity.getPreferenceManager()));
		root.addPreference(createPrefSensors(activity, activity.getPreferenceManager()));
		root.addPreference(createPrefGuiding(activity, activity.getPreferenceManager()));
		root.addPreference(createPrefLocale(activity, activity.getPreferenceManager()));
		root.addPreference(createPrefWherigo(activity, activity.getPreferenceManager()));
		return root;
	}
	
	public static PreferenceScreen createPrefGps(CustomPreferenceActivity activity,
			PreferenceManager prefManager) {
		PreferenceScreen preferenceGps = prefManager.createPreferenceScreen(activity);
		preferenceGps.setTitle(R.string.gps_and_location);
		// category global
        PreferenceCategory prefCatGlobal = 
        	addNewPreferenceCategory(activity, R.string.pref_global, preferenceGps);
        PreferenceCategory prefCatDisable = 
            	addNewPreferenceCategory(activity, R.string.disable_location, preferenceGps);
        SettingItems.addPrefGpsAltitudeManualCorrection(activity, prefCatGlobal);
        SettingItems.addPrefGpsBeepOnGpsFix(activity, prefCatGlobal);

        // disabling gps part
        SettingItems.addPrefGpsDisable(activity, prefCatDisable);
        SettingItems.addPrefGuidingGpsRequired(activity, prefCatDisable);
        return init(preferenceGps);
	}
	
	public static PreferenceScreen createPrefSensors(CustomPreferenceActivity activity,
			PreferenceManager prefManager) {
		PreferenceScreen preferenceSensors = prefManager.createPreferenceScreen(activity);
		preferenceSensors.setTitle(R.string.pref_sensors);
        PreferenceCategory prefCatOrient = 
        	addNewPreferenceCategory(activity, R.string.pref_orientation, preferenceSensors);

        SettingItems.addPrefSensorsCompassHardware(activity, prefCatOrient);
        SettingItems.addPrefSensorsCompassAutoChange(activity, prefCatOrient);
        SettingItems.addPrefSensorsCompassAutoChangeValue(activity, prefCatOrient);
        SettingItems.addPrefSensorsBearingTrue(activity, prefCatOrient);
        SettingItems.addPrefSensorsOrienFilter(activity, prefCatOrient);
        return init(preferenceSensors);
	}
	
	public static PreferenceScreen createPrefGuiding(CustomPreferenceActivity activity,
			PreferenceManager prefManager) {
		PreferenceScreen preferenceGuiding = prefManager.createPreferenceScreen(activity);
		preferenceGuiding.setTitle(R.string.pref_guiding);
		PreferenceCategory prefCatGlobal = 
			addNewPreferenceCategory(activity, R.string.pref_global, preferenceGuiding);
		PreferenceCategory prefCatWptGuide = 
			addNewPreferenceCategory(activity, R.string.waypoints, preferenceGuiding);

		SettingItems.addPrefGuidingCompassSounds(activity, prefCatGlobal);
		
		SettingItems.addPrefGuidingWptSound(activity, prefCatWptGuide);
		SettingItems.addPrefGuidingWptSoundDistance(activity, prefCatWptGuide);
        return init(preferenceGuiding);
	}
	
	public static PreferenceScreen createPrefLocale(CustomPreferenceActivity activity,
			PreferenceManager prefManager) {
		PreferenceScreen preferenceLocale = prefManager.createPreferenceScreen(activity);
		preferenceLocale.setTitle(R.string.pref_locale);
		PreferenceCategory prefCatLang = 
			addNewPreferenceCategory(activity, R.string.pref_language, preferenceLocale);
		PreferenceCategory prefCatLocale = 
			addNewPreferenceCategory(activity, R.string.pref_units, preferenceLocale);

		SettingItems.addPrefLocal(activity, prefCatLang);
        SettingItems.addPrefUnitsCooLatLon(activity, prefCatLocale);
        SettingItems.addPrefUnitsLength(activity, prefCatLocale);
        SettingItems.addPrefUnitsAltitude(activity, prefCatLocale);
        SettingItems.addPrefUnitsSpeed(activity, prefCatLocale);
        SettingItems.addPrefUnitsAngle(activity, prefCatLocale);
        
        return init(preferenceLocale);
	}
	
	public static PreferenceScreen createPrefWherigo( CustomPreferenceActivity activity,
	                PreferenceManager prefManager ) {
	    PreferenceScreen preferenceWherigo = prefManager.createPreferenceScreen( activity );
	    preferenceWherigo.setTitle( R.string.pref_wherigo_engine ); 

	    PreferenceCategory prefCatWherigo = 
	                    addNewPreferenceCategory(activity, R.string.pref_wherigo_engine, preferenceWherigo );
	    
	    SettingItems.addPrefWherigoDeviceId( activity, prefCatWherigo );
	    SettingItems.addPrefWherigoPlattform( activity, prefCatWherigo );
	    SettingItems.addPrefWherigoReplaceUsername( activity, prefCatWherigo );
	    SettingItems.addPrefWherigoUsername( activity, prefCatWherigo );
	    
	    return init(preferenceWherigo);
	}
}
