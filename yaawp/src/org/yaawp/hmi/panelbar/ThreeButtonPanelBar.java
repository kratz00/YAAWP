package org.yaawp.hmi.panelbar;

import java.util.Vector;


import org.yaawp.R;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;


import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ThreeButtonPanelBar {

	private int BOTTOM_COLOR_A3 = 0xFFDDDDDD;
	private static final int mButtonId[] = { R.id.button_positive, R.id.button_neutral, R.id.button_negative };
	
	private Vector<PanelBarButton> mButton = new Vector<PanelBarButton>();
	private View mView = null;
	
	public ThreeButtonPanelBar( Activity activity ) {
		mView = activity.findViewById(R.id.linear_layout_bottom);
		if ( mView == null ) {
			Logger.e("ThreeButtonPanelBar", "mView == null");
		}
    	// change colors for 3.0+
    	if (Utils.isAndroid30OrMore()) {
    		mView.setBackgroundColor(BOTTOM_COLOR_A3);
    	}   		
    	mView.setVisibility( View.GONE );		
	}
	
	public ThreeButtonPanelBar( View view ) {
		mView = view.findViewById(R.id.linear_layout_bottom);
		if ( mView == null ) {
			Logger.e("ThreeButtonPanelBar", "mView == null");
		}		
    	// change colors for 3.0+
    	if (Utils.isAndroid30OrMore()) {
    		mView.setBackgroundColor(BOTTOM_COLOR_A3);
    	}   		
    	mView.setVisibility( View.GONE );
	}
	
	public void AddButton( PanelBarButton button ) {
		mButton.add( button );
		updateUI();
	}
	
	public void SetBackgroundColor( int backgroundColor ) {
		BOTTOM_COLOR_A3 = backgroundColor;
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
		
		for ( int i=0; i<3; i++ ) {
			Button btn = (Button) mView.findViewById(mButtonId[i]);
			btn.setVisibility(View.GONE);
			if ( i<mButton.size() ) {
				if ( mButton.get(i).isVisible() ) {
					iButtons++;		
					add( btn, mButton.get(i) );
				}				
			}
		}
    	   	
		if ( iButtons == 0 ) {
			mView.setVisibility( View.GONE );
		} else if ( iButtons == 1 ) {
			mView.setVisibility( View.VISIBLE );
			mView.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.VISIBLE);
			mView.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.VISIBLE);	      		
    	} else {
    		mView.setVisibility( View.VISIBLE );
			mView.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.GONE);
			mView.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.GONE);	    		
    	}
    
	}
}
