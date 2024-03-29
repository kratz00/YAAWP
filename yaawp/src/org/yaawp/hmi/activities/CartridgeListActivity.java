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

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.StringReader;
import java.util.Vector;
import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.hmi.adapter.*;
import org.yaawp.hmi.helper.ProgressDialogHelper;
import org.yaawp.hmi.listitem.ListItemGpsDisabledWarning;
import org.yaawp.positioning.LocationState;
import org.yaawp.preferences.PreferenceFunc;
import org.yaawp.preferences.PreferenceUtils;
import org.yaawp.preferences.Settings;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class CartridgeListActivity extends CustomActivity {

	private static final String TAG = "Main";	
	private ListAdapterCartridges mAdapter = null;
	private ListItemGpsDisabledWarning mGpsDisabledWarning  = null;
	private ListView mCartridgeListView = null;
	private Vector<YCartridge> mCartridges = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        A.registerMain(this);	
		setContentView(R.layout.layout_main);  
				
		mAdapter = new ListAdapterCartridges( this );  
		mGpsDisabledWarning = (ListItemGpsDisabledWarning)mAdapter.AddItem( new ListItemGpsDisabledWarning(this) );

		mCartridges = new Vector<YCartridge>();
		
		mCartridgeListView = new ListView(this);
		mCartridgeListView.setDivider( getResources().getDrawable( R.drawable.list_divider ) );
		mCartridgeListView.setDividerHeight(2);
        mCartridgeListView.setAdapter(mAdapter);
        mCartridgeListView.setOnItemClickListener( mAdapter.mOnItemClickListener );
        mCartridgeListView.setOnCreateContextMenuListener( mAdapter.mCtxMenu );
		
        RelativeLayout contentArea = (RelativeLayout) this.findViewById(R.id.relative_layout_content);
        contentArea.removeAllViews();
        contentArea.addView(mCartridgeListView);        
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
       
    public void updateCartridgeList() {
         	
    	mAdapter.RemoveAllItems();
    	
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
    	mAdapter.AddItem( mGpsDisabledWarning );
		/* --------------------------------------------- */
		
    	mAdapter.AddCartridges( mCartridges );
    		
    	

    	/* --------------------------------------------- */  	  
  
        runOnUiThread( new Runnable() {
                public void run() {
                    mAdapter.notifyDataSetChanged();
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
		}
		
		public boolean Update( final File uri ) {
			ProgressDialogHelper.Update( uri.getAbsolutePath() );
			return FileCollector.CONTINUE;
		}
    };
    
    FileCollectorListener mCartridgeCollectorListener = new CartridgeCollectorListener();
    
    
    @Override
    public boolean onContextItemSelected( MenuItem item ) {
    	/*
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = item.getItemId();
        
        final ListView listview = mCartridgeListView; 
        ListItemAdapter xadapter = (ListItemAdapter)listview.getAdapter();
        // boolean status = xadapter.onContextItemSelected( info.position, index );
         */
        return true;
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
                Intent intent03 = new Intent(CartridgeListActivity.this, AppInfoActivity.class);
                startActivity(intent03);            	
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
				  	
    	
    	if ( mCartridges.size() > 0 ) {
    	   	updateCartridgeList();
    	} else {

	    	FileCollectorCartridgeFilter filter = new FileCollectorCartridgeFilter( mCartridges );	
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
	    	

	    	mCartridges.clear();
	    	fc.startAsyncronCollecting();
    	}
		} else {
			updateCartridgeList();
		}
		
    }
    
    @Override
	public boolean onKeyTwiceDown( int keyCode, KeyEvent event ) {
		Log.i( TAG, "onKeyTwiceDown( KeyCode="+keyCode );
		boolean status = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	    	finish();
			status = true;
		} else {
			status = super.onKeyTwiceDown(keyCode, event);
		}
		return status;
	}
	
    @Override
	public boolean onKeyOnceDown( int keyCode, KeyEvent event ) {
		Log.i( TAG, "onKeyOnceDown( KeyCode="+keyCode );
		boolean status = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			ManagerNotify.toastShortMessage(R.string.double_hk_back_exit_app);
			status = true;
		} else {
			status = super.onKeyOnceDown(keyCode, event);
		}
		return status;
	}    
    

    
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
    