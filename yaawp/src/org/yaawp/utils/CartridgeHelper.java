package org.yaawp.utils;

import org.yaawp.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.formats.ICartridge;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.EventTable;

public class CartridgeHelper {

	public static Bitmap getCartridgeImage( ICartridge cartridge ) {
		Bitmap icon = null;
	   
		icon = getImage( cartridge, cartridge.getIconId(), R.drawable.icon_gc_wherigo );
			
		return icon;
	}
	
	public static Bitmap getIcon( Zone z, int defaultResource ) {
		Bitmap icon = null;
		if ( z != null ) {
			try {
				byte[] iconData = z.icon.getIcon();
				icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
		    } catch (Exception e) {
		    	icon = null ;
		    }				
		}
	    if ( icon == null ) {
	    	icon = Images.getImageB(defaultResource);
	    }		
		return icon;
	}
	
	public static Bitmap getIcon( EventTable et, int defaultResource ) {
		Bitmap icon = null;
		if ( et != null ) {
			try {
				byte[] iconData = et.icon.getIcon();
				icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
		    } catch (Exception e) {
		    	icon = null ;
		    }				
		}
	    if ( icon == null ) {
	    	icon = Images.getImageB(defaultResource);
	    }		
		return icon;
	}	
	
	public static Bitmap getImage( ICartridge cartridge, int mediaId, int defaultResource ) {
		Bitmap icon = null;
		   
	    try {
	    	if ( cartridge != null ) {
	    		byte[] iconData = cartridge.getFile(mediaId);
	    		if ( iconData != null ) {
	    			// TODO check if referenced media object a image
	    			icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
	    		}
	    	}
	    } catch (Exception e) {
	    	icon = null ;
	    }		
	    
	    if ( icon == null ) {
	    	icon = Images.getImageB(defaultResource);
	    }
	    
		return icon;		
	}
}
