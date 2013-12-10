package org.yaawp.app;

import java.util.Vector;
import org.yaawp.YCartridge;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.openwig.WUI;

public class YaawpAppData {
	
	private static YaawpAppData mInstance = null;
	
	private YaawpAppData() {
		mCartridges = new Vector<YCartridge>();
		mData = new Vector<AbstractListItem>();
		mCurrentCartridge = null;
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
		
	public Vector<YCartridge> mCartridges;
	
	public Vector<AbstractListItem> mData;
	
	public YCartridge mCurrentCartridge;
	
	public boolean mRefreshCartridgeList;
	
	public WUI mWui;
	
	public boolean mFileSystemCheck = false;
	
	public boolean mFileSystemSpace = false;
}
