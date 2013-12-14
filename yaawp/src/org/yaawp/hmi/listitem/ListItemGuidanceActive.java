package org.yaawp.hmi.listitem;

import android.content.Context;
import org.yaawp.R;
import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.guidance.interfaces.GuidingListener;
import org.yaawp.hmi.activities.GuidingActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonStopGuidance;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;

public class ListItemGuidanceActive extends ListItem3ButtonsHint implements GuidingListener {

	public Context mContext = null;
	
	public ListItemGuidanceActive( Context context ) {
		super(	/* TODO I18N */ "Guidance active",
				/* TODO I18N */ "Guidance to zone <i>" + A.getGuidingContent().getName() +"</i>",
				R.drawable.ic_main_gps );
		
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
		
		enableCancelButton( false );
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
