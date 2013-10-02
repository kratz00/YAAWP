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
	
	public static void FetchCartridgeX( String root, Vector<YCartridge> cartridges ) {
        // load cartridge files
        File[] files = FileSystem.getFiles(root, "gwc");
        cartridges.clear(); 
        
        if (files != null) {
            for (File file : files) {
                try {
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
