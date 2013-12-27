package org.yaawp.bl;

import java.io.File;
import java.io.FileOutputStream;

import org.yaawp.app.YaawpAppData;
import org.yaawp.openwig.WLocationService;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import org.yaawp.openwig.WUI;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.Logger;
import org.yaawp.utils.UtilsFormat;


import cz.matejcik.openwig.Engine;

import org.yaawp.R;
import org.yaawp.YCartridge;

public class CartridgeSession
{
    private static final String TAG = "CartridgeSession";
    
       
    public static WLocationService wLocationService = new WLocationService();
    
    
    
    private CartridgeSession() {
    }
    
    static public boolean Start( String filename, WUI _wui ) {
    	
		File file;
        try {
        	file = new File( filename );

        	if ( file.isFile() ) {
	            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
	            return Start( cart, _wui );
        	} else {
        		Logger.e( TAG, filename+" is not a file" ); 
        	}
        } catch (Exception e) {
        	Logger.e( TAG, "", e );       	
        }	    
        
        return false;
    }
    
    static public boolean Start( YCartridge cartridge, WUI _wui ) { // this method replaces Main.loadCartridge
        
    	if ( cartridge == null ) {
            return false;
        }   
            	
        try {
            File fileLog = new File( cartridge.getLogFileName() );
            if (!fileLog.exists()) {
                fileLog.createNewFile();
            }
            
            File fileSave = new File( cartridge.getSaveFileName() );
            if ( fileSave.exists() ) {
                fileSave.delete();
            }              
            
            FileOutputStream log = new FileOutputStream(fileLog);
            if ( _wui != null ) {
            	_wui.loadCartridge();
            }

            Engine.newInstance( cartridge, log, _wui, wLocationService).start();
        } catch (Exception e) {
            Logger.e(TAG, "loadCartridge() - create empy saveGame file", e);
        }
        
        return true;
    }
    
    static public boolean Continue( String filename, WUI _wui ) {
    	
		File file;
        try {
        	file = new File( filename );

        	if ( file.isFile() ) {
	            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
	            return Continue( cart, _wui );
        	} else {
        		Logger.e( TAG, filename+" is not a file" ); 
        	}
        } catch (Exception e) {
        	Logger.e( TAG, "", e );       	
        }	    
        
        return false;
    }    
    
    public static boolean Continue( YCartridge cartridge, WUI _wui ) {
        
    	Logger.w(TAG, "Continue()");
        if ( cartridge == null ) {
            return false;
        }   
        
        try {
        	File fileLog = new File( cartridge.getLogFileName() );
            if (!fileLog.exists()) {
                fileLog.createNewFile();
            }
            
            FileOutputStream log = new FileOutputStream(fileLog);
            if ( _wui != null ) {
            	_wui.loadCartridge();
            }
            
            Engine.newInstance( cartridge, log, _wui, wLocationService).restore();
        } catch (Exception e) {
            Logger.e(TAG, "restoreCartridge() - create empty saveGame file", e);
        }
        
        return true;
    }    
    
}
