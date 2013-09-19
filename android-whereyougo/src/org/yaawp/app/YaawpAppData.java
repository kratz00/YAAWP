package org.yaawp.app;

import java.util.Vector;
import cz.matejcik.openwig.formats.CartridgeFile;
import org.yaawp.hmi.adapter.CartridgeListItem;

public class YaawpAppData {
	
	private static YaawpAppData mInstance = null;
	
	private YaawpAppData() {
		// mWigFiles = new Vector<CartridgeFile>();
		mCartridgeListItems = new Vector<CartridgeListItem>();
	}
	
	public static YaawpAppData GetInstance() {
		if ( mInstance == null ) {
			mInstance = new YaawpAppData();
		}
		return mInstance;
	}
	
	/* ---------------------------------- */
	
	// public Vector<CartridgeFile> mWigFiles;
	
	public Vector<CartridgeListItem> mCartridgeListItems;
}
