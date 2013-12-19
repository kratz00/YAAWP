package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.utils.CartridgeHelper;

import android.graphics.Color;
import android.view.View;
import cz.matejcik.openwig.EventTable;
import org.yaawp.hmi.listitem.styles.*;

public class ListItemWherigoElement extends ListItem3ButtonsHint {

	EventTable mObject = null;
	
	public ListItemWherigoElement( EventTable e, AbstractListItem parent ) {
		super("","",false,null,parent);
		setSelectable(true);
		enableCancelButton(false);
		mObject = e;
		
		mStyleBackground = Styles.mStyleBackgroundLightGray; 
		mStyleImageLeft = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, 32, 32, null );
		// mStyleCancelButton = null;
    	// mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 
		// TODO Icon
		mDataTextMajor = mObject.name;
		// TODO mBody = distance, inside/outside/approxym..
		mDataImageLeft = CartridgeHelper.getIcon( mObject, R.drawable.icon_locations );	// TODO default icon!?!?	
	}
}
