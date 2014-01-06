package org.yaawp.hmi.panelbar.buttons;

import org.yaawp.R;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.maps.mapsforge.CartridgeMapActivity;

import android.app.Activity;
import android.content.Intent;

public class PanelBarButtonShowMap extends PanelBarButton {

	private Activity mActivity = null;
	
	public PanelBarButtonShowMap( Activity activity ) {
		super(  activity.getString(R.string.map), null );	
		mActivity = activity;
	}
	
	public void onClick() {
		try {
	        Intent intent = new Intent( mActivity, CartridgeMapActivity.class );
	        intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" );
	        // TODO intent.putExtra( MAP_CENTER_LATITUDE, x.y );
	        // TODO intent.putExtra( MAP_CENTER_LONGITUDE, x.y );
	        // TODO intent.putExtra( CURRENT_POSITION_AS_MAP_CENTER, false );
	        // TODO map call helper
	        mActivity.startActivity(intent);  
		} catch (Exception e) {
			// TODO Logger.e(TAG, "btn02.click() - unknown problem", e);
		}	
	}	
}
	
