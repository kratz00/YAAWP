package org.yaawp.maps;

public class YGeoPoint {

	private double mLatitude;
	private double mLongitude;
	
	public YGeoPoint( double latitude, double longitude ) {
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public void setLocation( double latitude, double longitude ) {
		mLatitude = latitude;
		mLongitude = longitude;		
	}
	
}
