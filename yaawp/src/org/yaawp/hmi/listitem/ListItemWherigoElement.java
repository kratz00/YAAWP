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
		
    	mStyleBackground     = Styles.mStyleBackgroundDarkHolo;
    	mStyleTextMajor      = Styles.mStyleTextDarkHoloMajor;
    	mStyleTextMinor      = Styles.mStyleTextDarkHoloMinor;
    	mStyleTextMajorRight = Styles.mStyleTextDarkHoloMajor;
    	mStyleTextMinorRight = Styles.mStyleTextDarkHoloMinor;
    	mStyleImageLeft      = Styles.mStyleImageSmall;
    	mStyleImageRight     = Styles.mStyleImageSmall;
    	mStyleCancelButton   = null;		
	}
}
