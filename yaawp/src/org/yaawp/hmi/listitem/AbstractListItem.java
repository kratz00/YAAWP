package org.yaawp.hmi.listitem;

import org.yaawp.R;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.view.View;

public abstract class AbstractListItem {

	protected boolean mValid = true;
	protected View mView = null;
	protected boolean mVisible = true;
	protected int mLayoutId = -1;
	
	public AbstractListItem( int layoutId ) {
		mLayoutId = layoutId;
	}
	
	public View inflate( Context context ) {
		return (LinearLayout) LinearLayout.inflate( context, mLayoutId , null );
	}
	
	public abstract void layout( Context context, View view  );

	public boolean isEnabled() {
		return true;
	}
	
	public boolean isVisibel() {
		return mVisible;
	}
	
	public boolean isValid() {
		return mValid;
	}
	
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
