package org.yaawp;

import java.io.IOException;

import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.formats.Savegame;
import cz.matejcik.openwig.platform.FileHandle;
import cz.matejcik.openwig.platform.SeekableFile;

public class YCartridge extends CartridgeFile {

	private String filename;
	
	private YCartridge() { }
	
	private YCartridge( String _filename, SeekableFile source, FileHandle savefile ) throws IOException {
		super(source,savefile);
		filename = _filename;
	}
	
	public boolean isPlayAnywhere() {
    	return ( latitude % 360.0 == 0 && longitude % 360.0 == 0);
	}
	
	/** Read the specified file and return a corresponding CartridgeFile object.
	 *
	 * @param source file representing the cartridge
	 * @param savefile save file corresponding to this cartridge
	 * @return a CartridgeFile object corresponding to source
	 * @throws IOException
	 */
	public static YCartridge read ( String filename, SeekableFile source, FileHandle savefile)
	throws IOException {
		YCartridge cf = new YCartridge( filename, source, savefile );
		return cf;
	}	
	
	public String getFilename() {
		return filename;
	} 	
}
