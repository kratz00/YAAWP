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

package locus.api.objects.geocaching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import locus.api.objects.Storable;

public class GeocachingData extends Storable {
	
	public static final int CACHE_NUMBER_OF_TYPES = 20;
	
	public static final int CACHE_TYPE_TRADITIONAL = 0;
	public static final int CACHE_TYPE_MULTI = 1;
	public static final int CACHE_TYPE_MYSTERY = 2;
	public static final int CACHE_TYPE_VIRTUAL = 3;
	public static final int CACHE_TYPE_EARTH = 4;
	public static final int CACHE_TYPE_PROJECT_APE = 5;
	public static final int CACHE_TYPE_LETTERBOX = 6;
	public static final int CACHE_TYPE_WHERIGO = 7;
	public static final int CACHE_TYPE_EVENT = 8;
	public static final int CACHE_TYPE_MEGA_EVENT = 9;
	public static final int CACHE_TYPE_CACHE_IN_TRASH_OUT = 10;
	public static final int CACHE_TYPE_GPS_ADVENTURE = 11;
	public static final int CACHE_TYPE_WEBCAM = 12;
	public static final int CACHE_TYPE_LOCATIONLESS = 13;
	public static final int CACHE_TYPE_BENCHMARK = 14;
	public static final int CACHE_TYPE_MAZE_EXHIBIT = 15;
	public static final int CACHE_TYPE_WAYMARK = 16;
	public static final int CACHE_TYPE_GROUNDSPEAK = 17;
	public static final int CACHE_TYPE_LF_EVENT = 18;
	public static final int CACHE_TYPE_LF_CELEBRATION = 19;

	public static final int CACHE_SIZE_NOT_CHOSEN = 0;
	public static final int CACHE_SIZE_MICRO = 1;
	public static final int CACHE_SIZE_SMALL = 2;
	public static final int CACHE_SIZE_REGULAR = 3;
	public static final int CACHE_SIZE_LARGE = 4;
	public static final int CACHE_SIZE_HUGE = 5;
	public static final int CACHE_SIZE_OTHER = 6;
	
	/*
	 * INFO
	 * 
	 * all times are in format '2009-09-22T14:16:03.0000000+0200', where important is only first 
	 * part (Date), so it should looks for example only as '2009-09-22T'. This should work
	 */
	
	/* id of point - not needed as I remember */
	private long id;
	/**
	 * <font color="red">REQUIRED!</font><br/><br/>
	 * whole cache ID from gc.com - so GC...
	 **/
	private String cacheID;
	/* is available or disable */
	public boolean available;
	/* cache already archived or not */
	public boolean archived;
	/* available only for premium members */
	public boolean premiumOnly;
	/**
	 * <font color="red">REQUIRED!</font><br/><br/>
	 * name of cache visible on all places in Locus as an title
	 */
	private String name;
	/* time of last updated time (long since 1.1.1970 in ms) */
	public long lastUpdated;
	/* String with date of last exported - groundspeak:exported */
	public long dateCreated;
	/**
	 * name of person who placed cache (groundspeak:placed_by)
	 * <br /><br />
	 * - displayed in Locus when tapped on point or in main GC page
	 */
	private String placedBy;
	/**
	 * name of cache owner (groundspeak:owner)
	 * <br /><br />
	 * - this value is not displayed in locus
	 */
	private String owner;
	/* String with date of hidden - value from CachePrinter */
	public long hidden;
	/* cache type */
	public int type;
	/* container size */
	public int container;
	/* dificulty value - 1.0 - 5.0 (by 0.5) */
	public float difficulty;
	/* terrain value - 1.0 - 5.0 (by 0.5) */ 
	public float terrain;
	/* country name */
	private String country;
	/* state name */
	private String state;
	/* short description of cache */
	private String shortDescription;
	/* full description with complete (HTML) listing */
	private String longDescription;
	/* encoded hints */
	private String encodedHints;
	/* list of attributes */
	public ArrayList<GeocachingAttribute> attributes;
	/* list of logs */
	public ArrayList<GeocachingLog> logs;
	/* list of travel bugs */
	public ArrayList<GeocachingTrackable> trackables;
	/* list of waypoints */
	public ArrayList<GeocachingWaypoint> waypoints;
	/* user notes */
	private String notes;
	/* if cache is already computed - have final waypoint and is placed on it's location */
	public boolean computed;
	/* if cache is already found */
	public boolean found;
	/* url for cache itself */
	private String cacheUrl;
	/* number of favorite points */
	public int favoritePoints;
	
