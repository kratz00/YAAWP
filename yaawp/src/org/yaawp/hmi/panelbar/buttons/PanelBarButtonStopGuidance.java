package org.yaawp.hmi.panelbar.buttons;


import org.yaawp.R;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.utils.A;

public class PanelBarButtonStopGuidance extends PanelBarButton {

	public PanelBarButtonStopGuidance() {
		super( I18N.get(R.string.stop_navigate), null );	
	}
		
	public void onClick() {
		A.getGuidingContent().guideStop();	
	}	
}
