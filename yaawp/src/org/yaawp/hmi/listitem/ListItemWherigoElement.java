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
		super("","",false,null,parent);
		setSelectable(true);
		
		mObject = et;

		mDataTextMajor = mObject.name;
		mDataTextMinor = null;
		mDataImageLeft = CartridgeHelper.getIcon( mObject, R.drawable.icon_locations );	// TODO default icon!?!?
		
		mStyleBackground = Styles.mStyleBackgroundLightGray; 
		mStyleImageLeft  = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, 32, 32, null );
	}
}
