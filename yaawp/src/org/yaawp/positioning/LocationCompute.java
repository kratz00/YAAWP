package org.yaawp.positioning;

import org.yaawp.positioning.Location;

public class LocationCompute {

	private Location loc;
	
	// cache the inputs and outputs of computeDistanceAndBearing
	// so calls to distanceTo() and bearingTo() can share work
	private double mLat1 = 0.0;
	private double mLon1 = 0.0;
	private double mLat2 = 0.0;
	private double mLon2 = 0.0;
	private float[] mResults = new float[2];

	public LocationCompute(Location loc) {
		this.loc = loc;
	}

	public static void computeDistanceAndBearing(double lat1, double lon1,
			double lat2, double lon2, float[] results) {
		// Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
		// using the "Inverse Formula" (section 4)

		int MAXITERS = 20;
		// Convert lat/long to radians
		lat1 *= Math.PI / 180.0;
		lat2 *= Math.PI / 180.0;
		lon1 *= Math.PI / 180.0;
		lon2 *= Math.PI / 180.0;

		double a = 6378137.0; // WGS84 major axis
		double b = 6356752.3142; // WGS84 semi-major axis
		double f = (a - b) / a;
		double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

		double L = lon2 - lon1;
		double A = 0.0;
		double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
		double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

		double cosU1 = Math.cos(U1);
		double cosU2 = Math.cos(U2);
		double sinU1 = Math.sin(U1);
		double sinU2 = Math.sin(U2);
		double cosU1cosU2 = cosU1 * cosU2;
		double sinU1sinU2 = sinU1 * sinU2;

		double sigma = 0.0;
		double deltaSigma = 0.0;
		double cosSqAlpha = 0.0;
		double cos2SM = 0.0;
		double cosSigma = 0.0;
		double sinSigma = 0.0;
		double cosLambda = 0.0;
		double sinLambda = 0.0;

		double lambda = L; // initial guess
		for (int iter = 0; iter < MAXITERS; iter++) {
			double lambdaOrig = lambda;
			cosLambda = Math.cos(lambda);
			sinLambda = Math.sin(lambda);
			double t1 = cosU2 * sinLambda;
			double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
			double sinSqSigma = t1 * t1 + t2 * t2; // (14)
			sinSigma = Math.sqrt(sinSqSigma);
			cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
			sigma = Math.atan2(sinSigma, cosSigma); // (16)
			double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda
					/ sinSigma; // (17)
			cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
			cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2
					/ cosSqAlpha; // (18)
			double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn

			double A1 = (uSquared / 16384.0) * (4096.0 + uSquared * (-768.0 + uSquared * (320.0 - 175.0 * uSquared)));
			A = 1.0 + A1;
			double B = (uSquared / 1024.0) * // (4)
					(256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
			double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
			double cos2SMSq = cos2SM * cos2SM;
			deltaSigma = B * sinSigma * // (6)
					(cos2SM + (B / 4.0) * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0)
							* cos2SM * (-3.0 + 4.0 * sinSigma * sinSigma)
							* (-3.0 + 4.0 * cos2SMSq)));
			lambda = L
					+ (1.0 - C)
					* f
					* sinAlpha
					* (sigma + C
							* sinSigma
							* (cos2SM + C * cosSigma
									* (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

			double delta = (lambda - lambdaOrig) / lambda;
			if (Math.abs(delta) < 1.0e-12) {
				break;
			}
		}

		float distance = (float) (b * A * (sigma - deltaSigma));
		results[0] = distance;
		if (results.length > 1) {
			float initialBearing = (float) Math.atan2(cosU2 * sinLambda, cosU1
					* sinU2 - sinU1 * cosU2 * cosLambda);
			initialBearing *= 180.0 / Math.PI;
			results[1] = initialBearing;
			if (results.length > 2) {
				float finalBearing = (float) Math.atan2(cosU1 * sinLambda,
						-sinU1 * cosU2 + cosU1 * sinU2 * cosLambda);
				finalBearing *= 180.0 / Math.PI;
				results[2] = finalBearing;
			}
		}
	}

	/**
	 * Computes the approximate distance in meters between two locations, and
	 * optionally the initial and final bearings of the shortest path between
	 * them. Distance and bearing are defined using the WGS84 ellipsoid.
	 * 
	 * <p>
	 * The computed distance is stored in results[0]. If results has length 2 or
	 * greater, the initial bearing is stored in results[1]. If results has
	 * length 3 or greater, the final bearing is stored in results[2].
	 * 
	 * @param startLatitude
	 *            the starting latitude
	 * @param startLongitude
	 *            the starting longitude
	 * @param endLatitude
	 *            the ending latitude
	 * @param endLongitude
	 *            the ending longitude
	 * @param results
	 *            an array of floats to hold the results
	 * 
	 * @throws IllegalArgumentException
	 *             if results is null or has length < 1
	 */
	public static void distanceBetween(double startLatitude,
			double startLongitude, double endLatitude, double endLongitude,
			float[] results) {
		if (results == null || results.length < 1) {
			throw new IllegalArgumentException(
					"results is null or has length < 1");
		}
		computeDistanceAndBearing(startLatitude, startLongitude, endLatitude,
				endLongitude, results);
	}

	/**
	 * Returns the approximate distance in meters between this location and the
	 * given location. Distance is defined using the WGS84 ellipsoid.
	 * 
	 * @param dest
	 *            the destination location
	 * @return the approximate distance in meters
	 */
	public float distanceTo(Location dest) {
		// See if we already have the result
		synchronized (mResults) {
			if (loc.mLatitude != mLat1 || loc.mLongitude != mLon1
					|| dest.mLatitude != mLat2 || dest.mLongitude != mLon2) {
				computeDistanceAndBearing(loc.mLatitude, loc.mLongitude, dest.mLatitude,
						dest.mLongitude, mResults);
				mLat1 = loc.mLatitude;
				mLon1 = loc.mLongitude;
				mLat2 = dest.mLatitude;
				mLon2 = dest.mLongitude;
			}
			return mResults[0];
		}
	}

	/**
	 * Returns the approximate initial bearing in degrees East of true North
	 * when traveling along the shortest path between this location and the
	 * given location. The shortest path is defined using the WGS84 ellipsoid.
	 * Locations that are (nearly) antipodal may produce meaningless results.
	 * 
	 * @param dest
	 *            the destination location
	 * @return the initial bearing in degrees
	 */
	public float bearingTo(Location dest) {
		synchronized (mResults) {
			// See if we already have the result
			if (loc.mLatitude != mLat1 || loc.mLongitude != mLon1
					|| dest.mLatitude != mLat2 || dest.mLongitude != mLon2) {
				computeDistanceAndBearing(loc.mLatitude, loc.mLongitude, dest.mLatitude,
						dest.mLongitude, mResults);
				mLat1 = loc.mLatitude;
				mLon1 = loc.mLongitude;
				mLat2 = dest.mLatitude;
				mLon2 = dest.mLongitude;
			}
			return mResults[1];
		}
	}
}
