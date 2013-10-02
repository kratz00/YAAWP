package org.yaawp.hmi.adapter;

import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

import org.yaawp.R;

import cz.matejcik.openwig.formats.ICartridge;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartridgeListAdapterItemHeader implements CartridgeListAdapterItem {

	private static String TAG = "CartridgeListAdapterItemHeader";
    
    private static final int PADDING = (int) Utils.getDpPixels(2.0f);
    
    private String mTitle;
    
    public CartridgeListAdapterItemHeader( String title ) {
    	mTitle = title;
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
		
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + view + ")", e);
		}
		
		return view;		
	}	
}