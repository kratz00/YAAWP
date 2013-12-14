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
		item.attach();
		return item;
	}
	
	public void AddItems( Vector<AbstractListItem> items ) {
    	for ( int i=0; i<items.size(); i++ ) {
    		mAllListItems.add( items.get(i) );
    		items.get(i).attach();
    	}    		
	}
	
	public void RemoveAllItems() {
		mListItems.removeAllElements();
		for ( int i=0; i<mAllListItems.size(); i++ ) {
			mAllListItems.get(i).dettach();
    	} 
		mAllListItems.removeAllElements();		
	}
	
	/* --------------------------------------------------------- */
	public AdapterView.OnItemClickListener mListClick = new AdapterView.OnItemClickListener() { // TODO rename member
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Logger.d(TAG, "onItemClick:" + position);
			
			/* BaseAdapter adapter = (BaseAdapter)parent.getAdapter();
			if ( adapter instanceof ListItemAdapter ) {
				((ListItemAdapter) adapter ).onListItemClicked( position );
			}*/
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
	
	/* --------------------------------------------------------- */
	
	public boolean onContextItemSelected( int position, int index ) {
		return mListItems.get(position).onContextItemSelected( mActivity,index);
	}
	

    /* public void onListItemClicked( int position) {       
        mListItems.get(position).onListItemClicked( mActivity );
    }*/
	
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




