package org.yaawp.bl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.yaawp.openwig.WLocationService;
import org.yaawp.openwig.WUI;
import org.yaawp.R;
import org.yaawp.preferences.PreferenceUtils;

import menion.android.whereyougo.settings.SettingValues;
import menion.android.whereyougo.utils.Logger;
import cz.matejcik.openwig.Engine;
import org.yaawp.YCartridge;

public class CartridgeSession
{
    private static final String TAG = "CartridgeSession";
    
       
    public static WLocationService wLocationService = new WLocationService();
    
    
    
    private CartridgeSession() {
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
