package org.yaawp.maps;

import java.util.Vector;

import org.yaawp.maps.MapWaypoint;

public class MapOverlays {

	static public Vector<MapWaypoint> mWaypoints = new Vector<MapWaypoint>();
	
	static public void clear() {
		mWaypoints.clear();
	}
}
