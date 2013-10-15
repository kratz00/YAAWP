package org.yaawp.maps;

import org.mapsforge.android.maps.overlay.OverlayItem;

import cz.matejcik.openwig.formats.ICartridge;

public class MapCartridge extends MapWaypoint {

	public MapCartridge( ICartridge cartridge ) {
		super( cartridge.getName(), cartridge.getLatitude(), cartridge.getLongitude() );
	}
}
