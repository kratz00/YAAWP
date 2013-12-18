package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public abstract class ListItemWherigoCategoryHeader extends ListItem3ButtonsHint {

	protected boolean mOpen = false;
	
	public ListItemWherigoCategoryHeader() {
		super("","",false,null);
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
		super.layout( context, view );
	}
	
	public void layoutClose( Context context, View view  ) {
		super.layout( context, view );
	}
	
	public boolean isOpen() {
		return mOpen;
	}
}
