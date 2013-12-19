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

public class ListItemGpsDisabledWarning extends ListItem3ButtonsHint implements LocationEventListener {

	private Context mContext;
	
	public ListItemGpsDisabledWarning( Context context ) {
		super( I18N.get(R.string.gps_disabled),
				"Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view.",
				true,
				Images.getImageB( R.drawable.ic_main_gps ),null ) ;
		
		mContext = context;
		
    	mStyleCancelButton = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, -1, -1, new View.OnClickListener() {
		    public void onClick(View v) {
		    	ListItemGpsDisabledWarning.this.mValid = false;
		    	ListItemGpsDisabledWarning.this.notifyDataSetChanged();
		    } } );	
		
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
