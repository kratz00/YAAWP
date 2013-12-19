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
	public void refresh() {
		int count = Engine.instance.player.visibleThings();
		if ( count == 0 ) {
			mTitleOpen = I18N.get(R.string.inventory);
			mBodyOpen = "You have nothing"; // TODO I18N
			mTitleClose = mTitleOpen;	
			mBodyClose = mBodyOpen;					
		} else {
			mTitleOpen = I18N.get(R.string.inventory);
			mBodyOpen = "";
			mTitleClose = I18N.get(R.string.inventory) + " (" + count + ")";
			mBodyClose = getVisiblePlayerThingsDescription();				
		}
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
