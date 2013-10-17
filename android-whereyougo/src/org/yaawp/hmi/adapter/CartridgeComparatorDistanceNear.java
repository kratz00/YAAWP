package org.yaawp.hmi.adapter;

import java.util.Comparator;

import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import org.yaawp.YCartridge;
import org.yaawp.extra.Location;

public class CartridgeComparatorDistanceNear extends CartridgeListAdapterItemComparator {
 
	private static String TAG="CartridgeComparatorDistanceNear";
    	
	public int compare(YCartridge c1, YCartridge c2) {
       	
		Location loc1 = new Location(TAG);
		loc1.setLatitude(c1.getLatitude());
		loc1.setLongitude(c1.getLongitude());
		
		Location loc2 = new Location(TAG);
		loc2.setLatitude(c2.getLatitude());
		loc2.setLongitude(c2.getLongitude());

		float dis1 = LocationState.getLocation().distanceTo(loc1);
		float dis2 = LocationState.getLocation().distanceTo(loc2);
    	
		int result = 0;
		if ( dis1 > dis2 ) {
			result = 1;
		} else if ( dis1 < dis2 ) {
			result = -1;
		} else {
			result = 0;
		}
		return result;
    }
}
