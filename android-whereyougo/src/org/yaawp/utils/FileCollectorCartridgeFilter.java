package org.yaawp.utils;

import java.io.File;
import java.util.Vector;

import org.yaawp.YCartridge;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;

public class FileCollectorCartridgeFilter extends FileCollectorExtentionFilter {

	private static Vector<YCartridge> mCartridges;
	
	public FileCollectorCartridgeFilter( Vector<YCartridge> cartridges ) {
		super( "gwc" );
		mCartridges = cartridges;
	}
	
	public void setCartridgeStore( Vector<YCartridge> cartridges ) {
		mCartridges = cartridges;
	}
	
	public boolean accept(File dir, String name) {
		boolean status = super.accept( dir, name );
		if ( status ) {
			File file;
	        try {
	        	file = new File(dir.getAbsolutePath()+"/"+name);

	        	if ( file.isFile() ) {
		            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
		            
		            if (cart != null) {               
		                mCartridges.add( cart );
		            }
		            status = false;
	        	} else {
	        		status = true;
	        	}
	        } catch (Exception e) {
	        	status = false;
	        }			
		}
        return status;
    }
}
