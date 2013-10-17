package org.yaawp.maps;

import java.util.Vector;

import org.yaawp.maps.MapPoint;
import org.yaawp.maps.MapWaypoint;
import org.yaawp.maps.mapsforge.MfMapWaypoint;

public class MapOverlays {

	static public Vector<MapWaypoint> mWaypoints = new Vector<MapWaypoint>();
	
	static public Vector<MapPolygon> mPolygons = new Vector<MapPolygon>();
	
	static public void clear() {
		mWaypoints.clear();
		mPolygons.clear();
	}
	
	static public MapPoint X() {
        double latmin=0.0;
        double latmax=0.0;
        double lonmin=0.0;
        double lonmax=0.0;
        
		for (int i = 0; i < mWaypoints.size(); i++) {
			MapWaypoint wpt = mWaypoints.get(i);

			if ( i == 0 ) {
        		latmin = wpt.getLatitude();
        		latmax = wpt.getLatitude();
        		lonmin = wpt.getLongitude();
        		lonmax = wpt.getLongitude();
        	} else {
	        	latmin = Math.min(latmin,wpt.getLatitude());
	        	latmax = Math.max(latmax,wpt.getLatitude());
	        	lonmin = Math.min(lonmin,wpt.getLongitude());
	        	lonmax = Math.max(lonmax,wpt.getLongitude());						
			}
			
		}
        
        double lat = (latmin+90.0  + latmax+90.0 )/2 - 90.0;
        double lon = (lonmin+180.0 + lonmax+180.0)/2 - 180.0;
        
        return new MapPoint( lat, lon );
	}
}
