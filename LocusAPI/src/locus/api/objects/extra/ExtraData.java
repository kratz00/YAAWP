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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import locus.api.objects.Storable;
import locus.api.utils.Log;

public class ExtraData extends Storable {

	private static final String TAG = "ExtraData";

	// DATA SOURCE DEFINED IN PARAMETER 'PAR_SOURCE'
	
	/** source unknown or undefined */
	public static final int SOURCE_UNKNOWN = 0;
	/** special point for parking service */
	public static final int SOURCE_PARKING_SERVICE = 1;
	/** additional waypoint for geocache */
	public static final int SOURCE_GEOCACHING_WAYPOINT = 2;
	/** temporary point on map (not stored in database) */
	public static final int SOURCE_MAP_TEMP = 3;
	/** waypoint on route, location with some more values */
	public static final int SOURCE_ROUTE_WAYPOINT = 4;
	/** only location on route */
	public static final int SOURCE_ROUTE_LOCATION = 5;
	/** point coming from My Maps source */
	public static final int SOURCE_MY_MAPS = 6;
	/** point coming from OpenStreetBugs */
	public static final int SOURCE_OPENSTREETBUGS = 7;
	/** temporary item do not display on map */
	public static final int SOURCE_INVISIBLE = 8;
	/** items automatically loaded from OSM POI database */
	public static final int SOURCE_POI_OSM_DB = 9;
	
	// ROUTE TYPES DEFINED IN PARAMETER 'PAR_RTE_COMPUTE_TYPE'
	
	public static final int VALUE_RTE_TYPE_CAR = 6;
	public static final int VALUE_RTE_TYPE_CAR_FAST = 0;
	public static final int VALUE_RTE_TYPE_CAR_SHORT = 1;
	public static final int VALUE_RTE_TYPE_BICYCLE = 2;
	public static final int VALUE_RTE_TYPE_BICYCLE_FAST = 4;
	public static final int VALUE_RTE_TYPE_BICYCLE_SHORT = 5;
	public static final int VALUE_RTE_TYPE_FOOT = 3;
	
	// ROUTE ACTIONS DEFINED IN PARAMETER 'PAR_RTE_POINT_ACTION'

	/** No maneuver occurs here */
	public static final int VALUE_RTE_ACTION_NO_MANEUVER = 0;
	/** Continue straight */
	public static final int VALUE_RTE_ACTION_CONTINUE_STRAIGHT = 1;
	/** No maneuver occurs here. Road name changes */
	public static final int VALUE_RTE_ACTION_NO_MANEUVER_NAME_CHANGE = 2;
	/** Make a slight left */
	public static final int VALUE_RTE_ACTION_LEFT_SLIGHT = 3;
	/** Turn left */
	public static final int VALUE_RTE_ACTION_LEFT = 4;
	/** Make a sharp left */
	public static final int VALUE_RTE_ACTION_LEFT_SHARP = 5;
	/** Make a slight right */
	public static final int VALUE_RTE_ACTION_RIGHT_SLIGHT = 6;
	/** Turn right */
	public static final int VALUE_RTE_ACTION_RIGHT = 7;
	/** Make a sharp right */
	public static final int VALUE_RTE_ACTION_RIGHT_SHARP = 8;
	/** Stay left */
	public static final int VALUE_RTE_ACTION_STAY_LEFT = 9;
	/** Stay right */
	public static final int VALUE_RTE_ACTION_STAY_RIGHT = 10;
	/** Stay straight */
	public static final int VALUE_RTE_ACTION_STAY_STRAIGHT = 11;
	/** Make a U-turn */
	public static final int VALUE_RTE_ACTION_U_TURN = 12;
	/** Make a left U-turn */
	public static final int VALUE_RTE_ACTION_U_TURN_LEFT = 13;
	/** Make a right U-turn */
	public static final int VALUE_RTE_ACTION_U_TURN_RIGHT = 14;
	/** Exit left */
	public static final int VALUE_RTE_ACTION_EXIT_LEFT = 15;
	/** Exit right */
	public static final int VALUE_RTE_ACTION_EXIT_RIGHT = 16;
	/** Take the ramp on the left */
	public static final int VALUE_RTE_ACTION_RAMP_ON_LEFT = 17;
	/** Take the ramp on the right */
	public static final int VALUE_RTE_ACTION_RAMP_ON_RIGHT = 18;
	/** Take the ramp straight ahead */
	public static final int VALUE_RTE_ACTION_RAMP_STRAIGHT = 19;
	/** Merge left */
	public static final int VALUE_RTE_ACTION_MERGE_LEFT = 20;
	/** Merge right */
	public static final int VALUE_RTE_ACTION_MERGE_RIGHT = 21;
	/** Merge */
	public static final int VALUE_RTE_ACTION_MERGE = 22;
	/** Enter state/province */
	public static final int VALUE_RTE_ACTION_ENTER_STATE = 23;
	/** Arrive at your destination */
	public static final int VALUE_RTE_ACTION_ARRIVE_DEST = 24;
	/** Arrive at your destination on the left */
	public static final int VALUE_RTE_ACTION_ARRIVE_DEST_LEFT = 25;
	/** Arrive at your destination on the right */
	public static final int VALUE_RTE_ACTION_ARRIVE_DEST_RIGHT = 26;
	/** Enter the roundabout and take the 1st exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_1 = 27;
	/** Enter the roundabout and take the 2nd exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_2 = 28;
	/** Enter the roundabout and take the 3rd exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_3 = 29;
	/** Enter the roundabout and take the 4th exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_4 = 30;
	/** Enter the roundabout and take the 5th exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_5 = 31;
	/** Enter the roundabout and take the 6th exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_6 = 32;
	/** Enter the roundabout and take the 7th exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_7 = 33;
	/** Enter the roundabout and take the 8th exit */
	public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_8 = 34;
	/** Take a public transit bus or rail line */
	public static final int VALUE_RTE_ACTION_TRANSIT_TAKE = 35;
	/** Transfer to a public transit bus or rail line */
	public static final int VALUE_RTE_ACTION_TRANSIT_TRANSFER = 36;
	/** Enter a public transit bus or rail station */
	public static final int VALUE_RTE_ACTION_TRANSIT_ENTER = 37;
	/** Exit a public transit bus or rail station */
	public static final int VALUE_RTE_ACTION_TRANSIT_EXIT = 38;
	/** Remain on the current bus/rail car */
	public static final int VALUE_RTE_ACTION_TRANSIT_REMAIN_ON = 39;
	/** Pass POI */
	public static final int VALUE_RTE_ACTION_PASS_POI = 50;
	
