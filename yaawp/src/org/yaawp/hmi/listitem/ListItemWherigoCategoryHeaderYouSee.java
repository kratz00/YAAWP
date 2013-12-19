package org.yaawp.hmi.listitem;

import java.util.Vector;
import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.utils.Images;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

public class ListItemWherigoCategoryHeaderYouSee extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderYouSee() {
		super();
		mDataImageLeft = Images.getImageB( R.drawable.icon_search );
		refresh();
	}
	
	@Override
	public void layoutOpen( Context context, View view  ) {
		mDataTextMajor = mTitleOpen;
		mDataTextMinor = mBodyOpen;		
		super.layoutOpen( context, view );
	}
	
	@Override
	public void layoutClose( Context context, View view  ) {
		mDataTextMajor = mTitleClose;
		mDataTextMinor = mBodyClose;		
		super.layoutClose( context, view );
	}
	
	@Override
	public void refresh() {
		int count = Engine.instance.cartridge.visibleThings();
		if ( count == 0 ) {
			mTitleOpen = I18N.get(R.string.you_see);
			mBodyOpen = "You see nothing"; // TODO I18N	
			mTitleClose = mTitleOpen;	
			mBodyClose = mBodyOpen;
		} else {
			mTitleOpen = I18N.get(R.string.you_see);
			mBodyOpen = "";
			mTitleClose = I18N.get(R.string.you_see) + " (" + count + ")";
			mBodyClose = getVisibleCartridgeThingsDescription();					
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
