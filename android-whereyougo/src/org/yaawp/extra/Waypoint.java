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

package org.yaawp.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import org.yaawp.extra.GeoData;
import org.yaawp.extra.Storable;
import org.yaawp.extra.ExtraData;
// import locus.api.objects.geocaching.GeocachingData;
import org.yaawp.extra.Log;
import org.yaawp.extra.Utils;

public class Waypoint extends GeoData {
	
	private static final String TAG = "Waypoint";
	
	/* mLoc of this point */
	Location loc;
	
	/* additional geoCaching data */
	// public GeocachingData gcData;

	public Waypoint(String name, Location loc) {
		super();
		setName(name);
		this.loc = loc;
	}
	
	/**
	 * Empty constructor used for {@link Storable}
	 * <br />
	 * Do not use directly!
	 */
	public Waypoint() {
		this("", new Location(""));
	}
	
	public Waypoint(DataInputStream dis) throws IOException {
		super(dis);
	}
	
	public Waypoint(byte[] data) throws IOException {
		super(data);
	}
	
    /*******************************************/
    /*             STORABLE PART               */
    /*******************************************/
	
	@Override
	protected int getVersion() {
		return 1;
	}

	@Override
	protected void readObject(int version, DataInputStream dis) throws IOException {
		id = dis.readLong();
		name = readStringUTF(dis);
		loc = new Location(dis);
		
		// read extra data
		readExtraData(dis);
		readStyles(dis);
		
		// read geocaching
		// readGeocachingData(dis);
		
		// version 1 extension
		if (version >= 1) {
			timeCreated = dis.readLong();
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		dos.writeLong(id);
		writeStringUTF(dos, name);
		loc.write(dos);

		// write extra data
		writeExtraData(dos);
		writeStyles(dos);

		// write geocaching data
		// writeGeocachingData(dos);
		
		// version 1 extension
		dos.writeLong(timeCreated);
	}

	@Override
	public void reset() {
		id = -1;
		name = "";
		loc = null;
		extraData = null;
		styleNormal = null;
		styleHighlight = null;
		// gcData = null;
		
		// version 1 extension
		timeCreated = System.currentTimeMillis();
	}
	
	/*
	protected void readGeocachingData(DataInputStream dis) throws IOException {
		if (dis.readBoolean()) {
			gcData = new GeocachingData();
			gcData.read(dis);
		}
	}
	
	protected void writeGeocachingData(DataOutputStream dos) throws IOException {
		if (gcData != null) {
			dos.writeBoolean(true);
			gcData.write(dos);
		} else {
			dos.writeBoolean(false);
		}
	}
*/
    /*******************************************/
    /*             GET & SET PART              */
    /*******************************************/
	
	public static final String TAG_EXTRA_CALLBACK = "TAG_EXTRA_CALLBACK";
	public static final String TAG_EXTRA_ON_DISPLAY = "TAG_EXTRA_ON_DISPLAY";
	
	public Location getLocation() {
		return loc;
	}
	
	public String getExtraCallback() {
		if (extraData != null) {
			return extraData.getParameter(ExtraData.PAR_INTENT_EXTRA_CALLBACK);
		}
		return null;
	}
	
	/**
	 * Simply allow set callback value on point. This appear when you click on point
	 * and then under last button will be your button. Clicking on it, launch by you,
	 * defined intent
	 * <br /><br />
	 * Do not forget to set this http://developer.android.com/guide/topics/manifest/activity-element.html#exported
	 * to your activity, if you'll set callback to other then launcher activity
	 * @param btnName Name displayed on button
	 * @param packageName this value is used for creating intent that
	 *  will be called in callback (for example com.super.application)
	 * @param className the name of the class inside of com.super.application
	 *  that implements the component (for example com.super.application.Main)
	 * @param returnDataName String under which data will be stored. Can be
	 *  retrieved by String data = getIntent.getStringExtra("returnData");
	 * @param returnDataValue String under which data will be stored. Can be
	 *  retrieved by String data = getIntent.getStringExtra("returnData");
	 */
	public void setExtraCallback(String btnName, String packageName, String className,
			String returnDataName, String returnDataValue) {
		StringBuffer buff = new StringBuffer();
		buff.append(TAG_EXTRA_CALLBACK).append(";");
		buff.append(btnName).append(";");
		buff.append(packageName).append(";");
		buff.append(className).append(";");
		buff.append(returnDataName).append(";");
		buff.append(returnDataValue).append(";");
		addParameter(ExtraData.PAR_INTENT_EXTRA_CALLBACK, buff.toString());
	}
	
	/**
	 * If you want to remove PAR_INTENT_EXTRA_CALLBACK parametr from Locus database,
	 * you need to send "clear" value in updated waypoint back to Locus. After that,
	 * Locus will remove this parameter from new stored point.
	 * <br /><br />
	 * Second alternative, how to remove this callback, is to send new waypoints
	 * with forceOverwrite parameter set to <code>true</code>, that will overwrite
	 * completely all data 
	 */
	public void removeExtraCallback() {
		addParameter(ExtraData.PAR_INTENT_EXTRA_CALLBACK, "clear");
	}
	
	public String getExtraOnDisplay() {
		if (extraData != null) {
			return extraData.getParameter(ExtraData.PAR_INTENT_EXTRA_ON_DISPLAY);
		}
		return null;
	}
	
	/**
	 * Extra feature that allow to send to locus only partial point data. When you click on
	 * point (in time when small point dialog should appear), locus send intent to your app,
	 * you can then fill complete point and send it back to Locus. Clear and clever
	 * <br /><br />
	 * Do not forget to set this http://developer.android.com/guide/topics/manifest/activity-element.html#exported
	 * to your activity, if you'll set callback to other then launcher activity
	 * 
	 * @param btnName Name displayed on button
	 * @param packageName this value is used for creating intent that
	 *  will be called in callback (for example com.super.application)
	 * @param className the name of the class inside of com.super.application
	 *  that implements the component (for example com.super.application.Main)
	 * @param returnDataName String under which data will be stored. Can be
	 *  retrieved by String data = getIntent.getStringExtra("returnData");
	 * @param returnDataValue String under which data will be stored. Can be
	 *  retrieved by String data = getIntent.getStringExtra("returnData");
	 */
	public void setExtraOnDisplay(String packageName, String className,
			String returnDataName, String returnDataValue) {
		StringBuffer buff = new StringBuffer();
		buff.append(TAG_EXTRA_ON_DISPLAY).append(";");
		buff.append(packageName).append(";");
		buff.append(className).append(";");
		buff.append(returnDataName).append(";");
		buff.append(returnDataValue).append(";");
		addParameter(ExtraData.PAR_INTENT_EXTRA_ON_DISPLAY, buff.toString());
	}
	
	/**
	 * If you want to remove PAR_INTENT_EXTRA_ON_DISPLAY parametr from Locus database,
	 * you need to send "clear" value in updated waypoint back to Locus. After that,
	 * Locus will remove this parameter from new stored point.
	 * <br /><br />
	 * Second alternative, how to remove this callback, is to send new waypoints
	 * with forceOverwrite parameter set to <code>true</code>, that will overwrite
	 * completely all data 
	 */
	public void removeExtraOnDisplay() {
		addParameter(ExtraData.PAR_INTENT_EXTRA_ON_DISPLAY, "clear");
	}

	/*
	public byte[] getGeocachingData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// writeGeocachingData(dos);
			return baos.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, "getGeocachingDataRaw()", e);
			return null;
		} finally {
			Utils.closeStream(dos);
		}
	}
	
	public void setGeocachingData(byte[] data) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(data));
			// readGeocachingData(dis);
		} catch (Exception e) {
			Log.e(TAG, "setGeocachingData(" + data + ")", e);
			// gcData = null;
		} finally {
			Utils.closeStream(dis);
		}
	}
	*/
}
