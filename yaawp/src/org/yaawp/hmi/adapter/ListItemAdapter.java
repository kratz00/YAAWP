package org.yaawp.hmi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Vector;

import org.yaawp.hmi.listitem.AbstractListItem;

public class ListItemAdapter extends BaseAdapter {

	private static String TAG = "CartridgeListAdapter";

	private Vector<AbstractListItem> mListItems; 
    
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




