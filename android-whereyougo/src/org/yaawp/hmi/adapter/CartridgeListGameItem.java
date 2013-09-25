package org.yaawp.hmi.adapter;

import org.yaawp.hmi.adapter.CartridgeListItem;
import cz.matejcik.openwig.formats.ICartridge;

public class CartridgeListGameItem implements CartridgeListItem {

	public ICartridge mCartridge = null;
	
	public boolean isSeparator() {
		return false;
	}
	
	public CartridgeListGameItem( ICartridge c ) {
		mCartridge = c;
	}
}
