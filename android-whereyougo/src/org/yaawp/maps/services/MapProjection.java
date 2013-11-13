package org.yaawp.maps.services;

import org.yaawp.extra.Location;
import android.graphics.Point;

public interface MapProjection {

	public void toPixels( Location location, Point point );
	
	public void toPixels( double latitude, double longitude, Point point );
}
