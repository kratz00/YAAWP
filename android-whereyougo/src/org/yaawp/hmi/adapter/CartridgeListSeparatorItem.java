package org.yaawp.hmi.adapter;

import org.yaawp.hmi.adapter.CartridgeListItem;

public class CartridgeListSeparatorItem implements CartridgeListItem {

	public String mHeadline;
	
	public boolean isSeparator() {
		return true;
	}
	
	public CartridgeListSeparatorItem( String headline ) {
		mHeadline = headline;
	}
	
}
