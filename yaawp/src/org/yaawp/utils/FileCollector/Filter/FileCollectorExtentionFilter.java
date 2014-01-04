package org.yaawp.utils.FileCollector.Filter;

import java.io.File;

import org.yaawp.utils.FileCollector.FileCollectorFilter;
import org.yaawp.utils.FileCollector.FileCollectorListener;

public class FileCollectorExtentionFilter implements FileCollectorFilter {

	String mExtention;
	FileCollectorListener mListener;
	boolean mHidden = true;
	
	public FileCollectorExtentionFilter( String extention ) {
		mExtention = extention;
	}
	
	public void setListener( FileCollectorListener listener ) {
		mListener = listener;
	}
	
	public void acceptHiddenFiles( boolean hidden ) {
		mHidden = hidden;
	}
	
	public boolean accept(File dir, String name) {
		File f;
		boolean status = CONTINUE;
		
		if( ( mHidden == false ) && name.startsWith(".")){
			status = STOP;
		} else if (name.endsWith( mExtention )){
        	status = CONTINUE;
        } else { 
	        try {
	        	f = new File(dir.getAbsolutePath()+"/"+name);
	           	if ( mListener != null ) {
	           		mListener.Update( f );
	           	}   
	           	status = f.isDirectory();
	        } catch (Exception e) {
	        	status = STOP;
	        }
        }
        return status;
    }
}
