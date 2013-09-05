/*  
 * Copyright 2012, Asamm Software, s. r. o.
 * 
 * This file is part of LocusAPI.
 * 
 * LocusAPI is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *  
 * LocusAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *  
 * You should have received a copy of the Lesser GNU General Public
 * License along with LocusAPI. If not, see 
 * <http://www.gnu.org/licenses/lgpl.html/>.
 */

package locus.api.objects.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import locus.api.objects.GeoData;
import locus.api.objects.Storable;
import locus.api.utils.Log;
import locus.api.utils.Utils;

public class Track extends GeoData {

	private static final String TAG = "Track";
	
	/* locations of this track */
	ArrayList<Location> points;
    /* list containing all track break points */
    ArrayList<Integer> breaks;

    /* extra points (also may include routing data) */
	ArrayList<Waypoint> waypoints;
	
	/**
	 * flag that indicate whether to use parent folder style if exists
	 */
	boolean useFolderStyle;

	// STATISTICS
	
	// number of points (useful when points itself are not loaded)
	int numOfPoints = 0;
	// track start time (time of first point)
	long startTime = -1L;
	// track stop time (time of last point)
	long stopTime = -1L;
    // total length of done route
	float totalLength;
    // total track distance with speed
	float totalLengthMove;
    // total time of route
	long totalTime;
    // total track time with speed
	long totalTimeMove;
    // maximal speed of this route
	float speedMax;

	// maximum altitude on track
	float altitudeMax;
    // minimum altitude on track
	float altitudeMin;

    // neutral grade (distance)
    float eleNeutralDistance;
    // neutral grade (elevation)
    float eleNeutralHeight;
    // positive grade (distance)
    float elePositiveDistance;
    // positive grade (elevation)
    float elePositiveHeight;
    // negative grade (distance)
    float eleNegativeDistance;
    // negative grade (elevation)
    float eleNegativeHeight;
    // total grade (distance)
    float eleTotalAbsDistance;
    // total grade (elevation)
    float eleTotalAbsHeight;
	
    // CONSTRUCTOR
    
	public Track() {
		super();
	}
	
	public Track(DataInputStream dis) throws IOException {
		super(dis);
	}
	
	public Track(byte[] data) throws IOException {
		super(data);
	}
	
    /*******************************************/
    /*             STORABLE PART               */
    /*******************************************/
    
