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
import org.yaawp.R;
import org.yaawp.hmi.adapter.ListAdapterWigElements;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonShowMap;
import org.yaawp.maps.mapsforge.CartridgeMapActivity;
import org.yaawp.openwig.Refreshable;
import org.yaawp.openwig.WUI;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;
import org.yaawp.utils.ManagerNotify;


public class WigMainMenuActivity extends CustomActivity implements Refreshable {

	private static final String TAG = WigMainMenuActivity.class.getSimpleName();
	
	private ThreeButtonPanelBar mButtonPanelBar;
	private PanelBarButtonShowMap mButtonShowMap;
	private ListView mCartridgeListView = null;

	private ListAdapterWigElements mAdapter = null;	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_main); 
		        
		/* ------------------------------------------------------------------ */
		
		mAdapter = new ListAdapterWigElements(WigMainMenuActivity.this);
	    	
       	

		/* ------------------------------------------------------------------ */
    	
		mCartridgeListView = new ListView(this);  
		mCartridgeListView.setDivider( getResources().getDrawable( R.drawable.list_divider ) );
		mCartridgeListView.setDividerHeight(2);	
		mCartridgeListView.setAdapter(mAdapter);
		mCartridgeListView.setOnItemClickListener( mAdapter.mOnItemClickListener );
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
	
	@Override 
	public void onResume() {
		super.onResume();
		mAdapter.refresh();
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
    
    @Override
	public boolean onKeyTwiceDown( int keyCode, KeyEvent event ) {
		Log.i( TAG, "onKeyTwiceDown( KeyCode="+keyCode );
		boolean status = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	    	AlertDialog.Builder b = new AlertDialog.Builder(WigMainMenuActivity.this);
	    	b.setCancelable(true);
	    	b.setTitle(R.string.question);
	    	b.setMessage(R.string.save_game_before_exit);
	    	b.setPositiveButton(R.string.yes, 
	    		new DialogInterface.OnClickListener() {
						
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {	    		    
						Engine.requestSync();
						new SaveGameOnExit().execute();
		    		}
	    		});
	    	b.setNeutralButton(R.string.cancel, null);
	    	b.setNegativeButton(R.string.no, 
	    		new DialogInterface.OnClickListener() {
				
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {
						Engine.kill();
						WigMainMenuActivity.this.finish();
		    		}
		    	});
			b.show();
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
			ManagerNotify.toastShortMessage(R.string.double_hk_back_exit_game);
			status = true;
		} else {
			status = super.onKeyOnceDown(keyCode, event);
		}
		return status;
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
	
	@Override
	public void refresh() {
		mAdapter.refresh();
	}
}