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

package org.yaawp.hmi.activities;

import org.yaawp.R;
import org.yaawp.extra.Location;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.hmi.views.CompassView;
import org.yaawp.hmi.views.Satellite2DView;
import org.yaawp.hmi.helper.ThreeButtonBar;
import org.yaawp.maps.mapsforge.CartridgeMapActivity;

import cz.matejcik.openwig.Engine;

import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.hardware.sensors.OrientationListener;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author menion
 * @since 25.1.2010 2010
 */
public class GuidingActivity extends CustomActivity implements GuidingListener, OrientationListener {

//	private static final String TAG = "GuidingScreen";
	
	private CompassView viewCompass = null;
	
	public static final int STOP_GUIDANCE_AT_EXIT = 0;
	public static final int CONTINUE_GUIDANCE_AT_EXIT = 1;
	
	public static final String GUIDANCE_EXIT_BEHAVIOUR="GUIDANCE_EXIT_BEHAVIOUR";
	
	private int mExitBehaviour = STOP_GUIDANCE_AT_EXIT;
	
	private ThreeButtonBar mThreeButtonBar = new ThreeButtonBar();
	
	private TextView viewLat;
	private TextView viewLon;
	private TextView viewAlt;
	private TextView viewAcc;
	private TextView viewSpeed;
	private TextView viewTimeToTarget;
	private TextView viewDestination;
	
	/** azimuth from compass */
	private float mAzimuth;
	private float mPitch;
	private float mRoll;
	// azimuth to target
	private float azimuthToTarget;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guiding_screen);
        
        mExitBehaviour = getIntent().getIntExtra(GUIDANCE_EXIT_BEHAVIOUR,STOP_GUIDANCE_AT_EXIT);
        
        
        mAzimuth = 0.0f;
        mPitch = 0.0f;
        mRoll = 0.0f;
        
        azimuthToTarget = 0.0f;
        
            		
        
        LinearLayout llCompass = (LinearLayout) findViewById(R.id.linearLayoutCompass);
        // llCompass.removeAllViews();        
        
        // return and add view to first linearLayout
        viewCompass = new CompassView(this);
        llCompass.addView(viewCompass,  LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);      
        
        
        
        
		viewAlt = (TextView) findViewById(R.id.textViewAltitude);
        viewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        viewAcc = (TextView) findViewById(R.id.textViewAccuracy);
    	viewLat = (TextView) findViewById(R.id.textViewLatitude);
    	viewLon = (TextView) findViewById(R.id.textViewLongitude);
    	viewTimeToTarget = (TextView) findViewById(R.id.text_view_time_to_target);
    	viewDestination = (TextView)findViewById(R.id.textViewDestination);
   	
		mThreeButtonBar.AddButton(this,
				getString(R.string.stop_navigate), new ThreeButtonBar.OnClickListener() {
					@Override
					public boolean onClick(View v) {	
						A.getGuidingContent().guideStop();
						GuidingActivity.this.finish();
						return true;					
					}
				} );	
		
		mThreeButtonBar.AddButton(this,
				getString(R.string.map), new ThreeButtonBar.OnClickListener() {
					@Override
					public boolean onClick(View v) {			
						try {
					        Intent intent = new Intent( GuidingActivity.this, CartridgeMapActivity.class );
					        intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" );
					        // TODO intent.putExtra( MAP_CENTER_LATITUDE, x.y );
					        // TODO intent.putExtra( MAP_CENTER_LONGITUDE, x.y );
					        // TODO intent.putExtra( CURRENT_POSITION_AS_MAP_CENTER, false );
					        startActivity(intent);  
						} catch (Exception e) {
							// TODO Logger.e(TAG, "btn02.click() - unknown problem", e);
						}
						return true;
					}
				} );	
		
		mThreeButtonBar.EnableButton( this, 1, A.getGuidingContent().isGuiding() );
		
		onOrientationChanged(mAzimuth, mPitch, mRoll);
			
    }
    
    public void onDestory() {
    	if ( mExitBehaviour == STOP_GUIDANCE_AT_EXIT ) {
    		A.getGuidingContent().guideStop();
    		onStop();
    	}
    }
    
    public void onStart() {
    	super.onStart();
    	A.getGuidingContent().addGuidingListener(this);
   		A.getRotator().addListener(this);
    }
    
    public void onStop() {
    	super.onStop();
    	A.getGuidingContent().removeGuidingListener(this);
    	A.getRotator().removeListener(this);
    }
    
	@Override
	public void receiveGuideEvent(Guide guide, String targetName, float azimuthToTarget,
			double distanceToTarget) {
		this.azimuthToTarget = azimuthToTarget;
       	viewCompass.setDistance(distanceToTarget);
       	
       	if (LocationState.getLocation().getSpeed() > 1) {
       		viewTimeToTarget.setText(UtilsFormat.formatTime(true,
       				(long) (distanceToTarget / LocationState.getLocation().getSpeed()) * 1000));
       	} else {
       		viewTimeToTarget.setText(UtilsFormat.formatTime(true, 0));
       	}
       	repaint();
	}
	
	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
//Logger.d(TAG, "onOrientationChanged(" + azimuth + ", " + pitch + ", " + roll + ")");
		Location loc = LocationState.getLocation();
		mAzimuth = azimuth;
		mPitch = pitch;
		mRoll = roll;
		
        viewLat.setText(UtilsFormat.formatLatitude(loc.getLatitude()));
        viewLon.setText(UtilsFormat.formatLongitude(loc.getLongitude()));
        viewAlt.setText(UtilsFormat.formatAltitude(loc.getAltitude(), true));
        viewAcc.setText(UtilsFormat.formatDistance((double) loc.getAccuracy(), false));
        viewSpeed.setText(UtilsFormat.formatSpeed(loc.getSpeed(), false));
        
        String text = A.getGuidingContent().getName();
        viewDestination.setText(text);
        
        
        
        repaint();
	}
	
	private void repaint() {
		viewCompass.moveAngles(azimuthToTarget, mAzimuth, mPitch, mRoll);
	}

	@Override
	public void guideStart() {
	}

	@Override
	public void guideStop() {
	}

	@Override
	public void trackGuideCallRecalculate() {
		// ignore
	}
	
	public static boolean callGudingScreen(Activity activity, int exitBehaviour ) {
		Intent intent = new Intent(activity, GuidingActivity.class);
		intent.putExtra( GUIDANCE_EXIT_BEHAVIOUR, exitBehaviour );
		activity.startActivity(intent);
		return true;
	}	
}
