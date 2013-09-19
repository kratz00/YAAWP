package org.yaawp.hmi.helper;

import cz.matejcik.openwig.formats.CartridgeFile;

public class CartridgeHelper {

	public static boolean isPlayAnywhere( CartridgeFile cartridge ) {
    	return ( cartridge.latitude % 360.0 == 0 && cartridge.longitude % 360.0 == 0);
	}
	
}
