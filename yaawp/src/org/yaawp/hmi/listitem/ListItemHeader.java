package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.listitem.AbstractListItem.StyleDefine;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemHeader extends AbstractListItem {

	private static String TAG = "ListItemHeader";
    
    private String mTitleLeft;
    private String mTitleRight;
    private Bitmap mBitmap;
    
    public ListItemHeader( String titleLeft, String titleRight, Bitmap bitmap ) {
    	super( R.layout.list_separator, null );
    	mTitleLeft = titleLeft;
    	mTitleRight = titleRight;
    	mBitmap = Images.resizeBitmap( bitmap, 40 );
    }    
    
    @Override
	public boolean isEnabled() {
		return false;
	}    
    
	@Override
	public void layout( Context context, View view  ) {
	
		try {				
		
			ImageView iv01 = (ImageView) view.findViewById(R.id.image_left);
			if ( iv01 != null ) {
				if ( mBitmap != null ) {
					iv01.setImageBitmap(mBitmap);
					iv01.setVisibility(View.VISIBLE);
				} else {
					iv01.setVisibility(View.GONE);
				}
			}
			
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
