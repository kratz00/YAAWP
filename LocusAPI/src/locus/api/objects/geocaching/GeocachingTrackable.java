/*  
 * Copyright 2011, Asamm Software, s.r.o.
 * 
 * This file is part of LocusAddonPublicLib.
 * 
 * LocusAddonPublicLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * LocusAddonPublicLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with LocusAddonPublicLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package locus.api.objects.geocaching;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import locus.api.objects.Storable;

public class GeocachingTrackable extends Storable {
	
	/**
	 * ID of trackable. Currently used for GeoKrety web server
	 */
	public long id;
	/**
	 * name of travel bug
	 */
	public String name;
	/**
	 * image url to this travel bug
	 */
	public String imgUrl;
	/**
	 * URL to trackable object. This is very important value, because URL contain TBCode, like this
	 * http://www.geocaching.com/track/details.aspx?tracker=TB4342X
	 */
	public String srcDetails;
	
	/** 
	 * original owner of TB
	 */
	public String originalOwner;
	/** 
	 * current owner of TB
	 */
	public String currentOwner;
	/**
	 * time of release to public (long since 1.1.1970 in ms)
	 */
	public long released;
	/**
	 * origin place
	 */
	public String origin;
	/**
	 * goal of this TB
	 */
	public String goal;
	/**
	 * details 
	 */
	public String details;
	
	public GeocachingTrackable() {
		super();
	}
	
	public GeocachingTrackable(byte[] data) throws IOException {
		super(data);
	}

    /**************************************************/
    /*                  STORABLE PART                 */
	/**************************************************/
	
	@Override
	protected int getVersion() {
		return 1;
	}

	@Override
	protected void readObject(int version, DataInputStream dis)
			throws IOException {
		name = readStringUTF(dis);
		imgUrl = readStringUTF(dis);
		srcDetails = readStringUTF(dis);
		originalOwner = readStringUTF(dis);
		released = dis.readLong();
		origin = readStringUTF(dis);
		goal = readStringUTF(dis);
		details = readStringUTF(dis);
		if (version > 0) {
			id = dis.readLong();
			currentOwner = readStringUTF(dis);
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		writeStringUTF(dos, name);
		writeStringUTF(dos, imgUrl);
		writeStringUTF(dos, srcDetails);
		writeStringUTF(dos, originalOwner);
		dos.writeLong(released);
		writeStringUTF(dos, origin);
		writeStringUTF(dos, goal);
		writeStringUTF(dos, details);
		dos.writeLong(id);
		writeStringUTF(dos, currentOwner);
	}

	@Override
	public void reset() {
		id = 0L;
		name = "";
		imgUrl = "";
		srcDetails = "";
		
		originalOwner = "";
		currentOwner = "";
		released = 0L;
		origin = "";
		goal = "";
		details = "";
	}
	
	/**************************************************/
    /*                   UTILS PART                   */
	/**************************************************/
	
	public String getTbCode() {
		if (srcDetails == null || srcDetails.length() == 0) {
			return null;
		}
		if (srcDetails.startsWith("http://www.geocaching.com/track/details.aspx?tracker=")) {
			return srcDetails.substring(
					"http://www.geocaching.com/track/details.aspx?tracker=".length());
		}
		if (srcDetails.startsWith("http://coord.info/")) {
			return srcDetails.substring(
					"http://coord.info/".length());
		}
		return null;
		
	}
}