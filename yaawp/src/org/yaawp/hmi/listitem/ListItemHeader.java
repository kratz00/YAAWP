package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.utils.Logger;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class ListItemHeader extends AbstractListItem {

	private static String TAG = "ListItemHeader";
    
    private String mTitleLeft;
    private String mTitleRight;
    
    public ListItemHeader( String titleLeft, String titleRight ) {
    	super( R.layout.list_separator );
    	mTitleLeft = titleLeft;
    	mTitleRight = titleRight;
    }    
    
    @Override
	public boolean isEnabled() {
		return false;
	}    
    
	@Override
	public void layout( Context context, View view  ) {
	
		try {				
		
			TextView tvLeft  = (TextView) view.findViewById(R.id.linearLayoutSeparatorHeadline);
			tvLeft.setVisibility(View.VISIBLE);
			tvLeft.setText(Html.fromHtml(mTitleLeft));
			
			TextView tvRight = (TextView) view.findViewById(R.id.linearLayoutSeparatorHeadline2);
			tvRight.setVisibility(View.VISIBLE);
			tvRight.setText(Html.fromHtml(mTitleRight));

		} catch (Exception e) {
			Logger.e(TAG, "getView(" + view + ")", e);
		}
			
	}	
}
