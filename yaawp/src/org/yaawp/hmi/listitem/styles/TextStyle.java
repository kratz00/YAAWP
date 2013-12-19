package org.yaawp.hmi.listitem.styles;

import org.yaawp.hmi.listitem.styles.StyleDefine;

import android.graphics.Color;
import android.graphics.Typeface;

public class TextStyle extends StyleDefine  {
	public int mBackground = Color.TRANSPARENT;
	public int mTextColor = Color.BLACK;
	public int mTextSize = 18;
	public int mTypeface = Typeface.NORMAL;
	public boolean mHTML = true;
	
	public TextStyle( int background, int textColor, int textSize, int typeface ) {
		super(background);
		mTextColor = textColor;
		mTextSize = textSize;
		mTypeface = typeface;
	}		 
};
