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

package org.yaawp.hmi.gui;

import java.util.ArrayList;
import java.util.Vector;

import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.SatellitePosition;

import android.os.Bundle;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class ListZones extends ListVarious implements LocationEventListener {

	@SuppressWarnings("unused")
	private static final String TAG = "ListZones";
	
	@Override
	protected void callStuff(Object what) {
		ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, (Zone) what);
		ListZones.this.finish();
	}

	@Override
	protected String getStuffName(Object what) {
		return ((Zone) what).name;
	}

	@Override
	protected Vector<Object> getValidStuff() {
		Vector<Object> ret = new Vector<Object>();
		@SuppressWarnings("unchecked")
		Vector<Zone> v = Engine.instance.cartridge.zones;
		for (int i = 0; i < v.size(); i++) {
			if (v.get(i).isVisible())
				ret.add(v.get(i));
		}
		return ret;
	}

	@Override
	protected boolean stillValid() {
		return true;
	}
	
	public void onStart() {
		super.onStart();
		LocationState.addLocationChangeListener(this);
	}
	
	public void onStop() {
		super.onStop();
		LocationState.removeLocationChangeListener(this);
	}


	public void onLocationChanged(Location location) {
		refresh();
	}

	public void onStatusChanged(String provider, int state, Bundle extras) {}

	public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> sats) {
	}
	
	public int getPriority() {
		return LocationEventListener.PRIORITY_MEDIUM;
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public String getName() {
		return TAG;
	}
}
