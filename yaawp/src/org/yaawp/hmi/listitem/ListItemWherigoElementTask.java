package org.yaawp.hmi.listitem;

import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.utils.Logger;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class ListItemWherigoElementTask extends ListItemWherigoElement {

	private static final String TAG = "ListItemWherigoElementTask";
	
	public ListItemWherigoElementTask( Task taskObject, AbstractListItem parent ) {
		super( taskObject, parent );
	}	
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if ( mObject instanceof Task ) {
			Task z = (Task) mObject;
			if (z.hasEvent("OnClick")) {
				Engine.callEvent(z, "OnClick", null);
			} else {
				ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, z);
			}
		} else {
			Logger.e( TAG, "member mZone is null!" );
		}	
	}	
}
