package org.yaawp.utils.FileCollector.Filter;

import java.io.File;
import java.util.Vector;

import org.yaawp.YCartridge;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import org.yaawp.utils.Logger;

public class FileCollectorCartridgeFilter extends FileCollectorExtentionFilter {

	private static String TAG = FileCollectorCartridgeFilter.class.getSimpleName();
	
	private static Vector<YCartridge> mCartridges;
	
	public FileCollectorCartridgeFilter( Vector<YCartridge> cartridges ) {
		super( "gwc" );
		mCartridges = cartridges;
	}
		
	public boolean accept(File dir, String name) {
		boolean status = super.accept( dir, name );
		if ( status == FileCollectorExtentionFilter.CONTINUE ) {		
			File file;
	        try {
	        	file = new File(dir.getAbsolutePath()+"/"+name);

	        	if ( file.isFile() ) {
		            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
		            
		            if (cart != null) {               
		                mCartridges.add( cart );
		            }
		            status = STOP;
	        	} else if ( file.isDirectory() ){
	        		status = CONTINUE;
	        	} else {
	        		Logger.e( TAG, "file object is not a file or a directory" );
	        	}
	        } catch (Exception e) {
	        	status = false;
	        }	
	 		
		}
        return status;
    }
}
