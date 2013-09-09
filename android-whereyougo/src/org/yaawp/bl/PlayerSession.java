package org.yaawp.bl;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import cz.matejcik.openwig.formats.CartridgeFile;
import android.app.ProgressDialog;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.Main;
import menion.android.whereyougo.WSaveFile;
import menion.android.whereyougo.WSeekableFile;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.settings.Loc;
import menion.android.whereyougo.settings.SettingValues;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import org.yaawp.R;

public class PlayerSession implements Runnable
{
    private static final String TAG = "PlayerSession";
    
    private boolean invalidCartridgeList = true;
    private boolean invalidCartridgeStatus = true;
    private static Vector<CartridgeFile> cartridgeFiles;
    
    PlayerSessionListener _listener = null;
    
    public PlayerSession( PlayerSessionListener listener ) {
        _listener = listener;
    }
    
    public CartridgeFile GetCartridge( int position ) {
        return cartridgeFiles.get( position );
    }
    
    public Vector<CartridgeFile> GetList() {
        return cartridgeFiles;
    }
    
    public void InvalidCartridgeList() {
        invalidCartridgeList = true;
        invalidCartridgeStatus = true;
    }
    public void InvalidCartridgeStatus() {
        invalidCartridgeStatus = true;
    }
    
    public void refresh() {
        if ( invalidCartridgeList || invalidCartridgeStatus ) {
            NotifyListener( PlayerSessionListener.CARTRIDGE_LIST_UPDATING );
            new Thread((Runnable)this).start();   
        }
    }
    
    public boolean isAnyCartridgeAvailable() {
        if (cartridgeFiles == null || cartridgeFiles.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void run() {
        
 
        if ( invalidCartridgeList ) {           
            updateCartridgeList();
            invalidCartridgeStatus = true; // force status update
        }
        
        /* if ( invalidCartridgeStatus ) {
            updateCartrigeStatus();
        } */
        
        invalidCartridgeList = false;
        invalidCartridgeStatus = false;        
        if ( isAnyCartridgeAvailable() ) {
            NotifyListener( PlayerSessionListener.CARTRIDGE_LIST_UPDATED );            
        } else {
            NotifyListener( PlayerSessionListener.CARTRIDGE_NOT_AVAILABLE );
        }        
    }
    
    private void NotifyListener( int msgid ) {
        if ( _listener == null )
            return;
        _listener.UpdatedPlayerSession( msgid );
    }
    
    private void updateCartridgeList() {
        
        // load cartridge files
        File[] files = FileSystem.getFiles(FileSystem.ROOT, "gwc");
        cartridgeFiles = new Vector<CartridgeFile>();
        
        // add cartridges to map
        ArrayList<Waypoint> wpts = new ArrayList<Waypoint>();
        
        File actualFile = null;
        if (files != null) {
            for (File file : files) {
                try {
                    actualFile = file;
                    CartridgeFile cart = CartridgeFile.read(new WSeekableFile(file), new WSaveFile(file));
                    
                    if (cart != null) {
                                        
                        cart.filename = file.getAbsolutePath();
                        
                        Location loc = new Location(TAG);
                        loc.setLatitude(cart.latitude);
                        loc.setLongitude(cart.longitude);
                        Waypoint waypoint = new Waypoint(cart.name, loc);
                        
                        cartridgeFiles.add(cart);
                        wpts.add(waypoint);
                    }
                } catch (Exception e) {
                    Logger.w(TAG, "updateCartridgeList(), file:" + actualFile + ", e:" + e.toString());
                    ManagerNotify.toastShortMessage(Loc.get(R.string.invalid_cartridge, file.getName()));
                }
            }
        }

        if (wpts.size() > 0) {
            // TODO add items on map
        }        
    }
    

    
    /* private void updateCartrigeStatus() {
        
    } */
       
    public void sort( Comparator<CartridgeFile> comparator ) {
        if ( comparator != null ) {
            Collections.sort( cartridgeFiles, comparatorDistance );
        }
    }
    
    public static final Comparator<CartridgeFile> comparatorDistance = new Comparator<CartridgeFile>() {

        final Location actLoc = LocationState.getLocation();
        final Location loc1 = new Location(TAG);
        final Location loc2 = new Location(TAG);
        
        @Override
        public int compare(CartridgeFile object1, CartridgeFile object2) {
            loc1.setLatitude(object1.latitude);
            loc1.setLongitude(object1.longitude);
            loc2.setLatitude(object2.latitude);
            loc2.setLongitude(object2.longitude);
            return (int) (actLoc.distanceTo(loc1) - actLoc.distanceTo(loc2));
        }
    }; 
    
    public static final Comparator<CartridgeFile> comparatorNameAZ = new Comparator<CartridgeFile>() {
        
        @Override
        public int compare(CartridgeFile object1, CartridgeFile object2) {          
            return (int) object1.name.compareTo( object2.name );
        }
    };     
    
    public static final Comparator<CartridgeFile> comparatorNameZA = new Comparator<CartridgeFile>() {
        
        @Override
        public int compare(CartridgeFile object1, CartridgeFile object2) {          
            return (int) object2.name.compareTo( object1.name );
        }
    };    
}
