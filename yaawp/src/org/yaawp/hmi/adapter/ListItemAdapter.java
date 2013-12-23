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
	
	private Vector<AbstractListItem> mListItems = null; 
	
    
    private Context mContext;
    private Activity mActivity;
       
    /* --- ListItemAdapter methods ------------------------ */
    
	public ListItemAdapter( Activity activity ) {	
		super();
		this.mListItems = new Vector<AbstractListItem>();
		this.mContext = (Context)activity;
		mActivity = activity;
    }
	
	public AbstractListItem AddItem( AbstractListItem item ) {
		mListItems.add( item );
		item.attach();
		return item;
	}
	
	public void AddItems( Vector<AbstractListItem> items ) {
    	for ( int i=0; i<items.size(); i++ ) {
    		mListItems.add( items.get(i) );
    		items.get(i).attach();
    	}    		
	}
	
	public void RemoveAllItems() {
		mListItems.removeAllElements();
		for ( int i=0; i<mListItems.size(); i++ ) {
			mListItems.get(i).dettach();
    	} 
		mListItems.removeAllElements();		
	}
	
	/* --------------------------------------------------------- */
	public AdapterView.OnItemClickListener mListClick = new AdapterView.OnItemClickListener() { // TODO rename member
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
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
		Logger.i( TAG, "getDropDownView( postion="+position+", ...)");
		return getView(position,convertView,parent);
    }
	   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.i( TAG, "getView( postion="+position+", ...)");
		
		AbstractListItem item = mListItems.get(position);
		View view = item.getView();
		if ( view == null ) {
			view = item.createView( mContext );
			item.updateView();
		}	
		return view;
	}
	
	@Override
	public void notifyDataSetChanged() {
		Logger.i( TAG, "notifyDataSetChanged()");
		super.notifyDataSetChanged();
		return;
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		Logger.i( TAG, "notifyDataSetInvalidatedd()");
		super.notifyDataSetInvalidated();
	}
}




