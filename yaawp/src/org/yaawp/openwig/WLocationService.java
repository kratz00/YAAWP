/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.openwig;

import org.yaawp.positioning.LocationState;
import org.yaawp.utils.Logger;

import cz.matejcik.openwig.platform.LocationService;

public class WLocationService implements LocationService {

	private static final String TAG = "WLocationService";
	
	public void connect() {
		Logger.w(TAG, "connect()");
	}

	public void disconnect() {
		Logger.w(TAG, "disconnect()");
	}

	public double getAltitude() {
		return LocationState.getLocation().getAltitude();
	}

	public double getHeading() {
		return LocationState.getLocation().getBearing();
	}

	public double getLatitude() {
		return LocationState.getLocation().getLatitude();
	}

	public double getLongitude() {
		return LocationState.getLocation().getLongitude();
	}

	public double getPrecision() {
		return LocationState.getLocation().getAccuracy();
	}

	public int getState() {
		if (LocationState.isActuallyHardwareGpsOn())
			return LocationService.ONLINE;
		else
			return LocationService.OFFLINE;
	}
}
