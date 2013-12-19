package org.yaawp.hmi.listitem;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.Images;

import cz.matejcik.openwig.Engine;

import org.yaawp.R;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import org.yaawp.hmi.listitem.styles.*;

public class ListItemCartridgeHeadline extends ListItem3ButtonsHint {

	public ListItemCartridgeHeadline() {
		super( false, null );
		    	
    	if ( Engine.instance != null && Engine.instance.cartridge != null ) {
    		mDataTextMajor = Engine.instance.cartridge.name; 
    		mDataImageLeft = CartridgeHelper.getIconFrom( Engine.instance.cartridge, R.drawable.icon_gc_wherigo );
    	} else {
    		mDataTextMajor = "{ERROR no engine or cartridge instance}"; 
    		mDataImageLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
    	}
   			
    	// ----
    	mStyleBackground = new StyleBasics( View.VISIBLE, 0xFF24699c ); // wherigo-blue 
    	mStyleTextMajor  = new TextStyle( View.VISIBLE, Color.TRANSPARENT, Color.WHITE, 18, Typeface.BOLD );
    	mStyleTextMinor  = null;
    	mStyleImageLeft  = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, 48, 48, null );
    	mStyleCancelButton = null;		
	}
}

