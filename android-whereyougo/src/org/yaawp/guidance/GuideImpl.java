package org.yaawp.guidance;

import java.util.Timer;
import java.util.TimerTask;

import org.yaawp.R;
import org.yaawp.extra.Location;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.preferences.PreferenceItems;
import org.yaawp.preferences.PreferenceValues;

import android.net.Uri;

import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.audio.AudioClip;

public class GuideImpl implements Guide {

	private static final String TAG = "GuideImpl";
		
	private Location mLocation;
	private String mName;

    private float mAzimuth;
    private float mDistance;
    
	/** last sound sonar call */
	private long mLastSonarCall;
	/** audio sound for beeping */
	private AudioClip mAudioBeep; // TODO static?

	private boolean mAlreadyBeeped;
	
    public GuideImpl(String name, Location location) {
		mName = name;
		mLocation = location;
    	mAlreadyBeeped = false;
    	mLastSonarCall = 0;
        mAudioBeep = new AudioClip(A.getApp(), R.raw.sound_beep_01);
    }	
    
	@Override
	public Location getLocation() {
		return mLocation;
	}

	@Override
    public String getTargetName() {
        return mName;
    }

	@Override
    public float getAzimuthToTaget() {
        return mAzimuth;
    }
    
	@Override
    public float getDistanceToTarget() {
        return mDistance;
    }
    
	@Override
	public long getTimeToTarget() {
       	if (LocationState.getLocation().getSpeed() > 1.0) {
       		return (long) ((getDistanceToTarget() / LocationState.getLocation().getSpeed()) * 1000);
       	} else {
       		return 0;
       	}
	}

	public boolean actualizeState(Location actualLocation) {
		mAzimuth = actualLocation.bearingTo(mLocation);
		mDistance = actualLocation.distanceTo(mLocation);
		return true;
	}

	@Override
	public void manageDistanceSoundsBeeping(double distance) {
		try {
			
			switch ( PreferenceItems.getGuidingSoundType() ) {
			case PreferenceValues.GuidingWaypointSound.BEEP_ON_DISTANCE:
				if (distance < PreferenceItems.getGuidingSoundDistance() &&
						!mAlreadyBeeped) {
					playSingleBeep();
					mAlreadyBeeped = true;
				}
				break;
			case PreferenceValues.GuidingWaypointSound.INCREASE_CLOSER:
				long currentTime = System.currentTimeMillis();
				float sonarTimeout = getSonarTimeout(distance);
				if ((currentTime - mLastSonarCall) > sonarTimeout) { // (currentTime - lastSonarCall) > soundSonarDuration && 
					mLastSonarCall = currentTime;
					playSingleBeep();
				}
				break;
			case PreferenceValues.GuidingWaypointSound.CUSTOM_SOUND:
				if (distance < PreferenceItems.getGuidingSoundDistance() &&
						!mAlreadyBeeped) {
					playCustomSound();
					mAlreadyBeeped = true;
				}
				break;
			} 
		} catch (Exception e) {
			Logger.e(TAG, "manageDistanceSounds(" + distance + "), e:" + e.toString());
		}
	}
	
	public void playSingleBeep() {
		mAudioBeep.play();
	}
	
	public void playCustomSound() {
		String uri = Settings.getPrefString(Settings.VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI, "");
		if (uri.length() > 0) {
			final AudioClip audioClip = new AudioClip(A.getApp(), Uri.parse(uri));
			audioClip.play();
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					AudioClip.destroyAudio(audioClip);
				}
			}, 5000);
		}		
	}
	
	private long getSonarTimeout(double distance) {		
		if (distance < PreferenceItems.getGuidingSoundDistance() ) {
			return (long) (distance * 1000 / 33);
		} else {
			return Long.MAX_VALUE;
		}
	}	
}
