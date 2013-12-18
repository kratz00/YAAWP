package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.utils.Logger;

import android.app.Activity;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoElementZone extends ListItemWherigoElement {

	private static final String TAG = "ListItemWherigoElementZone";
		
	public ListItemWherigoElementZone( Zone z, AbstractListItem parent ) {
		super( z, parent );
	}	
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if ( mObject instanceof Zone ) {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, (Zone) mObject );
		} else {
			Logger.e( TAG, "member mZone is null!" );
		}	
	}	
}
