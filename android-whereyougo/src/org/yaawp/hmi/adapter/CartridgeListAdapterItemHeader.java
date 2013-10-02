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
    
    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    
    /** visibility of bottom view */
    private int textView02Visibility = View.VISIBLE;
    /** hide bottom view if no text is available */
    private boolean textView02HideIfEmpty = false;
    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
    // rescale image size
    private float multiplyImageSize = 1.0f;		
	
    private String mTitle;
    
    public CartridgeListAdapterItemHeader( String title ) {
    	mTitle = title;
    }    
    
	public LinearLayout createView( Context context ) {

		LinearLayout view = (LinearLayout) LinearLayout.inflate( context, R.layout.list_separator, null);
		
		try {
			
			view.setBackgroundColor(Color.LTGRAY);
			
			View v = view.findViewById(R.id.linear_layout_separator);
			LinearLayout llMain = (LinearLayout) v;
			llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (minHeight != Integer.MIN_VALUE) {
				llMain.setMinimumHeight(minHeight);
			}
	
			TextView tv01 = (TextView) view.findViewById(R.id.linearLayoutSeparatorHeadline);

			
			llMain.setOnClickListener(null);
			llMain.setOnLongClickListener(null);
			llMain.setLongClickable(false);			
			
			// set TextView top
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setTextColor(Color.BLACK);
	
			if ( mTitle == null) {
				tv01.setVisibility(View.GONE);
			} else {
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(mTitle));
			}
			
		
			llMain.setBackgroundColor(Color.TRANSPARENT);
		
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + view + ")", e);
		}
		
		view.forceLayout(); // TODO was macht forceLayout?
		return view;		
	}	
}
