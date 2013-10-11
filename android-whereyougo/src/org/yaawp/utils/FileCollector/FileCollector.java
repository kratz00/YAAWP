package org.yaawp.utils.FileCollector;

import java.io.File;
import java.util.Vector;


public class FileCollector {

	private static final String TAG = "FileCollector";
	
	public final static boolean CONTINUE = false;
	public final static boolean ABORT = true;
	
	private Vector<String> mDirectories;
	private FileCollectorFilter mFilter;
	private boolean mRecursive;
	private FileCollectorListener mListener;
	private boolean mAbort;
	
	public FileCollector( String directory, FileCollectorFilter filter, boolean recursive, FileCollectorListener listener ) {
		mDirectories.add(directory);
		mFilter = filter;
		mRecursive = recursive;
		mListener = listener;		
	}
	
	public FileCollector( Vector<String> directories, FileCollectorFilter filter, boolean recursive, FileCollectorListener listener ) {
		mDirectories = directories;
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
			
			for ( int i=0; i<mDirectories.size(); i++ ) {
				File parent = new File( mDirectories.get(i) );
				
				if ( mListener != null ) {
					mAbort = mListener.Begin( parent );
				}
				
				if ( !mAbort ) {
					mFilter.setListener( mListener );
					mAbort = X( parent );
					mFilter.setListener( null );
				}
			
				if ( mListener != null ) {
					mListener.End( mAbort );
				}				
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
