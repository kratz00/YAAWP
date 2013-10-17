package org.yaawp.maps.mapsforge;

import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

// import org.yaawp.maps.MapOverlays;
import org.yaawp.maps.MapWaypoint;

import android.graphics.drawable.Drawable;
import org.yaawp.maps.mapsforge.MfMapOverlay;


public class MfMapWaypoint extends MapWaypoint implements MfMapOverlay  {

	MfMapWaypoint( MapWaypoint wpt ) {
		super(wpt);
	}
	
	// @Override
	OverlayItem getOverlayItem() {
		GeoPoint point = new GeoPoint( mLatitude, mLongitude );
		OverlayItem item = new OverlayItem( point, mName, "n/a" );

		Drawable marker = mMarker;
		if ( marker != null ) {
		    int intrinsicWidth = marker.getIntrinsicWidth();
		    int intrinsicHeight = marker.getIntrinsicHeight();
		    marker.setBounds(intrinsicWidth / -2, intrinsicHeight / -2, intrinsicWidth / 2, intrinsicHeight / 2);
			item.setMarker( marker );			
		} 
		return item;
	}
	
    
}