	// object V2
	
	/* GCVote - number of votes */
	public int gcVoteNumOfVotes;
	/* average (not median) value */
	public float gcVoteAverage;
	/* user value for GCVote */
	public float gcVoteUserVote;
	
	public GeocachingData() {
		super();
	}
	
	public GeocachingData(byte[] data) throws IOException {
		super(data);
	}
	
    /*******************************************/
    /*             STORABLE PART               */
    /*******************************************/
	
	@Override
	protected int getVersion() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void readObject(int version, DataInputStream dis)
			throws IOException {
		id = dis.readLong();
		cacheID = readStringUTF(dis);
		available = dis.readBoolean();
		archived = dis.readBoolean();
		premiumOnly = dis.readBoolean();
		name = readStringUTF(dis);
		lastUpdated = dis.readLong();
		dateCreated = dis.readLong();
		placedBy = readStringUTF(dis);
		owner = readStringUTF(dis);
		hidden = dis.readLong();
		type = dis.readInt();
		container = dis.readInt();
		difficulty = dis.readFloat();
		terrain = dis.readFloat();
		country = readStringUTF(dis);
		state = readStringUTF(dis);
		
		int size = dis.readInt();
		int lengthSD = dis.readInt();
		 		
		byte[] data = new byte[size];
		dis.read(data);

		GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(data), 10240);
		StringBuffer buffer = new StringBuffer();
		 		
		InputStreamReader isr = new InputStreamReader(zis, "UTF-8");
		char[] dataD = new char[1024];
		int charsRead;
		while ((charsRead = isr.read(dataD)) != -1) {
			buffer.append(dataD, 0, charsRead);
		}
		String result = buffer.toString();
		isr.close();
		
		// read short description
		if (lengthSD > 0) {
			shortDescription = result.substring(0, lengthSD);
		} else {
			shortDescription = "";
		}
		
		// read long description
		longDescription = result.substring(lengthSD);

		encodedHints = readStringUTF(dis);
		attributes = (ArrayList<GeocachingAttribute>) readList(GeocachingAttribute.class, dis);
		logs = (ArrayList<GeocachingLog>) readList(GeocachingLog.class, dis);
		trackables = (ArrayList<GeocachingTrackable>) readList(GeocachingTrackable.class, dis);
		waypoints = (ArrayList<GeocachingWaypoint>) readList(GeocachingWaypoint.class, dis);
		notes = readStringUTF(dis);
		computed = dis.readBoolean();
		found = dis.readBoolean();
		cacheUrl = readStringUTF(dis);
		favoritePoints = dis.readInt();
		
		// V1
		if (version >= 1) {
			gcVoteNumOfVotes = dis.readInt();
			gcVoteAverage = dis.readFloat();
			gcVoteUserVote = dis.readFloat();
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		dos.writeLong(id);
		writeStringUTF(dos, cacheID);
		dos.writeBoolean(available);
		dos.writeBoolean(archived);
		dos.writeBoolean(premiumOnly);
		writeStringUTF(dos, name);
		dos.writeLong(lastUpdated);
		dos.writeLong(dateCreated);
		writeStringUTF(dos, placedBy);
		writeStringUTF(dos, owner);
		dos.writeLong(hidden);
		dos.writeInt(type);
		dos.writeInt(container);
		dos.writeFloat(difficulty);
		dos.writeFloat(terrain);
		writeStringUTF(dos, country);
		writeStringUTF(dos, state);

		// write listings
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream zos = new GZIPOutputStream(baos);
			
		zos.write(shortDescription.getBytes("utf-8"));
		zos.write(longDescription.getBytes("utf-8"));
		zos.close();
			
		byte[] data = baos.toByteArray();
		baos.close();

		dos.writeInt(data.length);
		dos.writeInt(shortDescription.length());
		dos.write(data);

		// write rest
		writeStringUTF(dos, encodedHints);
		writeList(attributes, dos);
		writeList(logs, dos);
		writeList(trackables, dos);
		writeList(waypoints, dos);
		writeStringUTF(dos, notes);
		dos.writeBoolean(computed);
		dos.writeBoolean(found);
		writeStringUTF(dos, cacheUrl);
		dos.writeInt(favoritePoints);
		
		// V1
		dos.writeInt(gcVoteNumOfVotes);
		dos.writeFloat(gcVoteAverage);
		dos.writeFloat(gcVoteUserVote);
	}

