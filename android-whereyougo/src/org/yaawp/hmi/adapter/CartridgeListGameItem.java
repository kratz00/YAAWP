package org.yaawp.hmi.adapter;

import org.yaawp.hmi.adapter.CartridgeListItem;
import org.yaawp.YCartridge;

public class CartridgeListGameItem implements CartridgeListItem {

	public YCartridge mCartridge = null;
	
	public boolean isSeparator() {
		return false;
	}
	
	public CartridgeListGameItem( YCartridge c ) {
		mCartridge = c;
	}
}
