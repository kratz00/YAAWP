package org.yaawp.hmi.listitem;

import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.utils.Logger;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;

/* both for YouSee object and for Inventory objects */
public class ListItemWherigoElementThing extends ListItemWherigoElement {
	
	private static final String TAG = "ListItemWherigoElementInventory";
	
	public ListItemWherigoElementThing( Thing t ) {
		super( t );
	}	
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if ( mObject instanceof Thing ) {
			Thing t = (Thing) mObject;
			if (t.hasEvent("OnClick")) {
				Engine.callEvent(t, "OnClick", null);
			} else {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, t);
			}			
		} else {
			Logger.e( TAG, "member mZone is null!" );
		}	
	}
}