	@Override
	public void reset() {
		id = 0;
		cacheID = "";
		available = true;
		archived = false;
		premiumOnly = false;
		name = "";
		lastUpdated = 0L;
		dateCreated = 0L;
		placedBy = "";
		owner = "";
		hidden = 0L;
		type = CACHE_TYPE_TRADITIONAL;
		container = CACHE_SIZE_NOT_CHOSEN;
		difficulty = -1.0f;
		terrain = -1.0f;
		country = "";
		state = "";
		shortDescription = "";
		longDescription = "";
		encodedHints = "";
		attributes = new ArrayList<GeocachingAttribute>();
		logs = new ArrayList<GeocachingLog>();
		trackables = new ArrayList<GeocachingTrackable>();
		waypoints = new ArrayList<GeocachingWaypoint>();
		notes = "";
		computed = false;
		found = false;
		cacheUrl = "";
		favoritePoints = -1;
		
		// V1
		gcVoteNumOfVotes = -1;
		gcVoteAverage = 0.0f;
		gcVoteUserVote = 0.0f;
	}
	
    /*******************************************/
    /*               TOOLS PART                */
    /*******************************************/
	
	// ID
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	// cache ID
	public String getCacheID() {
		return cacheID;
	}
	public void setCacheID(String cacheID) {
		if (cacheID != null && cacheID.length() > 0)
			this.cacheID = cacheID;
	}
	
