package org.yaawp.hmi.listitem;

import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.Images;

import cz.matejcik.openwig.Engine;

import org.yaawp.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import org.yaawp.hmi.listitem.styles.*;

public class ListItemCartridgeHeadline extends ListItem3ButtonsHint {

	public ListItemCartridgeHeadline() {
		super( true, null );
		    	
    	if ( Engine.instance != null && Engine.instance.cartridge != null ) {
    		mDataTextMajor = Engine.instance.cartridge.name; 
    		mDataImageLeft = CartridgeHelper.getIconFrom( Engine.instance.cartridge, R.drawable.ic_launcher );
    		mDataTextMinor = I18N.get(R.string.author) + " " + Engine.instance.gwcfile.getAuthor();
    		mDataTextMinorRight = Engine.instance.gwcfile.getVersion();
    	} else {
    		mDataTextMajor = "{ERROR no engine or cartridge instance}"; 
    		mDataImageLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
    	}
   			
    	// ----
    	mStyleBackground = new StyleBasics( View.VISIBLE, 0xFF24699c ); // wherigo-blue 
    	mStyleTextMajor  = new StyleText( View.VISIBLE, Color.TRANSPARENT, Color.WHITE, 18, Typeface.BOLD );
    	mStyleTextMinor  = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xffdddddd, 12, Typeface.NORMAL );
    	mStyleTextMinorRight  = mStyleTextMinor;
    	mStyleImageLeft  = new StyleImage( View.VISIBLE, Color.TRANSPARENT, 48, 48, null );
    	mStyleCancelButton = null;		
	}
	
	public void onListItemClicked( Activity activity ) {
		return;
	}		
}

