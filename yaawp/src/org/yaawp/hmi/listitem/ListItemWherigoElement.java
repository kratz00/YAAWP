package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.utils.CartridgeHelper;

import android.graphics.Color;
import cz.matejcik.openwig.EventTable;

public class ListItemWherigoElement extends ListItem3ButtonsHint {

	EventTable mObject = null;
	
	public ListItemWherigoElement( EventTable e, AbstractListItem parent ) {
		super("","",false,null,parent);
		setSelectable(true);
		enableCancelButton(false);		mObject = e;
		mStyleImageLeft = new ImageStyle( Color.TRANSPARENT, 32, 32, null );
		// mStyleCancelButton = null;
    	// mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 
		// TODO Icon
		mTitle = mObject.name;
		// TODO mBody = distance, inside/outside/approxym..
		mIconLeft = CartridgeHelper.getIcon( mObject, R.drawable.icon_locations );	// TODO default icon!?!?	
	}
}
