package org.yaawp.maps.mapsforge;

import android.graphics.Bitmap;
import android.os.Bundle;
import java.io.File;
import java.lang.Math;

import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Utils;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.YCartridge;

import cz.matejcik.openwig.formats.ICartridge;

import android.graphics.drawable.Drawable;
import org.yaawp.maps.YGeoPoint;

public class CartridgeMapActivity extends MapActivity {

	public final static String CARTRIDGES = "CARTRIDGES";
	public final static String MAPFILE = "MAPFILE";

	// 
	
	private String mMapFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapView mapview = new MapView(this);
		mapview.setClickable(true);
		mapview.setBuiltInZoomControls(true);
	
		mMapFile = getIntent().getStringExtra(MAPFILE);
		File file = new File (mMapFile);
		mapview.setMapFile(file);
		setContentView(mapview);
		
	
		int array[] = this.getIntent().getIntArrayExtra(CARTRIDGES);
		
		if ( array.length > 0 ) {
			// create a default marker for the overlay
	        // R.drawable.marker is just a placeholder for your own drawable
	        Drawable defaultMarker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
	       // Bitmap b = Images.getImageB(R.drawable.ic_title_logo, (int) Utils.getDpPixels(32.0f));	
	        
	        // GeoPoint center = new GeoPoint(10.0,10.0);
	        YGeoPoint center = new YGeoPoint(10.0,10.0);
	        ArrayItemizedOverlay itemizedOverlay = getCartridgeOverlays( array, defaultMarker, center );
	        
	        // add the ArrayItemizedOverlay to the MapView
	        mapview.getOverlays().add(itemizedOverlay);

	        mapview.setCenter( new GeoPoint(center.getLatitude(),center.getLongitude()) );
		}
	}
	
	ArrayItemizedOverlay  getCartridgeOverlays( int array[], Drawable marker, YGeoPoint center ) {
		
        // create an ItemizedOverlay with the default marker
        ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(marker);

        YaawpAppData data = YaawpAppData.GetInstance();
        GeoPoint geoPoint = null;
        double latmin=0.0;
        double latmax=0.0;
        double lonmin=0.0;
        double lonmax=0.0;
        
        if ( array.length > 0) {
        	
        	boolean init = false;
	        for (int i = 0; i < array.length; i++) {
	        	
	            // create a GeoPoint with the latitude and longitude coordinates
	        	
                YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(array[i]);
                
				// remove location less
	        	if (cartridge.isPlayAnywhere()) {
					continue;
	        	}
	        	
	        	if ( !init ) {
	        		latmin = cartridge.getLatitude();
	        		latmax = cartridge.getLatitude();
	        		lonmin = cartridge.getLongitude();
	        		lonmax = cartridge.getLongitude();
	        		init = true;
	        	} else {	
		        	latmin = Math.min(latmin,cartridge.getLatitude());
		        	latmax = Math.max(latmax,cartridge.getLatitude());
		        	lonmin = Math.min(lonmin,cartridge.getLongitude());
		        	lonmax = Math.max(lonmax,cartridge.getLongitude());	        	
	        	}
	        	
	            geoPoint = new GeoPoint(cartridge.getLatitude(), cartridge.getLongitude());
	
	            // create an OverlayItem with title and description
	            OverlayItem item = new OverlayItem(geoPoint, cartridge.getName(),
	                    "Hier k�nnte ihre Werbung stehen!");
	
	            // add the OverlayItem to the ArrayItemizedOverlay
	            itemizedOverlay.addItem(item);        	
	        }
	        
	        double lat = (latmin+90.0  + latmax+90.0 )/2 - 90.0;
	        double lon = (lonmin+180.0 + lonmax+180.0)/2 - 180.0;
	        center.setLocation( lat, lon );
        }
        
        return itemizedOverlay;
	}
	
}




		
