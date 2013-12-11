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

import java.util.ArrayList;
import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.gui.dialogs.DialogMain;
import org.yaawp.hmi.gui.extension.CustomDialog;
import org.yaawp.hmi.gui.extension.DataInfo;
import org.yaawp.hmi.gui.extension.IconedListAdapter;
import org.yaawp.hmi.helper.I18N;

import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.listitem.ListItemWigItem;
import org.yaawp.hmi.adapter.ListItemAdapter;

import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.openwig.Refreshable;
import org.yaawp.openwig.WUI;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.A;
import org.yaawp.utils.Const;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;
import org.yaawp.utils.Utils;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonStopGuidance;
import android.widget.BaseAdapter;

public class WigMainMenuActivity extends CustomActivity implements Refreshable {

	private static final String TAG = "CartridgeMainMenu";
	
	private ThreeButtonPanelBar mButtonPanelBar;
	private PanelBarButtonStopGuidance mButtonStopGuidance;
	private PanelBarButtonShowMap mButtonShowMap;
	
	private AdapterView.OnItemClickListener listClick;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.custom_dialog);
		
		mButtonPanelBar = new ThreeButtonPanelBar(this);
		mButtonStopGuidance = new PanelBarButtonStopGuidance();
		mButtonShowMap = new PanelBarButtonShowMap(this);		
		
		listClick = new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Logger.d(TAG, "onItemClick:" + position);
				
				BaseAdapter adapter = (BaseAdapter)parent.getAdapter();
				if ( adapter instanceof ListItemAdapter ) {
					((ListItemAdapter) adapter ).onListItemClicked( WigMainMenuActivity.this, position );
				}
				
			}
		};
		
		CustomDialog.setTitle(this, Engine.instance.cartridge.name,
				null, CustomDialog.NO_IMAGE, null);
		
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

    	mButtonPanelBar.AddButton( mButtonStopGuidance );
    	mButtonStopGuidance.setVisible(A.getGuidingContent().isGuiding());
    	mButtonPanelBar.updateUI();
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		refresh();
    	mButtonStopGuidance.setVisible(A.getGuidingContent().isGuiding());
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
				
				ListItemAdapter adapter = new ListItemAdapter(WigMainMenuActivity.this);			
	    		ListItem3ButtonsHint itemHint = null;
	    		ListItemWigItem item = null;
	    		
				/* TODO make a common usable class to show this 'widget' */
		    	if ( LocationState.isActuallyHardwareGpsOn() == false ) {
		    		itemHint = new ListItem3ButtonsHint( I18N.get(R.string.gps_disabled) /* TODO I18N */,
		    				/* TODO I18N */ "Currently the GPS is off. Press the button 'GPS on' to switch on the GPS or 'Positioning' to change to the satellite view." ) ;
		    		
		    		itemHint.AddButton( new PanelBarButton( I18N.get(R.string.gps_on), 
							new PanelBarButton.OnClickListener() {
								@Override
								public boolean onClick() {
									LocationState.setGpsOn(WigMainMenuActivity.this);
									WigMainMenuActivity.this.refresh(); // TODO use a comment method to refresh the list
									return true;
								}
							}
						));  
		    		
		    		itemHint.AddButton( new PanelBarButton( I18N.get(R.string.positioning), 
							new PanelBarButton.OnClickListener() {
								@Override
								public boolean onClick() {
					                Intent intent02 = new Intent(WigMainMenuActivity.this, SatelliteActivity.class);
					                startActivity(intent02);
									return true;
								}
							}
						)); 
		    		
		    		itemHint.enableCancelButton( true );
		    		
		    		adapter.AddItem( itemHint );
		    	}	
		    		    		
	    		item = new ListItemWigItem( ListItemWigItem.WIGITEMTYPE_ZONES,
	    				I18N.get(R.string.locations) + " (" + Engine.instance.cartridge.visibleZones() + ")",
	    				getVisibleZonesDescription() ) ;
	    		// TODO R.drawable.icon_locations
	    		item.setSelectable(true);
	    		adapter.AddItem( item );
				
	    		item = new ListItemWigItem( ListItemWigItem.WIGITEMTYPE_YOUSEE,
	    				I18N.get(R.string.you_see) + " (" + Engine.instance.cartridge.visibleThings() + ")",
	    				getVisibleCartridgeThingsDescription() ) ;
	    		// TODO R.drawable.icon_search
	    		item.setSelectable(true);
	    		adapter.AddItem( item );
	    		
	    		item = new ListItemWigItem( ListItemWigItem.WIGITEMTYPE_INVENTORY,
	    				I18N.get(R.string.inventory) + " (" + Engine.instance.player.visibleThings() + ")",
	    				getVisiblePlayerThingsDescription() ) ;
	    		// TODO R.drawable.icon_inventory
	    		item.setSelectable(true);
	    		adapter.AddItem( item );
	    		
	    		item = new ListItemWigItem( ListItemWigItem.WIGITEMTYPE_TASKS,
	    				I18N.get(R.string.tasks) + " (" + Engine.instance.cartridge.visibleTasks() + ")",
	    				getVisibleTasksDescription() ) ;
	    		// TODO R.drawable.icon_tasks
	    		item.setSelectable(true);
	    		adapter.AddItem( item );	    		
	    		
				
				/* --- */
				ListView lv = new ListView(WigMainMenuActivity.this);			
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(listClick);
				CustomDialog.setContent(WigMainMenuActivity.this, lv, 0, true, false);
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
	
	/***********************************/
	/*     SPECIAL ITEMS FUNCTIONS     */
	/***********************************/
	
//	private Vector<Zone> getVisibleZones() {
//		Vector<Zone> zones = Engine.instance.cartridge.zones;
//		Vector<Zone> visible = new Vector<Zone>();
//		for (int i = 0; i < zones.size(); i++) {
//			Zone z = (Zone) zones.get(i);
//			if (z.isVisible())
//				visible.add(z);
//		}
//		return visible;
//	}
	
	private String getVisibleZonesDescription() {
		String description = null;
		@SuppressWarnings("unchecked")
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.get(i);
			if (z.isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += z.name;
			}
		}
		return description;
	}
	
	private String getVisibleCartridgeThingsDescription() {
		String description = null;
		@SuppressWarnings("unchecked")
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			String des = getVisibleThingsDescription(z);
			if (des != null) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += des;
			}
		}
		return description;
	}
	
	private String getVisibleThingsDescription(Zone z) {
		String description = null;
		if (!z.showThings())
			return null;
		Object key = null;
		while ((key = z.inventory.next(key)) != null) {
			Object o = z.inventory.rawget(key);
			if (o instanceof Player)
				continue;
			if (!(o instanceof Thing))
				continue;
			if (((Thing) o).isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";

				description += ((Thing) o).name;
			}
		}
		return description;
	}
	
	private String getVisiblePlayerThingsDescription() {
		Player p = Engine.instance.player;
		String description = null;
		Object key = null;
		while ((key = p.inventory.next(key)) != null) {
			Object o = p.inventory.rawget(key);
			if (o instanceof Thing && ((Thing) o).isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				
				description += ((Thing) o).name;
			}
		}
		return description;
	}
	
	public int getVisibleTasksCount() {
		int count = 0;
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) count++;
		}
		return count;
	}
	
	public String getVisibleTasksDescription() {
		String description = null;
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				description += a.name;
			}
		}
		return description;
	}
}