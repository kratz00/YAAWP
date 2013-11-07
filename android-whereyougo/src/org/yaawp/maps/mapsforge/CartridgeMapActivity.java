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

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.ArrayCircleOverlay;
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
import menion.android.whereyougo.gui.Refreshable;


import org.yaawp.maps.mapsforge.OverlayPosition;

public class CartridgeMapActivity extends MapActivity implements LocationEventListener, Refreshable  {

	public final static String MAPFILE = "MAPFILE";

	public ArrayItemizedOverlay mCurrentPosition = null;
	public ArrayWayOverlay mCurrentZones = null;
	public ArrayCircleOverlay mCurrentAccuracy = null;
	

	private Paint mPaintVisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintVisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

	private OverlayPosition mPositionOverlay = null;
	private OverlayZones mOverlayZones = null;
	
	private String mMapFile;
	public MapView mMapview = null;
	
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

		// 
		mCurrentZones = new ArrayWayOverlay(null,null);				
		mMapview.getOverlays().add(mCurrentZones);
		mMapview.invalidate();
		
		// --- draw current position
		Drawable marker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
		mCurrentPosition = new ArrayItemizedOverlay(marker);
		mMapview.getOverlays().add(mCurrentPosition);
		mMapview.invalidate();
		
		mPaintVisibleZoneFill.setStyle(Paint.Style.FILL);
		mPaintVisibleZoneFill.setColor(Color.BLUE);
		mPaintVisibleZoneFill.setAlpha(64);
		
		mPaintVisibleZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintVisibleZoneBorder.setColor(Color.BLUE);
		mPaintVisibleZoneBorder.setAlpha(128);
		mPaintVisibleZoneBorder.setStrokeWidth(3);				
			
	
		
		mPositionOverlay = new OverlayPosition(this,mMapview);
		mMapview.getOverlays().add(mPositionOverlay);
		mPositionOverlay.requestRedraw();
		
		mOverlayZones = new OverlayZones();
		mMapview.getOverlays().add(mOverlayZones);
		mOverlayZones.requestRedraw();
	}	

		
 
		

	
	public void onStart() {
		super.onStart();
		// if (et instanceof Zone)
		LocationState.addLocationChangeListener(this);
	}
	
	public void onStop() {
		super.onStop();
		LocationState.removeLocationChangeListener(this);
	}

	Location mLocation = null;
	
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
	
	
	public void refresh() {
		mOverlayZones.requestRedraw();
	}
	
	
	/*


	private OverlayWay getZoneBoundaryBox( Zone zone, Paint fill, Paint border ) {
		
		GeoPoint[][] mPoints = new GeoPoint[1][5];
		
		mPoints[0][0] = new GeoPoint( zone.bbLeft,  zone.bbBottom );
		mPoints[0][1] = new GeoPoint( zone.bbRight, zone.bbBottom );
		mPoints[0][2] = new GeoPoint( zone.bbRight, zone.bbTop );
		mPoints[0][3] = new GeoPoint( zone.bbLeft,  zone.bbTop );
		mPoints[0][4] = mPoints[0][0];
			
		return new OverlayWay( mPoints, fill, border );		
	}	
	

	*/
	
}	





		

