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
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoCategoryHeaderYouSee extends ListItem3ButtonsHint {

	public ListItemWherigoCategoryHeaderYouSee() {
		super( "", "", false, Images.getImageB( R.drawable.icon_search ) );
		setSelectable(false);	
		mStyleCancelButton = null;
		mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 
	}
	
	@Override
	public void layout( Context context, View view  ) {
		mTitle = I18N.get(R.string.you_see); // TODO + " (" + Engine.instance.cartridge.visibleThings() + ")";
		// TODO mBody = getVisibleCartridgeThingsDescription();
		super.layout( context, view );
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
