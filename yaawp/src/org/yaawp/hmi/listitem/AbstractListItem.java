package org.yaawp.hmi.listitem;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class AbstractListItem {

	protected boolean mValid = true;
	protected boolean mVisible = true;
	protected int mLayoutId = -1;
	private BaseAdapter mChangeObserver = null;
	
	public AbstractListItem( int layoutId ) {
		mLayoutId = layoutId;
	}
	
	public View inflate( Context context ) {
		return (LinearLayout) LinearLayout.inflate( context, mLayoutId , null );
	}
	
	public abstract void layout( Context context, View view  );

	public void attach() {
		return;
	}
	
	public void dettach() {
		return;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isVisibel() {
		return mVisible;
	}
	
	public boolean isValid() {
		return mValid;
	}
	
	public void setValid( boolean valid ) {
		mValid = valid;
	}
	
	public boolean createContextMenu( Activity activity, ContextMenu menu ) {       
        return true;
	}
	
	public boolean onContextItemSelected( Activity activity, int index ) {
		return false;
	}
	
	public void onListItemClicked( Activity activity ) {
		return;
	}	
	
	public void SetChangeObserver( BaseAdapter adapter ) {
		mChangeObserver = adapter;
	}
	
	protected void notifyDataSetChanged() {
		if ( mChangeObserver != null ) {
			mChangeObserver.notifyDataSetChanged();
		}
	}
}
