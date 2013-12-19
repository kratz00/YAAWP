package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.utils.Logger;
import org.yaawp.utils.UtilsFormat;

import android.app.Activity;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoElementZone extends ListItemWherigoElement {

	private static final String TAG = "ListItemWherigoElementZone";
		
	public ListItemWherigoElementZone( Zone z, AbstractListItem parent ) {
		super( z, parent );
		
		switch (z.contain) {
			case Zone.DISTANT: 
				mDataTextMinor = "distant, " + UtilsFormat.formatDistance(z.distance, false); // TODO I18N
				break;
			case Zone.PROXIMITY:
				mDataTextMinor = "near, "+ UtilsFormat.formatDistance(z.distance, false); // TODO I18N
				break; 
			case Zone.INSIDE:
				mDataTextMinor = "inside"; // TODO I18N
				break; 
			default:
				mDataTextMinor = "(nothing)"; // TODO I18N
				break;
		}		
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
