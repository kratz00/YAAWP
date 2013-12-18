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

public class ListItemWherigoCategoryHeaderZones extends ListItem3ButtonsHint {

	public ListItemWherigoCategoryHeaderZones() {
		super( "", "", false, Images.getImageB( R.drawable.icon_locations ) );
		setSelectable(false);
		mStyleCancelButton = null;
    	mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 		
	}
	
	@Override
	public void layout( Context context, View view  ) {
		mTitle = I18N.get(R.string.locations); // TODO  + " (" + Engine.instance.cartridge.visibleZones() + ")";
		// TODO mBody = getVisibleZonesDescription();
		super.layout( context, view );
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

