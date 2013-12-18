package org.yaawp.hmi.listitem;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.utils.CartridgeHelper;

import cz.matejcik.openwig.Engine;

import android.graphics.Color;
import android.graphics.Typeface;


public class ListItemCartridgeHeadline extends ListItem3ButtonsHint {

	public ListItemCartridgeHeadline() {
		super("","",true,null);
		    	
    	mTitle = Engine.instance.cartridge.name;   
    	mIconLeft = CartridgeHelper.getCartridgeImage( Engine.instance.gwcfile);
    	// ----
    	mStyleBackground = new StyleDefine( 0xFF24699c ); // wherigo-blue 
    	mStyleTextMajor = new TextStyle( Color.TRANSPARENT, Color.WHITE, 18, Typeface.BOLD );
    	mStyleTextMinor = null;
    	mStyleImageLeft = new ImageStyle( Color.TRANSPARENT, 48, 48, null );
    	mStyleCancelButton = null;		
	}
}

