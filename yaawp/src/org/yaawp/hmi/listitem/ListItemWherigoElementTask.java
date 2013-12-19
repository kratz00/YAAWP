package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;

import android.app.Activity;
import android.graphics.Bitmap;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class ListItemWherigoElementTask extends ListItemWherigoElement {

	private static final String TAG = "ListItemWherigoElementTask";
	
	private static Bitmap[] stateIcons;
	static {
		stateIcons = new Bitmap[3];
		stateIcons[Task.PENDING] = Images.getImageB(R.drawable.task_pending);
		stateIcons[Task.DONE] = Images.getImageB(R.drawable.task_done);
		stateIcons[Task.FAILED] = Images.getImageB(R.drawable.task_failed);
	}	
	
	public ListItemWherigoElementTask( Task taskObject, AbstractListItem parent ) {
		super( taskObject, parent );
		int state = ((Task)mObject).state();
		if ( state>=0 && state<3 ) {
			mDataImageLeft = stateIcons[state];
		}
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
