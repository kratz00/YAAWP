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


		root.addPreference(createPrefGuiding(activity, activity.getPreferenceManager()));


		return root;
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
	
}
