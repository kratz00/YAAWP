package org.yaawp.preferences;

import org.yaawp.R;
import org.yaawp.preferences.PreferenceUtils;
import menion.android.whereyougo.settings.*;

public class PreferenceItems {

	public static boolean useImperalSystem() {
		return PreferenceUtils.getPrefBoolean( R.string.pref_units_imperal_system );
	}
	
	public static int getUnitLength() {
		if ( useImperalSystem() ) {
			return Settings.VALUE_UNITS_LENGTH_IM;			
		} else {
			return Settings.VALUE_UNITS_LENGTH_ME;
		}
	}

	public static int getUnitAltitude() {
		if ( useImperalSystem() ) {
			return Settings.VALUE_UNITS_ALTITUDE_FEET;			
		} else {
			return Settings.VALUE_UNITS_ALTITUDE_METRES;
		}		
	}
	
	public static int getUnitVelocity() {
		if ( useImperalSystem() ) {
			return Settings.VALUE_UNITS_SPEED_MILH;			
		} else {
			return Settings.VALUE_UNITS_SPEED_KMH;
		}		
	}
	
	public static int getUnitAngle() {
		if ( useImperalSystem() ) {
			return Settings.VALUE_UNITS_ANGLE_DEGREE;			
		} else {
			return Settings.VALUE_UNITS_ANGLE_DEGREE;
		}		
	}
	
	public static int getLatLonFormat() {
		return PreferenceUtils.getPrefInteger( R.string.pref_units_coo_latlon );
	}
	

	
}
