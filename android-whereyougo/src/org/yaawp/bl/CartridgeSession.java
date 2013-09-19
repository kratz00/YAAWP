package org.yaawp.bl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.yaawp.bl.CartridgeSessionListener;
import org.yaawp.openwig.WLocationService;
import org.yaawp.openwig.WUI;
import org.yaawp.R;
import org.yaawp.preferences.PreferenceUtils;

import menion.android.whereyougo.settings.SettingValues;
import menion.android.whereyougo.utils.Logger;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.formats.CartridgeFile;

public class CartridgeSession
{
    private static final String TAG = "CartridgeSession";
    
    private CartridgeFile _cartridge = null;
    
    private CartridgeSessionListener _listener = null;
    
    public static WLocationService wLocationService = new WLocationService();
    
    public static WUI _wui = null;
    
    private String originalMember;
    
    public class CFile {
        private File _file;
        
        CFile() {
            _file = null;
        }
        
        CFile( String cartridgeFile, String extention  ) {
            _file = new File( cartridgeFile.substring(0, cartridgeFile.length() - 3) + extention );
        }
        
        public boolean CreateNew() {
            if ( _file == null ) {
                return false;
            }
            try {
                
                return _file.createNewFile(); 
            } catch( Exception e ) {
                return false;
            }            
        }
        
        public boolean Exists2() {
            if ( _file == null ) {
                return false;
            }
            return _file.exists();
        }
        
        public boolean Exists() {
            if ( _file == null ) {
                return false;
            }
            try {
                
                return _file.delete();
            } catch( Exception e ) {
                return false;
            }
        }
        
        public File GetFile() {
            return _file;
        }
    };
    
    public CartridgeSession() {
        _cartridge = null;
        _wui = null;
        _listener = null;
    }
    
    private void NotifyListener( int msgid ) {
        if ( _listener == null )
            return;
        
        _listener.UpdatedCartridgeSession( msgid, _cartridge );
    }
    public CartridgeSession( CartridgeFile cartridge, CartridgeSessionListener listener, WUI wui ) {
        _cartridge = cartridge;
        _wui = wui;
        _listener = listener;
        new CFile( cartridge.filename, "ows" );
        new CFile( cartridge.filename, "gwl" );
    }
    
    public void EndSession() {
        if ( _cartridge != null ) {
            _cartridge.member = originalMember;
            _cartridge = null;
        }
    }
    
    public CartridgeFile GetCartridge() {
        return _cartridge;
    }
    
    protected  void ChangeUsername() {
        if ( _cartridge != null && PreferenceUtils.getPrefBoolean( R.string.pref_wherigo_engine_replace_username ) ) {
            originalMember = _cartridge.member;
            _cartridge.member = PreferenceUtils.getPrefString( R.string.pref_wherigo_engine_username );
        }
    }
    
    public File getSaveFile() throws IOException {
        
        if ( _cartridge == null ) {
            return null;
        }
        
        try {
            File file = new File( _cartridge.filename.substring(0, _cartridge.filename.length() - 3) + "ows");
            return file;
        } catch (SecurityException e) {
            Logger.e(TAG, "getSaveFile()", e);
            return null;
        }
    }   

    public boolean SaveGameExists() {
        try {
            if (getSaveFile().exists()) {
                return true;
            }
        } catch( Exception e ) {
        }
        return false;
    }
    
    public boolean LogFileExists() {
        try {
            if (getLogFile().exists()) {
                return true;
            }
        } catch( Exception e ) {
        }
        return false;
    }    
    
    public File getLogFile() throws IOException {
        
        if ( _cartridge == null ) {
            return null;
        }        
        
        try {
            File file = new File( _cartridge.filename.substring(0, _cartridge.filename.length() - 3) + "gwl");
            return file;
        } catch (SecurityException e) {
            Logger.e(TAG, "getLogFile()", e);
            return null;
        }
    }   
    
    public boolean Start() { // this method replaces Main.loadCartridge
        if ( _cartridge == null ) {
            return false;
        }   
        
        try {
            File fileLog = getLogFile();
            if (!fileLog.exists()) {
                fileLog.createNewFile();
            }
            
            File fileSave = getSaveFile();
            if (!fileSave.exists()) {
                fileSave.delete();
            }              
            
            FileOutputStream log = new FileOutputStream(fileLog);
            NotifyListener( CartridgeSessionListener.CARTRIDGE_SESSION_LOADINING ); // TODO WUI.startProgressDialog();
            ChangeUsername();
            Engine.newInstance( _cartridge, log, _wui, wLocationService).start();
        } catch (Exception e) {
            Logger.e(TAG, "loadCartridge() - create empy saveGame file", e);
        }
        
        return true;
    }
      
    public boolean Continue() {
        Logger.w(TAG, "Restart()");
        if ( _cartridge == null ) {
            return false;
        }   
        
        try {
            File file = getLogFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileOutputStream log = new FileOutputStream(file);
            ChangeUsername();
            NotifyListener( CartridgeSessionListener.CARTRIDGE_SESSION_LOADINING ); // WUI.startProgressDialog();
            Engine.newInstance( _cartridge, log, _wui, wLocationService).restore();
        } catch (Exception e) {
            Logger.e(TAG, "restoreCartridge() - create empty saveGame file", e);
        }
        
        return true;
    }    
    
}
