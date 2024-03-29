package org.yaawp.hmi.listitem;

import android.content.Context;
import android.view.View;

import org.yaawp.R;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.hmi.activities.GuidingActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.ListItemUniversalLayout;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonStopGuidance;
import org.yaawp.utils.A;
import org.yaawp.utils.Images;
import android.graphics.Color;
import org.yaawp.hmi.listitem.styles.*;

public class ListItemGuidanceActive extends ListItemUniversalLayout implements GuidingListener {

	public Context mContext = null;
	
	public ListItemGuidanceActive( Context context ) {
		super( false, null );
		mContext = context;
		
		AddButton( new PanelBarButton( I18N.get(R.string.navigate ), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {									
						GuidingActivity.callGudingScreen( mContext, GuidingActivity.CONTINUE_GUIDANCE_AT_EXIT );
						return true;
					}
				}
			)); 
		
		AddButton( new PanelBarButtonStopGuidance() );  
		
    	mStyleBackground     = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor      = Styles.mStyleTextMajor;
    	mStyleTextMinor      = Styles.mStyleTextMinor;
    	mStyleTextMajorRight = null;
    	mStyleTextMinorRight = null;    	
    	mStyleImageLeft      = Styles.mStyleImageLarge;
    	mStyleImageRight     = null;
    	mStyleCancelButton   = Styles.mStyleCancelButton;	
		
		/* --- */

		mDataTextMajor = /* TODO I18N */ "Guidance active" ;
		mDataTextMinor = /* TODO I18N */ "Guidance to zone <i>" + A.getGuidingContent().getName() +"</i>";
		mDataImageLeft = Images.getImageB( R.drawable.ic_main_gps );		
	}
	
	@Override
	public void attach() {
		A.getGuidingContent().addGuidingListener(this);
	}
	
	@Override
	public void dettach() {
		A.getGuidingContent().removeGuidingListener(this);	
	}		
	
	@Override
	public boolean isVisible() {
		return A.getGuidingContent().isGuiding();
	}
	
	/* --- GuidanceListener ------------------------------------------------ */
	@Override
	public void receiveGuideEvent(Guide guide, String targetName, float azimuthToTarget, double distanceToTarget) {
		
	}
	
	@Override
	public void trackGuideCallRecalculate() {
	}	
	
	@Override
	public void guideStart() {
		notifyDataSetChanged();
	}

	@Override
	public void guideStop() {
		notifyDataSetChanged();
	}	
}
