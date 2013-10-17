package org.yaawp.maps.mapsforge;

import org.yaawp.maps.MapPolygon;

import org.mapsforge.android.maps.overlay.ArrayCircleOverlay;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.OverlayCircle;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;

import org.yaawp.maps.MapPoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class MfMapPolygon extends MapPolygon implements MfMapOverlay {

	public MfMapPolygon( MapPolygon polygon ) {
		super( polygon );
	}
	
	public OverlayWay getOverlayWay() {
		/* // create the default paint objects for overlay ways
		Paint wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
		wayDefaultPaintFill.setColor(Color.BLUE);
		wayDefaultPaintFill.setAlpha(160);
		wayDefaultPaintFill.setStrokeWidth(7);
		wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
		wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));

		Paint wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
		wayDefaultPaintOutline.setColor(Color.BLUE);
		wayDefaultPaintOutline.setAlpha(128);
		wayDefaultPaintOutline.setStrokeWidth(7);
		wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND); */
		
		// create the default paint objects for overlay circles
		Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintFill.setStyle(Paint.Style.FILL);
		paintFill.setColor(Color.BLUE);
		paintFill.setAlpha(64);

		Paint paintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintOutline.setStyle(Paint.Style.STROKE);
		paintOutline.setColor(Color.BLUE);
		paintOutline.setAlpha(128);
		paintOutline.setStrokeWidth(3);		
	
		// create the WayOverlay and add the ways
		// ArrayWayOverlay wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill,
		//		wayDefaultPaintOutline);
		
		GeoPoint[][] x = new GeoPoint[1][mPoints.size()+1];
		for ( int i=0; i<mPoints.size()+1; i++ ) {
			MapPoint point = mPoints.get(i%mPoints.size());
			x[0][i] = new GeoPoint( point.mLatitude, point.mLongitude );
		}
		
		
		return new OverlayWay( x, paintFill, paintOutline );
	}

}
