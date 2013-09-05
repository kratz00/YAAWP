package locus.api.android;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import locus.api.android.objects.PackWaypoints;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.utils.Log;
import locus.api.utils.Utils;
import android.content.Context;
import android.content.Intent;

public class ActionDisplayPoints extends ActionDisplay {

	private static final String TAG = "ActionDisplayPoints";
	
	// ONE PACK_WAYPOINT OVER INTENT
	
	/**
	 * Simple way how to send data over intent to Locus. Count that intent in
	 * Android have some size limits so for larger data, use another method
	 * @param context actual {@link Context}
	 * @param data {@link PackWaypoints} object that should be send to Locus
	 * @param callImport whether import with this data should be called after Locus starts.
	 * Otherwise data will be displayed as temporary objects on map.
	 * @param centerOnData allow to center on displayed data. This parameter is ignored
	 * if <code>callImport = true</code>. Suggested if <code>false</code> because 
	 * unexpected centering breaks usability.
	 * @return <code>true</code> if action was success
	 * @throws RequiredVersionMissingException 
	 */
	public static boolean sendPack(Context context, 
			PackWaypoints data, ExtraAction extraAction)
					throws RequiredVersionMissingException {
		return sendPack(LocusConst.ACTION_DISPLAY_DATA,
				context, data, extraAction == ExtraAction.IMPORT,
				extraAction == ExtraAction.CENTER);
	}
	
	/**
	 * Silent methods are useful in case, Locus is already running and you want to
	 * display any data without interrupting a user. This method send data over 
	 * Broadcast intent
	 * @param context actual {@link Context} 
	 * @param data {@link PackWaypoints} object that should be send to Locus
	 * @param centerOnData allow to center on displayed data. This parameter is ignored
	 * if <code>callImport = true</code>. Suggested if <code>false</code> because 
	 * unexpected centering breaks usability.
	 * @return <code>true</code> if action was success
	 * @throws RequiredVersionMissingException
	 */
	public static boolean sendPackSilent(Context context, 
			PackWaypoints data, boolean centerOnData)
					throws RequiredVersionMissingException {
		return sendPack(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, 
				context, data, false, centerOnData);
	}
	
	private static boolean sendPack(String action, Context context, 
			PackWaypoints data, boolean callImport, boolean centerOnData)
					throws RequiredVersionMissingException {
		// check data
		if (data == null) {
			return false;
		}
		
		// create and send intent
		Intent intent = new Intent();
		intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA, 
				data.getAsBytes());
		return sendData(action, context, intent, callImport, centerOnData);
	}
	
	// LIST OF PACK_WAYPOINTS OVER INTENT
	
	/**
	 * Simple way how to send ArrayList<PackWaypoints> object over intent to Locus. Count that
	 * intent in Android have some size limits so for larger data, use another method
	 * @param context actual {@link Context}
	 * @param data {@link ArrayList} of data that should be send to Locus
	 * @return true if success
	 * @throws RequiredVersionMissingException 
	 */
	public static boolean sendPacks(Context context, 
			ArrayList<PackWaypoints> data, ExtraAction extraAction)
					throws RequiredVersionMissingException {
		return sendPacks(LocusConst.ACTION_DISPLAY_DATA, 
				context, data, extraAction == ExtraAction.IMPORT,
				extraAction == ExtraAction.CENTER);
	}
	
	public static boolean sendPacksSilent(Context context, 
			ArrayList<PackWaypoints> data, boolean centerOnData)
					throws RequiredVersionMissingException {
		return sendPacks(LocusConst.ACTION_DISPLAY_DATA_SILENTLY,
				context, data, false, centerOnData);
	}
	
	private static boolean sendPacks(String action, Context context, 
			ArrayList<PackWaypoints> data, boolean callImport, boolean centerOnData)
					throws RequiredVersionMissingException {
		// check data
		if (data == null) {
			return false;
		}
		
		// create and send intent
		Intent intent = new Intent();
		intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY, 
				Storable.getAsBytes(data));
		return sendData(action, context, intent, callImport, centerOnData);
	}
	
	// MORE PACK_WAYPOINTS OVER FILE
	
	/**
	 * Allow to send data to locus, by storing serialized version of data into file. This method
	 * can have advantage over cursor in simplicity of implementation and also that filesize is
	 * not limited as in Cursor method. On second case, need permission for disk access and should
	 * be slower due to IO operations. Be careful about size of data. This method can cause OutOfMemory
	 * error on Locus side if data are too big
	 *   
	 * @param context
	 * @param data
	 * @param filepath
	 * @param callImport
	 * @return
	 * @throws RequiredVersionMissingException 
	 */
	public static boolean sendPacksFile(Context context, 
			ArrayList<PackWaypoints> data, String filepath, ExtraAction extraAction)
					throws RequiredVersionMissingException {
		return sendPacksFile(LocusConst.ACTION_DISPLAY_DATA,
				context, data, filepath, extraAction == ExtraAction.IMPORT,
				extraAction == ExtraAction.CENTER);
	}
	
	public static boolean sendPacksFileSilent(Context context, 
			ArrayList<PackWaypoints> data, String filepath, boolean centerOnData) 
					throws RequiredVersionMissingException {
		return sendPacksFile(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, 
				context, data, filepath, false, centerOnData);
	}
	
	private static boolean sendPacksFile(String action, Context context, 
			ArrayList<PackWaypoints> data, String filepath, boolean callImport, boolean centerOnData)
					throws RequiredVersionMissingException {
		if (sendDataWriteOnCard(data, filepath)) {
			Intent intent = new Intent();
			intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_FILE_PATH, filepath);
			return sendData(action, context, intent, callImport, centerOnData);	
		} else {
			return false;
		}
	}
	
	private static boolean sendDataWriteOnCard(ArrayList<PackWaypoints> data, String filepath) {
		if (data == null || data.size() == 0)
			return false;
		
		DataOutputStream dos = null;
		try {
			File file = new File(filepath);
			file.getParentFile().mkdirs();

			// delete previous file
			if (file.exists())
				file.delete();

			// create stream
			dos = new DataOutputStream(new FileOutputStream(file, false));
	
			// write current version
			Storable.writeList(data, dos);
			dos.flush();
			return true;
		} catch (Exception e) {
			Log.e(TAG, "sendDataWriteOnCard(" + filepath + ", " + data + ")", e);
			return false;
		} finally {
			Utils.closeStream(dos);
		}
	}

	/**
	 * Invert method to {@link #sendDataFile}. This load serialized data from file object
	 * @param filepath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<PackWaypoints> readDataWriteOnCard(String filepath) {
		// check file
		File file = new File(filepath);
		if (!file.exists())
			return new ArrayList<PackWaypoints>();
		
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(file));
			return (ArrayList<PackWaypoints>) Storable.readList(PackWaypoints.class, dis);
		} catch (Exception e) {
			Log.e(TAG, "getDataFile(" + filepath + ")", e);
		} finally {
			Utils.closeStream(dis);
		}
		return new ArrayList<PackWaypoints>();
	}
}
