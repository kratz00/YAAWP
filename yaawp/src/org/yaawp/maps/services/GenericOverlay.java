package org.yaawp.maps.services;

import org.yaawp.maps.services.MapProjection;
import org.yaawp.positioning.Location;

import android.graphics.Canvas;
import android.graphics.Point;

public abstract class GenericOverlay {

	protected Location mLocation;
    private Point center = new Point();
    private Point left = new Point();
    
    public abstract void drawBitmap(Canvas canvas, MapProjection projection);   
    
    public void onLocationChanged( Location location ) {
    	mLocation = location;
    }
    
    public int distanceInMap( float distanceMeter, MapProjection projection ) {
        float longitudeLineDistance = mLocation.longitudeLineDistance();
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        projection.toPixels( latitude, longitude - (distanceMeter / longitudeLineDistance), left);
        projection.toPixels( latitude, longitude, center);
        return center.x - left.x;  
    }
  
}
