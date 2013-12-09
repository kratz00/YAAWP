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


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import org.yaawp.MainApplication;
import org.yaawp.hmi.gui.extension.CustomMain;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.Settings;
import org.yaawp.utils.A;
import org.yaawp.utils.Const;
import org.yaawp.utils.Logger;

import java.lang.System;

public class CustomActivity extends FragmentActivity {

	// protected Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.v(getLocalClassName(), "onCreate(), id:" + hashCode());
		try {
			super.onCreate(savedInstanceState);
			customOnCreate(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onCreate()", e);
		}
	}
	
	protected static void customOnCreate(Activity activity) {
		//Logger.v(activity.getLocalClassName(), "customOnCreate(), id:" + activity.hashCode());
		// set main activity parameters
		if (!(activity instanceof CustomMain)) {
			//	Settings.setLanguage(this);
			Settings.setScreenBasic(activity);
		}

		// set screen size
		Const.SCREEN_WIDTH = activity.getWindowManager().getDefaultDisplay().getWidth();
		Const.SCREEN_HEIGHT = activity.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	@Override
	public void onStart() {
		Logger.v(getLocalClassName(), "onStart(), id:" + hashCode());
		try {
			super.onStart();
			customOnStart(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onStart()", e);
		}
	}
	
	protected static void customOnStart(Activity activity) {
		//Logger.v(activity.getLocalClassName(), "customOnStart(), id:" + activity.hashCode());
		PreferenceFunc.setScreenFullscreen(activity);
	}

	@Override
	protected void onResume() {
		Logger.v(getLocalClassName(), "onResume(), id:" + hashCode());
		try {
			super.onResume();
			customOnResume(this);
			// set values again, this fix problem when activity is started after
			// activity in e.g. fixed portrait mode
			Const.SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
        	Const.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onResume()", e);
		}
	}
	
	protected static void customOnResume(Activity activity) {
		//Logger.v(activity.getLocalClassName(), "customOnResume(), id:" + activity.hashCode());
		// set current activity
		Settings.setCurrentActivity(activity);
   		// enable permanent screen on
		PreferenceFunc.enableWakeLock();
	}
	
	@Override
	protected void onPause() {
Logger.v(getLocalClassName(), "onPause(), id:" + hashCode());
		try {
			super.onPause();
			customOnPause(this);
		} catch (Exception e) {
			Logger.e(getLocalClassName(), "onPause()", e);
		}
	}
	
	protected static void customOnPause(Activity activity) {
//Logger.v(activity.getLocalClassName(), "customOnPause(), id:" + activity.hashCode());
		// activity is not in foreground
		if (Settings.getCurrentActivity() == activity) {
			Settings.setCurrentActivity(null);
		}
		// disable location
		MainApplication.onActivityPause();
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
	
}