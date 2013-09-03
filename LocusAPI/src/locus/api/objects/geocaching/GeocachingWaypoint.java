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
import locus.api.utils.Utils;

public class GeocachingWaypoint extends Storable {
	
	public static final String CACHE_WAYPOINT_TYPE_QUESTION = "Question to Answer";
	public static final String CACHE_WAYPOINT_TYPE_FINAL = "Final Location";
	public static final String CACHE_WAYPOINT_TYPE_PARKING = "Parking Area";
	public static final String CACHE_WAYPOINT_TYPE_TRAILHEAD = "Trailhead";
	public static final String CACHE_WAYPOINT_TYPE_STAGES = "Stages of a Multicache";
	public static final String CACHE_WAYPOINT_TYPE_REFERENCE = "Reference Point";
	
	/* code of wpt */
	public String code;
	/* name of waypoint */
	public String name;
	/* description (may be HTML code) */
	public String desc;
	/* type of waypoint (defined in PointGeocachingData) */
	public String type;
	/* image URL to this wpt (not needed) */
	public String typeImagePath;
	/* longitude of waypoint */
	public double lon;
	/* latitude of waypoint */
	public double lat;

	public GeocachingWaypoint() {
		super();
	}

	@Override
	public String toString() {
		return Utils.toString(this);
	}
	
    /*******************************************/
    /*             STORABLE PART               */
    /*******************************************/
	
	@Override
	protected int getVersion() {
		return 0;
	}

	@Override
	protected void readObject(int version, DataInputStream dis)
			throws IOException {
		code = readStringUTF(dis);
		name = readStringUTF(dis);
		desc = readStringUTF(dis);
		type = readStringUTF(dis);
		typeImagePath = readStringUTF(dis);
		lon = dis.readDouble();
		lat = dis.readDouble();
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		writeStringUTF(dos, code);
		writeStringUTF(dos, name);
		writeStringUTF(dos, desc);
		writeStringUTF(dos, type);
		writeStringUTF(dos, typeImagePath);
		dos.writeDouble(lon);
		dos.writeDouble(lat);
	}

	@Override
	public void reset() {
		code = "";
		name = "";
		desc = "";
		type = "";
		typeImagePath = "";
		lon = 0.0;
		lat = 0.0;
	}
}