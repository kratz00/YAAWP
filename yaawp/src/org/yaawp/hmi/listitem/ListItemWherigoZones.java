package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoZones extends ListItem3ButtonsHint {

	public ListItemWherigoZones() {
		super( "", "", Images.getImageB( R.drawable.icon_locations ) );
		setSelectable(true);
		enableCancelButton(false);
	}
	
	@Override
	public void layout( Context context, View view  ) {
		mTitle = I18N.get(R.string.locations) + " (" + Engine.instance.cartridge.visibleZones() + ")";
		mBody = getVisibleZonesDescription();
		super.layout( context, view );
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

