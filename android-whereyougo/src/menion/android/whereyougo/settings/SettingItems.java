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

package menion.android.whereyougo.settings;

import static menion.android.whereyougo.settings.Settings.*;
import org.yaawp.R;
import menion.android.whereyougo.gui.extension.CustomPreferenceActivity;
import menion.android.whereyougo.settings.SettingValues;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;
import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.text.InputType;

public class SettingItems {
	
	private static final String TAG = "SettingItems";
	

	/*****************************/
	/*           GLOBAL          */
	/*****************************/
	// GLOBAL
	
	
	public static void addPrefConfirmOnExit(CustomPreferenceActivity activity, 
			PreferenceCategory category) {
		activity.addCheckBoxPreference(category,
				R.string.pref_confirm_on_exit,
				R.string.pref_confirm_on_exit_desc,
				Settings.KEY_B_CONFIRM_ON_EXIT,
				Settings.DEFAULT_CONFIRM_ON_EXIT);
	}
	

	/**************************/
	/*           GPS          */
	/**************************/
	


	



	

        
   

	/***************************/
	/*         SENSORS         */
	/***************************/
	

	
	
	

	

	
	/********************************/
	/*            GUIDING           */
	/********************************/
	
	public static void addPrefGuidingCompassSounds(CustomPreferenceActivity activity, 
			PreferenceCategory category) {
		activity.addCheckBoxPreference(category,
				R.string.pref_guiding_compass_sounds,
				R.string.pref_guiding_compass_sounds_desc,
				Settings.KEY_B_GUIDING_COMPASS_SOUNDS,
				Settings.DEFAULT_GUIDING_COMPASS_SOUNDS,
				new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						setPrefGuidingCompassSounds(false, Utils.parseBoolean(newValue));
						return true;
					}
				});
	}
	
	public static void setPrefGuidingCompassSounds(boolean saveToPref, boolean value) {
		if (saveToPref) {
			Settings.setPrefBoolean(Settings.KEY_B_GUIDING_COMPASS_SOUNDS, value);
		}
		SettingValues.GUIDING_SOUNDS = value;
	}
	
	
	public static void addPrefGuidingWptSound(final CustomPreferenceActivity activity, 
			PreferenceCategory category) {
        CharSequence[] entries = new CharSequence[] {
        		activity.getString(R.string.pref_guiding_waypoint_sound_increasing),
        		activity.getString(R.string.pref_guiding_waypoint_sound_beep_on_distance),
        		activity.getString(R.string.pref_guiding_waypoint_sound_custom_on_distance)};
        CharSequence[] entryValues = new CharSequence[] {
        		String.valueOf(VALUE_GUIDING_WAYPOINT_SOUND_INCREASE_CLOSER),
        		String.valueOf(VALUE_GUIDING_WAYPOINT_SOUND_BEEP_ON_DISTANCE),
        		String.valueOf(VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND)};
		ListPreference pref = activity.addListPreference(category,
				R.string.pref_guiding_sound_type,
				R.string.pref_guiding_sound_type_waypoint_desc,
				KEY_S_GUIDING_WAYPOINT_SOUND,
				DEFAULT_GUIDING_WAYPOINT_SOUND,
				entries, entryValues,
				new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference pref, Object newValue) {
						int result = Utils.parseInt(newValue);
						if (result != VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND) {
							setPrefGuidingWptSound(activity, (ListPreference) pref,
									Utils.parseInt(newValue));
							return true;
						} else {
							lastUsedPreference = (ListPreference) pref;
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType("audio/*");
							if (!Utils.isIntentAvailable(intent)) {
								intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
							}
							activity.startActivityForResult(intent, REQUEST_GUIDING_WPT_SOUND);
							return false;
						}
					}
				});
		setPrefGuidingWptSound(activity, pref,
				SettingValues.GUIDING_WAYPOINT_SOUND);
	}
	
	private static void setPrefGuidingWptSound(CustomPreferenceActivity activity,
			ListPreference pref, int value) {
		SettingValues.GUIDING_WAYPOINT_SOUND = value;
		setListPreference(activity, pref, value,
        		R.string.pref_guiding_sound_type_waypoint_desc);
	}
	
	public static void addPrefGuidingWptSoundDistance(final CustomPreferenceActivity activity, 
			PreferenceCategory category) {
		EditTextPreference pref = activity.addEditTextPreference(category,
				R.string.pref_guiding_sound_distance,
				R.string.pref_guiding_sound_distance_waypoint_desc,
				Settings.KEY_S_GUIDING_WAYPOINT_SOUND_DISTANCE,
				Settings.DEFAULT_GUIDING_WAYPOINT_SOUND_DISTANCE,
				InputType.TYPE_CLASS_NUMBER,
				new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference pref, Object newValue) {
						int value = Utils.parseInt(newValue);
						if (value > 0) {
							SettingValues.GUIDING_WAYPOINT_SOUND_DISTANCE = value;
							setEditTextPreference(activity, (EditTextPreference) pref,
									SettingValues.GUIDING_WAYPOINT_SOUND_DISTANCE + "m",
									R.string.pref_guiding_sound_distance_waypoint_desc);
							return true;
						} else {
							ManagerNotify.toastShortMessage(R.string.invalid_value);
							return false;
						}
					}
				});
		setEditTextPreference(activity, pref,
				SettingValues.GUIDING_WAYPOINT_SOUND_DISTANCE + "m",
				R.string.pref_guiding_sound_distance_waypoint_desc);
	}
	
	/********************************/
	/*             UNITS            */
	/********************************/
	

	
	private static String getLanguageText(String value) {
		if (value.equals(VALUE_LANGUAGE_DEFAULT)) {
			return Loc.get(R.string.pref_language_default);
		} else if (value.equals(VALUE_LANGUAGE_AR)) {
			return Loc.get(R.string.pref_language_ar);
		} else if (value.equals(VALUE_LANGUAGE_CZ)) {
			return Loc.get(R.string.pref_language_cs);
		} else if (value.equals(VALUE_LANGUAGE_DA)) {
			return Loc.get(R.string.pref_language_da);
		} else if (value.equals(VALUE_LANGUAGE_DE)) {
			return Loc.get(R.string.pref_language_de);
		} else if (value.equals(VALUE_LANGUAGE_EL)) {
			return Loc.get(R.string.pref_language_el);
		} else if (value.equals(VALUE_LANGUAGE_EN)) {
			return Loc.get(R.string.pref_language_en);
		} else if (value.equals(VALUE_LANGUAGE_ES)) {
			return Loc.get(R.string.pref_language_es);
		} else if (value.equals(VALUE_LANGUAGE_FI)) {
			return Loc.get(R.string.pref_language_fi);
		} else if (value.equals(VALUE_LANGUAGE_FR)) {
			return Loc.get(R.string.pref_language_fr);
		} else if (value.equals(VALUE_LANGUAGE_HU)) {
			return Loc.get(R.string.pref_language_hu);
		} else if (value.equals(VALUE_LANGUAGE_IT)) {
			return Loc.get(R.string.pref_language_it);
		} else if (value.equals(VALUE_LANGUAGE_JA)) {
			return Loc.get(R.string.pref_language_ja);
		} else if (value.equals(VALUE_LANGUAGE_KO)) {
			return Loc.get(R.string.pref_language_ko);
		} else if (value.equals(VALUE_LANGUAGE_NL)) {
			return Loc.get(R.string.pref_language_nl);
		} else if (value.equals(VALUE_LANGUAGE_PL)) {
			return Loc.get(R.string.pref_language_pl);
		} else if (value.equals(VALUE_LANGUAGE_PT)) {
			return Loc.get(R.string.pref_language_pt);
		} else if (value.equals(VALUE_LANGUAGE_PT_BR)) {
			return Loc.get(R.string.pref_language_pt_br);
		} else if (value.equals(VALUE_LANGUAGE_RU)) {
			return Loc.get(R.string.pref_language_ru);
		} else if (value.equals(VALUE_LANGUAGE_SK)) {
			return Loc.get(R.string.pref_language_sk);
		} else {
			return "";
		}
	}
	


	
	
	private static void setPreferenceText(Activity activity, Preference pref,
			String value, int desc) {
		pref.setSummary(getFormatedText(
				pref.isEnabled(), value, activity.getString(desc)));
	}
	
	public static void setListPreference(Activity activity, ListPreference pref,
			int index, int desc) {
		setListPreference(activity, pref, index, activity.getString(desc));
	}
	
	public static void setListPreference(Activity activity, ListPreference pref,
			int index, String desc) {
		pref.setSummary(getFormatedText(
				pref.isEnabled(), pref.getEntries()[index].toString(), desc));
	}
	
	private static void setEditTextPreference(Activity activity, EditTextPreference pref,
			String value, int desc) {
		pref.setSummary(getFormatedText(
				pref.isEnabled(), value, activity.getString(desc)));
	}
	
	private static String getFormatedText(boolean enabled, String value, String desc) {
		StringBuffer buff = new StringBuffer();
		buff.append("(");
		buff.append(value);
		buff.append(") ");
		buff.append(desc);
		return buff.toString();
	}
	
	private static final int REQUEST_GUIDING_WPT_SOUND = 0; 
	
	private static ListPreference lastUsedPreference;
	
	public static void handleResponse(CustomPreferenceActivity activity,
			int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GUIDING_WPT_SOUND) {
			if (resultCode == Activity.RESULT_OK && data != null) {
				Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				//Uri uri = data.getData();//getStringExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
				if (uri != null) {
					Logger.d(TAG, "uri:" + uri.toString());
					Settings.setPrefString(KEY_S_GUIDING_WAYPOINT_SOUND,
							String.valueOf(VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND));
					Settings.setPrefString(VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI,
							uri.toString());
					setPrefGuidingWptSound(activity, lastUsedPreference,
							VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND);
				}
			}
			lastUsedPreference = null;
		}
	}
}