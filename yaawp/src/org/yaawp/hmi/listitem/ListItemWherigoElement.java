package org.yaawp.hmi.listitem;

import cz.matejcik.openwig.EventTable;
import org.yaawp.R;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.hmi.listitem.styles.*;

public class ListItemWherigoElement extends ListItemUniversalLayout {

	EventTable mObject = null;
	
	public ListItemWherigoElement( EventTable et, AbstractListItem parent ) {
		super( false, parent );
		
		mObject = et;

		mDataTextMajor = mObject.name;
		mDataTextMinor = mObject.description;
		mDataImageLeft = CartridgeHelper.getIconFrom( et, R.drawable.icon_locations );				
		
    	mStyleBackground     = Styles.mStyleBackgroundLightGray;
    	mStyleTextMajor      = Styles.mStyleTextMajor;
    	mStyleTextMinor      = Styles.mStyleTextMinor;
    	mStyleTextMajorRight = Styles.mStyleTextMajor;
    	mStyleTextMinorRight = Styles.mStyleTextMinor;
    	mStyleImageLeft      = Styles.mStyleImageSmall;
    	mStyleImageRight     = Styles.mStyleImageSmall;
    	mStyleCancelButton   = null;		
	}
}
