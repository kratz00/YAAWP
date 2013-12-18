package org.yaawp.maps.services;


import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


public class OverlayGuidance extends GenericOverlay {

	private Location mLocation = null;	
	private Point pxStart = new Point();
	private Point pxEnd = new Point();
	private Paint mPaintBeeline = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public OverlayGuidance() {
		mPaintBeeline.setStyle(Paint.Style.STROKE);
		mPaintBeeline.setColor(Color.MAGENTA);
		mPaintBeeline.setAlpha(255);
		mPaintBeeline.setStrokeWidth(2);	
	}
	
	@Override
	public void onLocationChanged(Location location) {
   		mLocation = location;
	}

    @Override
    public void drawBitmap(Canvas canvas, MapProjection projection) {

    	Location destination = null;
    	
		Guide g = A.getGuidingContent().getGuide();
		if ( g != null ) {
			destination = g.getLocation();    	
		}
		
		mLocation = LocationState.getLocation();
		
        if ( mLocation == null || destination == null ) {
            return;
        }
        
        projection.toPixels( mLocation, pxStart );
        projection.toPixels( destination, pxEnd );

        canvas.drawLine( pxStart.x, pxStart.y, pxEnd.x, pxEnd.y, mPaintBeeline );
    }
    

}