	@Override
	public int getVersion() {
		return 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readObject(int version, DataInputStream dis) throws IOException {
		id = dis.readLong();
		name = readStringUTF(dis);

		// load locations
		points = (ArrayList<Location>) Storable.readList(Location.class, dis);
		
		// read breaks
		int breaksSize = dis.readInt();
		if (breaksSize > 0) {
			byte[] breaks = new byte[breaksSize];
			dis.read(breaks);
			setBreaksData(breaks);
		}

		// read waypoints
		waypoints = (ArrayList<Waypoint>) Storable.readList(Waypoint.class, dis);
		
		// read extra part
		readExtraData(dis);
		readStyles(dis);
		
		// read statistics
		numOfPoints = dis.readInt();
		startTime = dis.readLong();
		stopTime = dis.readLong();
		totalLength = dis.readFloat();
		totalLengthMove = dis.readFloat();
		totalTime = dis.readLong();
		totalTimeMove = dis.readLong();
		speedMax = dis.readFloat();
		altitudeMax = dis.readFloat();
		altitudeMin = dis.readFloat();

	    eleNeutralDistance = dis.readFloat();
	    eleNeutralHeight = dis.readFloat();
	    elePositiveDistance = dis.readFloat();
	    elePositiveHeight = dis.readFloat();
	    eleNegativeDistance = dis.readFloat();
	    eleNegativeHeight = dis.readFloat();
	    eleTotalAbsDistance = dis.readFloat();
	    eleTotalAbsHeight = dis.readFloat();
	    
	    // version 1 extension
	    if (version >= 1) {
	    	useFolderStyle = dis.readBoolean();
	    }
	    
		// version 2 extension
		if (version >= 2) {
			timeCreated = dis.readLong();
		}
	}

	@Override
	public void writeObject(DataOutputStream dos) throws IOException {
		dos.writeLong(id);
		writeStringUTF(dos, name);

		// write locations
		Storable.writeList(points, dos);
		
		// write breaks
		byte[] breaksData = getBreaksData();
		dos.writeInt(breaksData.length);
		if (breaksData.length > 0)
			dos.write(breaksData);
		
		// write waypoints
		Storable.writeList(waypoints, dos);
		
		// write extra data
		writeExtraData(dos);
		writeStyles(dos);
		
		// write statistics
		dos.writeInt(numOfPoints);
		dos.writeLong(startTime);
		dos.writeLong(stopTime);
		dos.writeFloat(totalLength);
		dos.writeFloat(totalLengthMove);
		dos.writeLong(totalTime);
		dos.writeLong(totalTimeMove);
		dos.writeFloat(speedMax);
		dos.writeFloat(altitudeMax);
		dos.writeFloat(altitudeMin);
		
		dos.writeFloat(eleNeutralDistance);
		dos.writeFloat(eleNeutralHeight);
		dos.writeFloat(elePositiveDistance);
		dos.writeFloat(elePositiveHeight);
		dos.writeFloat(eleNegativeDistance);
		dos.writeFloat(eleNegativeHeight);
		dos.writeFloat(eleTotalAbsDistance);
		dos.writeFloat(eleTotalAbsHeight);
		
	    // version 1 extension
		dos.writeBoolean(useFolderStyle);
		
		// version 2 extension
		dos.writeLong(timeCreated);
	}

	@Override
	public void reset() {
		id = -1;
		name = "";
		points = new ArrayList<Location>();
		breaks = new ArrayList<Integer>();
		waypoints = new ArrayList<Waypoint>();
		extraData = null;
		styleNormal = null;
		styleHighlight = null;
		
		// version 1 extension
		useFolderStyle = true;
		
		// version 2 extension
		timeCreated = System.currentTimeMillis();
		
		resetStatistics();
	}
	
	public void resetStatistics() {
		// basic statistics variables
		totalLength = 0.0f;
		totalLengthMove = 0.0f;
		totalTime = 0L;
        totalTimeMove = 0L;
        speedMax = 0.0f;
        resetStatisticsAltitude();
	}
	
	public void resetStatisticsAltitude() {
        altitudeMax = Float.NEGATIVE_INFINITY;
        altitudeMin = Float.POSITIVE_INFINITY;	
		
        // graph variables
        eleNeutralDistance = 0.0f;
        eleNeutralHeight = 0.0f;
        elePositiveDistance = 0.0f;
        elePositiveHeight = 0.0f;
        eleNegativeDistance = 0.0f;
        eleNegativeHeight = 0.0f;
        eleTotalAbsDistance = 0.0f;
        eleTotalAbsHeight = 0.0f;		
	}
	
	/**************************************/
	/*       MAIN GETTERS & SETTERS       */
	/**************************************/
	
	// POINTS
	
	public Location getPoint(int index) {
		return points.get(index);
	}
	
	public ArrayList<Location> getPoints() {
		return points;
	}
	
	public boolean setPoints(ArrayList<Location> points) {
		if (points == null) {
			Log.w(TAG, "setPoints(" + points + "), cannot be null!");
			return false;
		}
		this.points = points;
		return true;
	}
	
	// BREAKS
	
	public ArrayList<Integer> getBreaks() {
		return breaks;
	}
	
	public byte[] getBreaksData() {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			for (int i = 0; i < breaks.size(); i++) {
				dos.writeInt(breaks.get(i));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			Log.e(TAG, "getBreaksData()", e);
		} finally {
			Utils.closeStream(dos);
			Utils.closeStream(baos);
		}
		return new byte[0];
	}

	public boolean setBreaks(ArrayList<Integer> breaks) {
		if (breaks == null) {
			Log.w(TAG, "setBreaks(" + breaks + "), cannot be null!");
			return false;
		}
		this.breaks = breaks;
		return true;
	}
	
	public void setBreaksData(byte[] data) {
		if (data == null || data.length == 0)
			return;

		ByteArrayInputStream bais = null;
		DataInputStream dis = null;
		try {
			bais = new ByteArrayInputStream(data);
			dis = new DataInputStream(bais);
			breaks.clear();
			while (dis.available() > 0) {
				breaks.add(dis.readInt());
			}
		} catch (Exception e) {
			Log.e(TAG, "setBreaksData()", e);
			breaks.clear();
		} finally {
			Utils.closeStream(dis);
			Utils.closeStream(bais);
		}
	}
	
	// WAYPOINTS
	
	public Waypoint getWaypoint(int index) {
		return waypoints.get(index);
	}
	
	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public boolean setWaypoints(ArrayList<Waypoint> wpts) {
		if (wpts == null) {
			Log.w(TAG, "setWaypoints(" + wpts + "), cannot be null!");
			return false;
		}
		this.waypoints = wpts;
		return true;
	}
	
	// OTHER STATISTICS
	
	public int getNumberOfPoints() {
		return numOfPoints;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public float getTotalLength() {
		return totalLength;
	}

	public float getTotalLengthMove() {
		return totalLengthMove;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public long getTotalTimeMove() {
		return totalTimeMove;
	}

	public float getSpeedMax() {
		return speedMax;
	}

	public float getAltitudeMax() {
		return altitudeMax;
	}

	public float getAltitudeMin() {
		return altitudeMin;
	}
	
	public float getEleNeutralDistance() {
		return eleNeutralDistance;
	}

	public float getEleNeutralHeight() {
		return eleNeutralHeight;
	}

	public float getElePositiveDistance() {
		return elePositiveDistance;
	}

	public float getElePositiveHeight() {
		return elePositiveHeight;
	}

	public float getEleNegativeDistance() {
		return eleNegativeDistance;
	}

	public float getEleNegativeHeight() {
		return eleNegativeHeight;
	}

	public float getEleTotalAbsDistance() {
		return eleTotalAbsDistance;
	}

	public float getEleTotalAbsHeight() {
		return eleTotalAbsHeight;
	}

	public long getTrackTime(boolean onlyWithMove) {
		if (onlyWithMove)
			return totalTimeMove;
		else
			return totalTime;
	}

	public float getSpeedAverage(boolean onlyWithMove) {
		long rt = getTrackTime(onlyWithMove);
		if (rt > 0) {
			return (float) (totalLength / (rt / 1000.0));
		} else {
			return 0.0f;
		}
	}
	
	public long getCreateTime() {
    	if (points.size() > 0) {
    		return points.get(0).getTime();
    	}
    	return System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Track [");
		sb.append("id: ").append(id);
		sb.append(", name: ").append(name);
		sb.append(", extraData: ").append(extraData);
		sb.append(", styleNormal: ").append(styleNormal);
		sb.append(", styleHighlight: ").append(styleHighlight);
		sb.append(", points: ").append(points != null ? points.size() : 0);
		sb.append(", breaks: ").append(breaks != null ? breaks.size() : 0);
		sb.append(", waypoints: ").append(waypoints != null ? waypoints.size() : 0);
		sb.append(", numOfPoints: ").append(numOfPoints);
		sb.append(", startTime: ").append(startTime);
		sb.append(", stopTime: ").append(stopTime);
		sb.append(", totalLength: ").append(totalLength);
		sb.append(", totalLengthMove: ").append(totalLengthMove);
		sb.append(", totalTime: ").append(totalTime);
		sb.append(", totalTimeMove: ").append(totalTimeMove);
		sb.append(", speedMax: ").append(speedMax);
		sb.append(", altitudeMax: ").append(altitudeMax);
		sb.append(", altitudeMin: ").append(altitudeMin);
		sb.append(", eleNeutralDistance: ").append(eleNeutralDistance);
		sb.append(", eleNeutralHeight: ").append(eleNeutralHeight);
		sb.append(", elePositiveDistance: ").append(elePositiveDistance);
		sb.append(", elePositiveHeight: ").append(elePositiveHeight);
		sb.append(", eleNegativeDistance: ").append(eleNegativeDistance);
		sb.append(", eleNegativeHeight: ").append(eleNegativeHeight);
		sb.append(", eleTotalAbsDistance: ").append(eleTotalAbsDistance);
		sb.append(", eleTotalAbsHeight: ").append(eleTotalAbsHeight);
		sb.append("]");
		return sb.toString();
	}

	// FOLDER STYLE
	
	public boolean isUseFolderStyle() {
		return useFolderStyle;
	}

	public void setUseFolderStyle(boolean useFolderStyle) {
		this.useFolderStyle = useFolderStyle;
	}
}
