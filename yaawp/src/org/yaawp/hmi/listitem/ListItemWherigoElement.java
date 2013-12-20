package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.utils.CartridgeHelper;

import android.graphics.Color;
import android.view.View;
import cz.matejcik.openwig.EventTable;
import org.yaawp.hmi.listitem.styles.*;

public class ListItemWherigoElement extends ListItem3ButtonsHint {

	EventTable mObject = null;
	
	public ListItemWherigoElement( EventTable et, AbstractListItem parent ) {
		super( true, parent );
		
		mObject = et;

		mDataTextMajor = mObject.name;
		mDataTextMinor = mObject.description;
		mDataImageLeft = CartridgeHelper.getIconFrom( et, R.drawable.icon_locations );				
		
		mStyleBackground = Styles.mStyleBackgroundLightGray; 
		mStyleImageLeft  = new StyleImage( View.VISIBLE, Color.TRANSPARENT, 32, 32, null );
	}
}
