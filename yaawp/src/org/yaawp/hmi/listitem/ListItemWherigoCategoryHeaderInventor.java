package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.listitem.styles.*;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;

public class ListItemWherigoCategoryHeaderInventor extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderInventor() {
		super();
		mDataImageLeft = Images.getImageB( R.drawable.icon_inventory );	
		refresh();
	}
	
	@Override
	public int getCountChild() {
		int count = Engine.instance.player.visibleThings();
		return count;
	}
	
	@Override
	public String getTitle() {
		return I18N.get(R.string.inventory);
	}
	@Override
	public String getSubtitle() {
		if ( getCountChild() == 0) {
			return "You have nothing"; // TODO I18N	
		} 
		return getVisiblePlayerThingsDescription();		
	}	
	
	private String getVisiblePlayerThingsDescription() {
		Player p = Engine.instance.player;
		String description = null;
		Object key = null;
		while ((key = p.inventory.next(key)) != null) {
			Object o = p.inventory.rawget(key);
			if (o instanceof Thing && ((Thing) o).isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += ((Thing) o).name;
			}
		}
		return description;
	}	
}
