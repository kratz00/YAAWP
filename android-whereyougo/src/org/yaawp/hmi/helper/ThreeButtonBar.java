package org.yaawp.hmi.helper;

import menion.android.whereyougo.utils.Utils;

import org.yaawp.R;

import android.app.Activity;
import android.view.View;
import android.widget.Button;


public class ThreeButtonBar {



	public static final int BOTTOM_COLOR_A3 = 0xFFDDDDDD;
			
    public interface OnClickListener {
    	public boolean onClick(View v);
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
    
    public int getButtonId( int index ) {
    	int btnId=0; 
		switch ( index ) {
			case 0:
				btnId = R.id.button_positive;
				break;
			case 1:
				btnId = R.id.button_negative;
				break;
			case 2:
				btnId = R.id.button_neutral;
				break;					
		}
		return btnId;
    }
    
    public void VisibleButton( Activity activity, int index ) {
    	View view = activity.findViewById(R.id.linear_layout_bottom);
    	view.findViewById( getButtonId(index) ).setVisibility(View.VISIBLE);	  
    }
    
    public void InvisibleButton( Activity activity, int index ) {
    	View view = activity.findViewById(R.id.linear_layout_bottom);
    	view.findViewById( getButtonId(index) ).setVisibility(View.GONE);	  
    }
    
    public void AddButton(Activity activity, 
			String buttonText, final OnClickListener buttonClickListener ) {
		
		int btnId=0; 

		if ( mBottonCount < 3 ) {
			btnId = getButtonId( mBottonCount );
			
			mBottonCount++;
			
			View view = activity.findViewById(R.id.linear_layout_bottom);
			
	        if (buttonText != null && buttonClickListener != null) {
	        	// set button
	        	Button btn = (Button) view.findViewById(btnId);
	        	btn.setText(buttonText);
	            btn.setOnClickListener(new View.OnClickListener() {
	            	public void onClick(View v) {
	            		buttonClickListener.onClick(v);
	            	}
	            });	
	        } else {
	            // if no confirm button just set the visibility to GONE
	            view.findViewById(btnId).setVisibility(View.GONE);
	        }
	        
	        Update(activity);
		}
	}

    

    

    

}
