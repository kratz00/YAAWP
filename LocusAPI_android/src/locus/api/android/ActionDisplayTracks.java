package locus.api.android;

import java.util.ArrayList;

import locus.api.android.ActionDisplay.ExtraAction;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Track;
import android.content.Context;
import android.content.Intent;

public class ActionDisplayTracks extends ActionDisplay {

	// SEND ONE SINGLE TRACK
	
	public static boolean sendTrack(Context context,
			Track track, ExtraAction extraAction)
					throws RequiredVersionMissingException {
		return sendTrack(LocusConst.ACTION_DISPLAY_DATA,
				context, track, extraAction == ExtraAction.IMPORT,
						extraAction == ExtraAction.CENTER, false);
	}
	
	public static boolean sendTrack(Context context, 
			Track track, ExtraAction extraAction, boolean startNavigation)
					throws RequiredVersionMissingException {
		return sendTrack(LocusConst.ACTION_DISPLAY_DATA, 
				context, track, extraAction == ExtraAction.IMPORT,
						extraAction == ExtraAction.CENTER, startNavigation);
	}
	
	public static boolean sendTrackSilent(Context context, 
			Track track, boolean centerOnData)
					throws RequiredVersionMissingException {
		return sendTrack(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, 
				context, track, false, centerOnData, false);
	}
	
	private static boolean sendTrack(String action, Context context, Track track,
			boolean callImport, boolean centerOnData, boolean startNavigation)
			throws RequiredVersionMissingException {
		// check track
		if (track == null || track.getPoints().size() > 0) {
			return false;
		}
		
		// create and start intent
		Intent intent = new Intent();
		intent.putExtra(LocusConst.INTENT_EXTRA_TRACKS_SINGLE, track.getAsBytes());
		intent.putExtra(LocusConst.INTENT_EXTRA_START_NAVIGATION, startNavigation);
		return sendData(action, context, intent, callImport, centerOnData);
	}
	
	// SEND TRACK PACK (MORE THEN ONE)
	
	public static boolean sendTracks(Context context,
			ArrayList<Track> tracks, ExtraAction extraAction)
					throws RequiredVersionMissingException {
		return sendTracks(LocusConst.ACTION_DISPLAY_DATA, 
				context, tracks, extraAction == ExtraAction.IMPORT,
				extraAction == ExtraAction.CENTER);
	}
	
	public static boolean sendTracksSilent(Context context,
			ArrayList<Track> tracks, boolean centerOnData)
					throws RequiredVersionMissingException {
		return sendTracks(LocusConst.ACTION_DISPLAY_DATA_SILENTLY,
				context, tracks, false, centerOnData);
	}
	
	private static boolean sendTracks(String action, Context context,
			ArrayList<Track> tracks, boolean callImport, boolean centerOnData)
			throws RequiredVersionMissingException {
		// check data
		if (tracks == null || tracks.size() == 0) {
			return false;
		}
		
		// create and start intent
		Intent intent = new Intent();
		intent.putExtra(LocusConst.INTENT_EXTRA_TRACKS_MULTI, 
				Storable.getAsBytes(tracks));
		return sendData(action, context, intent, callImport, centerOnData);
	}
}
