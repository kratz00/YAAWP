package org.yaawp.hmi.adapter;

import org.yaawp.hmi.adapter.CartridgeListItem;
import cz.matejcik.openwig.formats.CartridgeFile;

public class CartridgeListGameItem implements CartridgeListItem {

	public CartridgeFile mCartridge = null;
	
	public boolean isSeparator() {
		return false;
	}
	
	public CartridgeListGameItem( CartridgeFile cf ) {
		mCartridge = cf;
	}
}
