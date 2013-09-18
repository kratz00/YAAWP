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
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import android.content.Context;

public class SettingValues {

	private static final String TAG = "SettingValues";
	
	// global things
	/** altitude format */
	public static int FORMAT_ALTITUDE;
	/** angle format */
	public static int FORMAT_ANGLE;
	/** latitude/longitude format */
	public static int FORMAT_COO_LATLON;
	/** distance format */
	public static int FORMAT_LENGTH;
	/** speed format */
	public static int FORMAT_SPEED;
	

	// GPS
	/** gps min time */
	public static int GPS_MIN_TIME;


    // GUIDING
    /** enable/disable guiding sounds */
    public static boolean GUIDING_SOUNDS;
    /** waypoint sound type */
    public static int GUIDING_WAYPOINT_SOUND;
    /** waypoint sound distance */
    public static int GUIDING_WAYPOINT_SOUND_DISTANCE;
    

    
	public static void init(Context c) {
		Logger.d(TAG, "init(" + c + ")");
		
		
		FORMAT_ALTITUDE = Utils.parseInt(getPrefString(c, KEY_S_UNITS_ALTITUDE, 
				DEFAULT_UNITS_ALTITUDE));
		FORMAT_ANGLE = Utils.parseInt(getPrefString(c, KEY_S_UNITS_ANGLE, 
				DEFAULT_UNITS_ANGLE));
		FORMAT_COO_LATLON = Utils.parseInt(getPrefString(c, KEY_S_UNITS_COO_LATLON, 
				DEFAULT_UNITS_COO_LATLON));
		FORMAT_LENGTH = Utils.parseInt(getPrefString(c, KEY_S_UNITS_LENGTH, 
				DEFAULT_UNITS_LENGTH));
		FORMAT_SPEED = Utils.parseInt(getPrefString(c, KEY_S_UNITS_SPEED, 
				DEFAULT_UNITS_SPEED));
		
		GPS_MIN_TIME = Utils.parseInt(getPrefString(c, KEY_S_GPS_MIN_TIME_NOTIFICATION, 
				DEFAULT_GPS_MIN_TIME_NOTIFICATION));


	}
}
