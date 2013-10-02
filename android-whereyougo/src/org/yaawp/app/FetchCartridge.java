package org.yaawp.app;

import java.io.File;
import java.io.FilenameFilter;

import menion.android.whereyougo.settings.Loc;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import java.util.Vector;
import android.os.Environment;

public class FetchCartridge implements FilenameFilter {

	private static final String TAG = "FetchCartrdige";
	
	private static FetchCartridgeListener mListener;
	private static Vector<YCartridge> mCartridges;
	private static String mRoot;
		
	public void startFetchingThread( FetchCartridgeListener listener, String root, Vector<YCartridge> cartridges ) {
		mListener = listener;
		mCartridges = cartridges;
		mRoot = root;
		new Thread( new Runnable() { 
        	public void run() {
        		startFetching();
        	}
        } ).start();
	}
	
	private void startFetching() {

		mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_START, "" );
		
		mCartridges.clear(); 
		
		File rootX = Environment.getExternalStorageDirectory();
		// String rootPath= rootX.getPath();
		FetchCartridgeX( rootX );

        if ( YaawpAppData.GetInstance().mCartridges.size() > 0 ) {
        	mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_END,"" );            
        } else {
        	mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_EMPTY,"" );
        }		
	}
	
	public boolean accept(File dir, String name) {
		File f;
        if(name.endsWith(".gwc")){
        return true;
        }

    	if ( mListener != null ) {
    		mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_UPDATE, dir.getAbsolutePath()+"/"+name );
    	}        
        
        f = new File(dir.getAbsolutePath()+"/"+name);

        return f.isDirectory();
    }
	
	private void FetchCartridgeX( File parent ) {
    
    	if ( mListener != null ) {
    		mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_UPDATE, parent.getAbsolutePath() );
    	}
		
        String files[] = parent.list(this);
        
        
        
        if (files != null) {
            for (String filename : files) {
                try {
                	File file = new File( parent.getAbsolutePath()+"/"+filename);
                	if ( mListener != null ) {
                		mListener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_UPDATE, file.getAbsolutePath() );
                	}
                	
                	if ( file.isDirectory() ) {
                		FetchCartridgeX( file );
                	} else {
                	
	                    // actualFile = file;
	                    YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
	                    
	                    if (cart != null) {               
	                        mCartridges.add( cart );
	                    }
                	}
                } catch (Exception e) {
                    // Logger.w(TAG, "updateCartridgeList(), file:" + file + ", e:" + e.toString());
                    // ManagerNotify.toastShortMessage(Loc.get(R.string.invalid_cartridge, file.getName()));
                }
            }
        }

	}
}
