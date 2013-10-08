package org.yaawp.utils;

import java.io.File;

import java.util.Vector;

import org.yaawp.YCartridge;
import org.yaawp.app.FetchCartridgeListener;
import org.yaawp.app.YaawpAppData;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;

import android.os.Environment;

public class FileCollector {

	private static final String TAG = "FileCollector";
	
	public final static boolean CONTINUE = false;
	public final static boolean ABORT = true;
	
	private File mStartDirectory;
	private FileCollectorFilter mFilter;
	private boolean mRecursive;
	private FileCollectorListener mListener;
	private boolean mAbort;
	
	public FileCollector( File startDirectory, FileCollectorFilter filter, boolean recursive, FileCollectorListener listener ) {
		mStartDirectory = startDirectory;
		mFilter = filter;
		mRecursive = recursive;
		mListener = listener;
	}
	
	public boolean startAsyncronCollecting() {
		mAbort = false;
		Thread thread = new Thread( new Runnable() { 
        	public void run() {
        		startCollecting();
        	}
        } );
		thread.setName( "FileCollectorThread" );
		thread.start();
		return true;
	}

	public void abortCollecting() {
		mAbort = true;
	}
	
	public boolean startCollecting() {
		
		mAbort = false;
		
		if ( mFilter != null ) {
			if ( mListener != null ) {
				mAbort = mListener.Begin( mStartDirectory );
			}
			
			if ( !mAbort ) {
				mFilter.setListener( mListener );
				mAbort = X( mStartDirectory );
				mFilter.setListener( null );
			}
		
			if ( mListener != null ) {
				mListener.End( mAbort );
			}
		}
		return mAbort;
	}
		
	private boolean X( File parent ) {
    	if ( mListener != null ) {
    		mAbort = mListener.Update( parent );
    	} else {
    		mAbort = false;
    	}
		
    	if ( !mAbort ) {
	        String files[] = parent.list(mFilter);
	        
	        if ( mRecursive ) {
		        if (files != null) {
		            for (String filename : files) {
		                try {
		                	File file = new File( parent.getAbsolutePath()+"/"+filename);
		                	
		                	if ( file.isDirectory() ) {
		                		mAbort = X( file );
		                	} else {
		                	}
		                } catch (Exception e) {
		                    // Logger.w(TAG, "updateCartridgeList(), file:" + file + ", e:" + e.toString());
		                    // ManagerNotify.toastShortMessage(Loc.get(R.string.invalid_cartridge, file.getName()));
		                }
		            }
		        }
	        }
    	}
        return mAbort;
	}
}
