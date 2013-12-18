package org.yaawp.maps.services;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class OverlayZones extends GenericOverlay {


	private Paint mPaintVisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintVisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintInvisibleZoneFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintInvisibleZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Paint mPaintBoundaryBoxZoneFill = null;
	private Paint mPaintBoundaryBoxZoneBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
		
	
	public OverlayZones() {
		
		mPaintVisibleZoneFill.setStyle(Paint.Style.FILL);
		mPaintVisibleZoneFill.setColor(Color.BLUE);
		mPaintVisibleZoneFill.setAlpha(64);
		
		mPaintVisibleZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintVisibleZoneBorder.setColor(Color.BLUE);
		mPaintVisibleZoneBorder.setAlpha(196);
		mPaintVisibleZoneBorder.setStrokeWidth(3);	
		
		// --- draw boundary box
		mPaintBoundaryBoxZoneBorder.setStyle(Paint.Style.STROKE);
		mPaintBoundaryBoxZoneBorder.setColor(Color.BLUE);
		mPaintBoundaryBoxZoneBorder.setAlpha(128);
		mPaintBoundaryBoxZoneBorder.setStrokeWidth(3);			
	}
	
	public void drawZone( Zone z, Canvas canvas, MapProjection projection ) {
		int number = z.points.length;
		Point[] mPoints = new Point[number];
		
		for( int i=0; i<number; i++ ) {
			mPoints[i] = new Point();
			projection.toPixels( z.points[i].latitude, z.points[i].longitude, mPoints[i] );
		}
		
		Path wallpath = new Path();
		wallpath.reset(); // only needed when reusing this path for a new build
		wallpath.moveTo( mPoints[0].x, mPoints[0].y); // used for first point
		for( int i=1; i<number; i++ ) {
			wallpath.lineTo(mPoints[i].x, mPoints[i].y);
		}
		wallpath.lineTo(mPoints[0].x, mPoints[0].y);
		

		canvas.drawPath(wallpath, mPaintVisibleZoneFill );		
		canvas.drawPath(wallpath, mPaintVisibleZoneBorder );			
	}
	
    @Override
    public void drawBitmap(Canvas canvas, MapProjection projection) {

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
