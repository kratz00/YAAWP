/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.hmi.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.activities.CustomActivity;
import org.yaawp.hmi.gui.extension.CustomDialog;
import org.yaawp.hmi.gui.extension.DataInfo;
import org.yaawp.hmi.gui.extension.IconedListAdapter;
import org.yaawp.openwig.Refreshable;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.Const;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;
import org.yaawp.utils.UtilsFormat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;

public abstract class ListVarious extends CustomActivity implements Refreshable {

	private static final String TAG = "ListVarious";
	
	private ListView lv;
	protected String title;
		
	private Vector<Object> stuff = new Vector<Object>();
	
	abstract protected void callStuff (Object what);
	abstract protected boolean stillValid ();
	abstract protected Vector<Object> getValidStuff();
	abstract protected String getStuffName (Object what);
	
	protected Bitmap getStuffIcon (Object object) {
		if (((EventTable) object).isLocated()) {
			return getLocatedIcon((EventTable) object);
		} else {
			Media media = (Media) ((EventTable) object).table.rawget("Icon");
			if (media != null) {
				byte[] icon;
				try {
					icon = Engine.mediaFile(media);
				} catch (IOException e) {
					Logger.e(TAG, "getStuffIcon()", e);
					return Images.IMAGE_EMPTY_B;
				}
				return BitmapFactory.decodeByteArray(icon, 0, icon.length);
			} else { 
				return Images.IMAGE_EMPTY_B;
			}
		}
	}
	
	private static Paint paintText;
	private static Paint paintArrow;
	private static Paint paintArrowBorder;
	static {
		paintText = new Paint();
		paintText.setColor(Color.RED);
		paintText.setTextSize(Utils.getDpPixels(12.0f));
		paintText.setTypeface(Typeface.DEFAULT_BOLD);
		paintText.setAntiAlias(true);
		
		paintArrow = new Paint();
		paintArrow.setColor(Color.YELLOW);
		paintArrow.setAntiAlias(true);
		paintArrow.setStyle(Style.FILL);
		
		paintArrowBorder = new Paint();
		paintArrowBorder.setColor(Color.BLACK);
		paintArrowBorder.setAntiAlias(true);
		paintArrowBorder.setStyle(Style.STROKE);
	}
	
