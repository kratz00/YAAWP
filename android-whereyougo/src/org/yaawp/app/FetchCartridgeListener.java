package org.yaawp.app;

public interface FetchCartridgeListener {

	public static int FETCH_CARTRIDGES_START  = 0;
	public static int FETCH_CARTRIDGES_END    = 1;
	public static int FETCH_CARTRIDGES_UPDATE = 2;
	public static int FETCH_CARTRIDGES_EMPTY  = 3;
	
	public void UpdateFetchCartridge( int msgid, String msg );	
}
