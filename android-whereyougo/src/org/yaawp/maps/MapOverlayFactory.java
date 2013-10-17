package org.yaawp.maps;

import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.formats.ICartridge;

public class MapOverlayFactory  {

	public static MapWaypoint createWaypoint( ICartridge cartridge ) {
		return new MapWaypoint( cartridge.getName(), cartridge.getLatitude(), cartridge.getLongitude() );
	}

	public static MapPolygon createPolygon( Zone zone ) {
		MapPolygon polygon = new MapPolygon();
		
		for( int i=0; i<zone.points.length; i++ ) {
			polygon.addPoint( new MapPoint( zone.points[i].latitude, zone.points[i].longitude ) );
		}
		
		return polygon;
	}

}