	protected Bitmap getLocatedIcon(EventTable thing) {
		if (!thing.isLocated())
			return Images.IMAGE_EMPTY_B;
		
		try {
			Bitmap bitmap = Bitmap.createBitmap(
					(int) Utils.getDpPixels(80.0f), 
					(int) Utils.getDpPixels(40.0f),
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bitmap);
			c.drawColor(Color.TRANSPARENT);
			
			Location nearest = new Location(TAG);
			if (thing instanceof Zone) {
				nearest.setLatitude(((Zone) thing).nearestPoint.latitude);
				nearest.setLongitude(((Zone) thing).nearestPoint.longitude);
			} else {
				nearest.setLatitude(thing.position.latitude);
				nearest.setLongitude(thing.position.longitude);
			}
			
			float azimuth = LocationState.getLocation().bearingTo(nearest);
			float distance = LocationState.getLocation().distanceTo(nearest);
			
	        double a;
	        int radius = bitmap.getHeight() / 2;
	        int cX = radius;
	        int cY = bitmap.getHeight() / 2;
	        float x1, x2, x3, x4, y1, y2, y3, y4;
	
	        a = azimuth / Const.RHO;
	        x1 = (float) (Math.sin(a) * (radius * 0.90));
	        y1 = (float) (Math.cos(a) * (radius * 0.90));
	
	        a = (azimuth + 180) / Const.RHO;
	        x2 = (float) (Math.sin(a) * (radius * 0.2));
	        y2 = (float) (Math.cos(a) * (radius * 0.2));
	
	        a = (azimuth + 140) / Const.RHO;
	        x3 = (float) (Math.sin(a) * (radius * 0.6));
	        y3 = (float) (Math.cos(a) * (radius * 0.6));
	
	        a = (azimuth + 220) / Const.RHO;
	        x4 = (float) (Math.sin(a) * (radius * 0.6));
	        y4 = (float) (Math.cos(a) * (radius * 0.6));
	
	        Path path = new Path();
	        path.moveTo(cX + x1, cY - y1);
	        path.lineTo(cX + x2, cY - y2);
	        path.lineTo(cX + x3, cY - y3);
	        c.drawPath(path, paintArrow);
	        
	        path = new Path();
	        path.moveTo(cX + x1, cY - y1);
	        path.lineTo(cX + x2, cY - y2);
	        path.lineTo(cX + x4, cY - y4);
	        c.drawPath(path, paintArrow);

	        c.drawLine(cX + x1, cY - y1, cX + x3, cY - y3, paintArrowBorder);
	        c.drawLine(cX + x1, cY - y1, cX + x4, cY - y4, paintArrowBorder);
	        c.drawLine(cX + x2, cY - y2, cX + x3, cY - y3, paintArrowBorder);
	        c.drawLine(cX + x2, cY - y2, cX + x4, cY - y4, paintArrowBorder);
	        
			c.drawText(UtilsFormat.formatDistance(distance, false), radius * 2 + 2,
					cY + paintText.getTextSize() / 2, paintText);
			return bitmap;
		} catch (Exception e) {
			Logger.e(TAG, "getLocatedIcon(" + thing + ")", e);
			return Images.IMAGE_EMPTY_B;
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set layout
		setContentView(R.layout.custom_dialog);

		// set title
		if (getIntent().getStringExtra("title") != null) {
			title = getIntent().getStringExtra("title");
		}
		CustomDialog.setTitle(this, title, null,
				R.drawable.ic_cancel,
				new CustomDialog.OnClickListener() {
					
			@Override
			public boolean onClick(CustomDialog dialog, View v, int btn) {
				ListVarious.this.finish();
				return true;
			}
		});
		
        // center linearLayout
		lv = new ListView(ListVarious.this);
		CustomDialog.setContent(this, lv, 0, false, true);
	}
	
	public void onResume() {
		super.onResume();
		refresh();
	}
	
	public void refresh() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (!stillValid()) {
					ListVarious.this.finish();
					return;
				}
				
				Vector<Object> newStuff = getValidStuff();
				int scrollY = lv.getFirstVisiblePosition();
				// first, validate the stuff already in there
				// TODO
//				for (int i = 0; i < stuff.size(); i++) {
//					Object s = stuff.get(i);
//					int in = newStuff.indexOf(s);
//					if (in == -1) {
//						stuff.remove(i);
//						i--;
//					} else {
//						newStuff.setElementAt(null, in);
//					}
//				}
				// then, add the rest
				stuff.clear();
				for (int i = 0; i < newStuff.size(); i++) {
					Object s = newStuff.get(i);
					if (s != null) {
						stuff.add(s);
					}
				}
				
				// create visual part
				ArrayList<DataInfo> data = new ArrayList<DataInfo>();
				for (int i = 0; i < stuff.size(); i++) {
					Object s = stuff.get(i);
					DataInfo dataInfo = new DataInfo("");
//Logger.e("ListVarious", "addToList:" + s + ", " + (s instanceof Action) + ", " + (s instanceof Cartridge) + ", " + (s instanceof Container) + ", " + (s instanceof Thing));
					if (s instanceof Thing) {
						dataInfo = new DataInfo(((Thing) s).name, null, getStuffIcon(s));
					} else if (s instanceof Action) {
						dataInfo = new DataInfo(((Action) s).text, null, getStuffIcon(s));
					} else {
						dataInfo = new DataInfo(s.toString(), null, getStuffIcon(s));
					}
					data.add(dataInfo);
				}
				
				IconedListAdapter adapter = new IconedListAdapter(ListVarious.this, data, lv);
				adapter.setMultiplyImageSize(1.5f);
				adapter.setTextView02Visible(View.VISIBLE, true);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Logger.d(TAG, "onItemClick:" + position);
						
						Object s = null;
						synchronized (this) {
							if (position >= 0 && position < stuff.size()) {
								s = stuff.get(position);
							}
						}
						if (s != null) {
							callStuff(s);
						}
					}
				});
				
				lv.setSelectionFromTop(scrollY, 5);
			}
		});
	}
}
