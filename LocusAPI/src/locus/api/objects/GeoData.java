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

package locus.api.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import locus.api.objects.extra.ExtraData;
import locus.api.objects.extra.ExtraStyle;
import locus.api.utils.Log;
import locus.api.utils.Utils;

public abstract class GeoData extends Storable {

	private static final String TAG = "GeoData";
	
	// unique ID of this object
	public long id;
	
	// name of object, have to be unique
	protected String name;
	
	// time the data was created
	protected long timeCreated;
	
    // EXTRA CONTAINERS
    
	/* extra data */
	public ExtraData extraData;
	
	/* extra style for normal state */
	public ExtraStyle styleNormal;
	/* extra style for highlight state */
	public ExtraStyle styleHighlight;
	
	// PRIVATE PART
	
	/** normal state */
	public static final byte STATE_VISIBLE = 0;
	/** item is selected */
	public static final byte STATE_SELECTED = 1;
	/** item is disabled */
	public static final byte STATE_HIDDEN = 2;
	/** actual state */
	public byte state = STATE_VISIBLE;
	
	/**
	 * Additional temporary storage object. Object is not serialized!
	 * For Locus personal usage only
	 */
	public Object tag;
	
	/** temp variable for sorting */
	public int dist;

	public GeoData() {
		super();
	}
	
	public GeoData(DataInputStream dis) throws IOException {
		super(dis);
	}
	
	public GeoData(byte[] data) throws IOException {
		super(data);
	}
	
    /*******************************************/
    /*             STORABLE PART               */
    /*******************************************/
	
	protected void readExtraData(DataInputStream dis) throws IOException {
		if (dis.readBoolean()) {
			extraData = new ExtraData();
			extraData.read(dis);
		}
	}
	
	protected void writeExtraData(DataOutputStream dos) throws IOException {
		if (extraData != null && extraData.getCount() > 0) {
			dos.writeBoolean(true);
			extraData.write(dos);
		} else {
			dos.writeBoolean(false);
		}
	}
	
	protected void readStyles(DataInputStream dis) throws IOException {
		if (dis.readBoolean()) {
			styleNormal = new ExtraStyle("");
			styleNormal.read(dis);
		}
		if (dis.readBoolean()) {
			styleHighlight = new ExtraStyle("");
			styleHighlight.read(dis);
		}
	}
	
	protected void writeStyles(DataOutputStream dos) throws IOException {
		if (styleNormal != null) {
			dos.writeBoolean(true);
			styleNormal.write(dos);
		} else {
			dos.writeBoolean(false);
		}
		
		if (styleHighlight != null) {
			dos.writeBoolean(true);
			styleHighlight.write(dos);
		} else {
			dos.writeBoolean(false);
		}
	}
	
	/**************************************/
	/*       MAIN GETTERS & SETTERS       */
	/**************************************/
	
	// NAME
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && name.length() > 0) {
			this.name = name;
		}
	}
	
	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	// EXTRA DATA

	public boolean hasExtraData() {
		return extraData != null;
	}
	
	public byte[] getExtraData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			writeExtraData(dos);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, "getExtraDataRaw()", e);
			return null;
		} finally {
			Utils.closeStream(dos);
		}
	}
	
	public void setExtraData(byte[] data) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(data));
			readExtraData(dis);
		} catch (Exception e) {
			Log.e(TAG, "setExtraData(" + data + ")", e);
			extraData = null;
		} finally {
			Utils.closeStream(dis);
		}
	}
	
	// EXTRA DATA - PARAMETERS
	// these are helper functions for more quick access
	// to parameter values without need to check state
	// of ExtraData object
	
	public boolean addParameter(int paramId, String param) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		return extraData.addParameter(paramId, param);
	}
	
	public String getParameter(int paramId) {
		if (extraData == null) {
			return null;
		}
		return extraData.getParameter(paramId);
	}
	
	public boolean hasParameter(int paramId) {
		if (extraData == null) {
			return false;
		}
		return extraData.hasParameter(paramId);		
	}
	
	public String removeParameter(int paramId) {
		if (extraData == null) {
			return null;
		}
		return extraData.removeParameter(paramId);
	}

	// SHORTCUTS FOR MOST USEFUL PARAMS
	
	public void setParameterSource(int source) {
		addParameter(ExtraData.PAR_SOURCE, String.valueOf(source));
	}
	
	public int getParameterSource() {
		if (extraData == null) {
			return ExtraData.SOURCE_UNKNOWN;
		}
		String res = extraData.getParameter(ExtraData.PAR_SOURCE);
		try {
			return Integer.parseInt(res);
		} catch (Exception e) {
			return ExtraData.SOURCE_UNKNOWN;
		}
	}
	
	public void setParameterStyleName(String style) {
		addParameter(ExtraData.PAR_STYLE_NAME, style);
	}
	
	public String getParameterStyleName() {
		if (extraData == null) {
			return "";
		}
		return extraData.getParameter(ExtraData.PAR_STYLE_NAME);
	}

	public void addEmail(String email) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		extraData.addEmail(email);
	}
	
	public void addPhone(String phone) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		extraData.addPhone(phone);
	}
	
	public void addUrl(String url) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		extraData.addUrl(url);
	}
	
	public void addUrl(String label, String url) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		extraData.addUrl(label, url);
	}
	
	public void addPhoto(String url) {
		if (extraData == null) {
			extraData = new ExtraData();
		}
		extraData.addPhoto(url);
	}
	
	// STYLES
	
	public byte[] getStyles() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			writeStyles(dos);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, "getStylesRaw()", e);
			return null;
		} finally {
			Utils.closeStream(dos);
		}
	}

	public void setStyles(byte[] data) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(data));
			readStyles(dis);
		} catch (Exception e) {
			Log.e(TAG, "setExtraStyle(" + data + ")", e);
			extraData = null;
		} finally {
			Utils.closeStream(dis);
		}
	}
}