	// PRIVATE REFERENCES (0 - 29)
	
	public static final int PAR_SOURCE = 0;
	public static final int PAR_MAP_ID = 1;
	public static final int PAR_MAP_EDIT_URI = 2;
	public static final int PAR_GOOGLE_PLACES_ID = 3;
	public static final int PAR_GOOGLE_PLACES_REFERENCE = 4;
	public static final int PAR_STYLE_NAME = 5;
	
	public static final int PAR_EXTRA_01 = 10;
	public static final int PAR_EXTRA_02 = 11;
	public static final int PAR_AREA_SIZE = 12;
	
	public static final int PAR_INTENT_EXTRA_CALLBACK = 20;
	public static final int PAR_INTENT_EXTRA_ON_DISPLAY = 21;

	// PUBLIC VALUES (30 - 49)
	
	// visible description
	public static final int PAR_DESCRIPTION = 30;
	// storage for comments
	public static final int PAR_COMMENT = 31;
	// relative path to working dir (for images for example)
	public static final int PAR_RELATIVE_WORKING_DIR = 32;

	// LOCATION PARAMETERS (50 - 59)
	
	public static final int PAR_STREET = 50;
	public static final int PAR_CITY = 51;
	public static final int PAR_REGION = 52;
	public static final int PAR_POST_CODE = 53;
	public static final int PAR_COUNTRY = 54;
	
	// ROUTE PARAMETERS (100 - 199)
	
	// PARAMETERS FOR NAVIGATION POINTS (WAYPOINT)
	
	/**
	 * Index to point list 
	 * <br />
	 * Locus internal variable, <b>DO NOT SET</b>
	 */
	public static final int PAR_RTE_INDEX = 100;
	/**
	 * Distance (in metres) from current navPoint to next 
	 * <br />
	 * Locus internal variable, <b>DO NOT SET</b>
	 */
	public static final int PAR_RTE_DISTANCE = 101;
	/**
	 * time (in sec) from current navPoint to next
	 */
	public static final int PAR_RTE_TIME = 102;
	/** 
	 * speed (in m/s) from current navPoint to next 
	 */
	public static final int PAR_RTE_SPEED = 103;
	/** 
	 *  Number of seconds to transition between successive links along
	 *  the route. These take into account the geometry of the intersection,
	 *  number of links at the intersection, and types of roads at
	 *  the intersection. This attempts to estimate the time in seconds it
	 *  would take for stops, or places where a vehicle must slow to make a turn.
	 */
	public static final int PAR_RTE_TURN_COST = 104;
	/**
	 * String representation of next street label
	 */
	public static final int PAR_RTE_STREET = 109;
	/**
	 * used to determine which type of action should be taken in order to stay on route
	 */
	public static final int PAR_RTE_POINT_ACTION = 110;

