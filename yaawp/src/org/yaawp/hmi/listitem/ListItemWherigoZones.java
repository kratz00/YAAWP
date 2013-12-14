package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoZones extends ListItem3ButtonsHint {

	public ListItemWherigoZones() {
		super( I18N.get(R.string.locations) + " (" + Engine.instance.cartridge.visibleZones() + ")",
			   "",
			   R.drawable.icon_locations );

	    mBody = getVisibleZonesDescription();
		setSelectable(true);
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if (Engine.instance.cartridge.visibleZones() >= 1) {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_LOCATIONSCREEN, null);
		}		
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

