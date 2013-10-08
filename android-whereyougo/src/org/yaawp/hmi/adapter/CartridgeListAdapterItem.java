package org.yaawp.hmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public interface CartridgeListAdapterItem {

	public LinearLayout createView( Context context );

	public boolean createContextMenu( Activity activity, ContextMenu contextMenu );
	
	public boolean onContextItemSelected( Activity activity, MenuItem item, int index );
	
	public void onListItemClicked( Activity activity );
}
