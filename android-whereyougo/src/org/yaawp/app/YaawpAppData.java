package org.yaawp.app;

import java.util.Vector;
import org.yaawp.YCartridge;
import org.yaawp.hmi.adapter.CartridgeListAdapterItem;

public class YaawpAppData {
	
	private static YaawpAppData mInstance = null;
	
	private YaawpAppData() {
		mCartridges = new Vector<YCartridge>();
		mData = new Vector<CartridgeListAdapterItem>();
	}
	
	public static YaawpAppData GetInstance() {
		if ( mInstance == null ) {
			mInstance = new YaawpAppData();
		}
		return mInstance;
	}
	
	/* ---------------------------------- */
		
	public Vector<YCartridge> mCartridges;
	
	public Vector<CartridgeListAdapterItem> mData;
}
