package org.yaawp.hmi.panelbar;

import java.util.Vector;

import menion.android.whereyougo.utils.Utils;

import org.yaawp.R;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;


import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ThreeButtonPanelBar {

	public static final int BOTTOM_COLOR_A3 = 0xFFDDDDDD;
	private static final int mButtonId[] = { R.id.button_positive, R.id.button_neutral, R.id.button_negative };
	
	private Vector<PanelBarButton> mButton = new Vector<PanelBarButton>();
	private Activity mActivity = null;
	
	public ThreeButtonPanelBar( Activity activity ) {
		mActivity = activity;
	}
	
	public void AddButton( PanelBarButton button ) {
		mButton.add( button );
		updateUI();
	}
	
	private void add( final Button btn, final PanelBarButton panelButton ) {
        
        btn.setVisibility(View.VISIBLE);
        btn.setEnabled( panelButton.isEnabled() );
        btn.setText( panelButton.getText() );
        
        btn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	panelButton.onClick();
	        }
        });			
	}
	
	public void updateUI() {
		
		int iButtons = 0;
		View view = mActivity.findViewById(R.id.linear_layout_bottom);
		
		for ( int i=0; i<3; i++ ) {
			Button btn = (Button) view.findViewById(mButtonId[i]);
			btn.setVisibility(View.GONE);
			if ( i<mButton.size() ) {
				if ( mButton.get(i).isVisible() ) {
					iButtons++;		
					add( btn, mButton.get(i) );
				}				
			}
		}
    	
    	// change colors for 3.0+
    	if (Utils.isAndroid30OrMore()) {
    		view.findViewById(R.id.linear_layout_bottom).setBackgroundColor(BOTTOM_COLOR_A3);
    	}   
    	
    	if ( iButtons == 1 ) {
			view.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.VISIBLE);
			view.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.VISIBLE);	      		
    	} else {
			view.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.GONE);
			view.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.GONE);	    		
    	}
    
	}
}
