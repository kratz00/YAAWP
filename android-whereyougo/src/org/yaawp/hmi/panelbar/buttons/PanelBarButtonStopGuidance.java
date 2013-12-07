package org.yaawp.hmi.panelbar.buttons;

import menion.android.whereyougo.utils.A;

import org.yaawp.R;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.helper.I18N;

public class PanelBarButtonStopGuidance extends PanelBarButton {

	public PanelBarButtonStopGuidance() {
		super( I18N.get(R.string.stop_navigate), null );	
	}
		
	public void onClick() {
		A.getGuidingContent().guideStop();	
	}	
}
