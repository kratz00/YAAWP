package org.yaawp.hmi.helper;

import java.io.ByteArrayInputStream;

import org.yaawp.utils.A;
import org.yaawp.utils.Logger;


public class AudioHelper {
	
	private static final String TAG = "AudioHelper";
	
	public static void playSound(byte[] data, String mime) {
		Logger.i(TAG, "playSound(" + (data != null ? data.length : 0) + ", "+ mime + ")");
		
		if (data == null || data.length == 0 || mime == null) {
			Logger.e( TAG, "playSound(): invalid parameters" );
			return;
		}
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if ("audio/x-wav".equals(mime)) {
				A.getManagerAudio().playMp3File("audio", ".wav", bis);
			} else if ("audio/mpeg".equals(mime)) {
				A.getManagerAudio().playMp3File("audio", ".mp3", bis);
			} else {
				Logger.e( TAG, "playSound(): unsupported mime-type" );
			}
		} catch (Exception e) {
			Logger.e(TAG, "playSound() failed", e);
		}
	}
}