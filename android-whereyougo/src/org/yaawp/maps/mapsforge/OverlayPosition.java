package org.yaawp.maps.mapsforge;

import java.util.ArrayList;

import org.yaawp.extra.Location;
import menion.android.whereyougo.hardware.location.LocationEventListener;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.hardware.location.SatellitePosition;

import org.mapsforge.android.maps.overlay.Overlay;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.MapView;
import org.yaawp.R;

import menion.android.whereyougo.hardware.sensors.Orientation;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.utils.geometry.Point2D;

import org.mapsforge.core.GeoPoint;

// import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class OverlayPosition extends Overlay {

	private Activity mActivity;
	private Location mLocation;
	private MapView mMapView;
	
	private Paint mPaintCurrentAccuracyFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintCurrentAccuracyBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

	
    public OverlayPosition(Activity activity, MapView mapview ) {
        this.mActivity = activity;
        this.mMapView = mapview;
        // this.mapItemFactory = Settings.getMapProvider().getMapItemFactory();
        // this.ovlImpl = ovlImpl;
        
		mPaintCurrentAccuracyFill.setStyle(Paint.Style.FILL);
		mPaintCurrentAccuracyFill.setColor(Color.BLUE);
		mPaintCurrentAccuracyFill.setAlpha(32);
		
		mPaintCurrentAccuracyBorder.setStyle(Paint.Style.STROKE);
		mPaintCurrentAccuracyBorder.setColor(Color.BLUE);
		mPaintCurrentAccuracyBorder.setAlpha(128);
		mPaintCurrentAccuracyBorder.setStrokeWidth(2);	        
    }

    /* ---- LocationEventListener ------------- */
	public void onLocationChanged(Location location) {
   		mLocation = location;
	}
       
    /*
     * public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> gpsStatus) {

	}

	public void onStatusChanged(String provider, int state, Bundle extra) {
	}
	*/
	
    @Override
    public void drawOverlayBitmap(Canvas canvas, Point drawPosition,
            Projection projection, byte drawZoomLevel) {

        drawInternal(canvas, projection);
    }

   
    private float heading = 0f;
    // private Paint accuracyCircle = null;
    private Point center = new Point();
    private Point left = new Point();
    private Bitmap arrow = null;
    private int widthArrowHalf = 0;
    private int heightArrowHalf = 0;
    private PaintFlagsDrawFilter setfil = null;
    private PaintFlagsDrawFilter remfil = null;


    
    private void drawInternal(Canvas canvas, Projection projection) {

        if ( mLocation == null) {
            return;
        }

        if (setfil == null) {
            setfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
        }
        if (remfil == null) {
            remfil = new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0);
        }

        canvas.setDrawFilter(setfil);

        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        float accuracy = mLocation.getAccuracy();

        // float[] result = new float[1];

        Location next = new Location(mLocation);
        next.setLongitude(mLocation.getLongitude()+1);

        // Location.distanceBetween(latitude, longitude, latitude, longitude + 1, result);
        float longitudeLineDistance = mLocation.distanceTo(next); 

        final GeoPoint leftCoords = new GeoPoint(latitude, longitude - accuracy / longitudeLineDistance);
        final GeoPoint location = new GeoPoint(latitude,longitude);
        
        // GeoPointImpl leftGeo = mapItemFactory.getGeoPointBase(leftCoords);
        projection.toPixels(leftCoords, left);
        projection.toPixels(location, center);
        int radius = center.x - left.x;

        canvas.drawCircle(center.x, center.y, radius, mPaintCurrentAccuracyFill);
        canvas.drawCircle(center.x, center.y, radius, mPaintCurrentAccuracyBorder);

        if (arrow == null) {
            arrow = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.my_location_chevron);
            widthArrowHalf = arrow.getWidth() / 2;
            heightArrowHalf = arrow.getHeight() / 2;
        }

        int marginLeft = center.x - widthArrowHalf;
        int marginTop = center.y - heightArrowHalf;

        Matrix matrix = new Matrix();
        matrix.setRotate(heading, widthArrowHalf, heightArrowHalf);
        matrix.postTranslate(marginLeft, marginTop);

        canvas.drawBitmap(arrow, matrix, null);

        canvas.setDrawFilter(remfil);
    }
    
}
