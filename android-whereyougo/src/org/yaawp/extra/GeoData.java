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

import org.yaawp.extra.Log;
import org.yaawp.extra.Utils;

public abstract class GeoData  {

	private static final String TAG = "GeoData";
	
	// unique ID of this object
	public long id;
	
	// name of object, have to be unique
	protected String name;
	
	// time the data was created
	protected long timeCreated;
	
	
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
	}
	
	public GeoData(byte[] data) throws IOException {
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
	
	public byte[] getExtraData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// writeExtraData(dos);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, "getExtraDataRaw()", e);
			return null;
		} finally {
			Utils.closeStream(dos);
		}
	}
	
}
