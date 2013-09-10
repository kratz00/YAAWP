package org.yaawp.hmi.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import org.yaawp.R;

public class YaawpPreferenceActivity extends PreferenceActivity {

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.addPreferencesFromResource(R.xml.preferences);
	    initPreferences();
	
	    /* TODO Intent intent = getIntent();
	    int gotoPage = intent.getIntExtra(INTENT_GOTO, 0);
	    if (gotoPage == INTENT_GOTO_SERVICES) {
	        // start with services screen
	        PreferenceScreen main = (PreferenceScreen) getPreference(R.string.pref_fakekey_main_screen);
	        int index = getPreference(R.string.pref_fakekey_services_screen).getOrder();
	        main.onItemClick(null, null, index, 0);
	    }
	    */
	}
	
	/* @Override
	protected void onPause() {
	    super.onPause();
	}*/
	
	private void initPreferences() {
	}
}