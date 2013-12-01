package org.yaawp.hmi.helper;

import menion.android.whereyougo.gui.extension.CustomDialog;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.gui.extension.CustomDialog.OnClickListener;
import menion.android.whereyougo.utils.Utils;

import org.yaawp.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ThreeButtonBar {

	public static final int NO_IMAGE = Integer.MIN_VALUE;

	public static final int BOTTOM_COLOR_A3 = 0xFFDDDDDD;
			
    public interface OnClickListener {
    	public boolean onClick(View v, int btn);
    }


    private int mBottonCount = 0;
    
   
    public void Update(Activity activity) {
 	
    	
    	View view = activity.findViewById(R.id.linear_layout_bottom);
    	
    	// change colors for 3.0+
    	if (Utils.isAndroid30OrMore()) {
    		view.findViewById(R.id.linear_layout_bottom).setBackgroundColor(BOTTOM_COLOR_A3);
    	}    	
    	
    	switch( mBottonCount ) {
	    	case 0:
	    		view.findViewById(R.id.linear_layout_bottom).setVisibility(View.GONE);
	    		break;
	    	case 1:
				view.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.VISIBLE);
				view.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.VISIBLE);	    		
	    		view.findViewById(R.id.linear_layout_bottom).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_positive).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_negative).setVisibility(View.GONE);
	    		view.findViewById(R.id.button_neutral).setVisibility(View.GONE);	    		
	    		break;	    	
	    	case 2:
				view.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.GONE);
				view.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.GONE);		    		
	    		view.findViewById(R.id.linear_layout_bottom).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_positive).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_negative).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_neutral).setVisibility(View.GONE);	    		
	    		break;	
	    	case 3:
				view.findViewById(R.id.linear_layout_left_spacer).setVisibility(View.GONE);
				view.findViewById(R.id.linear_layout_right_spacer).setVisibility(View.GONE);		    		
	    		view.findViewById(R.id.linear_layout_bottom).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_positive).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_negative).setVisibility(View.VISIBLE);
	    		view.findViewById(R.id.button_neutral).setVisibility(View.VISIBLE);	    		
	    		break;		    		
    	}
	

    }
    
    public void AddButton(Activity activity, 
			String buttonText, OnClickListener buttonClickListener ) {
		
		int btnId=0; 
		int btnType=0;
		if ( mBottonCount < 3 ) {
			switch ( mBottonCount ) {
				case 0:
					btnId = R.id.button_positive;
					btnType = DialogInterface.BUTTON_POSITIVE;
					break;
				case 1:
					btnId = R.id.button_negative;
					btnType = DialogInterface.BUTTON_NEGATIVE;
					break;
				case 2:
					btnId = R.id.button_neutral;
					btnType = DialogInterface.BUTTON_NEUTRAL;
					break;					
			}
			mBottonCount++;
			
			View view = activity.findViewById(R.id.linear_layout_bottom);
			
	        setSingalButton(view, btnId, btnType, buttonText, buttonClickListener );	
	        
	        Update(activity);
		}
	}

    

    
    private static boolean setSingalButton(View layout, int btnId, final int btnType, 
    		String text, final OnClickListener click) {
        if (text != null && click != null) {
        	// set button
        	Button btn = (Button) layout.findViewById(btnId);
        	btn.setText(text);
            btn.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		click.onClick(v, btnType);
            	}
            });	
            return true;
        } else {
            // if no confirm button just set the visibility to GONE
            layout.findViewById(btnId).setVisibility(View.GONE);
            return false;
        }
    }
    

}
