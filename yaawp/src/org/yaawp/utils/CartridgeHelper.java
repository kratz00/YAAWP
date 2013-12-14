package org.yaawp.utils;

import org.yaawp.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.formats.ICartridge;

public class CartridgeHelper {

	public static Bitmap getCartridgeImage( ICartridge cartridge ) {
		Bitmap icon = null;
	   
	    try {	    	
	    	byte[] iconData = cartridge.getFile(cartridge.getIconId());
	    	icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
	    } catch (Exception e) {
	    	icon = Images.getImageB(R.drawable.icon_gc_wherigo);
	    }		
		return icon;
	}
}
