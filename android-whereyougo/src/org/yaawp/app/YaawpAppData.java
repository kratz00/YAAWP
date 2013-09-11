package org.yaawp.app;

import java.util.Vector;

import cz.matejcik.openwig.formats.CartridgeFile;

public class YaawpAppData {
	
	private static YaawpAppData mInstance = null;
	
	private YaawpAppData() {
		mWigFiles = new Vector<CartridgeFile>();
	}
	
	public static YaawpAppData GetInstance() {
		if ( mInstance == null ) {
			mInstance = new YaawpAppData();
		}
		return mInstance;
	}
	
	/* ---------------------------------- */
	
	public Vector<CartridgeFile> mWigFiles;
}
