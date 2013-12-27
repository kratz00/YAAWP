package org.yaawp.hmi.listitem;

import org.yaawp.R;

import android.app.Activity;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.hmi.activities.CartridgeDetailsActivity;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.styles.Styles;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.CartridgeHelper;
import org.yaawp.utils.UtilsFormat;
import android.content.DialogInterface;


import java.io.File;




public class ListItemCartridge extends ListItemUniversalLayout {

	private static String TAG = ListItemCartridge.class.getSimpleName();
    public boolean mIsAnywhere;
    private String mFilename;
    
	public ListItemCartridge( String filename, AbstractListItem parent ) {
		super( false, parent );	
		mFilename = filename;
		File file;
        try {
        	file = new File( filename );

        	if ( file.isFile() ) {
	            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
	            
	    		mDataImageLeft = CartridgeHelper.getIconFromId( cart, cart.getIconId(), R.drawable.icon_gc_wherigo );
	    		mDataTextMajor = cart.getName();
	    		mIsAnywhere = cart.isPlayAnywhere();
	    		
	    		String description = "";
	            if ( !cart.isPlayAnywhere() ) {
	        		Location loc = new Location(TAG);
	        		loc.setLatitude(cart.getLatitude());
	        		loc.setLongitude(cart.getLongitude());

	        		description += (description.length()>0 ? ", " : "") + UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(loc), false);
	            }
	           
	            description += (description.length()>0 ? ", " : "") + cart.getAuthor();
	            description += (description.length()>0 ? ", " : "") + cart.getVersion();		
	            mDataTextMinor = description;
	            		
        	} else {
        		mDataTextMajor = "{error}";
        		mDataTextMinor = filename;	
        	}
        } catch (Exception e) {
    		mDataTextMajor = "{exception:"+e.toString()+"}";
    		mDataTextMinor = filename;     	
        }		
		      
        /*
        if (mCartridge.getSavegame().exists()) { 
        	iconRight = Images.getImageB(android.R.drawable.ic_menu_save);
        }
        */        
        
    	mStyleBackground     = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor      = Styles.mStyleTextMajor;
    	mStyleTextMinor      = Styles.mStyleTextMinor;
    	mStyleTextMajorRight = null;
    	mStyleTextMinorRight = null;    	
    	mStyleImageLeft      = Styles.mStyleImageLarge;
    	mStyleImageRight     = null;
    	mStyleCancelButton   = null;	        
        	        
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
        if ( /*mCartridge.existsSaveFile()*/ true ) {
            UtilsGUI.showDialogQuestion( activity,
                    R.string.resume_previous_cartridge,
                    new DialogInterface.OnClickListener() {
        
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                    CartridgeSession.Continue( mFilename, YaawpAppData.GetInstance().mWui );
                }
            }, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                	CartridgeDetailsActivity.showScreen(mFilename);
                }
            });
        } else {
        	CartridgeDetailsActivity.showScreen(mFilename);
        }
	}	
	
	// updateView() - Daten verändern sich nicht
	

	/*
   
 
	
    @Override
	public boolean createContextMenu( Activity activity, ContextMenu menu ) {
        YCartridge cartridge = mCartridge;
        menu.setHeaderTitle( cartridge.getName() );
                           
        menu.add( Menu.NONE, R.string.ctx_menu_send_game, 1, activity.getString(R.string.ctx_menu_send_game) ); 
        
        if ( !cartridge.isPlayAnywhere()) {
        	menu.add( Menu.NONE, R.string.ctx_menu_show_on_map, 2, activity.getString(R.string.ctx_menu_show_on_map) );
        }
        
        try {
            if ( cartridge.existsSaveFile() ) {
                menu.add( Menu.NONE, R.string.ctx_menu_continue_game, 0, activity.getString(R.string.ctx_menu_continue_game) );
                menu.add( Menu.NONE, R.string.ctx_menu_restart_game, 0, activity.getString(R.string.ctx_menu_restart_game) );
                menu.add( Menu.NONE, R.string.ctx_menu_del_saved_game, 3, activity.getString(R.string.ctx_menu_del_saved_game) );
            } else {
                menu.add( Menu.NONE, R.string.ctx_menu_play, 0, activity.getString(R.string.ctx_menu_play) );
            }
            
            if ( cartridge.existsLogFile() ) {
                menu.add( Menu.NONE, R.string.ctx_menu_delete_log_file, 5, activity.getString(R.string.ctx_menu_delete_log_file) );
                menu.add( Menu.NONE, R.string.ctx_menu_send_log_file, 6, activity.getString(R.string.ctx_menu_send_log_file) );
            } else {
                
            }
        } catch (Exception e) {
            Logger.e(TAG, "onResume() - create empty saveGame file", e);
        }
        
        return true;
	}
	
	@Override
	public boolean onContextItemSelected( Activity activity, int index ) {
        YCartridge cartridge = mCartridge;   
        
        switch( index )
        {
            case R.string.ctx_menu_send_game:
                break;
            case R.string.ctx_menu_show_on_map:
            	/* TODO MAP
                Intent intent = new Intent( activity, CartridgeMapActivity.class );
                intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" );
                Drawable defaultMarker = activity.getResources().getDrawable(R.drawable.icon_gc_wherigo);
                MapOverlays.clear();
            	MapWaypoint mapCartridge = MapOverlayFactory.createWaypoint( cartridge );
            	mapCartridge.setMarker( defaultMarker );
        		MapOverlays.mWaypoints.add( mapCartridge );                  
                activity.startActivity(intent);     
                */    
/*	
                break;
            case R.string.ctx_menu_continue_game:
                CartridgeSession.Continue( cartridge, YaawpAppData.GetInstance().mWui );
                break;
            case R.string.ctx_menu_restart_game:
                CartridgeSession.Start( cartridge, YaawpAppData.GetInstance().mWui );
                break;
            case R.string.ctx_menu_del_saved_game:
        		try {
                    File file = new File( cartridge.getSaveFileName() );
                    file.delete();
        		} catch( Exception e ) {
                }
                break;
            case R.string.ctx_menu_play:
            	onListItemClicked( activity, cartridge );
                break;
            case R.string.ctx_menu_delete_log_file:
        		try {
                    File file = new File( cartridge.getLogFileName() );
                    file.delete();
        		} catch( Exception e ) {
                }
                break;

            case R.string.ctx_menu_send_log_file:
                break;
            default:
                break;    
        }
        return true;
	}
	
	@Override
    public void onListItemClicked( Activity activity ) {           	
        onListItemClicked( activity, mCartridge );
    }
    
    private void onListItemClicked( Activity activity, final YCartridge cartridge ) {
    	
        
        if ( cartridge.existsSaveFile() ) {
            UtilsGUI.showDialogQuestion( activity,
                    R.string.resume_previous_cartridge,
                    new DialogInterface.OnClickListener() {
        
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                    CartridgeSession.Continue( cartridge, YaawpAppData.GetInstance().mWui );
                }
            }, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                	YaawpAppData.GetInstance().mCurrentCartridge = cartridge;
                    ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
                }
            });
        } else {
        	YaawpAppData.GetInstance().mCurrentCartridge = cartridge;
        	ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
        }
    }    	
  */  
    
}
