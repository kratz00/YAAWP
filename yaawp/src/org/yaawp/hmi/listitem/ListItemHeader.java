package org.yaawp.hmi.listitem;


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

public class ListItemHeader extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemHeader";
    
    private static final int PADDING = (int) Utils.getDpPixels(2.0f);
    
    private String mTitle;
    private String mTitleRight;
    
    public ListItemHeader( String title, String right ) {
    	super( R.layout.list_separator );
    	mTitle = title;
    	mTitleRight = right;
    }    
    
	public boolean isEnabled() {
		return false;
	}    
    
	public void layout( Context context, View view  ) {
	
		try {
						
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
			
	}	
}
