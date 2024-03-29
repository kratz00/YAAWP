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

package org.yaawp.positioning;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.yaawp.R;
import org.yaawp.audio.UtilsAudio;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;

import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsConnection {

	private static final String TAG = "GpsConnection";
	
	private LocationManager locationManager;
	private List<String> providers;
	
	private MyLocationListener llGPS;
	private MyLocationListener llNetwork;
	private MyGpsListener gpsListener;
	
	private boolean isFixed;
	private Timer mGpsTimer;

	// temp variable for indicationg wheather network provider is enabled
	private boolean networkProviderEnabled;
	
	public GpsConnection(Context context) {
		Logger.w(TAG, "onCreate()");

		// create listeners
        llGPS = new MyLocationListener();
        llNetwork = new MyLocationListener();
        gpsListener = new MyGpsListener();
        
        // init basic fixing values
        isFixed = false;
        
        // initialize connection
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
   		providers = locationManager.getAllProviders();
   		networkProviderEnabled = false;

   		// remove updates
		try {
			locationManager.removeUpdates(llGPS);
		} catch (Exception e) {
			Logger.w(TAG, "problem removing listeners llGPS, e:" + e);
		}
		try {
			locationManager.removeUpdates(llNetwork);
		} catch (Exception e) {
			Logger.w(TAG, "problem removing listeners llNetwork, e:" + e);
		}
		
		// add new listeners NETWORK
		if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			try {
				// TODO SettingsValues - extend preferences
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						/* SettingValues.GPS_MIN_TIME * 1000 */ 0, 0, llNetwork);
				networkProviderEnabled = true;
			} catch (Exception e) {
				Logger.w(TAG, "problem adding 'network' provider, e:" + e);
				networkProviderEnabled = false;
			}
		}

		// add new listener GPS
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			try {
				// TODO SettingsValues - extend preferences
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						/* SettingValues.GPS_MIN_TIME * 1000 */ 0, 0, llGPS);	
			} catch (Exception e) {
				Logger.w(TAG, "problem adding 'GPS' provider, e:" + e);
			}
		}
		
		// add new listener GPS
		try {
			locationManager.addGpsStatusListener(gpsListener);	
		} catch (Exception e) {
			Logger.w(TAG, "problem adding 'GPS status' listener, e:" + e);
		}

		if (providers.size() == 0 && Settings.getCurrentActivity() != null) {
			UtilsGUI.showDialogInfo(Settings.getCurrentActivity(),
					R.string.no_location_providers_available);
			
			LocationState.setGpsOff(Settings.getCurrentActivity());
			destroy();
		} else {
			ManagerNotify.toastShortMessage(context, context.getString(R.string.gps_enabled));	
		}
	}
	
    public void destroy() {
//Logger.w(TAG, "onDestroy()");
		if (locationManager != null) {
			disableNetwork();
			locationManager.removeUpdates(llGPS);
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager = null;
			// XXX missing context to notify by widget
			ManagerNotify.toastShortMessage(R.string.gps_disabled);
		}
	}
    	    

	private void disableNetwork() {
//Logger.w(TAG, "disableNetwork() - " + networkProviderEnabled);
		if (networkProviderEnabled) {
			locationManager.removeUpdates(llNetwork);
			networkProviderEnabled = false;
		}
	}
	
	private void setNewTimer() {
		if (mGpsTimer != null) {
			mGpsTimer.cancel();
		}
		mGpsTimer = new Timer();
		mGpsTimer.schedule(new TimerTask() {
    		@Override
    		public void run() {
    			if (PreferenceUtils.getPrefBoolean( R.string.pref_gps_beep_on_gps_fix ))
    				UtilsAudio.playBeep(2);
    			mGpsTimer = null;
				isFixed = false;
    		}
    	}, 60000);
	}
	
	private synchronized void handleOnLocationChanged(Location location) {
//Logger.d(TAG, "handleOnLocationChanged(), fix:" + isFixed + ", loc:" + location);
		if (!isFixed) {
			if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
				if (PreferenceUtils.getPrefBoolean( R.string.pref_gps_beep_on_gps_fix ))
					UtilsAudio.playBeep(1);
				disableNetwork();
				isFixed = true;
			}
			LocationState.onLocationChanged(location);
		} else {
			if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
				LocationState.onLocationChanged(location);
				setNewTimer();
			} else {
				// do not send location
			}
		}
	}
	
	/**
	 * Convert a Location object from Android to Locus format
	 * @param oldLoc
	 * @return
	 */
	public static Location convertToL(android.location.Location oldLoc) {
		Location loc = new Location(oldLoc.getProvider());
		loc.setLongitude(oldLoc.getLongitude());
		loc.setLatitude(oldLoc.getLatitude());
		loc.setTime(oldLoc.getTime());
		if (oldLoc.hasAccuracy()) {
			loc.setAccuracy(oldLoc.getAccuracy());
		}
		if (oldLoc.hasAltitude()) {
			loc.setAltitude(oldLoc.getAltitude());
		}
		if (oldLoc.hasBearing()) {
			loc.setBearing(oldLoc.getBearing());
		}
		if (oldLoc.hasSpeed()) {
			loc.setSpeed(oldLoc.getSpeed());
		}
		return loc;
	}	
	
	private class MyLocationListener implements LocationListener {
		
		public MyLocationListener() {}
		
		public void onLocationChanged(android.location.Location location) {
			handleOnLocationChanged(convertToL(location));
		}

		public void onProviderDisabled(String provider) {
//Logger.i(TAG, "onProviderDisabled(" + provider + ")");
			LocationState.onProviderDisabled(provider);
			if (locationManager != null && 
					!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
					!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				LocationState.setGpsOff(Settings.getCurrentActivity());
				destroy();
			}
		}

		public void onProviderEnabled(String provider) {
//Logger.i(TAG, "onProviderEnabled(" + provider + ")");
			LocationState.onProviderEnabled(provider);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
//Logger.i(TAG, "onStatusChanged(" + provider + ", status: " + status + ", geoDataExtra: " + extras + ")");
			LocationState.onStatusChanged(provider, status, extras);
		}
	}
	
	private GpsStatus gpsStatus;
	
	private class MyGpsListener implements GpsStatus.Listener {
		public void onGpsStatusChanged(int event) {
//Logger.i(TAG, "onGpsStatusChanged(" + event + ")");
			try {
				if (locationManager == null)
					return;
				
				if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
//Logger.w(TAG, "onGpsStatusChanged(" + event + "), first fix");
				} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
//Logger.w(TAG, "onGpsStatusChanged(" + event + "), satellite status");
					if (gpsStatus == null)
						gpsStatus = locationManager.getGpsStatus(null);
					else
						gpsStatus = locationManager.getGpsStatus(gpsStatus);
					
					LocationState.onGpsStatusChanged(event, gpsStatus);
				} else if (event == GpsStatus.GPS_EVENT_STARTED) {
//Logger.w(TAG, "onGpsStatusChanged(" + event + "), started");
					LocationState.onGpsStatusChanged(event, null);
				} else if (event == GpsStatus.GPS_EVENT_STOPPED) {
//Logger.w(TAG, "onGpsStatusChanged(" + event + "), stopped");
					LocationState.onGpsStatusChanged(event, null);
					// XXX this happen for unknown reason!
				}
			} catch (Exception e) {
				Logger.e(TAG, "onGpsStatusChanged(" + event + ")", e);
			}
		}
	}
}
