package org.yaawp.hmi.listitem;

import org.yaawp.hmi.listitem.styles.*;

import android.app.Activity;
import android.view.View;


public abstract class ListItemWherigoCategoryHeader extends ListItemUniversalLayout {

	protected int mChildElements = 0;
	
	protected String mTitleOpen = null;
	protected String mTitleClose = null;
	protected String mTitleOpenRight = null;
	protected String mTitleCloseRight = null;	
	protected String mBodyOpen = null;
	protected String mBodyClose = null;

	public ListItemWherigoCategoryHeader() {
		super( true, null );    	
    	mStyleBackground     = Styles.mStyleBackgroundDarkHolo2; 
    	mStyleTextMajor      = Styles.mStyleTextDarkHoloMajorBold;
    	mStyleTextMinor      = Styles.mStyleTextDarkHoloMinor;
    	mStyleTextMajorRight = Styles.mStyleTextDarkHoloMajor;
    	mStyleTextMinorRight = Styles.mStyleTextDarkHoloMinor;  	
    	mStyleImageLeft      = Styles.mStyleImageLarge;
    	mStyleImageRight     = null;
    	mStyleCancelButton   = null;	       	
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		mOpen = !mOpen;
		this.mObserver.notifyDataSetChanged();
		return;
	}	
	
	@Override
	public void updateView( View view ) {
		if ( mOpen == true ) {
			layoutOpen( view );
		} else {
			layoutClose( view );
		}
	}	
	
	public void layoutOpen( View view ) {
		mDataTextMajor = mTitleOpen;
		mDataTextMajorRight = mTitleOpenRight;
		mDataTextMinor = mBodyOpen;			
		super.updateView( view );
	}
	
	public void layoutClose( View view ) {
		mDataTextMajor = mTitleClose;
		mDataTextMajorRight = mTitleCloseRight;
		mDataTextMinor = mBodyClose;		
		super.updateView( view );
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
