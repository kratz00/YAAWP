/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.hmi.activities;

import java.io.File;
import java.io.StringReader;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.*;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.yaawp.MainApplication;
import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.hmi.gui.dialogs.DialogMain;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ProgressDialogHelper;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.hmi.listitem.ListItemCartridge;
import org.yaawp.hmi.listitem.ListItemHeader;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.positioning.LocationState;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;

import android.os.Bundle;
import android.os.Debug;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.Vector;
import org.yaawp.hmi.adapter.*;

import java.util.Collections;

import org.yaawp.utils.A;
import org.yaawp.utils.AssetHelper;
import org.yaawp.utils.Const;
import org.yaawp.utils.FileSystem;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;
import org.yaawp.utils.Utils;
import org.yaawp.utils.FileCollector.FileCollector;
import org.yaawp.utils.FileCollector.FileCollectorListener;
import org.yaawp.utils.FileCollector.Filter.FileCollectorCartridgeFilter;



public class CartridgeListActivity extends CustomActivity {

	private static final String TAG = "Main";	
	public static ListItemAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        A.registerMain(this);

        // callSecondInit = false;
        // callRegisterOnly = false;
        // if (A.getApp() == null) { // first app run
//Logger.w(TAG, "onCreate() - init new");

    	// not test some things
        // if ( && ) {
	        // set last known location


    	// TODO eventFirstInit();
    	Settings.setScreenBasic(this);

    	
		setContentView(R.layout.layout_main);

