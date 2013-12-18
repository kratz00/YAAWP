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

import java.util.Vector;

import org.yaawp.R;

import org.yaawp.hmi.listitem.*;
import org.yaawp.hmi.adapter.ListItemAdapter;

import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.openwig.Refreshable;
import org.yaawp.openwig.WUI;
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
import android.widget.RelativeLayout;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

import org.yaawp.hmi.listitem.ListItemGuidanceActive;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;
import org.yaawp.utils.CartridgeHelper;

public class WigMainMenuActivity extends CustomActivity implements Refreshable {

	private static final String TAG = "CartridgeMainMenu";
	
	private ThreeButtonPanelBar mButtonPanelBar;
	private PanelBarButtonShowMap mButtonShowMap;
	
	private ListItemGpsDisabledWarning mGpsDisabledWarning = null;
	private ListItemGuidanceActive mGuidanceActive = null;
	private ListItemWherigoCategoryHeaderInventor mWherigoInventory = null;
	private ListItemWherigoCategoryHeaderTasks mWherigoTasks = null;
	private ListItemWherigoCategoryHeaderYouSee mWherigoYouSee = null;
	private ListItemWherigoCategoryHeaderZones mWherigoZones = null;
	
	private ListItemAdapter mAdapter = null;	
	private ListView mCartridgeListView = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_main); 
		        
		/* ------------------------------------------------------------------ */
		
		mAdapter = new ListItemAdapter(WigMainMenuActivity.this);
	
		mGpsDisabledWarning = (ListItemGpsDisabledWarning) mAdapter.AddItem( new ListItemGpsDisabledWarning( this ) );
    	mGuidanceActive     = (ListItemGuidanceActive)     mAdapter.AddItem( new ListItemGuidanceActive(WigMainMenuActivity.this) );
    	
    	/* ------------------------------------------------------------------ */
    	
    	mAdapter.AddItem( new ListItemCartridgeHeadline() );
    	
    	/* ------------------------------------------------------------------ */
    	addElementsOfCategoryZones();
    	addElementsOfCategoryYouSee();
    	addElementsOfCategoryInventory();
    	addElementsOfCategoryTasks(); 	
		/* ------------------------------------------------------------------ */
    	
		mCartridgeListView = new ListView(this);  
		mCartridgeListView.setDividerHeight(0);
		mCartridgeListView.setDivider( null );		
		mCartridgeListView.setAdapter(mAdapter);
		mCartridgeListView.setOnItemClickListener( mAdapter.mListClick );
		mCartridgeListView.setOnCreateContextMenuListener( null );

        RelativeLayout contentArea = (RelativeLayout) this.findViewById(R.id.relative_layout_content);
        contentArea.removeAllViews();
        contentArea.addView(mCartridgeListView);   		
	
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
	
	private void addElementsOfCategoryZones() {
    	mWherigoZones       = (ListItemWherigoCategoryHeaderZones)       mAdapter.AddItem( new ListItemWherigoCategoryHeaderZones() );
		for (int i = 0; i < Engine.instance.cartridge.zones.size(); i++) {
			Zone z = (Zone)Engine.instance.cartridge.zones.get(i);
			if (z.isVisible()) {
				mAdapter.AddItem( new ListItemWherigoElementZone( z, mWherigoZones ) );
			}
		}  		
	}
	
	private void addElementsOfCategoryYouSee() {
    	mWherigoYouSee      = (ListItemWherigoCategoryHeaderYouSee)      mAdapter.AddItem( new ListItemWherigoCategoryHeaderYouSee() );
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);			
			if (z.showThings()) {
				Object key = null;
				while ((key = z.inventory.next(key)) != null) {
					Object o = z.inventory.rawget(key);
					if (o instanceof Player)
						continue;
					if (!(o instanceof Thing))
						continue;
					if (((Thing) o).isVisible()) {
						mAdapter.AddItem( new ListItemWherigoElementThing( (Thing) o, mWherigoYouSee ) );
					}
				}
			}
		}   		
	}
	private void addElementsOfCategoryInventory() {
    	mWherigoInventory   = (ListItemWherigoCategoryHeaderInventor)   mAdapter.AddItem( new ListItemWherigoCategoryHeaderInventor() );
		Player p = Engine.instance.player;
		Object key = null;
		while ((key = p.inventory.next(key)) != null) {
			Object o = p.inventory.rawget(key);
			if (o instanceof Thing && ((Thing) o).isVisible()) {
				mAdapter.AddItem( new ListItemWherigoElementThing( (Thing) o, mWherigoInventory ) );
			}
		}		
	}
	private void addElementsOfCategoryTasks() {
    	mWherigoTasks       = (ListItemWherigoCategoryHeaderTasks)       mAdapter.AddItem( new ListItemWherigoCategoryHeaderTasks() ); 	
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) {
				mAdapter.AddItem( new ListItemWherigoElementTask( a, mWherigoTasks ) );
			}
		}   		
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
                Intent intent03 = new Intent( this, AppInfoActivity.class);
                startActivity(intent03); 
                
            default:
                status = super.onOptionsItemSelected(item);
                break;
        }
    
        return status;
    }            
    
	public void refresh() {
		runOnUiThread(new Runnable() {
			public void run() {
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