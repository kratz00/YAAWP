/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

public class AudioPlayService extends Service {

//	private static final String TAG = "AudioPlayService";
	
	public static final String EXTRA_FILEPATHS = "EXTRA_FILEPATHS";
	public static final String EXTRA_DELETE_FILE = "EXTRA_DELETE_FILE";
	public static final String EXTRA_PLAY_AS_NOTIFICATION = "EXTRA_PLAY_AS_NOTIFICATION";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

    @Override
    public void onCreate() {
        super.onCreate();
    }

	private MediaPlayer mediaPlayer;
	
    private ArrayList<String> filePaths;
    private String actualFile;
    
    private boolean playAsNotification;
    private boolean deleteFile;
    
    private int originalVolumeMedia = Integer.MIN_VALUE;
    
    public void onStart(Intent intent, int value) {
    	String filePath = intent.getStringExtra(EXTRA_FILEPATHS);
    	playAsNotification = intent.getBooleanExtra(EXTRA_PLAY_AS_NOTIFICATION, true);
    	deleteFile = intent.getBooleanExtra(EXTRA_DELETE_FILE, false);
//Logger.w(TAG, "serviceStart:" + filePath + ", " + playAsNotification + ", " + deleteFile);
    	StringTokenizer token = new StringTokenizer(filePath, ";");
    	filePaths = new ArrayList<String>();
    	while (token.hasMoreTokens()) {
    		String file = token.nextToken().trim();
    		if (file.length() > 0 && (new File(file).exists()))
    			filePaths.add(file);
    	}
    	
    		
//for (String file : filePaths) {
//	Logger.i(TAG, "play:" + file);
//}

    	if (filePath == null || filePaths.size() == 0)
    		return;
    	
    	if (mediaPlayer == null && playAsNotification) {
    		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    		originalVolumeMedia = am.getStreamVolume(AudioManager.STREAM_MUSIC);    
    		am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeMedia / 4, AudioManager.FLAG_VIBRATE);
    	}
    	
    	playNextMedia();
    }
    
    private void playNextMedia() {
    	try {
    		if (filePaths.size() == 0) {
    			stopSelf();
    			return;
    		}
    		
    		if (mediaPlayer == null) {
    			initNewMediaPlayer();
    		}
    		try {
    			mediaPlayer.reset();
    		} catch (Exception e) {
    			initNewMediaPlayer();	
    		}

    		actualFile = filePaths.remove(0);
    		mediaPlayer.setDataSource(actualFile);
    		mediaPlayer.prepareAsync();
    		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					try {
						mediaPlayer.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void onDestroy() {
    	super.onDestroy();
    	
    	mediaPlayer.release();
    	mediaPlayer = null;
    	
    	if (originalVolumeMedia != Integer.MIN_VALUE && playAsNotification) {
    		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    		am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeMedia, AudioManager.FLAG_VIBRATE);
    	}
    }
    
    private void initNewMediaPlayer() {
    	if (mediaPlayer != null) {
    		try {
    			mediaPlayer.stop();
    		} catch (Exception e) {}
    		mediaPlayer = null;
    	}
    	
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				mediaPlayer.release();
				if (deleteFile)
					try {
						(new File(actualFile)).delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				playNextMedia();
			}
		});
    }
}
