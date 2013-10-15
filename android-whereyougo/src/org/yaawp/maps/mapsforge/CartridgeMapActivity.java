package org.yaawp.maps.mapsforge;

import android.graphics.Bitmap;
import android.os.Bundle;
import java.io.File;
import java.lang.Math;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;


import android.graphics.drawable.Drawable;

import org.yaawp.maps.MapOverlays;

public class CartridgeMapActivity extends MapActivity {

	public final static String MAPFILE = "MAPFILE";

	// 
	
	private String mMapFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapView mapview = new MapView(this);
		mapview.setClickable(true);
		mapview.setBuiltInZoomControls(true);
	
	
		if ( MapOverlays.mWaypoints.size() > 0 ) {
			
			ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(null);
			
			GeoPoint center = null;
	        double latmin=0.0;
	        double latmax=0.0;
	        double lonmin=0.0;
	        double lonmax=0.0;
	        
			for (int i = 0; i < MapOverlays.mWaypoints.size(); i++) {
				MfWaypoint wpt = new MfWaypoint( MapOverlays.mWaypoints.get(i) );
				itemizedOverlay.addItem(wpt.getOverlayItem());	
				
				if ( i == 0 ) {
	        		latmin = wpt.getLatitude();
	        		latmax = wpt.getLatitude();
	        		lonmin = wpt.getLongitude();
	        		lonmax = wpt.getLongitude();
	        	} else {
		        	latmin = Math.min(latmin,wpt.getLatitude());
		        	latmax = Math.max(latmax,wpt.getLatitude());
		        	lonmin = Math.min(lonmin,wpt.getLongitude());
		        	lonmax = Math.max(lonmax,wpt.getLongitude());						
				}
				
			}
            
	        double lat = (latmin+90.0  + latmax+90.0 )/2 - 90.0;
	        double lon = (lonmin+180.0 + lonmax+180.0)/2 - 180.0;
	        center = new GeoPoint( lat, lon );
	        
	        // add the ArrayItemizedOverlay to the MapView
	        mapview.getOverlays().add(itemizedOverlay);

	        mapview.setCenter( center );	        
		}
		
		mMapFile = getIntent().getStringExtra(MAPFILE);
		File file = new File (mMapFile);
		mapview.setMapFile(file);
		setContentView(mapview);	
	}
	
	
}




		
