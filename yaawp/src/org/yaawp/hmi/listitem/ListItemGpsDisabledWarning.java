package org.yaawp.hmi.listitem;

import java.util.ArrayList;

import org.yaawp.R;
import org.yaawp.hmi.activities.SatelliteActivity;
import org.yaawp.hmi.activities.WigMainMenuActivity;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationEventListener;
import org.yaawp.positioning.LocationState;
import org.yaawp.positioning.SatellitePosition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ListItemGpsDisabledWarning extends ListItem3ButtonsHint implements LocationEventListener {

	private Context mContext;
	
	// TODO deregister ListItem from LocationEventListener!
	
	public ListItemGpsDisabledWarning( Context context ) {
		super( I18N.get(R.string.gps_disabled),
				"Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view.",
				R.drawable.ic_main_gps ) ;
		
		mContext = context;
		
		AddButton( new PanelBarButton( I18N.get(R.string.gps_on), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
						LocationState.setGpsOn( mContext );
						notifyDataSetChanged();
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
		
		enableCancelButton( true ); 
		
    	LocationState.addLocationChangeListener(this);
    	// LocationState.removeLocationChangeListener(this);		
    }
    
	public void onLocationChanged(Location location) {
	}

    public void onStatusChanged(String provider, int state, Bundle extras) {
    	notifyDataSetChanged();
    }
    
    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> sats) {
    	notifyDataSetChanged();
    }
    
    public int getPriority() {
    	return LocationEventListener.PRIORITY_MEDIUM;
    }
    
    // TODO set valid to true if gps available
    public boolean isValid() {
    	return this.mValid && ( LocationState.isActuallyHardwareGpsOn() == false );
    }
    /** Is required when screen turn off */
    public boolean isRequired() {
    	return true;
    }
    
    public String getName() {
    	return "X";
    }
}
