package org.yaawp.maps.services;

import org.yaawp.maps.services.MapProjection;
import org.yaawp.positioning.Location;

import android.graphics.Canvas;
import android.graphics.Point;

public abstract class GenericOverlay {

	protected Location mLocation;
    
    public abstract void drawBitmap(Canvas canvas, MapProjection projection);   
    
    public void onLocationChanged( Location location ) {
    	mLocation = location;
    }
    
    protected static int distanceInMap( Location location, float distanceMeter, MapProjection projection ) {
    	Point center = new Point();
    	Point left = new Point();
    	float longitudeLineDistance = location.longitudeLineDistance();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        projection.toPixels( latitude, longitude - (distanceMeter / longitudeLineDistance), left);
        projection.toPixels( latitude, longitude, center);
        return center.x - left.x;  
    }
  
}
