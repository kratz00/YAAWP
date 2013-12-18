package org.yaawp.hmi.listitem;

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
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;

public class ListItemWherigoCategoryHeaderInventor extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderInventor() {
		super();
		mIconLeft = Images.getImageB( R.drawable.icon_inventory );			
	}
	
	@Override
	public void layoutOpen( Context context, View view  ) {
		int count = Engine.instance.player.visibleThings();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.tasks);
			mBody = "You have nothing"; // TODO I18N
		} else {
			mTitle = I18N.get(R.string.inventory);
			mBody = "";	
		}
		super.layoutOpen( context, view );
	}
	
	@Override
	public void layoutClose( Context context, View view  ) {
		int count = Engine.instance.player.visibleThings();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.tasks);
			mBody = "You have nothing"; // TODO I18N
		} else {
			mTitle = I18N.get(R.string.inventory)+ " (" + count + ")";
			mBody = getVisiblePlayerThingsDescription();		
		}
		super.layoutClose( context, view );		
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
