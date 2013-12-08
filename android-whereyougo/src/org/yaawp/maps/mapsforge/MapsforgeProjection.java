package org.yaawp.maps.mapsforge;

import org.mapsforge.core.GeoPoint;
import org.mapsforge.android.maps.Projection;
import org.yaawp.maps.services.MapProjection;
import org.yaawp.positioning.Location;

import android.graphics.Point;

public class MapsforgeProjection implements MapProjection {
	
	private Projection mProjection = null;
	
	public MapsforgeProjection( Projection projection ) {
		mProjection = projection;
	}
	
	public void toPixels( Location location, Point point ) {
        final GeoPoint geopoint = new GeoPoint(location.getLatitude(), location.getLongitude());       
        mProjection.toPixels( geopoint, point );		
	}
	
	public void toPixels( double latitude, double longitude, Point point ) {
        final GeoPoint geopoint = new GeoPoint(latitude, longitude);       
        mProjection.toPixels( geopoint, point );			
	}
}
