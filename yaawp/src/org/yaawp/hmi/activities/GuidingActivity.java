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

import org.yaawp.R;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.hmi.views.CompassView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonStopGuidance;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.OrientationListener;
import org.yaawp.utils.A;
import org.yaawp.utils.Const;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;
import org.yaawp.utils.UtilsFormat;


public class GuidingActivity extends CustomActivity implements GuidingListener, OrientationListener {

//	private static final String TAG = "GuidingScreen";
	
	private CompassView viewCompass = null;
	
	public static final int STOP_GUIDANCE_AT_EXIT = 0;
	public static final int CONTINUE_GUIDANCE_AT_EXIT = 1;
	
	public static final String GUIDANCE_EXIT_BEHAVIOUR="GUIDANCE_EXIT_BEHAVIOUR";
	
	private int mExitBehaviour = STOP_GUIDANCE_AT_EXIT;
	
	private ThreeButtonPanelBar mButtonPanelBar;
	private PanelBarButtonStopGuidance mButtonStopGuidance;
	private PanelBarButtonShowMap mButtonShowMap;
	
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
        
    	mButtonPanelBar = new ThreeButtonPanelBar(this);
    	mButtonStopGuidance = new PanelBarButtonStopGuidance();
    	mButtonShowMap = new PanelBarButtonShowMap(this);
    	
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
   	
    	mButtonPanelBar.AddButton( mButtonStopGuidance );
    	mButtonStopGuidance.setEnabled(A.getGuidingContent().isGuiding());
    	mButtonPanelBar.updateUI();
		
    	mButtonPanelBar.AddButton( mButtonShowMap );
    	mButtonPanelBar.updateUI();
		
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
    	this.finish();
	}

	@Override
	public void trackGuideCallRecalculate() {
		// ignore
	}
	
	public static boolean callGudingScreen( Context context, int exitBehaviour ) {
		Intent intent = new Intent( context, GuidingActivity.class);
		intent.putExtra( GUIDANCE_EXIT_BEHAVIOUR, exitBehaviour );
		context.startActivity(intent);
		return true;
	}	
	
    private long lastPressedTime;
    
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	Log.i("GuidingActivity", "onKeyDown( KeyCode="+keyCode );
    	
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	    	if ( this.mExitBehaviour == GuidingActivity.STOP_GUIDANCE_AT_EXIT ) {
                if (event.getDownTime() - lastPressedTime < Const.DOUBLE_PRESS_HK_BACK_PERIOD) {
                	A.getGuidingContent().guideStop();	
                    finish();
                } else {
                	ManagerNotify.toastShortMessage(R.string.double_hk_back_exit_guidance);
                    lastPressedTime = event.getEventTime();
                }
	    	} else {
	    		finish();
	    	}
	    	return true; 
    	} else {
    		super.onKeyDown(keyCode, event);
    	}
	        
        return false;
    } 	
}
