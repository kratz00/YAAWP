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

package locus.api.android.utils;

public class LocusConst {

	/****************** 
	 * these intent are used for extending functionality of Locus. All description is 
	 * in 'LocusUtils' class 
	 ******************/
	
	/**************************************************/
	/*                ITEMS IN MENUS                  */
	/**************************************************/
	
	/**
	 * Add your activity in list of "Location sources"
	 */
	public static final String INTENT_ITEM_GET_LOCATION = "locus.api.android.INTENT_ITEM_GET_LOCATION";
	
	/**
	 * Add your activity in sub-menu in waypoint dialog (last button > tools)
	 */
	public static final String INTENT_ITEM_POINT_TOOLS = "locus.api.android.INTENT_ITEM_POINT_TOOLS";

	/**
	 * This Filter add your activity into list of main functions
	 */
	public static final String INTENT_ITEM_MAIN_FUNCTION = "locus.api.android.INTENT_ITEM_MAIN_FUNCTION";

	/**
	 * This Filter add your activity into list of search options in Locus "Search" function
	 */
	public static final String INTENT_ITEM_SEARCH_LIST = "locus.api.android.INTENT_ITEM_SEARCH_LIST";
	
	/**
	 * This Filter add your activity into list of tools in Points screen
	 */
	public static final String INTENT_ITEM_POINTS_SCREEN_TOOLS = "locus.api.android.INTENT_ITEM_POINTS_SCREEN_TOOLS";
	
	/**************************************************/
	/*                  BASIC TASKS                   */
	/**************************************************/
	
	/**
	 * Intent used for getting location from Locus to your application. This one, is used just to start
	 * Locus with this request.
	 */
	public static final String ACTION_PICK_LOCATION = "locus.api.android.ACTION_PICK_LOCATION";

	/**
	 * Action used for receiving Location from Locus
	 */
	public static final String ACTION_RECEIVE_LOCATION = "locus.api.android.ACTION_RECEIVE_LOCATION";
	
	/**
	 * Intent that allow to send WMS url link directly into Locus
	 * Available since Locus 2.4.1 (code 216)
	 */
	public static final String ACTION_ADD_NEW_WMS_MAP = "locus.api.android.ACTION_ADD_NEW_WMS_MAP";
	
	/** 
	 * start navigation
	 */
	public static final String ACTION_NAVIGATION_START = "locus.api.android.ACTION_NAVIGATION_START";
	
	/** 
	 * start navigation
	 */
	public static final String ACTION_GUIDING_START = "locus.api.android.ACTION_GUIDING_START";

	/**************************************************/
	/*               BASIC DATA HANDLING              */
	/**************************************************/
	
	/**
	 * Basic intent used for display data. Use API for creating intent and not directly
	 */
	public static final String ACTION_DISPLAY_DATA = "locus.api.android.ACTION_DISPLAY_DATA";

	/**
	 * Used for sending data to Locus. These data should be small (and fast).
	 * <br /><br />
	 * Content is same as with sending data by INTENT_DISPLAY_DATA anyway think mainly on best user
	 * experience!! So suggestion is to send intent filled by EXTRA_POINTS_DATA or EXTRA_POINTS_DATA_ARRAY.
	 * These are fastest methods and should be enough for sending useful amount of informations
	 * <br /><br />
	 * Also, these data will have automatically EXTRA_CALL_IMPORT set to false, so they'll be directly
	 * displayed on map without possibility for import! Also after display, map will not be centered.  
	 */
	public static final String ACTION_DISPLAY_DATA_SILENTLY = "locus.api.android.ACTION_DISPLAY_DATA_SILENTLY";
	
	/**
	 * Basic intent used for hiding previously displayed items. This method allow to hide only temp items!
	 */
	public static final String ACTION_REMOVE_DATA_SILENTLY = "locus.api.android.ACTION_REMOVE_DATA_SILENTLY";
	
	/**************************************************/
	/*               BROADCAST INTENTS                */
	/**************************************************/
	
	/**
	 * start track record
	 */
	public static final String ACTION_TRACK_RECORD_START = "locus.api.android.ACTION_TRACK_RECORD_START";
	
	/**
	 * pause track record
	 */
	public static final String ACTION_TRACK_RECORD_PAUSE = "locus.api.android.ACTION_TRACK_RECORD_PAUSE";
	
	/**
	 * stop track record
	 */
	public static final String ACTION_TRACK_RECORD_STOP = "locus.api.android.ACTION_TRACK_RECORD_STOP";
	
	/**
	 * add waypoint
	 */
	public static final String ACTION_TRACK_RECORD_ADD_WPT = "locus.api.android.ACTION_TRACK_RECORD_ADD_WPT";

	
	/**************************************************/
	/*              BROADCAST RECEIVERS               */
	/**************************************************/
	
	/**
	 * Used for receiving locations from Locus
	 */
	public static final String ACTION_PERIODIC_UPDATE = "locus.api.android.ACTION_PERIODIC_UPDATE";
	
	/**
	 * Used for receiving events about changed point in database
	 */
	public static final String ACTION_POINT_IN_DB_CHANGED = "locus.api.android.ACTION_POINT_IN_DB_CHANGED";
	
