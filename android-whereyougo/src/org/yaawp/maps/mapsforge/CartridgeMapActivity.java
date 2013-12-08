package org.yaawp.maps.mapsforge;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

import menion.android.whereyougo.utils.A;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;


import org.yaawp.maps.services.Overlays;
import org.yaawp.openwig.Refreshable;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.OrientationListener;
import org.yaawp.positioning.SatellitePosition;
import org.yaawp.preferences.Settings;

public class CartridgeMapActivity extends MapActivity implements LocationEventListener, OrientationListener, Refreshable, GuidingListener  {

	public final static String MAPFILE = "MAPFILE";

	private Paint mPaintVisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintVisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

	private MapsforgeOverlay mOverlayPosition = new MapsforgeOverlay( Overlays.OVERLAY_CURRENT_POSITION );
	private MapsforgeOverlay mOverlayZones = new MapsforgeOverlay( Overlays.OVERLAY_ZONES );
	private MapsforgeOverlay mOverlayGuidance = new MapsforgeOverlay( Overlays.OVERLAY_GUIDANCE );	
	
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
		// Drawable marker = getResources().getDrawable(R.drawable.icon_gc_wherigo);

		
		mPaintVisibleZoneFill.setStyle(Paint.Style.FILL);
		mPaintVisibleZoneFill.setColor(Color.BLUE);
		mPaintVisibleZoneFill.setAlpha(64);
		
		mPaintVisibleZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintVisibleZoneBorder.setColor(Color.BLUE);
		mPaintVisibleZoneBorder.setAlpha(128);
		mPaintVisibleZoneBorder.setStrokeWidth(3);				
			
		// mOverlayGuidance = new OverlayGuidance();
		mMapview.getOverlays().add(mOverlayGuidance);
		Guide g = A.getGuidingContent().getGuide();
		mOverlayGuidance.show(g != null);
		mOverlayGuidance.requestRedraw();					
		
		// mOverlayPosition = new OverlayPosition(this,mMapview);
		mMapview.getOverlays().add(mOverlayPosition);
		mOverlayPosition.requestRedraw();
		
		// mOverlayZones = new OverlayZones();
		mMapview.getOverlays().add(mOverlayZones);
		mOverlayZones.requestRedraw();
		
		
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
			mOverlayPosition.onLocationChanged( location );
			mOverlayPosition.requestRedraw();
			
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
		mOverlayGuidance.show(g != null);
		mOverlayGuidance.requestRedraw();		
	}
	
	@Override
	public void guideStop() {
		mOverlayGuidance.hide();
		mOverlayGuidance.requestRedraw();	
	}
	
	@Override
	public void receiveGuideEvent(Guide guide, String targetName,
			float azimuthToTarget, double distanceToTarget) {
		
		Guide g = A.getGuidingContent().getGuide();
		mOverlayGuidance.show(g != null);
		mOverlayGuidance.requestRedraw();
	}

	@Override
	public void trackGuideCallRecalculate() {
		// ignore
	}
	
	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {	
		// TODO mOverlayPosition.onOrientationChanged( azimuth, pitch, roll );
		// TODO mOverlayPosition.requestRedraw();
	}	
	
}	





		

