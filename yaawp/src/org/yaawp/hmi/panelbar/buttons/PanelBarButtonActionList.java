package org.yaawp.hmi.panelbar.buttons;

import android.app.Activity;

import org.yaawp.hmi.gui.ListActions;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Thing;

public class PanelBarButtonActionList extends PanelBarButton {

	Activity mActivity = null;
	int mActions = 0;
	EventTable mEt = null;
	
	public PanelBarButtonActionList( Activity activity, int actions, EventTable et ) {
		super( "Actions (" + actions + ")", null ); // TODO I18N
		mActivity = activity;
		mActions = actions;
		mEt = et;
	}
	
	@Override
	public void onClick() {
    	ListActions.reset((Thing) mEt);
    	ScreenHelper.activateScreen(ScreenHelper.SCREEN_ACTIONS, mEt);
    	mActivity.finish();	
	}	
	
}
