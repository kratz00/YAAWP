package org.yaawp.hmi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Vector;

/*
 * CartridgeListAdapter
 * CartridgeListAdapterItem
 * CartridgeListAdapterItemHeader
 * CartridgeListAdapterItemCartridge
 * 
 * AdapterItem
 * AdapterItemHeader
 * AdapterItemCartridge
 * 
 * 
 * 
 */

public class CartridgeListAdapter extends BaseAdapter {

	private static String TAG = "CartridgeListAdapter";

	Vector<CartridgeListAdapterItem> mAdapterItems; 
    
    private Context context;
       
    /* --------------------------- */
    
	public CartridgeListAdapter(Context context, Vector<CartridgeListAdapterItem> data, View view) {	
		this.mAdapterItems = data;
		this.context = context;
    }
	
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	return true;
    }
    
    @Override
	public int getCount() {
		return mAdapterItems.size();
	}

    @Override
	public Object getItem(int position) {
		return mAdapterItems.get(position); 
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
		View view = mAdapterItems.get(position).createView(context);
		view.forceLayout();
		return view;
	}
	
	/*
	 * public void setTextView02Visible(int visibility, boolean hideIfEmpty) {
	
		this.textView02Visibility = visibility;
		this.textView02HideIfEmpty = hideIfEmpty;
	}
	
	public void setMinHeight(int i) {
		this.minHeight = i;
	}
	
    public void setMultiplyImageSize(float multiplyImageSize) {
		this.multiplyImageSize = multiplyImageSize;
	}
	*/
}




