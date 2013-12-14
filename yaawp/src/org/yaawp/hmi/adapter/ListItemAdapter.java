package org.yaawp.hmi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.Vector;

import org.yaawp.hmi.activities.WigMainMenuActivity;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;
import android.app.Activity;

public class ListItemAdapter extends BaseAdapter {

	private static String TAG = "ListItemAdapter";
	private Vector<AbstractListItem> mAllListItems = null; 
	private Vector<AbstractListItem> mListItems = null; 
	
    
    private Context mContext;
    private Activity mActivity;
       
    /* --- ListItemAdapter methods ------------------------ */
    
	public ListItemAdapter( Activity activity ) {	
		super();
		this.mAllListItems = new Vector<AbstractListItem>();
		this.mListItems = new Vector<AbstractListItem>();
		this.mContext = (Context)activity;
		mActivity = activity;
    }
	
	public AbstractListItem AddItem( AbstractListItem item ) {
		mAllListItems.add( item );
		return item;
	}
	
	public void AddItems( Vector<AbstractListItem> items ) {
    	for ( int i=0; i<items.size(); i++ ) {
    		mAllListItems.add( items.get(i) );
    	}    		
	}
	
	/* --------------------------------------------------------- */
	public AdapterView.OnItemClickListener mListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Logger.d(TAG, "onItemClick:" + position);
			
			BaseAdapter adapter = (BaseAdapter)parent.getAdapter();
			if ( adapter instanceof ListItemAdapter ) {
				((ListItemAdapter) adapter ).onListItemClicked( position );
			}
			
		}
	};
	
	
	/* --------------------------------------------------------- */
	
	public boolean createContextMenu( int position, ContextMenu menu ) {       
        return mListItems.get(position).createContextMenu( mActivity, menu );
	}

	public boolean onContextItemSelected( int position, int index ) {
		return mListItems.get(position).onContextItemSelected( mActivity,index);
	}	

    public void onListItemClicked( int position) {       
        mListItems.get(position).onListItemClicked( mActivity );
    }
	
	/* --- methods of BaseAdapter --- */
	
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	return mListItems.get(position).isEnabled();
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
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position,convertView,parent);
    }
	   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AbstractListItem item = mListItems.get(position);
		View view = item.inflate( mContext );
			
		item.layout( mContext, view );
		
		item.SetChangeObserver(this);
		view.forceLayout();
		return view;
	}
	
	@Override
	public void notifyDataSetChanged() {
		mListItems.clear();
		for ( int i=0; i<mAllListItems.size(); i++ ) {
			AbstractListItem item = mAllListItems.get(i);
			if ( item.isValid() == true ) {
				mListItems.add( item );
			}
    	}    		
		super.notifyDataSetChanged();
		return;
	}
}




