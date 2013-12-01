package org.yaawp.maps.services;

import org.yaawp.maps.services.MapProjection;
import android.graphics.Canvas;
import org.yaawp.extra.Location;

public abstract class GenericOverlay {

	protected Location mLocation;
	
    public abstract void drawBitmap(Canvas canvas, MapProjection projection);   
    
    public void onLocationChanged( Location location ) {
    	mLocation = location;
    }
           
}
