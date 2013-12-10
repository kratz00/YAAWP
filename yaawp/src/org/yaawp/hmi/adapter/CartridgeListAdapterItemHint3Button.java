package org.yaawp.hmi.adapter;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartridgeListAdapterItemHint3Button extends CartridgeListAdapterItemHint {

	private static String TAG = "CartridgeListAdapterItemHint3Button";
	
	
    
    public CartridgeListAdapterItemHint3Button( String title, String body ) {
    	super( title, body );
    }	
    
	public LinearLayout createView( Context context ) {
		
		LinearLayout view = (LinearLayout) super.createView( context );
				
		try {
					
			
	
				
					

		} catch (Exception e) {
			Logger.e(TAG, "getView( " + view + " )", e);
		}

		view.forceLayout();
		return view;
	}    
}
