package org.yaawp.hmi.adapter;


import org.yaawp.R;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartridgeListAdapterItemHeader implements CartridgeListAdapterItem {

	private static String TAG = "CartridgeListAdapterItemHeader";
    
    private static final int PADDING = (int) Utils.getDpPixels(2.0f);
    
    private String mTitle;
    private String mTitleRight;
    
    public CartridgeListAdapterItemHeader( String title, String right ) {
    	mTitle = title;
    	mTitleRight = right;
    }    
    
	public boolean createContextMenu( Activity activity, ContextMenu menu ) {
		return true;
	}
	
	public boolean onContextItemSelected( Activity activity, MenuItem item, int index ) {
		return true;
	}
	
	public void onListItemClicked( Activity activity ) {
		return;
	}    
    
	public LinearLayout createView( Context context ) {

		LinearLayout view = (LinearLayout) LinearLayout.inflate( context, R.layout.list_separator, null);
		
		try {
			
			view.setPadding(PADDING, PADDING, PADDING, PADDING);
			view.setOnClickListener(null);
			view.setOnLongClickListener(null);
			view.setLongClickable(false);			
			view.setBackgroundColor(Color.LTGRAY);
				
			TextView tv = (TextView) view.findViewById(R.id.linearLayoutSeparatorHeadline);
			tv.setBackgroundColor(Color.TRANSPARENT);
			tv.setTextColor(Color.BLACK);
			tv.setVisibility(View.VISIBLE);
			tv.setText(Html.fromHtml(mTitle));
			
			TextView tvR = (TextView) view.findViewById(R.id.linearLayoutSeparatorHeadline2);
			tvR.setBackgroundColor(Color.TRANSPARENT);
			tvR.setTextColor(Color.BLACK);
			tvR.setVisibility(View.VISIBLE);
			tvR.setText(Html.fromHtml(mTitleRight));
		
		
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + view + ")", e);
		}
		
		return view;		
	}	
}
