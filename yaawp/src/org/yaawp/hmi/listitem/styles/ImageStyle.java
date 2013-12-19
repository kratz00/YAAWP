package org.yaawp.hmi.listitem.styles;

import org.yaawp.hmi.listitem.styles.StyleDefine;

import android.view.View;

public class ImageStyle extends StyleDefine {
	
	public View.OnClickListener mClickListener = null;
	public int mWidth = -1;
	public int mHeight = -1;
	
	public ImageStyle( int background, int width, int height, View.OnClickListener clickListener ) {
		super(background);
		mWidth = width;
		mHeight = height;
		mClickListener = clickListener;
	}
	
};   
