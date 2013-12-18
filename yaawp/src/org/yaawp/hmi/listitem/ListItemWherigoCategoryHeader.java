package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.AbstractListItem.StyleDefine;
import org.yaawp.utils.Logger;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public abstract class ListItemWherigoCategoryHeader extends ListItem3ButtonsHint {

	protected int mChildElements = 0;
	
	protected String mTitleOpen = null;
	protected String mTitleClose = null;
	protected String mBodyOpen = null;
	protected String mBodyClose = null;
	
	public ListItemWherigoCategoryHeader() {
		super("","",false,null,null);
		setSelectable(true);
		mStyleCancelButton = null;
    	mStyleBackground = new StyleDefine( ListItemColor.DARK_GRAY ); 			
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		mOpen = !mOpen;
		this.notifyDataSetChanged();
		return;
	}	
	
	@Override
	public void layout( Context context, View view  ) {
		if ( mOpen == true ) {
			layoutOpen( context, view );
		} else {
			layoutClose( context, view );
		}
	}	
	
	public void layoutOpen( Context context, View view  ) {
		mTitle = mTitleOpen;
		mBody = mBodyOpen;		
		super.layout( context, view );
	}
	
	public void layoutClose( Context context, View view  ) {
		mTitle = mTitleClose;
		mBody = mBodyClose;		
		super.layout( context, view );
	}	
	
	public boolean isOpen() {
		return mOpen;
	}
	
	public void refresh() {
	}
}