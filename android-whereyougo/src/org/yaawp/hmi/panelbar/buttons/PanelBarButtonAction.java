package org.yaawp.hmi.panelbar.buttons;

import android.app.Activity;

import menion.android.whereyougo.gui.ListActions;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Thing;

public class PanelBarButtonAction extends PanelBarButton {

	Activity mActivity = null;
	Action mAction = null;
	EventTable mEt = null;
	
	public PanelBarButtonAction( Activity activity, final Action action, EventTable et ) {
		super( action.text, null ); 
		mActivity = activity;
		mAction = action;
		mEt = et;
	}
	
	@Override
	public void onClick() {
		ListActions.reset((Thing) mEt);
		ListActions.callAction(mAction);
    	mActivity.finish();	
	}	
	
}
