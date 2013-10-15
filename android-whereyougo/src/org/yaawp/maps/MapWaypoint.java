package org.yaawp.maps;

import android.graphics.drawable.Drawable;

public class MapWaypoint {

	protected String mName = "";
	protected double mLatitude = 0.0;
	protected double mLongitude = 0.0;
	protected Drawable mMarker = null;
	
	public MapWaypoint( String name, double latitude, double longitude ) {
		mName = name;
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	public MapWaypoint( final MapWaypoint wpt ) {
		mName = wpt.mName;
		mLatitude = wpt.mLatitude;
		mLongitude = wpt.mLongitude;
		mMarker = wpt.mMarker;
	}
	
	public String getName() {
		return mName;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public Drawable getMarker() {
		return mMarker;
	} 
	
	public void setMarker( Drawable marker ) {
		mMarker = marker;
	}
}
