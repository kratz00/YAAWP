package org.yaawp.maps;

import cz.matejcik.openwig.formats.ICartridge;

public class MapOverlayFactory  {

	public static MapWaypoint createWaypoint( ICartridge cartridge ) {
		return new MapWaypoint( cartridge.getName(), cartridge.getLatitude(), cartridge.getLongitude() );
	}
	
}

