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

public class GeocachingLog extends Storable {
	
	public static final int CACHE_LOG_TYPE_UNKNOWN = -1;
	public static final int CACHE_LOG_TYPE_FOUNDED = 0;
	public static final int CACHE_LOG_TYPE_NOT_FOUNDED = 1;
	public static final int CACHE_LOG_TYPE_WRITE_NOTE = 2;
	public static final int CACHE_LOG_TYPE_NEEDS_MAINTENANCE = 3;
	public static final int CACHE_LOG_TYPE_OWNER_MAINTENANCE = 4;
	public static final int CACHE_LOG_TYPE_PUBLISH_LISTING = 5;
	public static final int CACHE_LOG_TYPE_ENABLE_LISTING = 6;
	public static final int CACHE_LOG_TYPE_TEMPORARILY_DISABLE_LISTING = 7;
	public static final int CACHE_LOG_TYPE_UPDATE_COORDINATES = 8;
	public static final int CACHE_LOG_TYPE_ANNOUNCEMENT = 9;
	public static final int CACHE_LOG_TYPE_WILL_ATTEND = 10;
	public static final int CACHE_LOG_TYPE_ATTENDED = 11;
	public static final int CACHE_LOG_TYPE_POST_REVIEWER_NOTE = 12;
	public static final int CACHE_LOG_TYPE_NEEDS_ARCHIVED = 13;
	public static final int CACHE_LOG_TYPE_WEBCAM_PHOTO_TAKEN = 14;
	public static final int CACHE_LOG_TYPE_RETRACT_LISTING = 15;
	
	public long id;
	public int type;
	public long date;
	public String finder;
	public int finderFound;
	public String logText;

	public GeocachingLog() {
		super();
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
		id = dis.readLong();
		type = dis.readInt();
		date = dis.readLong();
		finder = readStringUTF(dis);
		finderFound = dis.readInt();
		logText = readStringUTF(dis);
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		dos.writeLong(id);
		dos.writeInt(type);
		dos.writeLong(date);
		writeStringUTF(dos, finder);
		dos.writeInt(finderFound);
		writeStringUTF(dos, logText);
	}

	@Override
	public void reset() {
		id = 0;
		type = CACHE_LOG_TYPE_UNKNOWN;
		date = 0L;
		finder = "";
		finderFound = 0;
		logText = "";
	}
}