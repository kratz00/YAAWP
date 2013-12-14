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
import java.util.Vector;

import org.yaawp.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

import org.yaawp.guidance.WaypointGuide;
import org.yaawp.guidance.ZoneGuide;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.hmi.gui.ListActions;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonGuidance;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonAction;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonActionList;
import org.yaawp.openwig.Refreshable;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.SatellitePosition;
import org.yaawp.utils.Logger;
import org.yaawp.utils.UtilsFormat;

// ADD locationListener to update UpdateNavi
public class WigDetailsActivity extends CustomActivity implements Refreshable, LocationEventListener {

	private static final String TAG = "Details";
	
	public static EventTable et;
	
	private ThreeButtonPanelBar mButtonPanelBar = null;

	private static final String[] taskStates = {
		I18N.get(R.string.pending),
		I18N.get(R.string.finished),
		I18N.get(R.string.failed)};
	
	private TextView tvName;
	private ImageView ivImage;
	private TextView tvImageText;
	private TextView tvDescription;
	private TextView tvDistance;
	private TextView tvState;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_details);
		
		mButtonPanelBar = new ThreeButtonPanelBar(this);
	}
	
	public void onResume() {
		super.onResume();
		Logger.d(TAG, "onResume(), et:" + et);
		if (et != null) {
			setTitle(et.name);
			
			tvName = (TextView) findViewById(R.id.layoutDetailsTextViewName);
			tvState = (TextView) findViewById(R.id.layoutDetailsTextViewState);
			tvDescription = (TextView) findViewById(R.id.layoutDetailsTextViewDescription);
			ivImage = (ImageView) findViewById(R.id.layoutDetailsImageViewImage);
			tvImageText = (TextView) findViewById(R.id.layoutDetailsTextViewImageText);
			tvDistance = (TextView) findViewById(R.id.layoutDetailsTextViewDistance);
		} else {
			Logger.i(TAG, "onCreate(), et == null, end!");
			WigDetailsActivity.this.finish();
		}
		
		refresh();
	}
	
	@Override
	public void refresh() {
		runOnUiThread(new Runnable() {
		
			@Override
			public void run() {
				if (!stillValid()) {
					Logger.d(TAG, "refresh(), not valid anymore");
					WigDetailsActivity.this.finish();
					return;
				}
				
				tvName.setText(et.name);
				tvDescription.setText(et.description);
						
				Media m = (Media) et.table.rawget("Media");
				if (m != null) {
					tvImageText.setText(m.altText);
//Logger.w(TAG, "SET: " + et.name + ", " + m.id);
					try {
						byte[] is = Engine.mediaFile(m);
						Bitmap i = BitmapFactory.decodeByteArray(is, 0, is.length);
						CartridgeListActivity.setBitmapToImageView(i, ivImage);
					} catch (Exception e) {
						Logger.e(TAG, "refresh()", e);
					}
				} else {
					ivImage.setImageBitmap(null);
					ivImage.setMinimumWidth(0);
					ivImage.setMinimumHeight(0);
				}
				
				updateNavi();
				setBottomMenu();
			}
		});
	}

	public boolean stillValid() {
		if (et != null) {
			if (et instanceof Thing) {
				return ((Thing) et).visibleToPlayer();
			}
			return et.isVisible();
		} else
			return false;
	}
	
	private void updateNavi() {
		if (!(et instanceof Zone)) {
			return;
		}
		
		Zone z = (Zone) et;
		String ss = "(nothing)";
		switch (z.contain) {
			case Zone.DISTANT: ss = "distant"; break; // TODO I18N
			case Zone.PROXIMITY: ss = "near"; break; // TODO I18N
			case Zone.INSIDE: ss = "inside"; break; // TODO I18N
		}
		tvState.setText("State: " + ss);
		
		if (z.contain == Zone.INSIDE) { 
			tvDistance.setText("Distance: inside");
		} else {
			tvDistance.setText("Distance: " + UtilsFormat.formatDistance(z.distance, false));
		}
	}
	
	private void setBottomMenu() {
		
		// get count of items
		boolean location = et.isLocated();
		
		int actions = 0;
		Vector<Object> validActions = null;
		
		if (et instanceof Thing) {
			Thing t = (Thing) et;
			actions = t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
			Logger.d(TAG, "actions:" + actions);			
			validActions = ListActions.getValidActions(t);
			actions = validActions.size();
			Logger.d(TAG, "validActions:" + actions);
		}
		
		Logger.d(TAG, "setBottomMenu(), loc:" + et.isLocated() + ", et:" + et + ", act:" + actions);

		mButtonPanelBar.RemoveAllButtons();
		
		// set location on first two buttons
		if (location) {
			
			Guide guidanceObject = null;
        
		    if (et instanceof Zone) {
		    	Zone z = ((Zone) et);
		    	guidanceObject = new ZoneGuide(z);
		    } else {
		        Location loc = new Location(TAG);
		        loc.setLatitude(et.position.latitude);
		        loc.setLongitude(et.position.longitude);
		        guidanceObject = new WaypointGuide(et.name, loc);
		    }	
		    
			mButtonPanelBar.AddButton( new PanelBarButtonGuidance( this, guidanceObject, true, false /* GuidingActivity.CONTINUE_GUIDANCE_AT_EXIT */ ) );
				
			// TODO set center object
			mButtonPanelBar.AddButton( new PanelBarButtonShowMap(this) ); 
			
			if ( actions > 0 ) {
				mButtonPanelBar.AddButton( new PanelBarButtonActionList( this, actions, et ) );
			}
		} else { // we have three free buttons for actions
			if ( actions > 3 ) {
				mButtonPanelBar.AddButton( new PanelBarButtonActionList( this, actions, et ) );
			} else {
				for ( int i=0; i<3 && i<actions; i++ ) {
					mButtonPanelBar.AddButton( new PanelBarButtonAction( this, (Action) validActions.get(i), et ) );
				}	
			}
		}

		// set title text
		if (et instanceof Task) {
			Task t = (Task) et;
			tvState.setText(taskStates[t.state()]);
		}
	}

	public void onStart() {
		super.onStart();
		if (et instanceof Zone)
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
