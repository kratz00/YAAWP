package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;

public class ListItemWherigoInventory extends ListItem3ButtonsHint {

	public ListItemWherigoInventory() {
		super(I18N.get(R.string.inventory) + " (" + Engine.instance.player.visibleThings() + ")",
				"", R.drawable.icon_inventory );
		mBody = getVisiblePlayerThingsDescription();
		setSelectable(true);		
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if (Engine.instance.player.visibleThings() >= 1) {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_INVENTORYSCREEN, null);
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
