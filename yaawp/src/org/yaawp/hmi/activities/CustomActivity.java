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

package org.yaawp.hmi.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.yaawp.MainApplication;
import org.yaawp.R;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.Const;
import org.yaawp.utils.Logger;

import java.lang.System;

public class CustomActivity extends FragmentActivity {

	private static String TAG = CustomActivity.class.getSimpleName();
	
	private long lastPressedTime;
	private long lastKeyCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.v(getLocalClassName(), "onCreate(), id:" + hashCode());
		try {
			super.onCreate(savedInstanceState);

			Const.SCREEN_WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
			Const.SCREEN_HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onCreate()", e);
		}
	}
	
	@Override
	public void onStart() {
		Logger.v(getLocalClassName(), "onStart(), id:" + hashCode());
		try {
			super.onStart();
	    	try {
				boolean fullScreen = PreferenceUtils.getPrefBoolean( R.string.pref_fullscreen );
				
	    		if (fullScreen) {
					this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
				} else {
					this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				}

	    	} catch (Exception e) {
	    		Logger.e( "CustomActivity", "setScreenFullScreen(" + this + ")", e);
	    	}
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onStart()", e);
		}
	}
	
	@Override
	protected void onResume() {
		Logger.v(getLocalClassName(), "onResume(), id:" + hashCode());
		try {
			super.onResume();
			Settings.setCurrentActivity(this);
	   		// enable permanent screen on
			PreferenceFunc.enableWakeLock();
			// set values again, this fix problem when activity is started after
			// activity in e.g. fixed portrait mode
			Const.SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
        	Const.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onResume()", e);
		}
	}
	
	@Override
	protected void onPause() {
Logger.v(getLocalClassName(), "onPause(), id:" + hashCode());
		try {
			super.onPause();
			if (Settings.getCurrentActivity() == this) {
				Settings.setCurrentActivity(null);
			}
			// disable location
			MainApplication.onActivityPause();
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onPause()", e);
		}
	}
	
	@Override
	public void onDestroy() {
Logger.v(getLocalClassName(), "onDestroy(), id:" + hashCode());
		try {
			super.onDestroy();
			
			if (getParentViewId() != -1) {
				unbindDrawables(findViewById(getParentViewId()));
				System.gc();
			}
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onDestroy()", e);
		}
	}
	
    private void unbindDrawables(View view) {
    	if (view == null)
    		return;
        if (view.getBackground() != null) {
        	view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }

	public int getParentViewId() {
		return -1;
	}
	
	public boolean onKeyTwiceDown( int keyCode, KeyEvent event ) {
		Log.i( TAG, "onKeyTwiceDown( KeyCode="+keyCode );
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onKeyOnceDown( int keyCode, KeyEvent event ) {
		Log.i( TAG, "onKeyOnceDown( KeyCode="+keyCode );
		return super.onKeyDown(keyCode, event);
	}	
	
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	Log.i( TAG, "onKeyDown( KeyCode="+keyCode+" KeyEvent="+event.getAction() );
    	boolean status = false;
    	
	    if ( event.getAction() == KeyEvent.ACTION_DOWN ) {
            if (    event.getKeyCode() == lastKeyCode
            	 && event.getDownTime() - lastPressedTime < Const.DOUBLE_PRESS_HK_BACK_PERIOD) {
            	status = onKeyTwiceDown( event.getKeyCode(), event );
            } else {
                lastPressedTime = event.getDownTime();
                lastKeyCode = event.getKeyCode();
                status = onKeyOnceDown( keyCode, event );
            }
    	} else {
    		status = super.onKeyDown( keyCode, event );
    	}
        return status;
    }	
}
