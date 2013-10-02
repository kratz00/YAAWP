package org.yaawp.app;

import java.io.File;

import menion.android.whereyougo.settings.Loc;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import java.util.Vector;

public class FetchCartridge {

	private static final String TAG = "FetchCartrdige";
	
	private static FetchCartridgeListener mListener;
	private static Vector<YCartridge> mCartridges;
	private static String mRoot;
		
	public static void startFetchingThread( FetchCartridgeListener listener, String root, Vector<YCartridge> cartridges ) {
		mListener = listener;
		mCartridges = cartridges;
		mRoot = root;
		new Thread( new Runnable() { 
        	public void run() {
        		startFetching( mListener, mRoot, mCartridges );
        	}
        } ).start();
	}
	
	private static void startFetching( final FetchCartridgeListener listener, String root, Vector<YCartridge> cartridges ) {

		listener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_START, "" );
		
		FetchCartridge.FetchCartridgeX( listener, root, cartridges );

        if ( YaawpAppData.GetInstance().mCartridges.size() > 0 ) {
        	listener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_END,"" );            
        } else {
        	listener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_EMPTY,"" );
        }		
	}
	
	private static void FetchCartridgeX( FetchCartridgeListener listener, String root, Vector<YCartridge> cartridges ) {
    
        File[] files = FileSystem.getFiles(root, "gwc");
        cartridges.clear(); 
        
        if (files != null) {
            for (File file : files) {
                try {
                	if ( listener != null ) {
                		listener.UpdateFetchCartridge( FetchCartridgeListener.FETCH_CARTRIDGES_UPDATE, file.getAbsolutePath() );
                	}
                	
                    // actualFile = file;
                    YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
                    
                    if (cart != null) {               
                        cartridges.add( cart );
                    }
                } catch (Exception e) {
                    Logger.w(TAG, "updateCartridgeList(), file:" + file + ", e:" + e.toString());
                    ManagerNotify.toastShortMessage(Loc.get(R.string.invalid_cartridge, file.getName()));
                }
            }
        }

	}
}