	// name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name != null && name.length() > 0)
			this.name = name;
	}
	
	// placed by
	public String getPlacedBy() {
		return placedBy;
	}
	public void setPlacedBy(String placedBy) {
		if (placedBy != null && placedBy.length() > 0)
			this.placedBy = placedBy;
	}
	
	// owner
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		if (owner != null && owner.length() > 0) {
			this.owner = owner;
		}
	}
	
	// cache type
	public void setType(String type) {
		this.type = getTypeAsInt(type);
	}
	
	// cache container
	public String getContainerText() {
		switch (container) {
		case CACHE_SIZE_MICRO:
			return "Micro";
		case CACHE_SIZE_SMALL:
			return "Small";
		case CACHE_SIZE_REGULAR:
			return "Regular";
		case CACHE_SIZE_LARGE:
			return "Large";
		case CACHE_SIZE_HUGE:
			return "Huge";
		case CACHE_SIZE_NOT_CHOSEN:
			return "Not chosen";
		case CACHE_SIZE_OTHER:
			return "Other";
		}
		return null;
	}
	public void setContainer(int container) {
		this.container = container;
	}
	public void setContainer(String container) {
		if (container.equalsIgnoreCase("Micro")) {
			setContainer(CACHE_SIZE_MICRO);
		} else if (container.equalsIgnoreCase("Small")) {
			setContainer(CACHE_SIZE_SMALL);
		} else if (container.equalsIgnoreCase("Regular")) {
			setContainer(CACHE_SIZE_REGULAR);
		} else if (container.equalsIgnoreCase("Large")) {
			setContainer(CACHE_SIZE_LARGE);
		} else if (container.equalsIgnoreCase("Huge")) {
			setContainer(CACHE_SIZE_HUGE);
		} else if (container.equalsIgnoreCase("Not chosen")) {
			setContainer(CACHE_SIZE_NOT_CHOSEN);
		} else if (container.equalsIgnoreCase("Other")) {
			setContainer(CACHE_SIZE_OTHER);
		}
	}
	
	// country
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		if (country != null && country.length() > 0) {
			this.country = country;
		}
	}
	
	// state
	public String getState() {
		return state;
	}
	public void setState(String state) {
		if (state != null && state.length() > 0) {
			this.state = state;
		}
	}
	
	// short description
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription, boolean isInHtml) {
		if (shortDescription != null && shortDescription.length() > 0) {
			//this.shortDescription = UtilsHttp.repairHtmlFile(shortDescription);
			if (isInHtml) {
				this.shortDescription = shortDescription;
			} else {
				this.shortDescription = fixToHtml(shortDescription);
			}
		}
	}
	
	private String fixToHtml(String text) {
		try {
			String result = text.replace("\n", "<br />");
			result = result.replace("  ", "&nbsp;&nbsp;");
			return result;
		} catch (Exception e) {
			return text;
		}
	}
	
	// long description
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription, boolean isInHtml) {
		if (longDescription != null && longDescription.length() > 0) {
			if (isInHtml) {
				this.longDescription = longDescription.replace("\n", " ");
			} else {
				this.longDescription = fixToHtml(longDescription);
			}
		}
	}
	
	// encodedHints
	public String getEncodedHints() {
		return encodedHints;
	}
	public void setEncodedHints(String hints) {
		if (hints != null && hints.length() > 0) {
			this.encodedHints = hints;
		}
	}

	// notes
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		if (notes == null)
			return;
		this.notes = notes;
	}
	
	// cache URL
	public void setCacheUrl(String url) {
		if (url != null && url.length() > 0)
			cacheUrl = url;
	}
	
	/******************************/
	/*           UTILS            */
	/******************************/

	public boolean isCacheValid() {
		return cacheID.length() > 0 && name.length() > 0;
	}

	public void sortTrackables() {
		if (trackables.size() <= 1)
			return;
		
		Collections.sort(trackables, new Comparator<GeocachingTrackable>() {
			public int compare(GeocachingTrackable object1,
					GeocachingTrackable object2) {
				return object1.name.compareTo(object2.name);
			}
		});
	}

	public boolean isGroundspeakGc() {
		return cacheID.toLowerCase().startsWith("gc");
	}
	
	public String getCacheUrlFull() {
		if (isGroundspeakGc()) {
			return "http://coord.info/" + cacheID;
		}
		if (cacheUrl != null && cacheUrl.length() > 0) {
			return cacheUrl;
		}
		return "http://www.geocaching.com/seek/cache_details.aspx?wp=" + cacheID;
	}

	public static String getTypeAsString(int type) {
		switch (type) {
		case GeocachingData.CACHE_TYPE_TRADITIONAL:
			return "Traditional Cache";
		case GeocachingData.CACHE_TYPE_MULTI:
			return "Multi-Cache";
		case GeocachingData.CACHE_TYPE_MYSTERY:
			return "Unknown Cache";
		case GeocachingData.CACHE_TYPE_VIRTUAL:
			return "Virtual Cache";
		case GeocachingData.CACHE_TYPE_EARTH:
			return "EarthCache";
		case GeocachingData.CACHE_TYPE_PROJECT_APE:
			return "Project APE Cache";
		case GeocachingData.CACHE_TYPE_LETTERBOX:
			return "Letterbox";
		case GeocachingData.CACHE_TYPE_WHERIGO:
			return "Wherigo Cache";
		case GeocachingData.CACHE_TYPE_EVENT:
			return "Event Cache";
		case GeocachingData.CACHE_TYPE_MEGA_EVENT:
			return "Mega-Event Cache";
		case GeocachingData.CACHE_TYPE_CACHE_IN_TRASH_OUT:
			return "Cache In Trash Out Event";
		case GeocachingData.CACHE_TYPE_GPS_ADVENTURE:
			return "GPS Adventure";
		case GeocachingData.CACHE_TYPE_WEBCAM:
			return "Webcam Cache";
		case GeocachingData.CACHE_TYPE_LOCATIONLESS:
			return "Location-less";
		case GeocachingData.CACHE_TYPE_BENCHMARK:
			return "Benchmark";
		case GeocachingData.CACHE_TYPE_MAZE_EXHIBIT:
			return "Maze Exhibit";
		case GeocachingData.CACHE_TYPE_WAYMARK:
			return "Waymark";
		case GeocachingData.CACHE_TYPE_GROUNDSPEAK:
			return "Groundspeak";
		case GeocachingData.CACHE_TYPE_LF_EVENT:
			return "L&F Event";
		case GeocachingData.CACHE_TYPE_LF_CELEBRATION:
			return "L&F Celebration";
		default:
			return "Geocache";
		}
	}

	public static int getTypeAsInt(String type) {
		if (type == null) {
			return -1;
		} else if (type.equalsIgnoreCase("Traditional Cache")) {
			return CACHE_TYPE_TRADITIONAL;
		} else if (type.equalsIgnoreCase("Multi-cache")) {
			return CACHE_TYPE_MULTI;
		} else if (type.equalsIgnoreCase("Unknown Cache") || type.equalsIgnoreCase("Mystery/Puzzle Cache")) {
			return CACHE_TYPE_MYSTERY;
		} else if (type.equalsIgnoreCase("Project A.P.E. Cache")) {
			return CACHE_TYPE_PROJECT_APE;
		} else if (type.equalsIgnoreCase("Letterbox Hybrid") || type.equalsIgnoreCase("Letterbox")) {
			return CACHE_TYPE_LETTERBOX;
		} else if (type.equalsIgnoreCase("Wherigo") || type.equalsIgnoreCase("Wherigo cache")) {
			return CACHE_TYPE_WHERIGO;
		} else if (type.equalsIgnoreCase("Event Cache")) {
			return CACHE_TYPE_EVENT;
		} else if (type.equalsIgnoreCase("Mega-Event Cache")) {
			return CACHE_TYPE_MEGA_EVENT;
		} else if (type.equalsIgnoreCase("Cache In Trash Out Event")) {
			return CACHE_TYPE_CACHE_IN_TRASH_OUT;
		} else if (type.equalsIgnoreCase("EarthCache")) {
			return CACHE_TYPE_EARTH;
		} else if (type.equalsIgnoreCase("GPS Adventures Maze Exhibit")) {
			return CACHE_TYPE_GPS_ADVENTURE;
		} else if (type.equalsIgnoreCase("Virtual Cache")) {
			return CACHE_TYPE_VIRTUAL;
		} else if (type.equalsIgnoreCase("Webcam Cache")) {
			return CACHE_TYPE_WEBCAM;
		} else if (type.equalsIgnoreCase("Locationless Cache")) {
			return CACHE_TYPE_LOCATIONLESS;
		} else if (type.equalsIgnoreCase("Benchmark")) {
			return CACHE_TYPE_BENCHMARK;
		} else if (type.equalsIgnoreCase("Maze Exhibit")) {
			return CACHE_TYPE_MAZE_EXHIBIT;
		} else if (type.equalsIgnoreCase("Waymark")) {
			return CACHE_TYPE_WAYMARK;
		} else if (type.equalsIgnoreCase("Groundspeak")) {
			return CACHE_TYPE_GROUNDSPEAK;
		} else if (type.equalsIgnoreCase("L&F Event")) {
			return CACHE_TYPE_LF_EVENT;
		} else if (type.equalsIgnoreCase("L&F Celebration")) {
			return CACHE_TYPE_LF_CELEBRATION;
		} else {
			return -1;
		}
	}
	
	public boolean containInData(String text) {
		if (owner.toLowerCase().contains(text)) {
			return true;
		}
		if (country.toLowerCase().contains(text)) {
			return true;
		}
		if (state.toLowerCase().contains(text)) {
			return true;
		}
		if (shortDescription.toLowerCase().contains(text)) {
			return true;
		}
		if (longDescription.toLowerCase().contains(text)) {
			return true;
		}
		return false;
	}
}
