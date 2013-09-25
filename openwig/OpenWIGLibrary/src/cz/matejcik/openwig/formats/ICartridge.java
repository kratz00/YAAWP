package cz.matejcik.openwig.formats;

import java.io.IOException;

public interface ICartridge {

	public String getAuthor();
	
	public String getCode();
	
	public String getDevice();
	
	public String getDescription();
	
	public int getIconId();
	
	public double getLatitude();
	
	public double getLongitude();
	
	public String getMember();
	
	public String getName();
	
	public String getType();
	
	public int getSplashId();

	public String getStartDescription();
	
	public String getUrl();

	public String getVersion();
		
	/** Return the Lua bytecode for this cartridge. */
	public byte[] getBytecode () throws IOException;

	/** Return data of the specified data file. */
	public byte[] getFile (int oid) throws IOException;
	
	/** Returns Savegame object */
	public Savegame getSavegame () throws IOException;
}
