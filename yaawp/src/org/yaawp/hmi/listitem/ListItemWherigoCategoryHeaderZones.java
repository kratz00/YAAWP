package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.listitem.AbstractListItem.StyleDefine;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoCategoryHeaderZones extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderZones() {
		super();
		mIconLeft = Images.getImageB( R.drawable.icon_locations );	
	}
	
	@Override
	public void layoutOpen( Context context, View view  ) {
		int count = Engine.instance.cartridge.visibleZones();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.locations);
			mBody = "No area to go"; // TODO I18N			
		} else {	
			mTitle = I18N.get(R.string.locations);
			mBody = "";		
		}
		super.layoutOpen( context, view );
	}
	
	@Override
	public void layoutClose( Context context, View view  ) {
		int count = Engine.instance.cartridge.visibleZones();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.locations);
			mBody = "No area to go"; // TODO I18N			
		} else {
			mTitle = I18N.get(R.string.locations)+ " (" + count + ")";
			mBody = getVisibleZonesDescription();			
		}
		super.layoutClose( context, view );
	}
	
	private String getVisibleZonesDescription() {
		String description = null;
		@SuppressWarnings("unchecked")
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.get(i);
			if (z.isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += z.name;
			}
		}
		return description;
	}	
}

