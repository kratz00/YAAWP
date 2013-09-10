package org.yaawp.hmi;

import java.io.ByteArrayInputStream;

import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;

public class AudioHelper {
	
	private static final String TAG = "AudioHelper";
	
	public void playSound(byte[] data, String mime) {
		Logger.i(TAG, "playSound(" + (data != null ? data.length : 0) + ", "+ mime + ")");
		
		if (data == null || data.length == 0 || mime == null)
			return;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if ("audio/x-wav".equals(mime)) {
				A.getManagerAudio().playMp3File("audio", ".wav", bis);
			} else if ("audio/mpeg".equals(mime)) {
				A.getManagerAudio().playMp3File("audio", ".mp3", bis);
			} 
		} catch (Exception e) {
			Logger.e(TAG, "playSound() failed", e);
		}
	}
}