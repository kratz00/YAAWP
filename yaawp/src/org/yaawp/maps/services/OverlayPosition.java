package org.yaawp.maps.services;

import org.yaawp.preferences.Settings;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import org.yaawp.R;



public class OverlayPosition extends GenericOverlay {

	private Activity mActivity;


	
	private Paint mPaintCurrentAccuracyFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaintCurrentAccuracyBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

	private float mAzimuth;
	private float mPitch;
	private float mRoll;	
	
    public OverlayPosition( ) {
        this.mActivity = Settings.getCurrentActivity();

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
    
	public void onOrientationChanged(float azimuth, float pitch, float roll) {	
		mAzimuth = azimuth;
		mPitch = pitch;
		mRoll = roll;
	}	
	
    /*
    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> gpsStatus) {
	}

	public void onStatusChanged(String provider, int state, Bundle extra) {
	}
	*/
	

  
    // private float heading = 0f;

    private Bitmap arrow = null;
    private int widthArrowHalf = 0;
    private int heightArrowHalf = 0;
    private PaintFlagsDrawFilter setfil = null;
    private PaintFlagsDrawFilter remfil = null;


    
    @Override
    public void drawBitmap(Canvas canvas, MapProjection projection) {

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

        Point position = new Point();
        projection.toPixels( mLocation, position );        

        // - calc & draw accuracy circle
        int radius = GenericOverlay.distanceInMap( mLocation, mLocation.getAccuracy(), projection );
        canvas.drawCircle( position.x, position.y, radius, mPaintCurrentAccuracyFill );
        canvas.drawCircle( position.x, position.y, radius, mPaintCurrentAccuracyBorder );

        if (arrow == null) {
            arrow = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.my_location_chevron);
            widthArrowHalf = arrow.getWidth() / 2;
            heightArrowHalf = arrow.getHeight() / 2;
        }

        int marginLeft = position.x - widthArrowHalf;
        int marginTop = position.y - heightArrowHalf;

        Matrix matrix = new Matrix();
        matrix.setRotate( mAzimuth, widthArrowHalf, heightArrowHalf);
        matrix.postTranslate(marginLeft, marginTop);

        canvas.drawBitmap(arrow, matrix, null);

        canvas.setDrawFilter(remfil);
    }
    
}
