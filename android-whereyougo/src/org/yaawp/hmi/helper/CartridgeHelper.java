package org.yaawp.hmi.helper;

import cz.matejcik.openwig.formats.ICartridge;

public class CartridgeHelper {

	public static boolean isPlayAnywhere( ICartridge cartridge ) {
    	return ( cartridge.getLatitude() % 360.0 == 0 && cartridge.getLongitude() % 360.0 == 0);
	}
	
}
