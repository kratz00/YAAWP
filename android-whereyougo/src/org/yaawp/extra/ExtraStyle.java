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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.yaawp.extra.Storable;
// import locus.api.objects.extra.ExtraStyle.LineStyle.ColorStyle;
// import locus.api.objects.extra.ExtraStyle.LineStyle.Units;
import org.yaawp.extra.Log;

public class ExtraStyle extends Storable {

	private static final String TAG = "ExtraStyle";
	
	// style name - id in style tag
	String id;
	// style name - name tag inside style tag	
	String name;
	
	// BALLON STYLE (not used yet)
	BalloonStyle balloonStyle;
	// ICON STYLE
	IconStyle iconStyle;
	// LABEL STYLE
	LabelStyle labelStyle;
	// LINE STYLE
	LineStyle lineStyle;
	// LIST STYLE (not used yet)
	ListStyle listStyle;
	// POLY STYLE
	PolyStyle polyStyle;
	
	public ExtraStyle() {
		this("");
	}
	
	public ExtraStyle(String name) {
		super();
		this.name = name;
	}

	public ExtraStyle(DataInputStream dis) throws IOException {
		super(dis);
	}
	
	public ExtraStyle(byte[] data) throws IOException {
		super(data);
	}
	
    /*******************************************/
    /*             OVERWRITE PART              */
    /*******************************************/
	
	@Override
	protected int getVersion() {
		return 1;
	}

	@Override
	public void reset() {
		id = "";
		name = "";
		balloonStyle = null;
		iconStyle = null;
		labelStyle = null;
		lineStyle = null;
		listStyle = null;
		polyStyle = null;
	}
	
