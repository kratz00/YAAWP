package org.yaawp.hmi.adapter;


import org.yaawp.YCartridge;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;



public class CartridgeComparatorDistanceFar extends CartridgeComparator {
    private static String TAG="CartridgeComparatorDistanceFar";
    
	@Override
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
			result = -1;
		} else if ( dis1 < dis2 ) {
			result = 1;
		} else {
			result = 0;
		}

		return result;
    }
}
