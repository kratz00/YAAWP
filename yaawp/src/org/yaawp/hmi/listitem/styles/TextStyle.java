package org.yaawp.hmi.listitem.styles;

import org.yaawp.hmi.listitem.styles.StyleBasics;

import android.graphics.Color;
import android.graphics.Typeface;

public class TextStyle extends StyleBasics  {
	public int mBackground = Color.TRANSPARENT;
	public int mTextColor = Color.BLACK;
	public int mTextSize = 18;
	public int mTypeface = Typeface.NORMAL;
	public boolean mHTML = true;
	
	public TextStyle( int visibility, int background, int textColor, int textSize, int typeface ) {
		super(visibility,background);
		mTextColor = textColor;
		mTextSize = textSize;
		mTypeface = typeface;
	}		 
};
