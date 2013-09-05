package locus.api.android;

import locus.api.objects.extra.Location;
import locus.api.utils.Utils;

public class UpdateContainer {

	// STATE CUSTOM VARIABLES
	
	// is new GPS location available
	protected boolean newMyLocation = false;
	// is new map center available
	protected boolean newMapCenter = false;
	// is new zoom level on map
	protected boolean newZoomLevel = false;
	// is user touching screen
	protected boolean isUserTouching = false;
	// is GPS/Wi-fi enabled
	protected boolean enabledMyLocation = false;
	
	// LOCATION, GPS, BASIC VALUES

	// current location (or just container for sensors)
	protected Location locMyLocation = null;
	// used number of satellites
	protected int gpsSatsUsed = 0;
	// total number of satellites
	protected int gpsSatsAll = 0;
	// current declination value
	protected float declination = 0.0f;
	// current orientation
	protected float orientCourse = 0.0f;
	// -1 * current orientation
	protected float orientCourseOpposit = 0.0f;
	// pitch orientation
	protected float orientPitch = 0.0f;
	// roll orientation
	protected float orientRoll = 0.0f;
	// current bearing (from last two GPS locations)
	protected float orientGpsShift = 0.0f;
	// GPS shift - orientation
	protected float orientGpsAngle = 0.0f;
	// little bit (last few secs) filtered vertical speed
	protected float speedVertical = 0.0f;
	// slope value
	protected float slope = 0.0f;
	
	// MAP STUFF
	
	// is map currently visible
	protected boolean mapVisible = false;
	// current map rotation
	protected float mapRotate = 0.0f;
	// last map center location
	protected Location locMapCenter = null;
	// location of top-left map corner
	protected Location mapTopLeft = null;
	// location of bottom-right map corner
	protected Location mapBottomRight = null;
	// current map zoom level
	protected int mapZoomLevel = -1;
	
	// TRACK RECORDING PART
	
	// is track record enabled
	protected boolean trackRecRecording = false;
	// if track record is enabled, is running or paused
	protected boolean trackRecPaused = false;
	// already recorded distance in meters
	protected double trackRecDist = 0.0;
	// recorded distance downhill
	protected double trackRecDistDownhill = 0.0;
	// recorded distance uphill
	protected double trackRecDistUphill = 0.0;
	// recorded minimal altitude
	protected float trackRecAltMin = 0.0f;
	// recorded maximal altitude
	protected float trackRecAltMax = 0.0f;
	// recorded altitude downhill
	protected float trackRecAltDownhill = 0.0f;
	// recorded altitude uphill
	protected float trackRecAltUphill = 0.0f;
	// cumulative altitude value
	protected float trackRecAltCumulative = 0.0f;
	// already recorded times in ms
	protected long trackRecTime = 0L;
	// recorded time (only movement)
	protected long trackRecTimeMove = 0L;
	// average recorded speed
	protected float trackRecSpeedAvg = 0.0f;
	// average recorded speed for segments during movement
	protected float trackRecSpeedAvgMove = 0.0f;
	// maximal recorded speed
	protected float trackRecSpeedMax = 0.0f;
	// already recorded points
	protected int trackRecPoints = 0;
	
	// GUIDING PART

	// flag if guiding is enabled
	protected boolean guidingEnabled = false;
	// name of guiding target
	protected String guideWptName = null;
	// current guiding location
	protected Location guideWptLoc = null;
	// distance to target
	protected double guideWptDist = 0.0;
	// azimuth to target
	protected float guideWptAzim = 0.0f;
	// bearing to target
	protected float guideWptAngle = 0.0f;
	// expected time to target
	protected long guideWptTime = 0L;

	// distance from start (in case of guiding along track)
	protected double guideDistFromStart = 0.0;
	// distance to finish (in case of guiding along track)
	protected double guideDistToFinish = 0.0;
	// expected time to finish (in case of guiding along track)
	protected long guideTimeToFinish = 0L;
	
	@Override
	public String toString() {
		return Utils.toString(this);
	}
	
	/**************************************************/
	/*                   GETTERS                      */
	/**************************************************/
	
	// STATE CUSTOM VARIABLES
	
	/**
	 * Check if exists new location received from GPS/Wi-fi
	 * @return <code>true</code> if exists
	 */
	public boolean isNewMyLocation() {
		return newMyLocation;
	}

	/**
	 * Check if user moved map to new location
	 * @return <code>true</code> if new map center exists
	 */
	public boolean isNewMapCenter() {
		return newMapCenter;
	}

	/**
	 * Check if user zoomed map to new zoom level
	 * @return <code>true</code> if new zoom is set
	 */
	public boolean isNewZoomLevel() {
		return newZoomLevel;
	}

	/**
	 * Indicate if user is currently touching a map screen. It do not indicate
	 * which specific action is doing, only that something is happening.
	 * @return <code>true</code> if user is currently handling with map
	 */
	public boolean isUserTouching() {
		return isUserTouching;
	}

	/**
	 * flag if GPS is currently enabled in Locus
	 * @return <code>true</code> if enabled
	 */
	public boolean isEnabledMyLocation() {
		return enabledMyLocation;
	}

	// LOCATION, GPS, BASIC VALUES
	
	/**
	 * Current location is GPS/Wi-fi enabled. Also contain all sensors values. 
	 * Should be only container for sensor data in case, GPS is off!
	 * @return current {@link Location} or just container for sensors data
	 */
	public Location getLocMyLocation() {
		return locMyLocation;
	}

	/**
	 * Return number of used satellites for GPS fix
	 * @return number of used satellites
	 */
	public int getGpsSatsUsed() {
		return gpsSatsUsed;
	}

	/**
	 * Method return total number of visible satellites
	 * @return number of all visible satellites
	 */
	public int getGpsSatsAll() {
		return gpsSatsAll;
	}

