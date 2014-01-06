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

package org.yaawp.utils;

import org.yaawp.MainApplication;
import org.yaawp.audio.ManagerAudio;
import org.yaawp.guidance.GuidingContent;
import org.yaawp.hmi.activities.CustomActivity;
import org.yaawp.positioning.Orientation;

import android.app.Application;
import android.util.Log;

/**
 * @author menion
 * @since 25.1.2010 2010
 */
public class A {

	private static String TAG = "A";

	private static MainApplication app;
	protected static CustomActivity main;
	private static GuidingContent guidingContent;
	private static ManagerAudio managerAudio;
	private static Orientation rotator;
	
	public static void printState() {
		Logger.i(TAG, "printState() - STATIC VARIABLES");
		Logger.i(TAG, "app:" + app);
		Logger.i(TAG, "managerAudio:" + managerAudio);
		Logger.i(TAG, "main:" + main);
		Logger.i(TAG, "guidingContent:" + guidingContent);
		Logger.i(TAG, "rotator:" + rotator);
	}
	
	public static void destroy() {
		guidingContent = null;			
		managerAudio = null;
		main = null;
		if (rotator != null) {
			rotator.removeAllListeners();
			rotator = null;
		}
		// finally destroy app
		if (app != null)
			app.destroy();
		app = null;
	}
	
	public static void registerApp(MainApplication app) {
		A.app = app;
	}
	
	public static void registerMain(CustomActivity main) {
		A.main = main;
	}
	
	public static Application getApp() {
		if ( app == null ) {
			Log.e(TAG, "application object is not set");
		}
		return app;
	}
	
	public static CustomActivity getMain() {
		return main;
	}
	
	public static ManagerAudio getManagerAudio() {
		if (managerAudio == null) {
			managerAudio = new ManagerAudio();
		}
		return managerAudio;
	}
	
	public static GuidingContent getGuidingContent() {
		if (guidingContent == null) {
			guidingContent = new GuidingContent();
		}
		return guidingContent;
	}
	
	public static Orientation getRotator() {
		if (rotator == null) {
			rotator = new Orientation();
		}
		return rotator;
	}
}
