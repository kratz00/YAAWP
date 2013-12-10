package org.yaawp.hmi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.utils.Utils;
import android.app.Activity;

public class ListItemAdapter extends BaseAdapter {

	private static String TAG = "CartridgeListAdapter";
    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    
    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
	private Vector<AbstractListItem> mListItems = null; 
    
    private Context mContext;
       
    /* --- ListItemAdapter methods ------------------------ */
    
	public ListItemAdapter( Context context /*, View view */ ) {	
		super();
		this.mListItems = new Vector<AbstractListItem>();
		this.mContext = context;
    }
	
	public void AddItem( AbstractListItem item ) {
		mListItems.add( item );
	}
	
	public void AddItems( Vector<AbstractListItem> items ) {
    	for ( int i=0; i<items.size(); i++ ) {
    		mListItems.add( items.get(i) );
    	}    		
	}
	
	
	public boolean createContextMenu( Activity activity, int position, ContextMenu menu ) {       
        return mListItems.get(position).createContextMenu( activity, menu );
	}

	public boolean onContextItemSelected( Activity activity, int position, int index ) {
		return mListItems.get(position).onContextItemSelected(activity,index);
	}	

	
    public void onListItemClicked( Activity activity, int position) {       
        mListItems.get(position).onListItemClicked(activity);
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
		
		view.setPadding(PADDING, PADDING, PADDING, PADDING);
		if (minHeight != Integer.MIN_VALUE) {
			view.setMinimumHeight(minHeight);
		}
		view.setBackgroundColor(Color.TRANSPARENT);
		
		item.layout( mContext, view );
		
		item.SetChangeObserver(this);
		view.forceLayout();
		return view;
	}
	
	@Override
	public void notifyDataSetChanged() {
		Vector<AbstractListItem> listItems = new Vector<AbstractListItem>();
		for ( int i=0; i<mListItems.size(); i++ ) {
			AbstractListItem item = mListItems.get(i);
			if ( item.isValid() == true ) {
				listItems.add( item );
			}
    	}    		
		mListItems = listItems;
		super.notifyDataSetChanged();
		return;
	}
}




