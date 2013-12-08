package org.yaawp.guidance;

import org.yaawp.R;
import org.yaawp.positioning.Location;
import org.yaawp.preferences.PreferenceItems;
import org.yaawp.preferences.PreferenceValues;

import android.content.Context;
import android.os.Vibrator;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.ManagerNotify;
import cz.matejcik.openwig.Zone;

public class ZoneGuide extends GuideImpl {

	// private static final String TAG = "ZoneGuide";

	private Zone mZone;
	
	private boolean mAlreadyEntered = false;
	
    public ZoneGuide(Zone zone) {
    	super(zone.name, new Location("Guidance: "+zone.name,zone.bbCenter.latitude,zone.bbCenter.longitude) );
    	mZone = zone;
    	mAlreadyEntered = false;
    }
    
    public boolean actualizeState( Location location ) {
    	boolean bContinue = super.actualizeState( location );
    	if ( mAlreadyEntered == false && mZone.contain == Zone.INSIDE ) {
    		mAlreadyEntered = true; 
    		
    		// issue #54 - acoustical
    		switch ( PreferenceItems.getGuidingSoundType() ) {
    			case PreferenceValues.GuidingWaypointSound.INCREASE_CLOSER:
    			case PreferenceValues.GuidingWaypointSound.BEEP_ON_DISTANCE:
   					playSingleBeep();
    				break;
    			case PreferenceValues.GuidingWaypointSound.CUSTOM_SOUND:
    				playCustomSound();
    				break;
    		}  	
    		
    		// issue #54 - visual
    		ManagerNotify.toastShortMessage(R.string.guidance_zone_entered);
    		
    		// issue #54 - vibration
    		Vibrator v = (Vibrator) A.getMain().getSystemService(Context.VIBRATOR_SERVICE);
    		v.vibrate(50);	
    		
    		// issue #42 
    		bContinue = false;
    	}
    	return bContinue;
    }
    
  
}
