package locus.api.android;

import java.util.ArrayList;

import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.RequiredVersionMissingException;
import locus.api.utils.Log;
import android.content.Context;
import android.content.Intent;

public class ActionDisplay {
	
	private static final String TAG = "ActionDisplay";
	
	public enum ExtraAction {
		NONE, CENTER, IMPORT
	}
	
	/**************************************************/
	/*                 PRIVATE CALLS                  */
	/**************************************************/
	
	protected static boolean sendData(String action, Context context, Intent intent,
			boolean callImport, boolean center) throws RequiredVersionMissingException {
		return sendData(action, context, intent, callImport, center, LocusUtils.LOCUS_API_SINCE_VERSION);
	}
	
	protected static boolean sendData(String action, Context context, Intent intent,
			boolean callImport, boolean center, int version) throws RequiredVersionMissingException {
		// really exist locus?
		if (version < LocusUtils.LOCUS_API_SINCE_VERSION) {
			version = LocusUtils.LOCUS_API_SINCE_VERSION;
		}
		if (!LocusUtils.isLocusAvailable(context, version)) {
			throw new RequiredVersionMissingException(version);
		}
		
		// check intent firstly
		if (!hasData(intent)) {
			Log.e(TAG, "Intent 'null' or not contain any data");
			return false;
		}
		
		// create intent with right calling method
		intent.setAction(action);
		// set centering tag
		intent.putExtra(LocusConst.INTENT_EXTRA_CENTER_ON_DATA, center);
		
		// set import tag
		if (action.equals(LocusConst.ACTION_DISPLAY_DATA_SILENTLY)) {
			// send broadcast
			context.sendBroadcast(intent);
		} else {
			// set import tag
			intent.putExtra(LocusConst.INTENT_EXTRA_CALL_IMPORT, callImport);
			// finally start activity
			context.startActivity(intent);			
		}
		
		return true;
	}
	
	public static boolean hasData(Intent intent) {
		if (intent == null) {
			return false;
		}
		
		return !(
				intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_POINTS_DATA) == null && 
				intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY) == null &&
				intent.getStringExtra(LocusConst.INTENT_EXTRA_POINTS_FILE_PATH) == null &&
				intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_TRACKS_SINGLE) == null &&
				intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_TRACKS_MULTI) == null &&
				intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_CIRCLES_MULTI) == null
				);
	}
	
	protected static boolean removeDataSilently(Context ctx, String extraName,
			ArrayList<Integer> itemsId) throws RequiredVersionMissingException {
		// really exist locus?
		if (!LocusUtils.isLocusAvailable(ctx, 241)) {
			throw new RequiredVersionMissingException(241);
		}
		
		// check intent firstly
		if (itemsId == null || itemsId.size() == 0) {
			Log.e(TAG, "Intent 'null' or not contain any data");
			return false;
		}
		
		// create intent with right calling method
		Intent intent = new Intent(LocusConst.ACTION_REMOVE_DATA_SILENTLY);
		intent.putIntegerArrayListExtra(extraName, itemsId);
		
		// set import tag
		ctx.sendBroadcast(intent);
		return true;
	}
}
