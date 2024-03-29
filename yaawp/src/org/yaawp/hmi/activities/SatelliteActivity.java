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

package org.yaawp.hmi.activities;

import java.util.ArrayList;

import org.yaawp.R;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.yaawp.hmi.gui.extension.CustomDialog;
import org.yaawp.hmi.views.Point2D;
import org.yaawp.hmi.views.Satellite2DView;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.Orientation;
import org.yaawp.positioning.SatellitePosition;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;
import org.yaawp.utils.Utils;
import org.yaawp.utils.UtilsFormat;

/**
 * @author menion
 * @since 25.1.2010 2010
 */
public class SatelliteActivity extends CustomActivity implements LocationEventListener {

	private static final String TAG = "SatelliteScreen";
	
	private Satellite2DView satelliteView;
	
	private ToggleButton buttonGps;
	
	public static ArrayList<SatellitePosition> satellites = new ArrayList<SatellitePosition>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satellite_screen_activity);
        
        createLayout();
    }
    
    private void createLayout() {
        LinearLayout llSkyplot = (LinearLayout) findViewById(R.id.linear_layout_skyplot);
        llSkyplot.removeAllViews();
        
        // return and add view to first linearLayout
        satelliteView = new Satellite2DView(SatelliteActivity.this);
        llSkyplot.addView(satelliteView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    	// change colors for 3.0+
    	if (Utils.isAndroid30OrMore()) {
    		findViewById(R.id.linear_layout_bottom_3).setBackgroundColor(
    				CustomDialog.BOTTOM_COLOR_A3);
    	}
    	
        // and final bottom buttons
        buttonGps = (ToggleButton) findViewById(R.id.btn_gps_on_off);
        buttonGps.setChecked(LocationState.isActuallyHardwareGpsOn());
        buttonGps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
        	@Override
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					LocationState.setGpsOff(SatelliteActivity.this);
					
					// disable satellites on screen
					satellites.clear();
					satelliteView.invalidate();
				} else {
					LocationState.setGpsOn(SatelliteActivity.this);
				}
				
				onGpsStatusChanged(0, null);
				PreferenceFunc.enableWakeLock();
			}
		});
        
        ToggleButton buttonCompass = (ToggleButton) findViewById(R.id.btn_compass_on_off);
        buttonCompass.setChecked(PreferenceUtils.getPrefBoolean( R.string.pref_sensors_compass_hardware ));
        buttonCompass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ManagerNotify.toastLongMessage(R.string.pref_sensors_compass_hardware_desc);
				// TODO Settings.setPrefBoolean(SatelliteScreen.this, Settings.KEY_B_HARDWARE_COMPASS_SENSOR, isChecked);
				// TODO PreferenceUtils.getPrefBoolean( R.string.pref_sensors_compass_hardware ) = Utils.parseBoolean(isChecked);
				A.getRotator().manageSensors();
			}
		});
    }
    
    public void notifyGpsDisable() {
   		buttonGps.setChecked(false);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    	onLocationChanged(LocationState.getLocation());
    	onGpsStatusChanged(0, null);
    }

    public void onStart() {
    	super.onStart();
    	LocationState.addLocationChangeListener(this);
    }
    
    public void onStop() {
    	super.onStop();
    	LocationState.removeLocationChangeListener(this);
    }

	public void onLocationChanged(final Location location) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((TextView) findViewById(R.id.text_view_latitude))
						.setText(UtilsFormat.formatLatitude(location.getLatitude()));
				((TextView) findViewById(R.id.text_view_longitude))
						.setText(UtilsFormat.formatLongitude(location.getLongitude()));
				((TextView) findViewById(R.id.text_view_altitude))
						.setText(UtilsFormat.formatAltitude(location.getAltitude(), true));
				((TextView) findViewById(R.id.text_view_accuracy))
						.setText(UtilsFormat.formatDistance(location.getAccuracy(), false));
				((TextView) findViewById(R.id.text_view_speed))
						.setText(UtilsFormat.formatSpeed(location.getSpeed(), false));
				((TextView) findViewById(R.id.text_view_progress))
						.setText(UtilsFormat.formatAngle(Orientation.getDeclination()));
				long lastFix = LocationState.getLastFixTime();
				if (lastFix > 0) {
					((TextView) findViewById(R.id.text_view_time_gps)).setText(UtilsFormat.formatDate(lastFix));
				} else {
					((TextView) findViewById(R.id.text_view_time_gps)).setText("~");
				}
			}
		});

				
	}

	private Point2D.Int setSatellites(ArrayList<SatellitePosition> sats) {
		synchronized (satellites) {
			Point2D.Int satCount = new Point2D.Int();
			satellites.clear();
			if (sats != null && satellites != null) {
				for (int i = 0; i < sats.size(); i++) {
					SatellitePosition sat = sats.get(i);
					if (sat.isFixed())
						satCount.x++;
					satCount.y++;
					satellites.add(sat);
				}
			}
			return satCount;
		}
	}
	
	public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> gpsStatus) {
		try {
			Point2D.Int num = setSatellites(gpsStatus);
			satelliteView.invalidate();
			((TextView) findViewById(R.id.text_view_satellites)).
			setText(num.x + " | " + num.y);
		} catch (Exception e) {
			Logger.e(TAG, "onGpsStatusChanged(" + event + ", " + gpsStatus + "), e:" + e.toString());
		}
	}

	public void onStatusChanged(String provider, int state, Bundle extra) {
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
