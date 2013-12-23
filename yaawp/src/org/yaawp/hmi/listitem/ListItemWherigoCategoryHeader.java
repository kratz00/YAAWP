package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.styles.*;
import org.yaawp.utils.Logger;

import cz.matejcik.openwig.Engine;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public abstract class ListItemWherigoCategoryHeader extends ListItem3ButtonsHint {

	protected int mChildElements = 0;
	
	protected String mTitleOpen = null;
	protected String mTitleClose = null;
	protected String mTitleOpenRight = null;
	protected String mTitleCloseRight = null;	
	protected String mBodyOpen = null;
	protected String mBodyClose = null;

	public ListItemWherigoCategoryHeader() {
		super( true, null );
		mStyleCancelButton = null;
    	mStyleBackground = Styles.mStyleBackgroundDarkGray; 			
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		mOpen = !mOpen;
		// TODO this.notifyDataSetChanged();
		return;
	}	
	
	@Override
	public void updateView() {
		if ( mOpen == true ) {
			layoutOpen();
		} else {
			layoutClose();
		}
	}	
	
	public void layoutOpen() {
		mDataTextMajor = mTitleOpen;
		mDataTextMajorRight = mTitleOpenRight;
		mDataTextMinor = mBodyOpen;			
		super.updateView();
	}
	
	public void layoutClose() {
		mDataTextMajor = mTitleClose;
		mDataTextMajorRight = mTitleCloseRight;
		mDataTextMinor = mBodyClose;		
		super.updateView();
	}	
	
	public boolean isOpen() {
		return mOpen;
	}
	
	public void refresh() {
		int count = getCountChild();
		mTitleOpen = getTitle();
		mTitleClose = getTitle();
	
		if ( count == 0 ) {
			mTitleOpenRight  = "";
			mBodyOpen        = getSubtitle(); // TODO I18N	
			mTitleCloseRight = "";
			mBodyClose       = mBodyOpen;					
		} else {
			mTitleOpenRight  = "" + count;
			mBodyOpen        = "";
			mTitleCloseRight = "" + count;
			mBodyClose       = getSubtitle();				
		}
	}
	
	public int getCountChild() {
		return 0;
	}
	
	public String getTitle() {
		return "???";
	}

	public String getSubtitle() {
		return "???";		
	}	
}