	// PARAMETERS FOR NAVIGATION ROUTE (TRACK)
	
	/** type of route (car_fast, car_short, cyclo, foot) */
	public static final int PAR_RTE_COMPUTE_TYPE = 120;
	/**
	 * Roundabout is usually defined from two points. First on enter correctly
	 * defined by ACTION 27 - 34, second on exit simply defined by exit angle.
	 * In case of usage only exit point, it's need to set this flag
	 */
	public static final int PAR_RTE_SIMPLE_ROUNDABOUTS = 121;
	
	// OSM BUGS (300 - 309)
	
	public static final int PAR_OSM_NOTES_ID = 301;
	public static final int PAR_OSM_NOTES_CLOSED = 302;
	
	// PHONES (1000 - 1099)
	
	private static final int PAR_PHONES_MIN = 1000;
	private static final int PAR_PHONES_MAX = 1099;
	
	// EMAILS (1100 - 1199)
	
	private static final int PAR_EMAILS_MIN = 1100;
	private static final int PAR_EMAILS_MAX = 1199;
	
	// URLS (1200 - 1299)
	
	private static final int PAR_URLS_MIN = 1200;
	private static final int PAR_URLS_MAX = 1299;
	
	// PHOTOS (1300 - 1399)
	
	private static final int PAR_PHOTOS_MIN = 1300;
	private static final int PAR_PHOTOS_MAX = 1399;
	
	// OTHER FILES (1800 - 1999)
	private static final int PAR_OTHER_FILES_MIN = 1800;
	private static final int PAR_OTHER_FILES_MAX = 1999;
	
	/** table for additional parameters */
	Hashtable<Integer, String> parameters;

	public static class LabelTextContainer {
		
		public final String label;
		public final String text;
		
		public LabelTextContainer(String value) {
			if (value.contains("|")) {
				int index = value.indexOf("|");
				this.label = value.substring(0, index);
				this.text = value.substring(index + 1);
			} else {
				this.label = "";
				this.text = value;
			}
		}
		
		public LabelTextContainer(String label, String text) {
			if (label == null) {
				this.label = "";
			} else {
				this.label = label;
			}
			this.text = text;
		}
		
		public String getAsText() {
			if (label.length() > 0) {
				return label + "|" + text;
			} else {
				return text;
			}
		}
	}

	public ExtraData() {
		super();
	}
	
	public ExtraData(DataInputStream dis) throws IOException {
		super(dis);
	}
	
	public ExtraData(byte[] data) throws IOException {
		super(data);
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
		int size = dis.readInt();
		parameters.clear();
		for (int i = 0; i < size; i++) {
			Integer key = dis.readInt();
			String value = readStringUTF(dis);
			parameters.put(key, value);
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		dos.writeInt(parameters.size());
		Enumeration<Integer> keys = parameters.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			dos.writeInt(key);
			writeStringUTF(dos, parameters.get(key));
		}
	}

	@Override
	public void reset() {
		parameters = new Hashtable<Integer, String>();
	}
	
    /*******************************************/
    /*             HANDLERS PART               */
    /*******************************************/
	
	public boolean addParameter(int key, String value) {
//Logger.d(TAG, "addParameter(" + key + ", " + value + ")");
		// check on 'null' value
		if (value == null) {
			return false;
		}
		// remove previous parameter
		removeParameter(key);
		
		// trim new value and insert into table
		value = value.trim();
		if (value.length() == 0) {
			return false;
		}
		if (key > 1000 && key < 2000) {
			Log.e(TAG, "addParam(" + key + ", " + value + "), values 1000 - 1999 reserved!");
			return false;
		}
		parameters.put(key, value);
		return true;
	}
	
	public String getParameter(int key) {
		return parameters.get(key);
	}
	
	public boolean hasParameter(int key) {
		return parameters.get(key) != null;
	}
	
	public String removeParameter(int key) {
		return parameters.remove(key);
	}
	
	public int getCount() {
		return parameters.size();
	}
	
	// PHONE
	
	public boolean addPhone(String phone) {
		return addToStorage("", phone, PAR_PHONES_MIN, PAR_PHONES_MAX);
	}
	
	public boolean addPhone(String label, String phone) {
		return addToStorage(label, phone, PAR_PHONES_MIN, PAR_PHONES_MAX);
	}
	
	public ArrayList<LabelTextContainer> getPhones() {
		return getFromStorage(PAR_PHONES_MIN, PAR_PHONES_MAX);
	}
	
	public boolean removePhone(String phone) {
		return removeFromStorage(phone, PAR_PHONES_MIN, PAR_PHONES_MAX);
	}
	
