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

package org.yaawp.guidance;

import java.util.ArrayList;

import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.SatellitePosition;
import org.yaawp.preferences.PreferenceItems;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;

import android.os.Bundle;

// TODO check if zone entered and stop guidance

/**
 * @author menion
 * @since 25.1.2010 2010
 */
public class GuidingContent implements LocationEventListener {

	private static String TAG = "NavigationContent";
	
	/** actual navigator */
    private Guide mGuide;
	
	/** name of target */
	private String mTargetName;
	/** azimuth to actual target */
	private float mAzimuthToTarget;
	/** distance to target */
	private float mDistanceToTarget;
	
	
	/** actual array of listeners */
	private ArrayList<GuidingListener> listeners;
	
    public GuidingContent() {
        listeners = new ArrayList<GuidingListener>();
    }
    
    public void addGuidingListener(GuidingListener listener) {
    	this.listeners.add(listener);
    	// actualize data and send event to new listener
    	onLocationChanged(LocationState.getLocation());
    }
    
    public void removeGuidingListener(GuidingListener listener) {
    	this.listeners.remove(listener);
    }
    
    public void guideStart(Guide guide) {
    	this.mGuide = guide;
    	
        // set location listener
        LocationState.addLocationChangeListener(this);
        // call one onLocationChange, to update actual values imediately
        onLocationChanged(LocationState.getLocation());
//Logger.d(TAG, "X");
    	Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					while (mGuide != null) {
						if ( PreferenceItems.getGuidingSound() ) {
							mGuide.manageDistanceSoundsBeeping(mDistanceToTarget);
						}
						Thread.sleep(100); // kann man das nicht reduzieren??? 200,300,500,700???
					}
				} catch (Exception e) {
					Logger.e(TAG, "guideStart(" + mGuide + ")", e);
				}
			}
		});
    	thread.start();
    	
    	for (GuidingListener list : listeners) {
			list.guideStart();
		}
    }
    
    public void guideStop() {
    	this.mGuide = null;

    	LocationState.removeLocationChangeListener(this);
 	    	
    	for (GuidingListener list : listeners) {
			list.guideStop();
		}
    }

    public boolean isGuiding() {
    	return getLocation() != null;
    }
    
    public Guide getGuide() {
    	return mGuide;
    }

    public Location getLocation() {
    	if (mGuide == null) {
    		return null;
    	} else {
    		return mGuide.getLocation();
    	}
    }
    
	public void onLocationChanged(Location location) {
//Logger.d(TAG, "onLocationChanged(" + location + ")");
		
		if ( mGuide != null ) {
			boolean bContinue = true;
			
			if (location != null) {
				bContinue = mGuide.actualizeState(location);	
				mTargetName = mGuide.getTargetName();
				mAzimuthToTarget = mGuide.getAzimuthToTaget();
				mDistanceToTarget = mGuide.getDistanceToTarget();
			} else {
				bContinue = true;
				mTargetName = null;
				mAzimuthToTarget = 0.0f;
				mDistanceToTarget = 0.0f;
			}
			
			if ( bContinue == true ) {
				for (GuidingListener list : listeners) {
					list.receiveGuideEvent(mGuide, mTargetName, mAzimuthToTarget, mDistanceToTarget);
				}			
			} else {
				A.getGuidingContent().guideStop();			
			}
		}
	}
	
	public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> sats) {
	}

	public void onStatusChanged(String provider, int state, Bundle extra) {
	}
	
	public int getPriority() {
		return LocationEventListener.PRIORITY_HIGH;
	}
	
	@Override
	public boolean isRequired() {
		return true; // TODO SettingValues.GUIDING_GPS_REQUIRED;
	}
	
	@Override
	public String getName() {
    	if (mGuide == null) {
    		return "";
    	} else {
    		return mGuide.getName();
    	}
	}
	
	protected void trackGuideCallRecalculate() {
		for (GuidingListener list : listeners) {
			list.trackGuideCallRecalculate();
		}
	}
}
