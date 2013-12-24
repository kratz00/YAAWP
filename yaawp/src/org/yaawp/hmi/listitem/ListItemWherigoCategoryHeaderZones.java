package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItemUniversalLayout;
import org.yaawp.hmi.listitem.styles.*;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoCategoryHeaderZones extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderZones() {
		super();
		mDataImageLeft = Images.getImageB( R.drawable.icon_locations );	
		refresh();
	}
	
	@Override
	public int getCountChild() {
		int count = Engine.instance.cartridge.visibleZones();
		return count;
	}
	
	@Override
	public String getTitle() {
		return I18N.get(R.string.locations);
	}
	@Override
	public String getSubtitle() {
		if ( getCountChild() == 0) {
			return "No area to go"; // TODO I18N	
		} 
		return getVisibleZonesDescription();		
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