	@Override
	protected void readObject(int version, DataInputStream dis)
			throws IOException {
		// read core
		id = readStringUTF(dis);
		name = readStringUTF(dis);
		
		// read old version 0
		if (version == 0) {
			// this method breaks compatibility if any app with older API will try to load new data
			readVersion0(dis);
			return;
		}
		
		// balloon style
		try {
			if (dis.readBoolean()) {
				balloonStyle = (BalloonStyle) Storable.read(BalloonStyle.class, dis);
			}
			if (dis.readBoolean()) {
				iconStyle = (IconStyle) Storable.read(IconStyle.class, dis);
			}
			if (dis.readBoolean()) {
				labelStyle = (LabelStyle) Storable.read(LabelStyle.class, dis);
			}
			if (dis.readBoolean()) {
				lineStyle = (LineStyle) Storable.read(LineStyle.class, dis);
			}
			if (dis.readBoolean()) {
				listStyle = (ListStyle) Storable.read(ListStyle.class, dis);
			}
			if (dis.readBoolean()) {
				polyStyle = (PolyStyle) Storable.read(PolyStyle.class, dis);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void readVersion0(DataInputStream dis) throws IOException {
		// balloon style
		if (dis.readBoolean()) {
			balloonStyle = new BalloonStyle();
			balloonStyle.bgColor = dis.readInt();
			balloonStyle.textColor = dis.readInt();
			balloonStyle.text = readStringUTF(dis);
			int displayMode = dis.readInt();
			if (displayMode < BalloonStyle.DisplayMode.values().length) {
				balloonStyle.displayMode = BalloonStyle.
						DisplayMode.values()[displayMode];
			}
		}
				
		// icon style
		if (dis.readBoolean()) {
			iconStyle = new IconStyle();
			iconStyle.color = dis.readInt();
			iconStyle.scale = dis.readFloat();
			iconStyle.heading = dis.readFloat();
			iconStyle.iconHref = readStringUTF(dis);
			// iconStyle.hotSpot = KmlVec2.read(dis);
		}
				
		// label style
		if (dis.readBoolean()) {
			labelStyle = new LabelStyle();
			labelStyle.color = dis.readInt();
			labelStyle.scale = dis.readFloat();
		}
				
		// line style
		if (dis.readBoolean()) {
			lineStyle = new LineStyle();
			lineStyle.color = dis.readInt();
			lineStyle.width = dis.readFloat();
			lineStyle.gxOuterColor = dis.readInt();
			lineStyle.gxOuterWidth = dis.readFloat();
			lineStyle.gxPhysicalWidth = dis.readFloat();
			lineStyle.gxLabelVisibility = dis.readBoolean();
			
			int colorStyle = dis.readInt();
			if (colorStyle < LineStyle.ColorStyle.values().length) {
				lineStyle.colorStyle = LineStyle.ColorStyle.values()[colorStyle];
			}
			int units = dis.readInt();
			if (units < LineStyle.Units.values().length) {
				lineStyle.units = LineStyle.Units.values()[units];
			}
		}
				
		// list style
		if (dis.readBoolean()) {
			listStyle = new ListStyle();
			int listItemStyle = dis.readInt();
			if (listItemStyle < ListStyle.ListItemType.values().length) {
				listStyle.listItemType = ListStyle.ListItemType.values()[listItemStyle];
			}
			listStyle.bgColor = dis.readInt();
			int itemsCount = dis.readInt();
			for (int i = 0; i < itemsCount; i++) {
				ListStyle.ItemIcon itemIcon = new ListStyle.ItemIcon();
				int iconStyle = dis.readInt();
				if (iconStyle < ListStyle.ItemIcon.State.values().length) {
					itemIcon.state = ListStyle.ItemIcon.State.values()[iconStyle];	
				}
				itemIcon.href = readStringUTF(dis);
				listStyle.itemIcons.add(itemIcon);
			}
		}
				
		// poly style
		if (dis.readBoolean()) {
			polyStyle = new PolyStyle();
			polyStyle.color = dis.readInt();
			polyStyle.fill = dis.readBoolean();
			polyStyle.outline = dis.readBoolean();
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		// write core
		writeStringUTF(dos, id);
		writeStringUTF(dos, name);
		
		// balloon style
		if (balloonStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			balloonStyle.write(dos);
		}
		
		// icon style
		if (iconStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			iconStyle.write(dos);
		}
		
		// label style
		if (labelStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			labelStyle.write(dos);
		}
		
		// line style
		if (lineStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			lineStyle.write(dos);
		}
		
		// list style
		if (listStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			listStyle.write(dos);
		}
		
		// poly style
		if (polyStyle == null) {
			dos.writeBoolean(false);
		} else {
			dos.writeBoolean(true);
			polyStyle.write(dos);
		}
	}

    /*******************************************/
    /*            SETERS & GETTERS             */
    /*******************************************/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// ICON STYLE
	
	public IconStyle getIconStyle() {
		return iconStyle;
	}
	
	public String getIconStyleIconUrl() {
		if (iconStyle == null) {
			return null;
		}
		return iconStyle.iconHref;
	}

	public void setIconStyle(String iconUrl, float scale) {
    	setIconStyle(iconUrl, COLOR_DEFAULT, 0.0f, scale);
    }
	
    public void setIconStyle(String iconUrl, int color, float heading, float scale) {
    	iconStyle = new IconStyle();
    	iconStyle.iconHref = iconUrl;
    	iconStyle.color = color;
    	iconStyle.heading = heading;
    	iconStyle.scale = scale;
    	// set hot spot
    	// setIconStyleHotSpot(HOTSPOT_BOTTOM_CENTER);
    }
	
	// definition of hotSpot of icon to bottom center
	public static final int HOTSPOT_BOTTOM_CENTER = 0;
	public static final int HOTSPOT_TOP_LEFT = 1;
	public static final int HOTSPOT_CENTER_CENTER = 2;

	/*
	public void setIconStyleHotSpot(int hotspot) {
		if (iconStyle == null) {
			Log.e(TAG, "setIconStyleHotSpot(" + hotspot + "), " +
					"initialize IconStyle before settings hotSpot!");
			return;
		}
		
		if (hotspot == HOTSPOT_TOP_LEFT) {
			iconStyle.hotSpot = new KmlVec2(
					0.0f, KmlVec2.Units.FRACTION, 1.0f, KmlVec2.Units.FRACTION);
		} else if (hotspot == HOTSPOT_CENTER_CENTER) {
			iconStyle.hotSpot = new KmlVec2(
					0.5f, KmlVec2.Units.FRACTION, 0.5f, KmlVec2.Units.FRACTION);
		} else {
			// hotspot == HOTSPOT_BOTTOM_CENTER
			iconStyle.hotSpot = generateDefaultHotSpot();
		}
	}
	
	private static KmlVec2 generateDefaultHotSpot() {
		// HOTSPOT_BOTTOM_CENTER
		return new KmlVec2(0.5f, KmlVec2.Units.FRACTION,
				0.0f, KmlVec2.Units.FRACTION);
	}
	
	public void setIconStyleHotSpot(KmlVec2 vec2) {
		if (iconStyle == null || vec2 == null) {
			Log.e(TAG, "setIconStyleHotSpot(" + vec2 + "), " +
					"initialize IconStyle before settings hotSpot or hotSpot is null!");
			return;
		}
		
		iconStyle.hotSpot = vec2;
	}
	*/
	// LINE STYLE

	public LineStyle getLineStyle() {
		return lineStyle;
	}
	
    public void removeLineStyle() {
    	lineStyle = null;
    }

    public void setLineStyle(int color, float width) {
    	// setLineStyle(ColorStyle.SIMPLE, color, width, Units.PIXELS);
    }
    
    public void setLineStyle(LineStyle.ColorStyle style, int color,
    		float width, LineStyle.Units units) {
    	if (lineStyle == null) {
    		lineStyle = new LineStyle();
    	}
    	lineStyle.colorStyle = style;
    	lineStyle.color = color;
    	lineStyle.width = width;
    	lineStyle.units = units;
    }
    
    public void setLineType(LineStyle.LineType type) {
    	if (lineStyle == null) {
    		lineStyle = new LineStyle();
    	}
    	lineStyle.lineType = type;
    }
    
	public void setLineOutline(boolean drawOutline, int colorOutline) {
    	if (lineStyle == null) {
    		lineStyle = new LineStyle();
    	}
    	lineStyle.drawOutline = drawOutline;
    	lineStyle.colorOutline = colorOutline;
	}

    // POLY STYLE
    
    public void setPolyStyle(int color, boolean fill, boolean outline) {
    	polyStyle = new PolyStyle();
    	polyStyle.color = color;
    	polyStyle.fill = fill;
    	polyStyle.outline = outline;
    }
    
    public void removePolyStyle() {
    	polyStyle = null;
    }
	
    /*******************************************/
    /*                 STYLES                  */
    /*******************************************/

    public static final int BLACK       = 0xFF000000;
    public static final int WHITE       = 0xFFFFFFFF;
    
	public static final int COLOR_DEFAULT = WHITE;

	public static class BalloonStyle extends Storable {

		public enum DisplayMode {
			DEFAULT, HIDE
		}
		
		public int bgColor;
		public int textColor;
		public String text;
		public DisplayMode displayMode;

		@Override
		protected int getVersion() {
			return 0;
		}

		@Override
		public void reset() {
			bgColor = WHITE;
			textColor = BLACK;
			text = "";
			displayMode = DisplayMode.DEFAULT;
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			bgColor = dis.readInt();
			textColor = dis.readInt();
			text = readStringUTF(dis);
			int mode = dis.readInt();
			if (mode < DisplayMode.values().length) {
				displayMode = DisplayMode.values()[mode];
			}
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeInt(bgColor);
			dos.writeInt(textColor);
			writeStringUTF(dos, text);
			dos.writeInt(displayMode.ordinal());
		}
	}
	
	
	public static class IconStyle extends Storable {
		
		public int color;
		public float scale;
		public float heading;
		public String iconHref;
		// public KmlVec2 hotSpot;
		
		// temporary variables for Locus usage that are not serialized
		// and are for private Locus usage only
		public Object icon;
		public int iconW;
		public int iconH;
		
		public IconStyle() {
			super();
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("IconStyle [");
			sb.append("color:").append(color);
			sb.append(", scale:").append(scale);
			sb.append(", heading:").append(heading);
			sb.append(", iconHref:").append(iconHref);
			// sb.append(", hotSpot:").append(hotSpot);
			sb.append(", icon:").append(icon);
			sb.append(", iconW:").append(iconW);
			sb.append(", iconH:").append(iconH);
			sb.append("]");
			return sb.toString();
		}
		
		@Override
		protected int getVersion() {
			return 0;
		}

		@Override
		public void reset() {
			color = COLOR_DEFAULT;
			scale = 1.0f;
			heading = 0.0f;
			iconHref = null;
			// hotSpot = generateDefaultHotSpot();
			icon = null;
			iconW = -1;
			iconH = -1;
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			color = dis.readInt();
			scale = dis.readFloat();
			heading = dis.readFloat();
			iconHref = readStringUTF(dis);
			// hotSpot = KmlVec2.read(dis);
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeInt(color);
			dos.writeFloat(scale);
			dos.writeFloat(heading);
			writeStringUTF(dos, iconHref);
			// hotSpot.write(dos);
		}
	}
	
	public static class LabelStyle extends Storable {
		
		public int color = COLOR_DEFAULT;
		public float scale = 1.0f;
		
		@Override
		protected int getVersion() {
			return 0;
		}

		@Override
		public void reset() {
			color = COLOR_DEFAULT;
			scale = 1.0f;			
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			color = dis.readInt();
			scale = dis.readFloat();
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeInt(color);
			dos.writeFloat(scale);
		}
	}
	
	public static class LineStyle extends Storable {
		
		public enum ColorStyle {
			SIMPLE,
			BY_SPEED, 
			BY_ALTITUDE,
			BY_ACCURACY,
			BY_SPEED_CHANGE,
			BY_ALTITUDE_CHANGE,
			BY_HRM,
			BY_CADENCE
		}
		
		public enum Units {
			PIXELS, METRES
		}
		
		public enum LineType {
			NORMAL, DOTTED, DASHED_1, DASHED_2, DASHED_3,
			SPECIAL_1, SPECIAL_2, SPECIAL_3
		}
		
		// KML styles
		public int color;
		public float width;
		public int gxOuterColor;
		public float gxOuterWidth;
		public float gxPhysicalWidth;
		public boolean gxLabelVisibility;
		
		// Locus extension
		public ColorStyle colorStyle;
		public Units units;
		public LineType lineType;
		public boolean drawOutline;
		public int colorOutline;
		
		public LineStyle() {
			super();
		}
		
		@Override
		protected int getVersion() {
			return 1;
		}

		@Override
		public void reset() {
			color = COLOR_DEFAULT;
			width = 1.0f;
			gxOuterColor = COLOR_DEFAULT;
			gxOuterWidth = 0.0f;
			gxPhysicalWidth = 0.0f;
			gxLabelVisibility = false;
			
			// Locus extension
			colorStyle = ColorStyle.SIMPLE;
			units = Units.PIXELS;
			lineType = LineType.NORMAL;
			drawOutline = false;
			colorOutline = WHITE;
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			color = dis.readInt();
			width = dis.readFloat();
			gxOuterColor = dis.readInt();
			gxOuterWidth = dis.readFloat();
			gxPhysicalWidth = dis.readFloat();
			gxLabelVisibility = dis.readBoolean();
			
			int cs = dis.readInt();
Log.d(TAG, "readObject, cs:" + cs);
			if (cs < ColorStyle.values().length) {
				colorStyle = ColorStyle.values()[cs];
			}
			int un = dis.readInt();
			if (un < Units.values().length) {
				units = Units.values()[un];
			}
			int lt = dis.readInt();
			if (lt < LineType.values().length) {
				lineType = LineType.values()[lt];
			}
			
			// extensions in version 1
			if (version >= 1) {
				drawOutline = dis.readBoolean();
				colorOutline = dis.readInt();
			}
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeInt(color);
			dos.writeFloat(width);
			dos.writeInt(gxOuterColor);
			dos.writeFloat(gxOuterWidth);
			dos.writeFloat(gxPhysicalWidth);
			dos.writeBoolean(gxLabelVisibility);
Log.d(TAG, "writeObject, colorStyle:" + colorStyle);
			dos.writeInt(colorStyle.ordinal());
			dos.writeInt(units.ordinal());
			dos.writeInt(lineType.ordinal());
			dos.writeBoolean(drawOutline);
			dos.writeInt(colorOutline);
		}
	}
	
	public static class ListStyle extends Storable {
		
		public enum ListItemType {
			CHECK, CHECK_OFF_ONLY, CHECK_HIDE_CHILDREN, RADIO_FOLDER
		}

		public static class ItemIcon {

			public enum State {
				OPEN, CLOSED, ERROR, FETCHING0, FETCHING1, FETCHING2
			}
			
			public State state = State.OPEN;
			public String href = "";
		}

		public ListItemType listItemType = ListItemType.CHECK;
		public int bgColor = WHITE;
		public ArrayList<ItemIcon> itemIcons = new ArrayList<ItemIcon>();

		@Override
		protected int getVersion() {
			return 0;
		}

		@Override
		public void reset() {
			listItemType = ListItemType.CHECK;
			bgColor = WHITE;
			itemIcons = new ArrayList<ItemIcon>();
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			int style = dis.readInt();
			if (style < ListStyle.ListItemType.values().length) {
				listItemType = ListStyle.ListItemType.values()[style];
			}
			bgColor = dis.readInt();
			int itemsCount = dis.readInt();
			for (int i = 0; i < itemsCount; i++) {
				ListStyle.ItemIcon itemIcon = new ListStyle.ItemIcon();
				int iconStyle = dis.readInt();
				if (iconStyle < ListStyle.ItemIcon.State.values().length) {
					itemIcon.state = ListStyle.ItemIcon.State.values()[iconStyle];	
				}
				itemIcon.href = readStringUTF(dis);
				itemIcons.add(itemIcon);
			}
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeBoolean(true);
			dos.writeInt(listItemType.ordinal());
			dos.writeInt(bgColor);
			dos.writeInt(itemIcons.size());
			for (ListStyle.ItemIcon itemIcon : itemIcons) {
				dos.writeInt(itemIcon.state.ordinal());
				writeStringUTF(dos, itemIcon.href);
			}
		}
	}
	
	public static class PolyStyle extends Storable {
		
		public int color = COLOR_DEFAULT;
		public boolean fill = true;
		public boolean outline = true;
		
		@Override
		protected int getVersion() {
			return 0;
		}

		@Override
		public void reset() {
			color = COLOR_DEFAULT;
			fill = true;
			outline = true;
		}

		@Override
		protected void readObject(int version, DataInputStream dis)
				throws IOException {
			color = dis.readInt();
			fill = dis.readBoolean();
			outline = dis.readBoolean();
		}
		
		@Override
		protected void writeObject(DataOutputStream dos) throws IOException {
			dos.writeInt(color);
			dos.writeBoolean(fill);
			dos.writeBoolean(outline);
		}
	}
}