		// set title
		((TextView) findViewById(R.id.title_text)).setText(
				MainApplication.APP_NAME);
	   	
		
    	// call after start actions here
        // TODO MainAfterStart.afterStartAction();
		initCartridgeList();
    }
    
    @Override
    public void onDestroy() {
		// stop debug if any forgotten
		Debug.stopMethodTracing();
		
		// remember value before A.getApp() exist
		boolean clearPackageAllowed = Utils.isPermissionAllowed(Manifest.permission.KILL_BACKGROUND_PROCESSES); 

		// disable highlight
		PreferenceFunc.disableWakeLock();
		// save last known location
		Settings.setLastKnownLocation();
		// disable GPS modul
		LocationState.destroy(this);

		// destroy static references
		A.destroy();
		// call native destroy
		super.onDestroy();

		// remove app from memory			
		if (clearPackageAllowed) {
	    	new Thread(new Runnable() {
				public void run() {
					try {
						ActivityManager aM = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);

						Thread.sleep(1250);
						aM.killBackgroundProcesses(getPackageName());
					} catch (Exception e) {
						Logger.e(TAG, "clearPackageFromMemory()", e);
					}
				}
			}).start();
		}


    }    
    
    private void Append( Vector<AbstractListItem> first, Vector<AbstractListItem> second ) {
    	for ( int i=0; i<second.size(); i++ ) {
    		first.add( second.get(i) );
    	}    	
    }
    
    public void updateCartridgeList() {
        
    	/*ListItemAdapter*/ adapter = new ListItemAdapter( this );  
    	
    	// TODO added warnings, notes and errors
		if ( YaawpAppData.GetInstance().mFileSystemCheck == false ) {
			
		}
		if ( YaawpAppData.GetInstance().mFileSystemSpace == false ) {
			
		}
    	
    	if ( !YaawpAppData.GetInstance().mRefreshCartridgeList ) {
    	 	return;
    	}
    		
    	// YaawpAppData.GetInstance().mRefreshCartridgeList = false;
    	
    	/* --------------------------------------------- */
    	
    	// Vector<AbstractListItem> data = YaawpAppData.GetInstance().mData;
    	// data.clear();    	
    	
    	/* --------------------------------------------- */
    	
    	/*
    	String newsInfo = "";
		newsInfo += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
		newsInfo += AssetHelper.loadAssetString(Settings.getLanguageCode() + "_first.html");
       	newsInfo += "</body></html>";    	
    	
		data.add( new CartridgeListAdapterItemWarning( MainApplication.APP_NAME, newsInfo ) );
		*/
    	
    	/* --------------------------------------------- */
    	
		/*
		int lastVersion = Settings.getApplicationVersionLast();
		final int actualVersion = Settings.getApplicationVersionActual();
		String news = getNewsFromTo(0, actualVersion);
		if (news != null && news.length() > 0) {		
			Settings.setApplicationVersionLast(actualVersion);
			data.add( new CartridgeListAdapterItemWarning( MainApplication.APP_NAME, news ) );
		}
		*/
		/* --------------------------------------------- */

    	if ( LocationState.isActuallyHardwareGpsOn() == false ) {
    		ListItem3ButtonsHint item = new ListItem3ButtonsHint( I18N.get(R.string.gps_disabled) /* TODO I18N */,
    				/* TODO I18N */ "Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view." ) ;
    		
    		item.AddButton( new PanelBarButton( I18N.get(R.string.gps_on), 
					new PanelBarButton.OnClickListener() {
						@Override
						public boolean onClick() {
							LocationState.setGpsOn(CartridgeListActivity.this);
							CartridgeListActivity.this.updateCartridgeList();
							return true;
						}
					}
				));  
    		
    		item.AddButton( new PanelBarButton( I18N.get(R.string.positioning), 
					new PanelBarButton.OnClickListener() {
						@Override
						public boolean onClick() {
			                Intent intent02 = new Intent(CartridgeListActivity.this, SatelliteActivity.class);
			                startActivity(intent02);
							return true;
						}
					}
				)); 
    		
    		adapter.AddItem( item );
    	}
    	
		/* --------------------------------------------- */
		
    	if ( YaawpAppData.GetInstance().mCartridges.size() > 0 ) {
    		addedCartridgeItems();
    		// TODO change it
    		Vector<AbstractListItem> data = YaawpAppData.GetInstance().mData;
    		adapter.AddItems( data );
    	} else {
    		ListItem3ButtonsHint item = new ListItem3ButtonsHint( "Note" /* TODO I18N */,
    				I18N.get(R.string.no_wherigo_cartridge_available,"<i>"+FileSystem.ROOT+"</i>", MainApplication.APP_NAME)); 
    		
    		adapter.AddItem( item );
    	}
    	/* --------------------------------------------- */
    	
    	// Vector<CartridgeListAdapterItem> data = YaawpAppData.GetInstance().mData;
    	
    	/* --------------------------------------------- */
    	  
  
        runOnUiThread( new Runnable() {
                public void run() {
                	final ListView listview = (ListView) findViewById(R.id.listView1); 
                    listview.setAdapter( adapter );
                }
            }
        );     	
    }
    
    private void addedCartridgeItems() {
	  
    	Vector<AbstractListItem> data = YaawpAppData.GetInstance().mData;
    	data.clear();
    	
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

		
    	
    	


    	
    	/*
    	if ( YaawpAppData.GetInstance().mCurrentCartridge != null  ) {
        	data.add( new CartridgeListAdapterItemHeader("Last game","") );
        	data.add( new CartridgeListAdapterItemCartridge( YaawpAppData.GetInstance().mCurrentCartridge ) );   		
    	}
    	*/
    	
    	/* --------------------------------------------- */
    	
    	if ( PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first ) ) {
        	if ( sorting==2 || sorting==3 ) {
    	    	Vector<AbstractListItem> localData2 = new Vector<AbstractListItem>();
    	    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    	    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
    	    		if ( cartridge.isPlayAnywhere() ) {
    	    			localData2.add( new ListItemCartridge( cartridge ) );
    	    		}
    	    	}
    	    	Collections.sort(localData2, comparator2 );
    	    	data.add( new ListItemHeader("Cartridge - location less", headerRight2 ) );
    	    	Append( data, localData2 );
        	}    		
    	}
    	
    	Vector<AbstractListItem> localData = new Vector<AbstractListItem>();
    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);

    		switch (sorting)
    		{
    			case 0:
    			case 1:
    				localData.add( new ListItemCartridge( cartridge ) );
    				break;			
    			case 2:
    			case 3:
    	    		if ( !cartridge.isPlayAnywhere() ) {
    	    			localData.add( new ListItemCartridge( cartridge ) );
    	    		}
    				break;
    		}    		
    	}
    	Collections.sort(localData, comparator1 );
    	data.add( new ListItemHeader("Cartridge", headerRight1) );
    	Append( data, localData );

       	/* --------------------------------------------- */
    	if ( !(PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first )) ) {
        	if ( sorting==2 || sorting==3 ) {
    	    	Vector<AbstractListItem> localData2 = new Vector<AbstractListItem>();
    	    	for ( int i=0; i<YaawpAppData.GetInstance().mCartridges.size(); i++ ) {
    	    		YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
    	    		if ( cartridge.isPlayAnywhere() ) {
    	    			localData2.add( new ListItemCartridge( cartridge ) );
    	    		}
    	    	}
    	    	Collections.sort(localData2, comparator2 );
    	    	data.add( new ListItemHeader("Cartridge - location less", headerRight2 ) );
    	    	Append( data, localData2 );
        	}    		
    	}   

   	
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
                    
                AbstractListItem item = YaawpAppData.GetInstance().mData.get(info.position);
                item.createContextMenu( CartridgeListActivity.this, menu );
            }
        } ); 
        
             
    }
    
    @Override
    public boolean onContextItemSelected( MenuItem item ) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = item.getItemId();
               
        AbstractListItem itemX = YaawpAppData.GetInstance().mData.get(info.position);  
        boolean status = itemX.onContextItemSelected( this, item, index );

        return status;
    }
            
    private void onListItemClicked(int position) {       
        AbstractListItem itemX = YaawpAppData.GetInstance().mData.get(position);    
        itemX.onListItemClicked( this );
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
                Intent intent02 = new Intent(CartridgeListActivity.this, SatelliteActivity.class);
                startActivity(intent02);
                break;
                
            case R.id.menu_map:
            	/* TODO MAP
                Intent intent = new Intent( CartridgeListActivity.this, CartridgeMapActivity.class );
                intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" ); 
                
                Drawable defaultMarker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
                MapOverlays.clear();               
                for (int i = 0; i < YaawpAppData.GetInstance().mCartridges.size(); i++) {
                	YCartridge cartridge = YaawpAppData.GetInstance().mCartridges.get(i);
                	if ( cartridge.isPlayAnywhere() == false ) {
                    	MapWaypoint mapCartridge = MapOverlayFactory.createWaypoint( cartridge );
                    	mapCartridge.setMarker( defaultMarker );
                		MapOverlays.mWaypoints.add( mapCartridge );  
                	}

                }
                startActivity(intent);  
                */            
                break;
                
			case R.id.menu_preferences:
				Intent intent2 = new Intent( CartridgeListActivity.this, YaawpPreferenceActivity.class );
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
		
    public void fetchCartridgeFiles() {

		if ( YaawpAppData.GetInstance().mFileSystemCheck == true && 
				 YaawpAppData.GetInstance().mFileSystemSpace == true) {
				  	
    	
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
		} else {
			updateCartridgeList();
		}
		
    }
    
    private long lastPressedTime;
    
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	Log.i("CartridgeListActivity", "onKeyDown( KeyCode="+keyCode );
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                if (event.getDownTime() - lastPressedTime < Const.DOUBLE_PRESS_HK_BACK_PERIOD) {
                    finish();
                } else {
                	ManagerNotify.toastShortMessage(R.string.double_hk_back_exit_app);
                    lastPressedTime = event.getEventTime();
                }
                return true;
            }
        }
        return false;
    }  
    
    // MainAfterStart
    public static String getNewsFromTo(int lastVersion, int actualVersion) {
//Logger.d(TAG, "getNewsFromTo(" + lastVersion + ", " + actualVersion + "), file:" + 
//		"news_" + (Const.isPro() ? "pro" : "free") + ".xml");
   		String versionInfo = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
      	String data = AssetHelper.loadAssetString("news.xml");
      	if (data == null || data.length() == 0)
      		data = AssetHelper.loadAssetString("news.xml");
    	if (data != null && data.length() > 0) {
            XmlPullParser parser;
            try {
            	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            	parser = factory.newPullParser();
            	parser.setInput(new StringReader(data));

            	int event;
                String tagName;

                boolean correct = false;
                while (true) {
                    event = parser.nextToken();
                    if (event == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                        	String name = parser.getAttributeValue(null, "name");
                        	int id = Utils.parseInt(parser.getAttributeValue(null, "id"));
                        	if (id > lastVersion && id <= actualVersion) {
                        		correct = true;
                        		versionInfo += ("<h4>" + name + "</h4><ul>");
                        	} else {
                        		correct = false;
                        	}
                        } else if (tagName.equalsIgnoreCase("li")) {
                        	if (correct) {
                        		versionInfo += ("<li>" + parser.nextText() + "</li>");
                        	}
                        }
                    } else if (event == XmlPullParser.END_TAG) {
                    	tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                        	if (correct) {
                        		correct = false;
                        		versionInfo += "</ul>";
                        	}
                        } else if (tagName.equals("document")) {
                        	break;
                        }
                    }
                }
            } catch (Exception e) {
            	Logger.e(TAG, "getNews()", e);
            }
    	}
   		
		versionInfo += "</body></html>";
    	return versionInfo;
    }    
}
    