	/**
	 * Return current declination computed from a) current GPS location, or b) last known
	 * location Locus knows
	 * @return current declination (in degrees)
	 */
	public float getDeclination() {
		return declination;
	}

	/**
	 * Return current device orientation. Source for value depend on settings in Locus.
	 * Also this value will be 0 in case, user do not need orientation for any action
	 * @return orientation value (in degrees) or 0 if orientation is not used or known
	 */
	public float getOrientCourse() {
		return orientCourse;
	}

	/**
	 * Return opposite value to {@link #getOrientCourse()}
	 * @return orientation (in degree) or 0 if orientation is not used or known
	 */
	public float getOrientCourseOpposit() {
		return orientCourseOpposit;
	}

	/**
	 * Pitch angle of current device. 
	 * For more info see <a href="http://en.wikipedia.org/wiki/Flight_dynamics">wiki</a>
	 * @return pitch angle (in degree) or 0 if not known
	 */
	public float getOrientPitch() {
		return orientPitch;
	}

	/**
	 * Roll angle of current device. 
	 * For more info see <a href="http://en.wikipedia.org/wiki/Flight_dynamics">wiki</a>
	 * @return roll angle (in degree) or 0 if not known
	 */
	public float getOrientRoll() {
		return orientRoll;
	}

	/**
	 * Returns value of current GPS shift. It's computed as bearing between previous and
	 * current GPS location
	 * @return GPS shift (in degrees) or 0 if GPS is disabled
	 */
	public float getOrientGpsShift() {
		return orientGpsShift;
	}

	/**
	 * Returns angle between GPS shift and current orientation. This values differ from 0
	 * based on current outside conditions
	 * @return angle (in degrees) or 0 if no shift exists or GPS or sensors are disabled
	 */
	public float getOrientGpsAngle() {
		return orientGpsAngle;
	}
	
	/**
	 * Returns vertical speed from last few (around 5) seconds of enabled GPS
	 * @return vertical speed in m/s
	 */
	public float getSpeedVertical() {
		return speedVertical;
	}
	
	/**
	 * Get current (little filtered) slope value. More about slope, for example at
	 * <a href="http://en.wikipedia.org/wiki/Slope">Wikipedia</a>
	 * @return slope in 0.01%
	 */
	public float getSlope() {
		return slope;
	}
	
	// MAP STUFF

	/**
	 * Check if some screen with map is currently visible
	 * @return <code>true</code> if map is visible
	 */
	public boolean isMapVisible() {
		return mapVisible;
	}

	/**
	 * Return current rotation value of map screen. This value may be same as current 
	 * orientation or should be value by user manual rotation
	 * @return angle (in degrees) as map rotate
	 */
	public float getMapRotate() {
		return mapRotate;
	}

	/**
	 * Even if map is visible or not, this function return current map center
	 * @return current map center
	 */
	public Location getLocMapCenter() {
		return locMapCenter;
	}

	/**
	 * Top-left coordinate of current map screen
	 * @return max top-left visible location
	 */
	public Location getMapTopLeft() {
		return mapTopLeft;
	}

	/**
	 * Bottom-right coordinate of current map screen
	 * @return max bottom-right visible location
	 */
	public Location getMapBottomRight() {
		return mapBottomRight;
	}

	/**
	 * Return current map zoom level. This value should not be precise in case of using
	 * custom map projections, anyway it's always closest possible value
	 * @return integer as a current zoom level (zoom 8 == whole world (1 tile 256x256px))
	 */
	public int getMapZoomLevel() {
		return mapZoomLevel;
	}

	// TRACK RECORDING PART

	// TODO - create javadocs for below getters
	
	public boolean isTrackRecRecording() {
		return trackRecRecording;
	}

	public boolean isTrackRecPaused() {
		return trackRecPaused;
	}

	public double getTrackRecDist() {
		return trackRecDist;
	}

	public double getTrackRecDistDownhill() {
		return trackRecDistDownhill;
	}

	public double getTrackRecDistUphill() {
		return trackRecDistUphill;
	}

	public float getTrackRecAltMin() {
		return trackRecAltMin;
	}

	public float getTrackRecAltMax() {
		return trackRecAltMax;
	}

	public float getTrackRecAltDownhill() {
		return trackRecAltDownhill;
	}

	public float getTrackRecAltUphill() {
		return trackRecAltUphill;
	}

	public float getTrackRecAltCumulative() {
		return trackRecAltCumulative;
	}

	public long getTrackRecTime() {
		return trackRecTime;
	}

	public long getTrackRecTimeMove() {
		return trackRecTimeMove;
	}

	public float getTrackRecSpeedAvg() {
		return trackRecSpeedAvg;
	}

	public float getTrackRecSpeedAvgMove() {
		return trackRecSpeedAvgMove;
	}

	public float getTrackRecSpeedMax() {
		return trackRecSpeedMax;
	}

	public int getTrackRecPoints() {
		return trackRecPoints;
	}

	public boolean isGuidingEnabled() {
		return guidingEnabled;
	}

	public String getGuideWptName() {
		return guideWptName;
	}

	public Location getGuideWptLoc() {
		return guideWptLoc;
	}

	public double getGuideWptDist() {
		return guideWptDist;
	}

	public float getGuideWptAzim() {
		return guideWptAzim;
	}

	public float getGuideWptAngle() {
		return guideWptAngle;
	}

	public long getGuideWptTime() {
		return guideWptTime;
	}

	public double getGuideDistFromStart() {
		return guideDistFromStart;
	}

	public double getGuideDistToFinish() {
		return guideDistToFinish;
	}

	public long getGuideTimeToFinish() {
		return guideTimeToFinish;
	}
}
