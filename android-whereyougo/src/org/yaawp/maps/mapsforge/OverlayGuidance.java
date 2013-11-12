package org.yaawp.maps.mapsforge;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;
import org.yaawp.extra.Location;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;

import menion.android.whereyougo.utils.A;

public class OverlayGuidance extends Overlay {

	private Location mLocation = null;	
	public Location mDestination = null;
	private Point pxStart = new Point();
	private Point pxEnd = new Point();
	private Paint mPaintBeeline = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public OverlayGuidance() {
		mPaintBeeline.setStyle(Paint.Style.STROKE);
		mPaintBeeline.setColor(Color.MAGENTA);
		mPaintBeeline.setAlpha(255);
		mPaintBeeline.setStrokeWidth(2);	
	}
	
	public void onLocationChanged(Location location) {
   		mLocation = location;
	}
	
    @Override
    public void drawOverlayBitmap(Canvas canvas, Point drawPosition,
            Projection projection, byte drawZoomLevel) {

        if ( mLocation == null || mDestination == null ) {
            return;
        }
       
        
        final GeoPoint start = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
        final GeoPoint end   = new GeoPoint(mDestination.getLatitude() , mDestination.getLongitude());
        
        
        projection.toPixels( start, pxStart );
        projection.toPixels( end, pxEnd );

        canvas.drawLine( pxStart.x, pxStart.y, pxEnd.x, pxEnd.y, mPaintBeeline );
    }
    
	// 
}
