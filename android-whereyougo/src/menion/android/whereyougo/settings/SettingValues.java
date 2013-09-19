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
		
		
	
		
		GPS_MIN_TIME = Utils.parseInt(getPrefString(c, KEY_S_GPS_MIN_TIME_NOTIFICATION, 
				DEFAULT_GPS_MIN_TIME_NOTIFICATION));


	}
}
