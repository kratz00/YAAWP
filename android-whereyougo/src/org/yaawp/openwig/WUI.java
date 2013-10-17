/*
  * This file is part of WhereYouGo.
  *
  * WhereYouGo is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * WhereYouGo is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with WhereYouGo.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2012 Menion <whereyougo@asamm.cz>
  */ 

package org.yaawp.openwig;

import java.io.ByteArrayInputStream;


import menion.android.whereyougo.Main;
import menion.android.whereyougo.gui.CartridgeDetails;
import menion.android.whereyougo.gui.CartridgeMainMenu;
import menion.android.whereyougo.gui.Details;
import menion.android.whereyougo.gui.InputScreen;
import menion.android.whereyougo.gui.ListActions;
import menion.android.whereyougo.gui.ListTargets;
import menion.android.whereyougo.gui.ListTasks;
import menion.android.whereyougo.gui.ListThings;
import menion.android.whereyougo.gui.ListZones;
import menion.android.whereyougo.gui.PushDialog;
import menion.android.whereyougo.gui.Refreshable;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.guiding.GuidingScreen;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import se.krka.kahlua.vm.LuaClosure;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.platform.UI;
import org.yaawp.hmi.helper.AudioHelper;
import org.yaawp.hmi.helper.ProgressDialogHelper;
import org.yaawp.hmi.helper.ScreenHelper;

public class WUI implements UI {

	private static final String TAG = "WUI";
	
	public static boolean saving = false;
	
	public void blockForSaving() {
		Logger.w(TAG, "blockForSaving()");
		saving = true;
	}
	
	public void unblock() {
		Logger.w(TAG, "unblock()");
		saving = false;
	}

	public void debugMsg(String msg) {
		Logger.w(TAG, "debugMsg(" + msg.trim() + ")");
	}

	public void playSound(byte[] data, String mime) {
		AudioHelper.playSound( data, mime );
	}
	
	public void showError(String msg) {
		Logger.w(TAG, "showError(" + msg.trim() + ")");
		UtilsGUI.showDialogError(Settings.getCurrentActivity(), msg);
	}

	public void pushDialog(String[] texts, Media[] media, String button1,
			String button2, LuaClosure callback) {
		Logger.w(TAG, "pushDialog(" + texts + ", " + media + ", " + button1 + ", " + button2 + ", " + callback + ")");

		Activity activity = ScreenHelper.getParentActivity();
		PushDialog.setDialog(texts, media, button1, button2, callback);
		Intent intent = new Intent(activity, PushDialog.class);
		activity.startActivity(intent);
		ScreenHelper.closeActivity(activity);
					
		Vibrator v = (Vibrator) A.getMain().getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(25);		
	}

	public void pushInput(EventTable input) {
		Logger.w(TAG, "pushInput(" + input + ")");
		Activity activity = ScreenHelper.getParentActivity();
		InputScreen.setInput(input);
		Intent intent = new Intent(activity, InputScreen.class);
		activity.startActivity(intent);
		ScreenHelper.closeActivity(activity);
	}

	public void refresh() {
		Logger.w(TAG, "refresh(), currentActivity:" + Settings.getCurrentActivity());
		if (Settings.getCurrentActivity() != null && Settings.getCurrentActivity() instanceof Refreshable) {
			((Refreshable) Settings.getCurrentActivity()).refresh();
		}
	}

	public void setStatusText(final String text) {
		Logger.w(TAG, "setStatus(" + text + ")");
		if (text == null || text.length() == 0)
			return;
		
		try {
			final Activity activity = ScreenHelper.getParentActivity();
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();	
				}
			});
		} catch (Exception e) {
			Logger.e(TAG, "setStatusText(" + text + ")", e);
		}	
	}

	public void showScreen(int screenId, EventTable details) {
		ScreenHelper.activateScreen( screenId, details );
	}
	
	public void loadCartridge() {
    	ProgressDialogHelper.Show( "", "Starting Cartridges" ); // TODO use string id
	}
	
	public void start() {
		ProgressDialogHelper.Hide();
		ScreenHelper.activateScreen(UI.MAINSCREEN, null);
	}
	
	public void end() {
		ProgressDialogHelper.Hide();
		Engine.kill();
		ScreenHelper.activateScreen(ScreenHelper.SCREEN_MAIN, null);
	}
	
}
