package org.yaawp.maps.mapsforge;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;

import org.yaawp.extra.Location;
import org.yaawp.maps.services.GenericOverlay;
import org.yaawp.maps.services.Overlays;
import org.yaawp.maps.services.OverlayPosition;
import org.yaawp.maps.services.OverlayZones;
import org.yaawp.maps.services.OverlayGuidance;

import android.graphics.Canvas;
import android.graphics.Point;

public class MapsforgeOverlay extends Overlay {

	private GenericOverlay mOverlay = null;
	
	private boolean mVisible = true;	
	
	public MapsforgeOverlay( Overlays overlays ) {
		switch( overlays ) {
			case OVERLAY_CURRENT_POSITION:
				mOverlay = new OverlayPosition();
				break;
			case OVERLAY_ZONES:
				mOverlay = new OverlayZones();
				break;
			case OVERLAY_GUIDANCE:
				mOverlay = new OverlayGuidance();
				break;
			case OVERLAY_WHERIGOS:
				// mOverlay = new OverlayWherigos();
				break;
		}
	}
	
    @Override
    public void drawOverlayBitmap(Canvas canvas, Point drawPosition,
            Projection projection, byte drawZoomLevel) {
    	if ( mVisible == true ) {
    		mOverlay.drawBitmap(canvas, new MapsforgeProjection(projection) );
    	}
    }
    
    public void onLocationChanged( Location location ) {
    	mOverlay.onLocationChanged( location );
    }    
    
    public void show() {
    	mVisible = true;
    }
 
    public void show( boolean visible ) {
    	mVisible = visible;
    }    
    
    public void hide() {
    	mVisible = false;
    }    
}
