package org.yaawp.hmi.listitem;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.utils.CartridgeHelper;

import cz.matejcik.openwig.Engine;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import org.yaawp.hmi.listitem.styles.*;

public class ListItemCartridgeHeadline extends ListItem3ButtonsHint {

	public ListItemCartridgeHeadline() {
		super("","",true,null,null);
		    	
    	mDataTextMajor = Engine.instance.cartridge.name;   
    	mDataImageLeft = CartridgeHelper.getCartridgeImage( Engine.instance.gwcfile);
    	// ----
    	mStyleBackground = new StyleBasics( View.VISIBLE, 0xFF24699c ); // wherigo-blue 
    	mStyleTextMajor = new TextStyle( View.VISIBLE, Color.TRANSPARENT, Color.WHITE, 18, Typeface.BOLD );
    	mStyleTextMinor = null;
    	mStyleImageLeft = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, 48, 48, null );
    	mStyleCancelButton = null;		
	}
}

