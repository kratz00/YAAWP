package org.yaawp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.formats.ICartridge;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import java.io.IOException;

public class CartridgeHelper {

	public static byte[] getRawMediaData( ICartridge cartridge, int id ) {
		byte[] iconData = null;
		try {
			iconData = cartridge.getFile(id);
		} catch ( IOException e ) {
			// TODO log message
			iconData = null;
		}
		return iconData;
	}
	
	
	public static Bitmap getIconFrom( EventTable et, int defaultResource ) {
		if ( et.icon != null ) {
			return getIconFromId( et.icon.id, defaultResource );
		}
		return Images.getImageB(defaultResource);
	}
	
	public static Bitmap getIconFromId( int id, int defaultResource ) {
		Bitmap icon = getIconFromId( Engine.instance.gwcfile, id, defaultResource );
		return icon;
	}
	
	public static Bitmap getIconFromId( ICartridge cartridge, int id, int defaultResource ) {
		Bitmap icon = null;
		byte[] iconData = getRawMediaData( cartridge, id );
		
		if ( iconData != null ) {
			icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
		} else {
			icon = Images.getImageB(defaultResource);
		}
		return icon;
	}
	
	

}
