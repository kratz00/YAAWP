package org.yaawp.hmi.listitem;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;

import android.app.Activity;
import org.yaawp.hmi.helper.ScreenHelper;
import cz.matejcik.openwig.Engine;
/*
 * import cz.matejcik.openwig.Player;
 
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
*/
public class ListItemWigItem extends ListItem3ButtonsHint {

	private int mWigItemType;
	
	public final static int WIGITEMTYPE_ZONES = 0;
	public final static int WIGITEMTYPE_YOUSEE = 1;
	public final static int WIGITEMTYPE_INVENTORY = 2;
	public final static int WIGITEMTYPE_TASKS = 3;
	
	public ListItemWigItem( int wigItemType, String title, String body ) {
		super(title, body);
		mWigItemType = wigItemType;
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		
		switch (mWigItemType) {
		case WIGITEMTYPE_ZONES:
			if (Engine.instance.cartridge.visibleZones() >= 1) {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_LOCATIONSCREEN, null);
			}
			break;
		case WIGITEMTYPE_YOUSEE:
			if (Engine.instance.cartridge.visibleThings() >= 1) {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_ITEMSCREEN, null);
			}
			break;
		case WIGITEMTYPE_INVENTORY:
			if (Engine.instance.player.visibleThings() >= 1) {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_INVENTORYSCREEN, null);
			}
			break;
		case WIGITEMTYPE_TASKS:
			if ( Engine.instance.cartridge.visibleTasks() >= 1) {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_TASKSCREEN, null);
			}
			break;
		};
	
		return;
	}	
		
}
