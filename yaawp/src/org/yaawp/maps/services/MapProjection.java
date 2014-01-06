package org.yaawp.maps.services;

import org.yaawp.positioning.Location;
import cz.matejcik.openwig.ZonePoint;
import android.graphics.Point;

public interface MapProjection {

	public void toPixels( Location location, Point point );
	
	public void toPixels( double latitude, double longitude, Point point );
	
	public void toPixels( ZonePoint zp, Point point );
}
