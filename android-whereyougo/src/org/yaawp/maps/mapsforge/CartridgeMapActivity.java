package org.yaawp.maps.mapsforge;

import android.graphics.Bitmap;
import android.os.Bundle;
import java.io.File;

import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Utils;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;
import org.yaawp.R;
import org.yaawp.app.YaawpAppData;
import org.yaawp.hmi.helper.CartridgeHelper;

import cz.matejcik.openwig.formats.CartridgeFile;

import android.graphics.drawable.Drawable;
import cz.matejcik.openwig.formats.CartridgeFile;

public class CartridgeMapActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapView mapview = new MapView(this);
		mapview.setClickable(true);
		mapview.setBuiltInZoomControls(true);
		File file = new File ("/mnt/sdcard/Maps/bayern.map");
		mapview.setMapFile(file);
		setContentView(mapview);
		
		// create a default marker for the overlay
        // R.drawable.marker is just a placeholder for your own drawable
        Drawable defaultMarker = getResources().getDrawable(R.drawable.icon_gc_wherigo);
       // Bitmap b = Images.getImageB(R.drawable.ic_title_logo, (int) Utils.getDpPixels(32.0f));

        // create an ItemizedOverlay with the default marker
        ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(defaultMarker);

        YaawpAppData data = YaawpAppData.GetInstance();
        GeoPoint geoPoint = null;
        for (int i = 0; i < data.mWigFiles.size(); i++) {
        	
            // create a GeoPoint with the latitude and longitude coordinates
        	CartridgeFile wigfile = data.mWigFiles.get(i);
        	
			// remove location less
        	if (CartridgeHelper.isPlayAnywhere(wigfile)) {
				continue;
        	}
        	
            geoPoint = new GeoPoint(wigfile.latitude, wigfile.longitude);

            // create an OverlayItem with title and description
            OverlayItem item = new OverlayItem(geoPoint, wigfile.name,
                    "Hier kšnnte ihre Werbung stehen!");

            // add the OverlayItem to the ArrayItemizedOverlay
            itemizedOverlay.addItem(item);        	
        }


        // add the ArrayItemizedOverlay to the MapView
        mapview.getOverlays().add(itemizedOverlay);

        mapview.setCenter(geoPoint);
		
	}
}
