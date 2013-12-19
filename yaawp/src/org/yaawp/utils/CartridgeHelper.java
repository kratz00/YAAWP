package org.yaawp.utils;

import java.io.IOException;

import org.yaawp.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.formats.ICartridge;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;

public class CartridgeHelper {

	public static byte[] getRawMediaData( int id ) {
		byte[] iconData = null;
		try {
			iconData = Engine.instance.gwcfile.getFile(id);
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
	
	private static Bitmap getIconFromId( int id, int defaultResource ) {
		Bitmap icon = null;
		byte[] iconData = getRawMediaData( id );
		
		if ( iconData != null ) {
			icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
		} else {
			icon = Images.getImageB(defaultResource);
		}
		return icon;
	}

}