	/**************************************************/
	/*                    EXTRAS                      */
	/**************************************************/
	
	/**
	 * location send from Locus, current GPS position
	 */
	public static final String INTENT_EXTRA_LOCATION_GPS = "INTENT_EXTRA_LOCATION_GPS";
	
	/**
	 * location send from Locus, map center
	 */
	public static final String INTENT_EXTRA_LOCATION_MAP_CENTER = "INTENT_EXTRA_LOCATION_MAP_CENTER";
	
	/**
	 * name send back in response to GET_LOCATION intent
	 */
	public static final String INTENT_EXTRA_NAME = "INTENT_EXTRA_NAME";
	
	/**
	 * location send back in response to GET_LOCATION intent
	 */
	public static final String INTENT_EXTRA_LOCATION = "INTENT_EXTRA_LOCATION";
	
	/**
	 * one PointData object, send over intent 
	 */
	public static final String INTENT_EXTRA_POINTS_DATA = "INTENT_EXTRA_POINTS_DATA";
	
	/** 
	 * array of PointData objects, send over intent
	 */
	public static final String INTENT_EXTRA_POINTS_DATA_ARRAY = "INTENT_EXTRA_POINTS_DATA_ARRAY";

	/**
	 * sends points data serialized as byte[] through file stored on SD card
	 */
	public static final String INTENT_EXTRA_POINTS_FILE_PATH = "INTENT_EXTRA_POINTS_FILE_PATH";
	
	/**
	 * Sends one single track to Locus
	 */
	public static final String INTENT_EXTRA_TRACKS_SINGLE = "INTENT_EXTRA_TRACKS_SINGLE";
	
	/**
	 * Sends multiple tracks to Locus
	 */
	public static final String INTENT_EXTRA_TRACKS_MULTI = "INTENT_EXTRA_TRACKS_MULTI";

	/**
	 * one PointData object, send over intent 
	 */
	public static final String INTENT_EXTRA_CIRCLES_MULTI = "INTENT_EXTRA_CIRCLES_MULTI";
	
	/**
	 * Extra parameter that set if data should be firstly imported. This is used in intent 
	 * that sends also 
	 */ 
	public static final String INTENT_EXTRA_CALL_IMPORT = "INTENT_EXTRA_CALL_IMPORT";
	
	/**
	 * Extra parameter that set if Locus should center on incoming data or not.
	 */ 
	public static final String INTENT_EXTRA_CENTER_ON_DATA = "INTENT_EXTRA_CENTER_ON_DATA";

	/**
	 * If you set to any point "setExtraOnDisplay" callback, then when Locus display points and
	 * ask for extended version, return result as Point object included in extra place in intent
	 */
	public static final String INTENT_EXTRA_POINT = "INTENT_EXTRA_POINT";

	/** 
	 * Optional boolean value in returning intent. Settings to true, Locus will overwrite point
	 * in database. If you want to call "setExtraOnDisplay" next time, don't forget to set it
	 * in updated waypoint!
	 */
	public static final String INTENT_EXTRA_POINT_OVERWRITE = "INTENT_EXTRA_POINT_OVERWRITE";
	
	/**
	 * Extra content when adding new WMS map directly to Locus
	 */
	public static final String INTENT_EXTRA_ADD_NEW_WMS_MAP_URL = "INTENT_EXTRA_ADD_NEW_WMS_MAP_URL";

	/**
	 * Single item in intent
	 */
	public static final String INTENT_EXTRA_ITEM_ID = "INTENT_EXTRA_ITEM_ID";
	
	/**
	 * list of IDs of points, received by INTENT_ITEM_POINTS_SCREEN_TOOLS
	 */
	public static final String INTENT_EXTRA_ITEMS_ID = "INTENT_EXTRA_ITEMS_ID";
	
	/**
	 * extra data - latitude, used in rare cases for application that start Locus not from API
	 */
	public static final String INTENT_EXTRA_LATITUDE = "INTENT_EXTRA_LATITUDE";
	
	/**
	 * extra data - longitude, used in rare cases for application that start Locus not from API
	 */
	public static final String INTENT_EXTRA_LONGITUDE = "INTENT_EXTRA_LONGITUDE";
	
	/**
	 * profile for track record. Use this extra value for <b>ACTION_TRACK_RECORD_START</b>. Value should
	 * contain String with name of required profile. If profile is no valid, current values will
	 * remain set in Locus. 
	 */
	public static final String INTENT_EXTRA_TRACK_REC_PROFILE = "INTENT_EXTRA_TRACK_REC_PROFILE";
	
	/**
	 * boolean value to determine if recorded track should be automatically saved, so no dialog
	 * with parameters before save will display. Default <code>false</code>. This extra parameter is
	 * used only with <b>ACTION_TRACK_RECORD_STOP</b> broadcast.
	 */
	public static final String INTENT_EXTRA_TRACK_REC_AUTO_SAVE = "INTENT_EXTRA_TRACK_REC_AUTO_SAVE";
	
	/**
	 * Sends multiple tracks to Locus
	 */
	public static final String INTENT_EXTRA_START_NAVIGATION = "INTENT_EXTRA_START_NAVIGATION";
}
