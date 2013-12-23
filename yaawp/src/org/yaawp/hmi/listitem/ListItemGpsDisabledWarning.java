package org.yaawp.hmi.listitem;

import java.util.ArrayList;

import org.yaawp.R;
import org.yaawp.hmi.activities.SatelliteActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.SatellitePosition;
import org.yaawp.utils.Images;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.graphics.Color;
import org.yaawp.hmi.listitem.styles.*;
import android.content.Context;

public class ListItemGpsDisabledWarning extends ListItem3ButtonsHint implements LocationEventListener {

	private Context mContext;
	
	public ListItemGpsDisabledWarning( Context context ) {
		super( false, null );
		mContext = context;
    }
    
	@Override
	public View createView( Context context ) {
		super.createView(context);

		mDataTextMajor = I18N.get(R.string.gps_disabled);
		mDataTextMinor = "Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view."; // TODO I18N
		mDataImageLeft = Images.getImageB( R.drawable.ic_main_gps );
		
    	mStyleCancelButton = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1, mOnClickListenerCancel );	
		
		AddButton( new PanelBarButton( I18N.get(R.string.gps_on), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
						LocationState.setGpsOn( mContext );
						return true;
					}
				}
			));  		
		
		AddButton( new PanelBarButton( I18N.get(R.string.positioning), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
		                Intent intent02 = new Intent( mContext, SatelliteActivity.class);
		                mContext.startActivity(intent02);
						return true;
					}
				}
			)); 
		
		return mView;
	}	
	
	@Override
	public void updateView() {
		super.updateView();
	}
	
	@Override
	public void attach() {
		LocationState.addLocationChangeListener(this);
	}
	
	@Override
	public void dettach() {
		LocationState.removeLocationChangeListener(this);
	}	
	
	public void onLocationChanged(Location location) {
	}

    public void onStatusChanged(String provider, int state, Bundle extras) {
    	// TODO
    	mView.invalidate();
    }
    
    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> sats) {
    	// TOD
    	mView.invalidate();
    }
    
    public int getPriority() {
    	return LocationEventListener.PRIORITY_MEDIUM;
    }
    
    // TODO set valid to true if gps available
    public boolean isVisible() {
    	if ( mVisible == true ) {
    		return LocationState.isActuallyHardwareGpsOn() == false; 
    	}	
    	return mVisible;
    }
    /** Is required when screen turn off */
    public boolean isRequired() {
    	return true;
    }
    
    public String getName() {
    	return "X";
    }
}
