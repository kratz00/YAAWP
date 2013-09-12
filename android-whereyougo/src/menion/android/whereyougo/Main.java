/*
  * This file is part of WhereYouGo.
  *
  * WhereYouGo is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * WhereYouGo is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with WhereYouGo.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2012 Menion <whereyougo@asamm.cz>
  */ 

package menion.android.whereyougo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import locus.api.android.ActionDisplay.ExtraAction;
import locus.api.android.ActionDisplayPoints;
import locus.api.android.objects.PackWaypoints;
import locus.api.objects.extra.ExtraData;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.gui.dialogs.DialogMain;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.gui.extension.CustomMain;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.gui.location.SatelliteScreen;
import menion.android.whereyougo.guiding.GuidingScreen;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.settings.Loc;
import menion.android.whereyougo.settings.UtilsSettings;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.*;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cz.matejcik.openwig.formats.CartridgeFile;
import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.bl.CartridgeSessionListener;
import org.yaawp.hmi.helper.CartridgeHelper;
import org.yaawp.hmi.helper.ProgressDialogHelper;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;
import org.yaawp.openwig.WUI;
import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.hmi.adapter.CartridgeListAdapter;
import org.yaawp.hmi.activities.YaawpPreferenceActivity;
import org.yaawp.hmi.helper.CartridgeHelper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class Main extends CustomMain implements CartridgeSessionListener {

	private static final String TAG = "Main";
	
    private static final int CARTRIDGE_LIST_UPDATING = 0;
    private static final int CARTRIDGE_LIST_UPDATED = 1;
    private static final int CARTRIDGE_NOT_AVAILABLE = 2;	
	
	public static WUI wui = new WUI();
		
	public static CartridgeSession cartridgeSession = null;
	
	public static CartridgeListAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCartridgeList();
    }
    
    @Override 
    public void UpdatedCartridgeSession( int msgid, CartridgeFile cartridge ) {
        switch( msgid) {
            case CartridgeSessionListener.CARTRIDGE_SESSION_LOADINING:
            	ProgressDialogHelper.Show( "", "Starting Cartridges" ); // TODO use string id
                break;
        }
    }

    public void fetchCartridgeFilesNotification( int msgid ) {
        switch( msgid) {
            case CARTRIDGE_LIST_UPDATING:
            	ProgressDialogHelper.Show( "", "Loading Cartridges" ); 
                break;
            case CARTRIDGE_LIST_UPDATED:
                // invalidateCartridgeList();
            	final ListView listview = (ListView) findViewById(R.id.listView1); 
                adapter = new CartridgeListAdapter( this, YaawpAppData.GetInstance().mWigFiles, null );    
                adapter.setTextView02Visible(View.VISIBLE, false);
                    
                runOnUiThread( new Runnable() {
                    public void run() {
                        listview.setAdapter( adapter );
                    }
                }
                );
                    
        		ProgressDialogHelper.Hide();
                break;
            case CARTRIDGE_NOT_AVAILABLE:   
            	ProgressDialogHelper.Hide();
                UtilsGUI.showDialogInfo(Main.this, 
                                getString(R.string.no_wherigo_cartridge_available,
                                        FileSystem.ROOT, MainApplication.APP_NAME));            
                break;
        }
    }   
    
    private void invalidateCartridgeList() {
    	if ( adapter != null ) {
	    	runOnUiThread( new Runnable() {
	            public void run() {
	            	Logger.i( TAG, "invalidateCartridgeList" );
	            	adapter.notifyDataSetChanged(); 
	            }
	        });
    	}
    }
    
    private void initCartridgeList() {
    	
    	final ListView listview = (ListView) findViewById(R.id.listView1); 
    	
        // set click listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClicked(position);
            }
        }); 
        
        listview.setOnCreateContextMenuListener( new OnCreateContextMenuListener() {
            public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                
                if ( v.getId()==R.id.listView1) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                    CartridgeSession file = new CartridgeSession( YaawpAppData.GetInstance().mWigFiles.get(info.position), null, wui );
                    menu.setHeaderTitle( file.GetCartridge().name );
                    // menu.setHeaderIcon( file.GetCartridge().iconId );
                    
                    
                    menu.add( Menu.NONE, R.string.ctx_menu_send_game, 1, getString(R.string.ctx_menu_send_game) ); 
                    
                    if ( !CartridgeHelper.isPlayAnywhere(file.GetCartridge())) {
                    	menu.add( Menu.NONE, R.string.ctx_menu_show_on_map, 2, getString(R.string.ctx_menu_show_on_map) );
                    }
                    
                    try {
                        if ( file.SaveGameExists() ) {
                            menu.add( Menu.NONE, R.string.ctx_menu_continue_game, 0, getString(R.string.ctx_menu_continue_game) );
                            menu.add( Menu.NONE, R.string.ctx_menu_restart_game, 0, getString(R.string.ctx_menu_restart_game) );
                            menu.add( Menu.NONE, R.string.ctx_menu_del_saved_game, 3, getString(R.string.ctx_menu_del_saved_game) );
                        } else {
                            menu.add( Menu.NONE, R.string.ctx_menu_play, 0, getString(R.string.ctx_menu_play) );
                        }
                        
                        if ( file.LogFileExists() ) {
                            menu.add( Menu.NONE, R.string.ctx_menu_delete_log_file, 5, getString(R.string.ctx_menu_delete_log_file) );
                            menu.add( Menu.NONE, R.string.ctx_menu_send_log_file, 6, getString(R.string.ctx_menu_send_log_file) );
                        } else {
                            
                        }
                    } catch (Exception e) {
                        Logger.e(TAG, "onResume() - create empty saveGame file", e);
                    }
                }
            }
        } ); 
        
             
    }
    
    @Override
    public boolean onContextItemSelected( MenuItem item ) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = item.getItemId();
        
        if ( cartridgeSession != null ) {
            cartridgeSession.EndSession();
        }
        cartridgeSession = new CartridgeSession( YaawpAppData.GetInstance().mWigFiles.get(info.position), this, wui );        
        
        switch( index )
        {
            case R.string.ctx_menu_send_game:
                break;
            case R.string.ctx_menu_show_on_map:
                break;
            case R.string.ctx_menu_continue_game:
                cartridgeSession.Continue();
                break;
            case R.string.ctx_menu_restart_game:
                cartridgeSession.Start();
                break;
            case R.string.ctx_menu_del_saved_game:
                try {
                    cartridgeSession.getSaveFile().delete();
                    invalidateCartridgeList();
                } catch ( Exception e ) {  
                }
                break;
            case R.string.ctx_menu_play:
                cartridgeSession.Start();
                break;
            case R.string.ctx_menu_delete_log_file:
                try {
                    cartridgeSession.getLogFile().delete();
                } catch ( Exception e ) {  
                }                
                break;
            case R.string.ctx_menu_send_log_file:
                break;
            default:
                break;    
        }

        return true;
    }
    
    private void startCartridge() {
        if (cartridgeSession.SaveGameExists()) {
            UtilsGUI.showDialogQuestion(this,
                    R.string.resume_previous_cartridge,
                    new DialogInterface.OnClickListener() {
        
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                    cartridgeSession.Continue();
                }
            }, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                    Logger.w(TAG, "SaveGame - onClick2 ");
                    ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
                }
            });
        } else {
        	ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
        }
    }
          
    private void onListItemClicked(int position) {
        if ( cartridgeSession != null ) {
            cartridgeSession.EndSession();
        }
        cartridgeSession = new CartridgeSession( YaawpAppData.GetInstance().mWigFiles.get(position), this, wui );
        startCartridge();
    }    
	
	@Override
	protected void eventFirstInit() {
    	// call after start actions here
        MainAfterStart.afterStartAction();
    }
	
	@Override
	protected void eventSecondInit() {
	}

	
	@Override
	protected void eventCreateLayout() {
		setContentView(R.layout.layout_main);

		// set title
		((TextView) findViewById(R.id.title_text)).setText(
				MainApplication.APP_NAME);
	}
	
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate( R.menu.menu, menu);
        return true;
    }
	
    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean status = true;
        
        switch (item.getItemId())
        { 
        	case R.id.menu_start:
                Intent intent2 = new Intent( Main.this, YaawpPreferenceActivity.class );
                startActivity(intent2); 
                break;
                
            case R.id.menu_positioning:
                Intent intent02 = new Intent(Main.this, SatelliteScreen.class);
                startActivity(intent02);
                break;
                
            case R.id.menu_map:
                Intent intent = new Intent( Main.this, CartridgeMapActivity.class );
                startActivity(intent);              
                break;
                
			case R.id.menu_preferences:
				UtilsSettings.showSettings(Main.this);
                break; 

            case R.id.menu_info:
                getSupportFragmentManager().
                    beginTransaction().
                    add(new DialogMain(), "DIALOG_TAG_MAIN").
                    commitAllowingStateLoss();
                break;
                
            default:
                status = super.onOptionsItemSelected(item);
                break;
        }
    
        return status;
    }        
    
    @Override
	protected void eventDestroyApp() {
	}
	
	@Override
    public void onResume() {
    	super.onResume();
    	invalidateCartridgeList(); 
    }
	
	@Override
	public void onStart() {
		super.onStart();
		fetchCartridgeFiles();
	}
    
	public static void setBitmapToImageView(Bitmap i, ImageView iv) {
		Logger.w(TAG, "setBitmapToImageView(), " + i.getWidth() + " x " + i.getHeight());
		float width = i.getWidth() - 10;
		float height = (Const.SCREEN_WIDTH / width) * i.getHeight();
	
		if ((height / Const.SCREEN_HEIGHT) > 0.60f) {
			height = 0.60f * Const.SCREEN_HEIGHT;
			width = (height / i.getHeight()) * i.getWidth();
		}
		iv.setMinimumWidth((int) width);
		iv.setMinimumHeight((int) height);
		iv.setImageBitmap(i);
	}
	
	/**
	 * Call activity that guide onto point.
	 * @param activity
	 * @return true if internal activity was called. False if external by intent.
	 */
	public static boolean callGudingScreen(Activity activity) {
		Intent intent = new Intent(activity, GuidingScreen.class);
		activity.startActivity(intent);
		return true;
	}

	@Override
	protected int getCloseValue() {
		return CLOSE_DESTROY_APP_NO_DIALOG;
	}

	@Override
	protected String getCloseAdditionalText() {
		return null;
	}

	@Override
	protected void eventRegisterOnly() {
	}
	
	
    public void fetchCartridgeFiles() {

    	if ( YaawpAppData.GetInstance().mWigFiles.size() > 0 ) {
    		return;
    	}
    	
        new Thread( new Runnable() { 
        	public void run() {
        		
        		fetchCartridgeFilesNotification( CARTRIDGE_LIST_UPDATING );
        		
                // load cartridge files
                File[] files = FileSystem.getFiles(FileSystem.ROOT, "gwc");
                YaawpAppData.GetInstance().mWigFiles.clear(); 
                
                if (files != null) {
                    for (File file : files) {
                        try {
                            // actualFile = file;
                            CartridgeFile cart = CartridgeFile.read(new WSeekableFile(file), new WSaveFile(file));
                            
                            if (cart != null) {               
                                cart.filename = file.getAbsolutePath();
                                YaawpAppData.GetInstance().mWigFiles.add(cart);
                            }
                        } catch (Exception e) {
                            Logger.w(TAG, "updateCartridgeList(), file:" + file + ", e:" + e.toString());
                            ManagerNotify.toastShortMessage(Loc.get(R.string.invalid_cartridge, file.getName()));
                        }
                    }
                }

                if ( YaawpAppData.GetInstance().mWigFiles.size() > 0 ) {
                	fetchCartridgeFilesNotification( CARTRIDGE_LIST_UPDATED );            
                } else {
                	fetchCartridgeFilesNotification( CARTRIDGE_NOT_AVAILABLE );
                }        		
        	}
        } ).start();
    }
   
}