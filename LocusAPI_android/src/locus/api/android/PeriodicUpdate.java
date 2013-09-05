package locus.api.android;

import static locus.api.android.utils.PeriodicUpdatesConst.*;

import locus.api.android.utils.LocusUtils;
import locus.api.objects.extra.Location;
import android.content.Context;
import android.content.Intent;

public class PeriodicUpdate {

	// private temporary variables for checking changes
	private Location mLastMapCenter;
	private Location mLastGps;
	private int mLastZoomLevel;
	
	// checker for new location
	private double mLocMinDistance;
	
	// instance
	private static PeriodicUpdate mPU;
	public static PeriodicUpdate getInstance() {
		if (mPU == null) {
			mPU = new PeriodicUpdate();
		}
		return mPU;
	}
	
	private PeriodicUpdate() {
		this.mLastZoomLevel = -1;
		this.mLocMinDistance = 1.0;
	}
	
	/**
	 * Set notification limit used for check if distance between previous and
	 * new location is higher than this value. So new locations is market as NEW 
	 * @param minDistance distance in metres
	 */
	public void setLocNotificationLimit(double locMinDistance) {
		this.mLocMinDistance = locMinDistance;
	}
	
	public interface OnUpdate {
		
		public void onUpdate(UpdateContainer update);
		
		public void onIncorrectData();
	}
	
