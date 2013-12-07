package org.yaawp.hmi.panelbar.buttons;

import menion.android.whereyougo.utils.A;
import android.app.Activity;
import org.yaawp.R;
import org.yaawp.hmi.activities.GuidingActivity;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;

import org.yaawp.guidance.interfaces.Guide;
import org.yaawp.hmi.helper.I18N;

public class PanelBarButtonGuidance extends PanelBarButton {

	private Guide mGuidanceObject = null;
	private Activity mActivity = null;
	private int mStopBehaviour = GuidingActivity.CONTINUE_GUIDANCE_AT_EXIT;
	private boolean mFinishActivity = false;
	
	public PanelBarButtonGuidance( Activity activity, Guide guidanceObject, boolean finishActivity, boolean stopGuidanceAtExit ) {
		super(  I18N.get(R.string.navigate), null );	
		mActivity = activity;
		mGuidanceObject = guidanceObject;
		if ( stopGuidanceAtExit ) {
			mStopBehaviour = GuidingActivity.STOP_GUIDANCE_AT_EXIT;
		}
		mFinishActivity = finishActivity;
	}
	
	@Override
	public void onClick() {
		A.getGuidingContent().guideStart( mGuidanceObject );
		GuidingActivity.callGudingScreen( mActivity, mStopBehaviour );
		if ( mFinishActivity == true ) {
			mActivity.finish();
		}
	}
	
}
