package org.yaawp.hmi.listitem;

import android.content.Context;
import android.view.View;

import org.yaawp.R;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.hmi.activities.GuidingActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonStopGuidance;
import org.yaawp.utils.A;
import org.yaawp.utils.Images;
import android.graphics.Color;
import org.yaawp.hmi.listitem.styles.*;

public class ListItemGuidanceActive extends ListItem3ButtonsHint implements GuidingListener {

	public Context mContext = null;
	
	public ListItemGuidanceActive( Context context ) {
		super(	/* TODO I18N */ "Guidance active",
				/* TODO I18N */ "Guidance to zone <i>" + A.getGuidingContent().getName() +"</i>",
				true,
				Images.getImageB( R.drawable.ic_main_gps ),null );
		
		mContext = context;
		
    	mStyleCancelButton = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, -1, -1, new View.OnClickListener() {
		    public void onClick(View v) {
		    	ListItemGuidanceActive.this.mValid = false;
		    	ListItemGuidanceActive.this.notifyDataSetChanged();
		    } } );		
		
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
		
		mStyleCancelButton = null;
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
	public boolean isValid() {
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
