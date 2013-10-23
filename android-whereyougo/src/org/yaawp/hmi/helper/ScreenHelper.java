package org.yaawp.hmi.helper;

import org.yaawp.hmi.activities.GuidingActivity;

import menion.android.whereyougo.gui.CartridgeDetails;
import menion.android.whereyougo.gui.CartridgeMainMenu;
import menion.android.whereyougo.gui.Details;
import menion.android.whereyougo.gui.ListActions;
import menion.android.whereyougo.gui.ListTargets;
import menion.android.whereyougo.gui.ListTasks;
import menion.android.whereyougo.gui.ListThings;
import menion.android.whereyougo.gui.ListZones;
import menion.android.whereyougo.gui.PushDialog;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import android.app.Activity;
import android.content.Intent;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.platform.UI;

public class ScreenHelper {

	private static final String TAG = "ScreenHelper";

	public static final int SCREEN_MAINSCREEN = UI.MAINSCREEN;
	public static final int SCREEN_DETAILSCREEN = UI.DETAILSCREEN;
	public static final int SCREEN_INVENTORYSCREEN = UI.INVENTORYSCREEN;
	public static final int SCREEN_ITEMSCREEN = UI.ITEMSCREEN;
	public static final int SCREEN_LOCATIONSCREEN = UI.LOCATIONSCREEN;
	public static final int SCREEN_TASKSCREEN = UI.TASKSCREEN;	
	
	public static final int SCREEN_MAIN = 10;
	public static final int SCREEN_CART_DETAIL = 11;
	public static final int SCREEN_ACTIONS = 12;
	public static final int SCREEN_TARGETS = 13;	
	
	public static void activateScreen(int screenId, EventTable details) {
		Activity activity = getParentActivity();
		Logger.i(TAG, "activateScreen(" + screenId + "), parent:" + activity + ", param:" + details);
				
		// disable currentActivity
		Settings.setCurrentActivity(null);
		
		switch (screenId) {
			case SCREEN_MAINSCREEN:
				Intent intent01 = new Intent(activity, CartridgeMainMenu.class);
				activity.startActivity(intent01);
				return;
			case SCREEN_CART_DETAIL:
				Intent intent02 = new Intent(activity, CartridgeDetails.class);
				activity.startActivity(intent02);
				return;
			case SCREEN_DETAILSCREEN:
				Details.et = details;
				Intent intent03 = new Intent(activity, Details.class);
				activity.startActivity(intent03);
				return;
			case SCREEN_INVENTORYSCREEN:
				Intent intent04 = new Intent(activity, ListThings.class);
				intent04.putExtra("title", "Inventory");
				intent04.putExtra("mode", ListThings.INVENTORY);
				activity.startActivity(intent04);
				return;
			case SCREEN_ITEMSCREEN:
				Intent intent05 = new Intent(activity, ListThings.class);
				intent05.putExtra("title", "You see");
				intent05.putExtra("mode", ListThings.SURROUNDINGS);
				activity.startActivity(intent05);
				return;
			case SCREEN_LOCATIONSCREEN:
				Intent intent06 = new Intent(activity, ListZones.class);
				intent06.putExtra("title", "Locations");
				activity.startActivity(intent06);
				return;
			case SCREEN_TASKSCREEN:
				Intent intent07 = new Intent(activity, ListTasks.class);
				intent07.putExtra("title", "Tasks");
				activity.startActivity(intent07);
				return;
			case SCREEN_ACTIONS:
				Intent intent09 = new Intent(activity, ListActions.class);
				if (details != null)
					intent09.putExtra("title", details.name);
				activity.startActivity(intent09);
				return;
			case SCREEN_TARGETS:
				Intent intent10 = new Intent(activity, ListTargets.class);
				if (details != null)
					intent10.putExtra("title", details.name);
				activity.startActivity(intent10);
				return;
		}
		
		closeActivity(activity);
	}	
	
	public static void closeActivity(Activity activity) {
		if (activity instanceof PushDialog ||
				activity instanceof GuidingActivity) {
			activity.finish();
		}
	}	
	
	public static Activity getParentActivity() {
		Activity activity = Settings.getCurrentActivity();

		if (activity == null)
			activity = (CustomActivity) A.getMain();
		
		return activity;
	}		
}
