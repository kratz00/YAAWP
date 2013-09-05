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

package locus.api.android.objects;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import locus.api.objects.Storable;
import locus.api.objects.extra.ExtraStyle;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.Log;
import locus.api.utils.Utils;

public class PackWaypoints extends Storable {

	private static final String TAG = "PackWaypoints";
	
	/**
	 * Unique name
	 * PackWaypoints send to Locus with same name (to display), will be overwrite in Locus
	 */
	private String mName;
	
	// icon applied to whole PackWaypoints
	private ExtraStyle mStyle;
	// bitmap for this pack
	private Bitmap mBitmap;
	
	// ArrayList of all points stored in this object
	private ArrayList<Waypoint> mWpts;
	
	/**
	 * Empty constructor used for {@link Storable}
	 * <br />
	 * Do not use directly!
	 */
	public PackWaypoints() {
		this("");
	}
	
	public PackWaypoints(String uniqueName) {
		super();
		this.mName = uniqueName;
	}
	
	public PackWaypoints(byte[] data) throws IOException {
		super(data);
	}
	
	public String getName() {
		return mName;
	}
	
	public Bitmap getBitmap() {
		return mBitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}
	
	public ExtraStyle getExtraStyle() {
		return mStyle;
	}

	public void setExtraStyle(ExtraStyle extraStyle) {
		mStyle = extraStyle;
	}
	
	public void addWaypoint(Waypoint wpt) {
		this.mWpts.add(wpt);
	}
	
	public ArrayList<Waypoint> getWaypoints() {
		return mWpts;
	}

	/****************************/
	/*       STORABLE PART      */
	/****************************/
	
	@Override
	protected int getVersion() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void readObject(int version, DataInputStream dis)
			throws IOException {
		// name
		mName = readStringUTF(dis);
		
		// style
		if (dis.readBoolean()) {
			mStyle = new ExtraStyle(dis);
		}
		
		// icon
		int size = 0;
		if ((size = dis.readInt()) > 0) {
			byte[] data = new byte[size];
			dis.read(data);
			mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			mBitmap = null;
		}
		
		// waypoints
		mWpts = (ArrayList<Waypoint>) readList(Waypoint.class, dis);
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		// name
		writeStringUTF(dos, mName);
		
		// style
		if (mStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			mStyle.write(dos);
		}
		
		// bitmap icon
		if (mBitmap == null) {
			dos.writeInt(0);
		} else {
			byte[] data = getBitmapAsByte(mBitmap);
			if (data == null || data.length == 0) {
				dos.writeInt(0);
			} else {
				dos.writeInt(data.length);
				dos.write(data);
			}
		}
		
		// waypoints itself
		writeList(mWpts, dos);
	}

	@Override
	public void reset() {
		this.mName = null;
		this.mStyle = null;
		this.mWpts = new ArrayList<Waypoint>();
	}
	
	private static byte[] getBitmapAsByte(Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)) {
				return baos.toByteArray();
			} else {
				Log.e(TAG, "Problem with converting image to byte[]");
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "getBitmapAsByte(" + bitmap + ")", e);
			return null;
		} finally {
			Utils.closeStream(baos);
		}
	}
}
