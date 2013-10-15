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

import menion.android.whereyougo.gui.dialogs.DialogMain;
import menion.android.whereyougo.gui.extension.CustomMain;
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.gui.location.SatelliteScreen;
import menion.android.whereyougo.guiding.GuidingScreen;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.*;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.hmi.helper.ProgressDialogHelper;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.openwig.WUI;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.maps.MapCartridge;
import org.yaawp.maps.MapOverlays;
import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.hmi.adapter.CartridgeListAdapter;
import org.yaawp.hmi.activities.YaawpPreferenceActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.Vector;
import java.util.Arrays;
import org.yaawp.hmi.adapter.*;

import java.util.Collections;

import org.yaawp.utils.FileCollector.FileCollector;
import org.yaawp.utils.FileCollector.FileCollectorFilter;
import org.yaawp.utils.FileCollector.FileCollectorListener;
import org.yaawp.utils.FileCollector.Filter.FileCollectorCartridgeFilter;



public class Main extends CustomMain {

	private static final String TAG = "Main";
	

	
	// public static WUI wui = new WUI();
		
	public static CartridgeListAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCartridgeList();
    }
    
    private void Append( Vector<CartridgeListAdapterItem> first, Vector<CartridgeListAdapterItem> second ) {
    	for ( int i=0; i<second.size(); i++ ) {
    		first.add( second.get(i) );
    	}    	
    }
    
    public void updateCartridgeList() {
        
    	if ( !YaawpAppData.GetInstance().mRefreshCartridgeList ) {
    	 	return;
    	}
    		
    	YaawpAppData.GetInstance().mRefreshCartridgeList = false;
    	
    	final ListView listview = (ListView) findViewById(R.id.listView1); 
    	
    	CartridgeListAdapterItemComparator comparator1 = null;
    	CartridgeListAdapterItemComparator comparator2 = null;
    	
    	String headerRight1 = "";
    	String headerRight2 = "";
    	int sorting = PreferenceUtils.getPrefInteger(R.string.pref_cartridgelist_sorting);
    	switch (sorting)
		{
			case 0:
				comparator1 = new CartridgeComparatorNameAtoZ();
				comparator2 = comparator1;
				headerRight1 = "A-Z";
				headerRight2 = headerRight1;
				break;
			case 1:
				comparator1 = new CartridgeComparatorNameZtoA();
				comparator2 = comparator1;
				headerRight1 = "Z-A";
				headerRight2 = headerRight1;
				break;			
			case 2:
				comparator1 = new CartridgeComparatorDistanceNear();
				comparator2 = new CartridgeComparatorNameAtoZ();
				headerRight1 = "near";
				headerRight2 = "A-Z";
				break;
			case 3:
				comparator1 = new CartridgeComparatorDistanceFar();
				comparator2 = new CartridgeComparatorNameAtoZ();
				headerRight1 = "far";
				headerRight2 = "A-Z";
				break;
		}		

		
    	
    	
    	Vector<CartridgeListAdapterItem> data = YaawpAppData.GetInstance().mData;
    	data.clear();
    	
    	if ( YaawpAppData.GetInstance().mCurrentCartridge != null  ) {
        	data.add( new CartridgeListAdapterItemHeader("Last game","") );
        	data.add( new CartridgeListAdapterItemCartridge( YaawpAppData.GetInstance().mCurrentCartridge ) );   		
    	}
    	
    	/* --------------------------------------------- */
    	
    	if ( PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first ) ) {
        	if ( sorting==2 || sorting==3 ) {
    	    	Vector<CartridgeListAdapterItem> localData2 = new Vector<CartridgeListAdapterItem>();
    	    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    	    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
    	    		if ( cartridge.isPlayAnywhere() ) {
    	    			localData2.add( new CartridgeListAdapterItemCartridge( cartridge ) );
    	    		}
    	    	}
    	    	Collections.sort(localData2, comparator2 );
    	    	data.add( new CartridgeListAdapterItemHeader("Cartridge - location less", headerRight2 ) );
    	    	Append( data, localData2 );
        	}    		
    	}
    	
    	Vector<CartridgeListAdapterItem> localData = new Vector<CartridgeListAdapterItem>();
    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);

    		switch (sorting)
    		{
    			case 0:
    			case 1:
    				localData.add( new CartridgeListAdapterItemCartridge( cartridge ) );
    				break;			
    			case 2:
    			case 3:
    	    		if ( !cartridge.isPlayAnywhere() ) {
    	    			localData.add( new CartridgeListAdapterItemCartridge( cartridge ) );
    	    		}
    				break;
    		}    		
    	}
    	Collections.sort(localData, comparator1 );
    	data.add( new CartridgeListAdapterItemHeader("Cartridge", headerRight1) );
    	Append( data, localData );

       	/* --------------------------------------------- */
    	if ( !(PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first )) ) {
        	if ( sorting==2 || sorting==3 ) {
    	    	Vector<CartridgeListAdapterItem> localData2 = new Vector<CartridgeListAdapterItem>();
    	    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    	    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
    	    		if ( cartridge.isPlayAnywhere() ) {
    	    			localData2.add( new CartridgeListAdapterItemCartridge( cartridge ) );
    	    		}
    	    	}
    	    	Collections.sort(localData2, comparator2 );
    	    	data.add( new CartridgeListAdapterItemHeader("Cartridge - location less", headerRight2 ) );
    	    	Append( data, localData2 );
        	}    		
    	}   

    	/* --------------------------------------------- */
    	
        adapter = new CartridgeListAdapter( this, data, null );    
  
        runOnUiThread( new Runnable() {
                public void run() {
                    listview.setAdapter( adapter );
                }
            }
        );    	
    }
    
    class CartridgeCollectorListener implements FileCollectorListener {
    	
		public boolean Begin( final File uri ) {
			ProgressDialogHelper.Show( "Scanning for Cartridges", "" ); 
			return FileCollector.CONTINUE;
		}
		
		public void End( final boolean abort ) {
	    	YaawpAppData.GetInstance().mRefreshCartridgeList = true;
	    	updateCartridgeList();
			ProgressDialogHelper.Hide();
			
			if ( YaawpAppData.GetInstance().mCartridges.size() <= 0 ) {
	            UtilsGUI.showDialogInfo(Main.this, 
	                    getString(R.string.no_wherigo_cartridge_available,
	                            FileSystem.ROOT, MainApplication.APP_NAME));  			
			}
		}
		
		public boolean Update( final File uri ) {
			ProgressDialogHelper.Update( uri.getAbsolutePath() );
			return FileCollector.CONTINUE;
		}
    };
    
    FileCollectorListener mCartridgeCollectorListener = new CartridgeCollectorListener();
    
    
    
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
        
        // set long press listener
        listview.setOnCreateContextMenuListener( new OnCreateContextMenuListener() {
            public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                    
                CartridgeListAdapterItem item = YaawpAppData.GetInstance().mData.get(info.position);
                item.createContextMenu( Main.this, menu );
            }
        } ); 
        
             
    }
    
    @Override
    public boolean onContextItemSelected( MenuItem item ) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = item.getItemId();
               
        CartridgeListAdapterItem itemX = YaawpAppData.GetInstance().mData.get(info.position);  
        boolean status = itemX.onContextItemSelected( this, item, index );

        return status;
    }
            
    private void onListItemClicked(int position) {       
        CartridgeListAdapterItem itemX = YaawpAppData.GetInstance().mData.get(position);    
        itemX.onListItemClicked( this );
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
        
            case R.id.menu_positioning:
                Intent intent02 = new Intent(Main.this, SatelliteScreen.class);
                startActivity(intent02);
                break;
                
            case R.id.menu_map:
                Intent intent = new Intent( Main.this, CartridgeMapActivity.class );
                intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" ); 
                
                Drawable defaultMarker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
                MapOverlays.clear();               
                for (int i = 0; i < YaawpAppData.GetInstance().mCartridges.size(); i++) {
                	YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
                	if ( cartridge.isPlayAnywhere() == false ) {
                    	MapCartridge mapCartridge = new MapCartridge( cartridge );
                    	mapCartridge.setMarker( defaultMarker );
                		MapOverlays.mWaypoints.add( mapCartridge );  
                	}

                }
                startActivity(intent);              
                break;
                
			case R.id.menu_preferences:
				Intent intent2 = new Intent( Main.this, YaawpPreferenceActivity.class );
                startActivity(intent2); 				
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
    	fetchCartridgeFiles();
    }
	
	@Override
	public void onStart() {
		super.onStart();
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

    	if ( YaawpAppData.GetInstance().mCartridges.size() > 0 ) {
    	   	updateCartridgeList();
    	} else {

	    	FileCollectorCartridgeFilter filter = new FileCollectorCartridgeFilter( YaawpAppData.GetInstance().mCartridges );	
	    	filter.acceptHiddenFiles(true);

	    	Vector<String> excludePaths = new Vector<String>();
	    	Vector<String> includePaths = new Vector<String>();
	    	boolean fullScan = PreferenceUtils.getPrefBoolean(R.string.pref_scan_external_storage);
	    	
		    	
	    	// include paths
    		if (fullScan) {
    			includePaths.add(FileSystem.getExternalStorageDir());
    			
    	    	if (	PreferenceUtils.getPrefBoolean(R.string.pref_exclude_android_dir)
    	    		&&	PreferenceUtils.getPrefBoolean(R.string.pref_include_dropbox_dir) ) {
    	    		includePaths.add("/mnt/sdcard/Android/data/com.dropbox.android/files/scratch");
    	    	}	
    	    	
    	    	if (PreferenceUtils.getPrefBoolean(R.string.pref_exclude_android_dir)) {
    	    		excludePaths.add("");
    	    	}
    	    	if (PreferenceUtils.getPrefBoolean(R.string.pref_exclude_whereyougo_dir)) {
    	    		excludePaths.add("");
    	    	}    	    	
    			
    	    	filter.acceptHiddenFiles(!PreferenceUtils.getPrefBoolean(R.string.pref_exclude_hidden_dirs));
    	    	
    		} else {
    			includePaths.add(FileSystem.ROOT);
    		}
    		
	    	    	
	    	FileCollector fc = new FileCollector( includePaths, filter, true, mCartridgeCollectorListener );
	    	

	    	YaawpAppData.GetInstance().mCartridges.clear();
	    	fc.startAsyncronCollecting();
    	}
    }
   
}