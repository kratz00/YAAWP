package org.yaawp.maps;

import java.util.Vector;

public class MapPolygon implements MapOverlay {

	protected Vector<MapPoint> mPoints = null;
	
	public MapPolygon() {
		mPoints = new Vector<MapPoint>();
	}
	
	public MapPolygon( MapPolygon polygon ) {
		mPoints = polygon.mPoints;
	}
	
	public void addPoint( MapPoint point ) {
		mPoints.add(point);
	}
}
