package org.yaawp.maps.mapsforge;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import menion.android.whereyougo.hardware.location.LocationEventListener;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.hardware.location.SatellitePosition;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.guiding.Guide;
import menion.android.whereyougo.guiding.GuidingListener;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;
import android.graphics.drawable.Drawable;

import org.yaawp.extra.Location;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.hardware.location.LocationEventListener;
import menion.android.whereyougo.hardware.sensors.OrientationListener;
import menion.android.whereyougo.gui.Refreshable;


import org.yaawp.maps.mapsforge.OverlayPosition;

public class CartridgeMapActivity extends MapActivity implements LocationEventListener, OrientationListener, Refreshable, GuidingListener  {

	public final static String MAPFILE = "MAPFILE";

	private Paint mPaintVisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintVisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

	private OverlayPosition mPositionOverlay = null;
	private OverlayZones mOverlayZones = null;
	private OverlayGuidance mOverlayGuidance = null;
	
	private String mMapFile;
	public MapView mMapview = null;
	
	private Location mLocation = null;
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Settings.setCurrentActivity(this);
		mMapview.invalidate();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMapview = new MapView(this);
		mMapview.setClickable(true);
		mMapview.setBuiltInZoomControls(true);
	
		mMapFile = getIntent().getStringExtra(MAPFILE);
		File file = new File (mMapFile);
		mMapview.setMapFile(file);
		setContentView(mMapview);	
		
		// --- draw current position
		Drawable marker = getResources().getDrawable(R.drawable.icon_gc_wherigo);

		
		mPaintVisibleZoneFill.setStyle(Paint.Style.FILL);
		mPaintVisibleZoneFill.setColor(Color.BLUE);
		mPaintVisibleZoneFill.setAlpha(64);
		
		mPaintVisibleZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintVisibleZoneBorder.setColor(Color.BLUE);
		mPaintVisibleZoneBorder.setAlpha(128);
		mPaintVisibleZoneBorder.setStrokeWidth(3);				
			
		mOverlayGuidance = new OverlayGuidance();
		mMapview.getOverlays().add(mOverlayGuidance);
		mOverlayGuidance.requestRedraw();					
		
		mPositionOverlay = new OverlayPosition(this,mMapview);
		mMapview.getOverlays().add(mPositionOverlay);
		mPositionOverlay.requestRedraw();
		
		mOverlayZones = new OverlayZones();
		mMapview.getOverlays().add(mOverlayZones);
		mOverlayZones.requestRedraw();
		
		if ( A.getGuidingContent().getGuide() != null && mOverlayGuidance != null ) {
			Guide g = A.getGuidingContent().getGuide();
			mOverlayGuidance.mDestination = g.getActualTarget().getLocation();
		}		
	}	

		
 
		

	
	public void onStart() {
		super.onStart();
		LocationState.addLocationChangeListener(this);
		A.getGuidingContent().addGuidingListener(this);
		A.getRotator().addListener(this);
	}
	
	public void onStop() {
		super.onStop();
		LocationState.removeLocationChangeListener(this);
    	A.getGuidingContent().removeGuidingListener(this);
    	A.getRotator().removeListener(this);		
	}


	
	public void onLocationChanged(Location location) {
		if (   ( mLocation == null )
			|| ( mLocation.getLongitude() != location.getLongitude() ) 
			|| ( mLocation.getLatitude()  != location.getLatitude() )
			|| ( mLocation.getBearing()   != location.getBearing() )
			|| ( mLocation.getAccuracy()  != location.getAccuracy() ) )
		{
			mLocation = location;
			
			// ----
			mPositionOverlay.onLocationChanged( location );
			mPositionOverlay.requestRedraw();
			
			if ( mOverlayGuidance != null ) {
				mOverlayGuidance.onLocationChanged( location );
				mOverlayGuidance.requestRedraw();			
			}
			
    		// center map
    		mMapview.setCenter(new GeoPoint(mLocation.getLatitude(),mLocation.getLongitude()) );		
		}
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
		return "X";
	}	
	
	@Override
	public void refresh() {
		mOverlayZones.requestRedraw();
	}
	
	/* --------------------------------------------------------------- */
	
	@Override
	public void guideStart() {
		Guide g = A.getGuidingContent().getGuide();
		if ( mOverlayGuidance != null && g != null ) {
			mOverlayGuidance.mDestination = g.getActualTarget().getLocation();
			mOverlayGuidance.requestRedraw();
		}		
	}
	
	@Override
	public void guideStop() {
		if ( mOverlayGuidance != null ) {
			mOverlayGuidance.mDestination = null;
			mOverlayGuidance.requestRedraw();
		}	
	}
	
	@Override
	public void receiveGuideEvent(Guide guide, String targetName,
			float azimuthToTarget, double distanceToTarget) {
		
		Guide g = A.getGuidingContent().getGuide();
		if ( mOverlayGuidance != null && g != null ) {
			mOverlayGuidance.mDestination = g.getActualTarget().getLocation();
			mOverlayGuidance.requestRedraw();
		}
	}

	@Override
	public void trackGuideCallRecalculate() {
		// ignore
	}
	
	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		Location loc = LocationState.getLocation();
		
		mPositionOverlay.onOrientationChanged( azimuth, pitch, roll );
		mPositionOverlay.requestRedraw();
	}	
	
}	





		

