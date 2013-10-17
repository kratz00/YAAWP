package org.yaawp.maps.mapsforge;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Vector;

import menion.android.whereyougo.gui.CartridgeMainMenu;
import menion.android.whereyougo.gui.Refreshable;
import menion.android.whereyougo.gui.extension.CustomDialog;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.hardware.location.LocationEventListener;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.hardware.location.SatellitePosition;
import menion.android.whereyougo.settings.Settings;
import menion.android.whereyougo.utils.Utils;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;


import android.graphics.drawable.Drawable;

import org.yaawp.extra.Location;
import org.yaawp.maps.MapOverlays;
import org.yaawp.maps.MapPoint;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.hardware.location.LocationEventListener;
import menion.android.whereyougo.gui.Refreshable;
import org.yaawp.maps.mapsforge.MfOverlayHelper;
public class CartridgeMapActivity extends MapActivity implements LocationEventListener  {

	public final static String MAPFILE = "MAPFILE";

	// 
	
	private String mMapFile;
	
	public MapView mMapview = null;
	
	@Override
	protected void onResume() {
		super.onResume();
		Settings.setCurrentActivity(this);
		refresh();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMapview = new MapView(this);
		mMapview.setClickable(true);
		mMapview.setBuiltInZoomControls(true);
	
		if ( MapOverlays.mPolygons.size() > 0 ) {
			ArrayWayOverlay x = new ArrayWayOverlay(null,null);		// create the WayOverlay and add the ways
			// ArrayWayOverlay wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill,
			//		wayDefaultPaintOutline);
			
			for (int i = 0; i < MapOverlays.mPolygons.size(); i++) {
				MfMapPolygon polygon = new MfMapPolygon( MapOverlays.mPolygons.get(i) );
				x.addWay( polygon.getOverlayWay() );
			}
			
			// mapview.getOverlays().add(x);
			
			
		}
		
		
		if ( MapOverlays.mWaypoints.size() > 0 ) {
			
			ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(null);
				        
			for (int i = 0; i < MapOverlays.mWaypoints.size(); i++) {
				MfMapWaypoint wpt = new MfMapWaypoint( MapOverlays.mWaypoints.get(i) );
				itemizedOverlay.addItem(wpt.getOverlayItem());				
			}
                    
	        // add the ArrayItemizedOverlay to the MapView
	        mMapview.getOverlays().add(itemizedOverlay);      
	        
			// set mapview center
			MapPoint point = MapOverlays.X();
			GeoPoint x = new GeoPoint( point.mLatitude, point.mLongitude );
			mMapview.setCenter( x );		        
		}
		
 
		
		mMapFile = getIntent().getStringExtra(MAPFILE);
		File file = new File (mMapFile);
		mMapview.setMapFile(file);
		setContentView(mMapview);	
	}
	
	public void onStart() {
		super.onStart();
		// if (et instanceof Zone)
		LocationState.addLocationChangeListener(this);
	}
	
	public void onStop() {
		super.onStop();
		LocationState.removeLocationChangeListener(this);
	}

	Location mLocation = null;
	
	public void onLocationChanged(Location location) {
		refresh();
		mLocation = location;
	}
	
	public void onStatusChanged(String provider, int state, Bundle extras) {}

	public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> sats) {
	}

	public int getPriority() {
		return LocationEventListener.PRIORITY_MEDIUM;
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public String getName() {
		return "X";
	}	
	
	public void refresh() {
		runOnUiThread(new Runnable() {
			public void run() {
				
				if ( mMapview == null ) {
					return;
				}
				

				if ( mLocation != null ) {
					GeoPoint point = new GeoPoint( mLocation.getLatitude(), mLocation.getLongitude() );
					OverlayItem item = new OverlayItem( point, "n/a", "n/a" );

					Drawable marker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
					if ( marker != null ) {
					    int intrinsicWidth = marker.getIntrinsicWidth();
					    int intrinsicHeight = marker.getIntrinsicHeight();
					    marker.setBounds(intrinsicWidth / -2, intrinsicHeight / -2, intrinsicWidth / 2, intrinsicHeight / 2);
						item.setMarker( marker );			
					} 
					
					ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(null);
			        
					itemizedOverlay.addItem(item);
					
		                    
			        // add the ArrayItemizedOverlay to the MapView
			        mMapview.getOverlays().add(0,itemizedOverlay);
			     
				}
				
				ArrayWayOverlay x = new ArrayWayOverlay(null,null);	
				Vector<Zone> zones = Engine.instance.cartridge.zones;
				for (int i = 0; i < zones.size(); i++) {
					Zone z = (Zone)zones.get(i);
					
					x.addWay( MfOverlayHelper.Overlay( z ) );
					
				}
				
				// mMapview.getOverlays().get(location)
				mMapview.getOverlays().clear();
				mMapview.getOverlays().add(x);	
								

				mMapview.invalidate();
	
					
			}
		});		
	}
	
}	





		

