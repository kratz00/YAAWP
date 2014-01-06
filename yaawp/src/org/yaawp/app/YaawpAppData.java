package org.yaawp.app;

import java.util.Vector;
import org.yaawp.YCartridge;
import org.yaawp.openwig.WUI;



public class YaawpAppData {
	
	private static YaawpAppData mInstance = null;
	
	private YaawpAppData() {
		mRefreshCartridgeList = true;
		mWui = new WUI();
	}
	
	public static YaawpAppData GetInstance() {
		if ( mInstance == null ) {
			mInstance = new YaawpAppData();
		}
		return mInstance;
	}
	
	/* ---------------------------------- */
			
	public boolean mRefreshCartridgeList;
	
	public WUI mWui;
	
	public boolean mFileSystemCheck = false;
	
	public boolean mFileSystemSpace = false;
}
