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

import org.yaawp.R;
import org.yaawp.hmi.gui.dialogs.DialogMain;
import org.yaawp.hmi.helper.I18N;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.listitem.ListItemWherigoInventory;
import org.yaawp.hmi.listitem.ListItemWherigoTasks;
import org.yaawp.hmi.listitem.ListItemWherigoYouSee;
import org.yaawp.hmi.listitem.ListItemWherigoZones;
import org.yaawp.hmi.adapter.ListItemAdapter;

import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.openwig.Refreshable;
import org.yaawp.openwig.WUI;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;
import org.yaawp.utils.Const;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import cz.matejcik.openwig.Engine;

import org.yaawp.hmi.listitem.ListItemGuidanceActive;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;

public class WigMainMenuActivity extends CustomActivity implements Refreshable {

	private static final String TAG = "CartridgeMainMenu";
	
	private ThreeButtonPanelBar mButtonPanelBar;
	// private PanelBarButtonStopGuidance mButtonStopGuidance;
	private PanelBarButtonShowMap mButtonShowMap;
	
	ListItem3ButtonsHint mGpsDisabled = null;
	ListItemGuidanceActive mGuidanceActive = null;
	ListItemWherigoInventory mWherigoInventory = null;
	ListItemWherigoTasks mWherigoTasks = null;
	ListItemWherigoYouSee mWherigoYouSee = null;
	ListItemWherigoZones mWherigoZones = null;
	
	ListItemAdapter mAdapter = null;	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_main); 
		
		/* ------------------------------------------------------------------ */
		mAdapter = new ListItemAdapter(WigMainMenuActivity.this);
		
		
    	mGpsDisabled = new ListItem3ButtonsHint( I18N.get(R.string.gps_disabled) /* TODO I18N */,
				/* TODO I18N */ "Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view.",
				R.drawable.ic_main_gps ) ;
		
    	mGpsDisabled.AddButton( new PanelBarButton( I18N.get(R.string.gps_on), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
						LocationState.setGpsOn(WigMainMenuActivity.this);
						WigMainMenuActivity.this.refresh(); // TODO use a comment method to refresh the listadapter
						return true;
					}
				}
			));  
		
    	mGpsDisabled.AddButton( new PanelBarButton( I18N.get(R.string.positioning), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
		                Intent intent02 = new Intent(WigMainMenuActivity.this, SatelliteActivity.class);
		                startActivity(intent02);
						return true;
					}
				}
			)); 
		
    	mGpsDisabled.enableCancelButton( true );
		
    	mGuidanceActive = new ListItemGuidanceActive(WigMainMenuActivity.this);
    	mWherigoInventory = new ListItemWherigoInventory();
    	mWherigoTasks = new ListItemWherigoTasks();
    	mWherigoYouSee = new ListItemWherigoYouSee();
    	mWherigoZones = new ListItemWherigoZones();	    	
    	
		mAdapter.AddItem( mGpsDisabled );    	
    	mAdapter.AddItem( mGuidanceActive );
    	mAdapter.AddItem( mWherigoInventory );
    	mAdapter.AddItem( mWherigoTasks );
    	mAdapter.AddItem( mWherigoYouSee );
    	mAdapter.AddItem( mWherigoZones );    	
    	mAdapter.notifyDataSetChanged();
    	
		


		
		final ListView lv = (ListView) findViewById(R.id.listView1);			
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener( mAdapter.mListClick );
		
		/* ------------------------------------------------------------------ */
		mButtonPanelBar = new ThreeButtonPanelBar(this);
		mButtonShowMap = new PanelBarButtonShowMap(this);
		
		mButtonPanelBar.AddButton( new PanelBarButton( getString(R.string.save), 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
				    	new Thread(new Runnable() {
							public void run() {
								Engine.instance.store();
							}
						}).start();
				    	return true;
					}
				}
			));			
			
    	mButtonPanelBar.AddButton( mButtonShowMap );
    	mButtonPanelBar.updateUI();
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		refresh();
    	mButtonPanelBar.updateUI();
	}
	
	@Override 
	public void onDestroy() { 
		A.getGuidingContent().guideStop();
		super.onDestroy();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate( R.menu.menu, menu);
        return true;
    }	

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean status = true;
        
        switch (item.getItemId())
        { 
        
            case R.id.menu_positioning:
                Intent intent02 = new Intent( this, SatelliteActivity.class);
                startActivity(intent02);
                break;
                
            case R.id.menu_map:
                Intent intent = new Intent( this, CartridgeMapActivity.class );
                intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" ); 
                startActivity(intent);              
                break;
                
			case R.id.menu_preferences:
				Intent intent2 = new Intent( this, YaawpPreferenceActivity.class );
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
    
	public void refresh() {
		runOnUiThread(new Runnable() {
			public void run() {

				/* TODO make a common usable class to show this 'widget' */
		    	if ( LocationState.isActuallyHardwareGpsOn() == false ) {
		    		mGpsDisabled.setValid( true );
		    	}	
		    	
		    	mAdapter.notifyDataSetChanged();
			}
		});		
	}

	private long lastPressedTime;
	
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	Log.i("GuidingActivity", "onKeyDown( KeyCode="+keyCode );
    	
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getDownTime() - lastPressedTime < Const.DOUBLE_PRESS_HK_BACK_PERIOD) {
    	    	AlertDialog.Builder b = new AlertDialog.Builder(WigMainMenuActivity.this);
    	    	b.setCancelable(true);
    	    	b.setTitle(R.string.question);
    	    	b.setMessage(R.string.save_game_before_exit);
    	    	b.setPositiveButton(R.string.yes, 
    	    			new DialogInterface.OnClickListener() {
    						
    	    		@Override
    	    		public void onClick(DialogInterface dialog, int which) {	    		    
    					Engine.requestSync();
    					// Main.currentCartridge = null;
    					new SaveGameOnExit().execute();
    	    		}
    	    	});
    	    	b.setNeutralButton(R.string.cancel, null);
    	    	b.setNegativeButton(R.string.no, 
    	    			new DialogInterface.OnClickListener() {
    				
    	    		@Override
    	    		public void onClick(DialogInterface dialog, int which) {
    					Engine.kill();
    					// Main.currentCartridge = null;
    					WigMainMenuActivity.this.finish();
    	    		}
    	    	});
    			b.show();
            } else {
            	ManagerNotify.toastShortMessage(R.string.double_hk_back_exit_game);
                lastPressedTime = event.getEventTime();
            }
	    	return true; 
    	} else {
    		super.onKeyDown(keyCode, event);
    	}
	        
        return false;
    }
    
	private class SaveGameOnExit extends AsyncTask<Void, Void, Void> {

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute () {
			dialog = ProgressDialog.show(WigMainMenuActivity.this, null, getString(R.string.working));
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// let thread sleep for a while to be sure that cartridge is saved!
			try {
				while (WUI.saving) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {}
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			try {
				if (dialog != null) {
					dialog.cancel();
					dialog = null;
				}
			} catch (Exception e) {
				Logger.w(TAG, "onPostExecute(), e:" + e.toString());
			}
			Engine.kill();
			WigMainMenuActivity.this.finish();
		}
		
	}
}