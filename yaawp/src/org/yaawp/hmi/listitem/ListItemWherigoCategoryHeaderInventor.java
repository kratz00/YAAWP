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

public class ListItemWherigoCategoryHeaderInventor extends ListItem3ButtonsHint {

	public ListItemWherigoCategoryHeaderInventor() {
		super( "", "", false, Images.getImageB( R.drawable.icon_inventory ) );
		setSelectable(false);
		mStyleCancelButton = null;
		mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 
	}
	
	@Override
	public void layout( Context context, View view  ) {
		mTitle = I18N.get(R.string.inventory); //  + " (" + Engine.instance.player.visibleThings() + ")";
		// TODO mBody = getVisiblePlayerThingsDescription();
		super.layout( context, view );
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