	public void removeAllPhones() {
		removeAllFromStorage(PAR_PHONES_MIN, PAR_PHONES_MAX);
	}
	
	// EMAIL
	
	public boolean addEmail(String email) {
		return addToStorage("", email, PAR_EMAILS_MIN, PAR_EMAILS_MAX);
	}
	
	public boolean addEmail(String label, String email) {
		return addToStorage(label, email, PAR_EMAILS_MIN, PAR_EMAILS_MAX);
	}
	
	public ArrayList<LabelTextContainer> getEmails() {
		return getFromStorage(PAR_EMAILS_MIN, PAR_EMAILS_MAX);
	}
	
	public boolean removeEmail(String email) {
		return removeFromStorage(email, PAR_EMAILS_MIN, PAR_EMAILS_MAX);
	}
	
	public void removeAllEmails() {
		removeAllFromStorage(PAR_EMAILS_MIN, PAR_EMAILS_MAX);
	}
	
	// URL
	
	public boolean addUrl(String url) {
		return addToStorage("", url, PAR_URLS_MIN, PAR_URLS_MAX);
	}
	
	public boolean addUrl(String label, String url) {
		return addToStorage(label, url, PAR_URLS_MIN, PAR_URLS_MAX);
	}
	
	public ArrayList<LabelTextContainer> getUrls() {
		return getFromStorage(PAR_URLS_MIN, PAR_URLS_MAX);
	}
	
	public boolean removeUrl(String url) {
		return removeFromStorage(url, PAR_URLS_MIN, PAR_URLS_MAX);
	}
	
	public void removeAllUrls() {
		removeAllFromStorage(PAR_URLS_MIN, PAR_URLS_MAX);
	}
	
	// PHOTO
	
	public boolean addPhoto(String photo) {
		return addToStorage("", photo, PAR_PHOTOS_MIN, PAR_PHOTOS_MAX);
	}
	
	public ArrayList<String> getPhotos() {
		return convertToTexts(getFromStorage(PAR_PHOTOS_MIN, PAR_PHOTOS_MAX));
	}
	
	public boolean removePhoto(String photo) {
		return removeFromStorage(photo, PAR_PHOTOS_MIN, PAR_PHOTOS_MAX);
	}

	// OTHER FILES
	
	public boolean addOtherFile(String filpath) {
		return addToStorage("", filpath, PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX);
	}
	
	public ArrayList<String> getOtherFiles() {
		return convertToTexts(getFromStorage(PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX));
	}
	
	public boolean removeOtherFile(String filpath) {
		return removeFromStorage(filpath, PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX);
	}
	
	// PRIVATE TOOLS

	private boolean addToStorage(String label, String text, int rangeFrom, int rangeTo) {
		// check text
		if (text == null || text.length() == 0) {
			return false;
		}
		
		// create item
		String item = null;
		if (label != null && label.length() > 0) {
			item = label + "|" + text;
		} else {
			item = text;
		}
		
		for (int i = rangeFrom; i <= rangeTo; i++) {
			String value = parameters.get(i);
			if (value == null) {
				parameters.put(i, item);
				return true;
			} else if (value.equalsIgnoreCase(item)) {
				// item already exists
				return false;
			} else {
				// some other item already included, move to next index
			}
		}
		return false;
	}
	
	private ArrayList<LabelTextContainer> getFromStorage(int rangeFrom, int rangeTo) {
		ArrayList<LabelTextContainer> data = new ArrayList<LabelTextContainer>();
		for (int i = rangeFrom; i <= rangeTo; i++) {
			String value = parameters.get(i);
			if (value == null || value.length() == 0) {
				continue;
			}
			
			// extract data
			data.add(new LabelTextContainer(value));
		}
		return data;
	}
	
	private ArrayList<String> convertToTexts(ArrayList<LabelTextContainer> data) {
		ArrayList<String> result = new ArrayList<String>();
		for (LabelTextContainer ltc : data) {
			result.add(ltc.text);
		}
		return result;
	}
	
	private boolean removeFromStorage(String item, int rangeFrom, int rangeTo) {
		// check text
		if (item == null || item.length() == 0) {
			return false;
		}
		
		for (int i = rangeFrom; i <= rangeTo; i++) {
			String value = parameters.get(i);
			if (value == null) {
				// no item
			} else if (value.endsWith(item)) {
				parameters.remove(i);
				return true;
			} else {
				// some other item already included, move to next index
			}
		}
		return false;
	}
	
	private void removeAllFromStorage(int rangeFrom, int rangeTo) {
		for (int i = rangeFrom; i <= rangeTo; i++) {
			parameters.remove(i);
		}
	}
}
