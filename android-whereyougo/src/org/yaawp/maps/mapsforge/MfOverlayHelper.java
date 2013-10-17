package org.yaawp.maps.mapsforge;

import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;

import android.graphics.Color;
import android.graphics.Paint;
import cz.matejcik.openwig.Zone;

public class MfOverlayHelper {

	
	static public OverlayWay Overlay( Zone zone ) {
		final int points = zone.points.length+1;
		
		GeoPoint[][] mPoints = new GeoPoint[1][points];
		
		for( int i=0; i<points; i++ ) {
			int j = i%zone.points.length;
			mPoints[0][i] = new GeoPoint( zone.points[j].latitude, zone.points[j].longitude );
		}
		
		Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		if (zone.isVisible()) {
			paintFill.setStyle(Paint.Style.FILL);
			paintFill.setColor(Color.BLUE);
			paintFill.setAlpha(64);
		} else {
			paintFill.setStyle(Paint.Style.FILL);
			paintFill.setColor(Color.RED);
			paintFill.setAlpha(64);			
		}
		
		


		Paint paintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintOutline.setStyle(Paint.Style.STROKE);
		paintOutline.setColor(Color.BLUE);
		paintOutline.setAlpha(128);
		paintOutline.setStrokeWidth(3);		
	
		
		return new OverlayWay( mPoints, paintFill, paintOutline );		
	}
	

}
