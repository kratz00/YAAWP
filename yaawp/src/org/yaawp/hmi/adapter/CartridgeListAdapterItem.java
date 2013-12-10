package org.yaawp.hmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public abstract class CartridgeListAdapterItem {

	public abstract LinearLayout createView( Context context );

	public boolean createContextMenu( Activity activity, ContextMenu menu ) {       
        return true;
	}
	
	public boolean onContextItemSelected( Activity activity, MenuItem item, int index ) {
		return false;
	}
	
	public void onListItemClicked( Activity activity ) {
		return;
	}	
}
