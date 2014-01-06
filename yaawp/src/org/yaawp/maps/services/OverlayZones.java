package org.yaawp.maps.services;

import java.util.Vector;

import org.yaawp.positioning.Location;
import org.yaawp.utils.Logger;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class OverlayZones extends GenericOverlay {

	private static String TAG = OverlayZones.class.getSimpleName();
	
	private Paint mPaintVisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintVisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintInvisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintInvisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintBoundaryBoxZoneFill = null;
	private Paint mPaintBoundaryBoxZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintZoneCenterFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintZoneCenterBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public OverlayZones() {
		
		mPaintVisibleZoneFill.setStyle(Paint.Style.FILL);
		mPaintVisibleZoneFill.setColor(Color.BLUE);
		mPaintVisibleZoneFill.setAlpha(64);
		
		mPaintVisibleZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintVisibleZoneBorder.setColor(Color.BLUE);
		mPaintVisibleZoneBorder.setAlpha(196);
		mPaintVisibleZoneBorder.setStrokeWidth(3);	
	
		mPaintZoneCenterFill.setStyle(Paint.Style.FILL);
		mPaintZoneCenterFill.setColor(Color.RED);
		mPaintZoneCenterFill.setAlpha(64);			

		mPaintZoneCenterBorder.setStyle(Paint.Style.STROKE);
		mPaintZoneCenterBorder.setColor(Color.BLUE);
		mPaintZoneCenterBorder.setAlpha(196);
		mPaintZoneCenterBorder.setStrokeWidth(2);		
		
		
		// --- draw boundary box
		mPaintBoundaryBoxZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintBoundaryBoxZoneBorder.setColor(Color.CYAN);
		mPaintBoundaryBoxZoneBorder.setAlpha(196);
		mPaintBoundaryBoxZoneBorder.setStrokeWidth(5);			
	}
	
	public void drawZone( Zone z, Canvas canvas, MapProjection projection ) {	
		Point point = new Point();
		Path wallpath = new Path();
		Location centerLocation = new Location();
		centerLocation.setLatitude( z.bbCenter.latitude); 
		centerLocation.setLongitude( z.bbCenter.longitude );		
		
		// --- boundary box
		wallpath.reset();
		projection.toPixels( z.bbBottom, z.bbLeft, point );
		wallpath.moveTo( point.x, point.y );
		projection.toPixels( z.bbTop, z.bbLeft, point );
		wallpath.lineTo( point.x, point.y );	
		projection.toPixels(  z.bbTop, z.bbRight, point );
		wallpath.lineTo( point.x, point.y );
		projection.toPixels( z.bbBottom, z.bbRight, point );
		wallpath.lineTo( point.x, point.y );	
		wallpath.close();
		canvas.drawPath( wallpath, mPaintBoundaryBoxZoneBorder );		
		
		// --- calc & draw nearest point
        int radius = GenericOverlay.distanceInMap( centerLocation, 1 /* meters */, projection ); 		
		projection.toPixels( z.nearestPoint, point );
		canvas.drawCircle( point.x, point.y, radius, mPaintZoneCenterBorder );
		
		// --- calc & draw center point
        int radiusCenter = GenericOverlay.distanceInMap( centerLocation, 1 /* meters */, projection ); 		
		projection.toPixels( z.bbCenter, point );
		canvas.drawCircle( point.x, point.y, radiusCenter, mPaintZoneCenterBorder );		
		
		// --- calc & draw pixel path of the zone

		wallpath.reset(); // only needed when reusing this path for a new build
		projection.toPixels( z.points[0], point );
		wallpath.moveTo( point.x, point.y );		
		for( int i=1; i<z.points.length; i++ ) {
			projection.toPixels( z.points[i], point );
			wallpath.lineTo( point.x, point.y );
		}
		wallpath.close();
		canvas.drawPath( wallpath, mPaintVisibleZoneFill );		
		canvas.drawPath( wallpath, mPaintVisibleZoneBorder );	
		

	}
	
    @Override
    public void drawBitmap(Canvas canvas, MapProjection projection) {
    	Logger.v( TAG, "drawBitmap" );
    	
    	if ( Engine.instance != null && Engine.instance.cartridge != null ) {
			Vector<Zone> zones = Engine.instance.cartridge.zones;
			for (int i = 0; i < zones.size(); i++) {
				Zone z = (Zone)zones.get(i);
				if ( z.isVisible() ) {
					drawZone( z, canvas, projection );
				}
			}
    	}
    }	
	
    
}
