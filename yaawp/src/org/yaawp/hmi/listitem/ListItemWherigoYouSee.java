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
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoYouSee extends ListItem3ButtonsHint {

	public ListItemWherigoYouSee() {
		super( "", "", false, Images.getImageB( R.drawable.icon_search ) );
		setSelectable(true);	
		enableCancelButton(false);
	}
	
	@Override
	public void layout( Context context, View view  ) {
		mTitle = I18N.get(R.string.you_see) + " (" + Engine.instance.cartridge.visibleThings() + ")";
		mBody = getVisibleCartridgeThingsDescription();
		super.layout( context, view );
	}	
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if (Engine.instance.cartridge.visibleThings() >= 1) {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_ITEMSCREEN, null);
		}		
	}	
	
	private String getVisibleCartridgeThingsDescription() {
		String description = null;
		@SuppressWarnings("unchecked")
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			String des = getVisibleThingsDescription(z);
			if (des != null) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += des;
			}
		}
		return description;
	}
	
	private String getVisibleThingsDescription(Zone z) {
		String description = null;
		if (!z.showThings())
			return null;
		Object key = null;
		while ((key = z.inventory.next(key)) != null) {
			Object o = z.inventory.rawget(key);
			if (o instanceof Player)
				continue;
			if (!(o instanceof Thing))
				continue;
			if (((Thing) o).isVisible()) {
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
