package org.yaawp.hmi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import java.util.Vector;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.utils.Logger;



public class ListItemAdapter extends BaseAdapter {

	private static String TAG = ListItemAdapter.class.getSimpleName();
	
	private Vector<AbstractListItem> mAllListItems = null; 
	private Vector<AbstractListItem> mListItems = null;
	
    protected Activity mActivity;
    
	public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Logger.i( TAG, "onItemClick( position="+position+" )" );
			ListItemAdapter.this.mListItems.get(position).onListItemClicked( mActivity );
		}
	};
	
    // set long press listener
	public AdapterView.OnCreateContextMenuListener mCtxMenu = new OnCreateContextMenuListener() {
        public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            ListItemAdapter.this.mListItems.get(info.position).createContextMenu( mActivity, menu );
        }
    } ;     
    
    /* --- ListItemAdapter methods ------------------------ */
    
	public ListItemAdapter( Activity activity ) {	
		super();
		mAllListItems = new Vector<AbstractListItem>();
		mListItems = new Vector<AbstractListItem>();
		mActivity = activity;
    }
	
	public AbstractListItem AddItem( AbstractListItem item ) {
		mAllListItems.add( item );
		item.mObserver = this;
		item.attach();
		return item;
	}
	
	public void AddItems( Vector<AbstractListItem> items ) {
    	for ( int i=0; i<items.size(); i++ ) {
    		mAllListItems.add( items.get(i) );
    		items.get(i).mObserver = this;
    		items.get(i).attach();
    	}    		
	}
	
	public void RemoveAllItems() {
		mAllListItems.removeAllElements();
		for ( int i=0; i<mAllListItems.size(); i++ ) {
			mAllListItems.get(i).dettach();
			mAllListItems.get(i).mObserver = null;
    	} 
		mAllListItems.removeAllElements();		
	}
	
	/* --------------------------------------------------------- */
	
	
	/* --------------------------------------------------------- */
	
		
	/* --- methods of BaseAdapter --- */
	
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	boolean enabled = mListItems.get(position).isEnabled();
    	Logger.i("", "isEnabled( position="+position+" ) = " + enabled );
    	return enabled;
    }
    
    @Override
	public int getCount() {
		return mListItems.size();
	}

    @Override
	public Object getItem(int position) {
		return mListItems.get(position); 
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 4;
	}
	
	@Override
	public int getItemViewType(int position) {
		AbstractListItem item = mListItems.get(position);
		return item.getViewType();
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		Logger.i( TAG, "getDropDownView( postion="+position+", ...)");
		return getView(position,convertView,parent);
    }
	   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Logger.i( TAG, "getView( postion="+position+", convertView="+convertView+", ...)");
		
		AbstractListItem item = mListItems.get(position);
		View view = convertView; 
		if ( view == null ) {
			view = item.createView( mActivity );
		}
		// view.setFocusableInTouchMode(false);
		// item.setView(view);
		item.updateView(view);
		// view.invalidate();
		// view.forceLayout();
		return view;
	}
	
	@Override
	public void notifyDataSetChanged() {
		Logger.i( TAG, "notifyDataSetChanged()");
		mListItems.removeAllElements();
		for ( int i=0; i<mAllListItems.size(); i++ ) {
			AbstractListItem item = mAllListItems.get(i);
			if ( item.isVisible() ) {
				mListItems.add(item);
			}
		}
		super.notifyDataSetChanged();
		return;
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		Logger.i( TAG, "notifyDataSetInvalidatedd()");
		mListItems.removeAllElements();
		for ( int i=0; i<mAllListItems.size(); i++ ) {
			AbstractListItem item = mAllListItems.get(i);
			if ( item.isVisible() ) {
				mListItems.add(item);
			}
		}		
		super.notifyDataSetInvalidated();
	}
}




