package locus.api.android.utils;

/**
 * List of variables used for PeriodicUpdates
 * <br /><br />
 * Do not use these constants directly. All should be handled by 
 * {@link locus.api.android.PeriodicUpdate PeriodicUpdate} and it's {@link locus.api.android.
 * PeriodicUpdate#PeriodicUpdateUpdateContainer PeriodicUpdateUpdateContainer} object
 * @author menion
 *
 */
public class PeriodicUpdatesConst {

	public static final String VAR_NO_ACTION = ("-1");
	
	// LOCATION, GPS, BASIC VALUES
	
	public static final String VAR_B_MY_LOCATION_ON = ("1000");
	
	public static final String VAR_LOC_MY_LOCATION = ("1001");
	public static final String VAR_I_GPS_SATS_USED = ("1005");
	public static final String VAR_I_GPS_SATS_ALL = ("1006");
	public static final String VAR_F_DECLINATION = ("1007");
	public static final String VAR_F_ORIENT_COURSE = ("1008");
	public static final String VAR_F_ORIENT_COURSE_OPPOSIT = ("1009");
	public static final String VAR_F_ORIENT_PITCH = ("1010");
	public static final String VAR_F_ORIENT_ROLL = ("1011");
	public static final String VAR_F_ORIENT_GPS_SHIFT = ("1012");
	public static final String VAR_F_ORIENT_GPS_ANGLE = ("1013");
	public static final String VAR_F_SPEED_VERTICAL = ("1014");
	public static final String VAR_F_SLOPE = ("1015");

	// MAP STUFF
	
	public static final String VAR_B_MAP_VISIBLE = ("1300");
	public static final String VAR_F_MAP_ROTATE = ("1301");
	public static final String VAR_LOC_MAP_CENTER = ("1302");
	public static final String VAR_LOC_MAP_BBOX_TOP_LEFT = ("1303");
	public static final String VAR_LOC_MAP_BBOX_BOTTOM_RIGHT = ("1304");
	public static final String VAR_I_MAP_ZOOM_LEVEL = ("1305");
	public static final String VAR_B_MAP_USER_TOUCHES = ("1306");

	// TRACK RECORDING PART
	
	public static final String VAR_B_REC_RECORDING = ("1200");
	public static final String VAR_B_REC_PAUSED = ("1201");
	
	public static final String VAR_D_REC_DIST = ("1202");
	public static final String VAR_D_REC_DIST_DOWNHILL = ("1203");
	public static final String VAR_D_REC_DIST_UPHILL = ("1204");
	public static final String VAR_F_REC_ALT_MIN = ("1205");
	public static final String VAR_F_REC_ALT_MAX = ("1206");
	public static final String VAR_F_REC_ALT_DOWNHILL = ("1207");
	public static final String VAR_F_REC_ALT_UPHILL = ("1208");
	public static final String VAR_F_REC_ALT_CUMULATIVE = ("1209");
	public static final String VAR_L_REC_TIME = ("1210");
	public static final String VAR_L_REC_TIME_MOVE = ("1211");
	public static final String VAR_F_REC_SPEED_AVG = ("1212");
	public static final String VAR_F_REC_SPEED_AVG_MOVE = ("1213");
	public static final String VAR_F_REC_SPEED_MAX = ("1214");
	public static final String VAR_I_REC_POINTS = ("1215");

	// GUIDING PART (last 1409)
	
	public static final String VAR_B_GUIDING = ("1400");
	
	public static final String VAR_S_GUIDE_WPT_NAME = ("1401");
	public static final String VAR_LOC_GUIDE_WPT = ("1402");
	public static final String VAR_D_GUIDE_WPT_DIST = ("1403");
	public static final String VAR_F_GUIDE_WPT_AZIM = ("1405");
	public static final String VAR_F_GUIDE_WPT_ANGLE = ("1406");
	public static final String VAR_L_GUIDE_WPT_TIME = ("1407");

	public static final String VAR_D_GUIDE_DIST_FROM_START = ("1409");
	public static final String VAR_D_GUIDE_DIST_TO_FINISH = ("1404");
	public static final String VAR_L_GUIDE_TIME_TO_FINISH = ("1408");
}
