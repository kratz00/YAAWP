package locus.api.android.ar;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import locus.api.android.ActionDisplay;
import locus.api.android.objects.PackWaypoints;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UtilsAddonAR {

	private static final String TAG = "UtilsAddonAR";
	
	// minimum required version of add-on
	private static final int REQUIRED_VERSION = 10;
	
	// value that point have no altitude
	public static final float NO_ALTITUDE = Float.MIN_VALUE;
	
	// intent call to view AR
	public static final String INTENT_VIEW = "locus.api.android.addon.ar.ACTION_VIEW";
	// broadcast ident
	public final static String BROADCAST_DATA = "locus.api.android.addon.ar.NEW_DATA";
	// id used for recognizing if add-on is closed or not. Register receiver in your 
	// starting activity on this number
	public static final int REQUEST_ADDON_AR = 30001;
	
	// extra ident for intent - location
	public static final String EXTRA_LOCATION = "EXTRA_LOCATION";
	// ident of actual guiding item
	public static final String EXTRA_GUIDING_ID = "EXTRA_GUIDING_ID";
	// result id of selected point
	public static final String RESULT_WPT_ID = "RESULT_WPT_ID";
//	// extra ident for intent - points
//	public static final String EXTRA_POINTS = "EXTRA_POINTS";
//	// result if ended as free version after some time
//	public static final String RESULT_FREE_END = "RESULT_FREE_END";
	
	// last used location
	private static Location lastLocation;
	
	public static boolean isInstalled(Context context) {
		// check if exist new version of AR add-on. Older version used
		// different and deprecated API, so not usable anymore
		return LocusUtils.isAppAvailable(context, 
				"menion.android.locus.addon.ar", REQUIRED_VERSION);
	}
	
	public static boolean showPoints(Activity context, ArrayList<PackWaypoints> data,
			Location yourLoc, long guidedWptId) {
		if (!isInstalled(context)) {
			Log.i(TAG, "missing required version " + REQUIRED_VERSION);
			return false;
		}
		
		// prepare intent and start it
		Intent intent = new Intent(INTENT_VIEW);
		intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY, 
				Storable.getAsBytes(data));
		intent.putExtra(EXTRA_LOCATION, 
				yourLoc.getAsBytes());
		intent.putExtra(EXTRA_GUIDING_ID, guidedWptId);
		
		// check intent firstly
		if (!ActionDisplay.hasData(intent)) {
			Log.w(TAG, "Intent 'null' or not contain any data");
			return false;
		}

		// store location
		lastLocation = yourLoc;

		// finally start activity
		context.startActivityForResult(intent, REQUEST_ADDON_AR);
		return true;
	}
	
	public static void updateLocation(Context context, Location loc) {
		// do some tests if is really need to send new location
		if ((loc.getTime() - lastLocation.getTime()) < 5000 ||
			(loc.distanceTo(lastLocation) < 5 &&
					Math.abs(loc.getAltitude() - lastLocation.getAltitude()) < 10)) {
			return;
		} else {
			lastLocation = loc;
			Intent intent = new Intent(BROADCAST_DATA);
			intent.putExtra(EXTRA_LOCATION, lastLocation.getAsBytes());
			context.sendBroadcast(intent);
		}
	}
	
	public static void showTracks(Context context, ArrayList<Track> tracks)
			throws NoSuchAlgorithmException {
		throw new NoSuchAlgorithmException("Not yet implemented");
	}
}
