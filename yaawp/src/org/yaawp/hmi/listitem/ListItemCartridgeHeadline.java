package org.yaawp.hmi.listitem;

import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.ListItemUniversalLayout;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.Images;
import cz.matejcik.openwig.Engine;
import org.yaawp.R;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import org.yaawp.hmi.listitem.styles.*;

import android.content.Context;

public class ListItemCartridgeHeadline extends ListItemUniversalLayout {

	public ListItemCartridgeHeadline() {
		super( false, null );	
		
    	mStyleBackground     = new StyleBasics( View.VISIBLE, 0xFF24699c ); // wherigo-blue 
    	mStyleTextMajor      = new StyleText( View.VISIBLE, Color.TRANSPARENT, Color.WHITE, 18, Typeface.BOLD );
    	mStyleTextMinor      = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xffdddddd, 12, Typeface.NORMAL );
    	mStyleTextMajorRight = null;
    	mStyleTextMinorRight = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xffdddddd, 12, Typeface.NORMAL );	
    	mStyleImageLeft      = Styles.mStyleImageLarge;
    	mStyleImageRight     = null;
    	mStyleCancelButton   = null;
    	   	
    	if ( Engine.instance != null && Engine.instance.cartridge != null ) {
    		mDataTextMajor = Engine.instance.cartridge.name; 
    		mDataImageLeft = CartridgeHelper.getIconFrom( Engine.instance.cartridge, R.drawable.ic_launcher );
    		mDataTextMinor = I18N.get(R.string.author) + " " + Engine.instance.gwcfile.getAuthor();
    		mDataTextMinorRight = Engine.instance.gwcfile.getVersion();
    	} else {
    		mDataTextMajor = "{ERROR no engine or cartridge instance}"; 
    		mDataImageLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
    	}    	
	}
		
}