	public void onReceive(final Context context, Intent i, OnUpdate handler) {
		// check parameters
		if (context == null || i == null || handler == null) {
			throw new IllegalArgumentException("Incorrect arguments");
		}
		
		// prepare data container
		UpdateContainer update = new UpdateContainer();
		
		// LOCATION, GPS, BASIC VALUES
		
		// check current GPS/network location
		update.enabledMyLocation = i.getBooleanExtra(
				VAR_B_MY_LOCATION_ON, false);
		update.newMyLocation = false;
		update.locMyLocation = LocusUtils.getLocationFromIntent(
				i, VAR_LOC_MY_LOCATION);
		if (update.enabledMyLocation) {
			// check if location is updated
			if (mLastGps == null || mLastGps.distanceTo(
					update.locMyLocation) > mLocMinDistance) {
				mLastGps = update.locMyLocation;
				update.newMyLocation = true;
			} 
		}
		
		// get basic variables
		update.gpsSatsUsed = i.getIntExtra(
				VAR_I_GPS_SATS_USED, 0);
		update.gpsSatsAll = i.getIntExtra(
				VAR_I_GPS_SATS_ALL, 0);
		update.declination = i.getFloatExtra(
				VAR_F_DECLINATION, 0.0f);
		update.orientCourse = i.getFloatExtra(
				VAR_F_ORIENT_COURSE, 0.0f);
		update.orientCourseOpposit = i.getFloatExtra(
				VAR_F_ORIENT_COURSE_OPPOSIT, 0.0f);
		update.orientPitch = i.getFloatExtra(
				VAR_F_ORIENT_PITCH, 0.0f);
		update.orientRoll = i.getFloatExtra(
				VAR_F_ORIENT_ROLL, 0.0f);
		update.orientGpsShift = i.getFloatExtra(
				VAR_F_ORIENT_GPS_SHIFT, 0.0f);
		update.orientGpsAngle = i.getFloatExtra(
				VAR_F_ORIENT_GPS_ANGLE, 0.0f);
		update.speedVertical = i.getFloatExtra(
				VAR_F_SPEED_VERTICAL, 0.0f);
		update.slope = i.getFloatExtra(
				VAR_F_SLOPE, 0.0f);

		// MAP STUFF

		update.mapVisible = i.getBooleanExtra(
				VAR_B_MAP_VISIBLE, false);
		update.newMapCenter = false;
		update.locMapCenter = LocusUtils.getLocationFromIntent(
				i, VAR_LOC_MAP_CENTER);
		if (mLastMapCenter == null || mLastMapCenter.distanceTo(
				update.locMapCenter) > mLocMinDistance) {
			mLastMapCenter = update.locMapCenter;
			update.newMapCenter = true;
		}

		// check MAP
		update.mapTopLeft = LocusUtils.getLocationFromIntent(
				i, VAR_LOC_MAP_BBOX_TOP_LEFT);
		update.mapBottomRight = LocusUtils.getLocationFromIntent(
				i, VAR_LOC_MAP_BBOX_BOTTOM_RIGHT);
		update.mapZoomLevel = i.getIntExtra(
				VAR_I_MAP_ZOOM_LEVEL, 0);
		update.newZoomLevel = update.mapZoomLevel != mLastZoomLevel;
		mLastZoomLevel = update.mapZoomLevel;
		update.mapRotate = i.getFloatExtra(
				VAR_F_MAP_ROTATE, 0.0f);
		update.isUserTouching = i.getBooleanExtra(
				VAR_B_MAP_USER_TOUCHES, false);
		
		// TRACK RECORDING PART
		
		update.trackRecRecording = i.getBooleanExtra(
				VAR_B_REC_RECORDING, false); 
		update.trackRecPaused = i.getBooleanExtra(
				VAR_B_REC_PAUSED, false);
		if (update.trackRecRecording || update.trackRecPaused) {
			update.trackRecDist = i.getDoubleExtra(
					VAR_D_REC_DIST, 0.0);
			update.trackRecDistDownhill = i.getDoubleExtra(
					VAR_D_REC_DIST_DOWNHILL, 0.0);
			update.trackRecDistUphill = i.getDoubleExtra(
					VAR_D_REC_DIST_UPHILL, 0.0);
			update.trackRecAltMin = i.getFloatExtra(
					VAR_F_REC_ALT_MIN, 0.0f);
			update.trackRecAltMax = i.getFloatExtra(
					VAR_F_REC_ALT_MAX, 0.0f);
			update.trackRecAltDownhill = i.getFloatExtra(
					VAR_F_REC_ALT_DOWNHILL, 0.0f);
			update.trackRecAltUphill = i.getFloatExtra(
					VAR_F_REC_ALT_UPHILL, 0.0f);
			update.trackRecAltCumulative = i.getFloatExtra(
					VAR_F_REC_ALT_CUMULATIVE, 0.0f);
			update.trackRecTime = i.getLongExtra(
					VAR_L_REC_TIME, 0L);
			update.trackRecTimeMove = i.getLongExtra(
					VAR_L_REC_TIME_MOVE, 0L);
			update.trackRecSpeedAvg = i.getFloatExtra(
					VAR_F_REC_SPEED_AVG, 0.0f);
			update.trackRecSpeedAvgMove = i.getFloatExtra(
					VAR_F_REC_SPEED_AVG_MOVE, 0.0f);
			update.trackRecSpeedMax = i.getFloatExtra(
					VAR_F_REC_SPEED_MAX, 0.0f);
			update.trackRecPoints = i.getIntExtra(
					VAR_I_REC_POINTS, 0);
		}
		
		// GUIDING PART
		
		update.guidingEnabled = i.getBooleanExtra(
				VAR_B_GUIDING, false);
		if (update.guidingEnabled) {
			update.guideWptName = i.getStringExtra(
					VAR_S_GUIDE_WPT_NAME);
			update.guideWptLoc = LocusUtils.getLocationFromIntent(
					i, VAR_LOC_GUIDE_WPT);
			update.guideWptDist = i.getDoubleExtra(
					VAR_D_GUIDE_WPT_DIST, 0.0);
			update.guideDistFromStart = i.getDoubleExtra(
					VAR_D_GUIDE_DIST_FROM_START, 0.0);
			update.guideDistToFinish = i.getDoubleExtra(
					VAR_D_GUIDE_DIST_TO_FINISH, 0.0);
			update.guideWptAzim = i.getFloatExtra(
					VAR_F_GUIDE_WPT_AZIM, 0.0f);
			update.guideWptAngle = i.getFloatExtra(
					VAR_F_GUIDE_WPT_ANGLE, 0.0f);
			update.guideWptTime = i.getLongExtra(
					VAR_L_GUIDE_WPT_TIME, 0L);
			update.guideTimeToFinish = i.getLongExtra(
					VAR_L_GUIDE_TIME_TO_FINISH, 0L);
		}

		// send update back by handler
		handler.onUpdate(update);
	}
}
