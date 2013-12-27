package org.yaawp.hmi.listitem.styles;

import org.yaawp.hmi.listitem.styles.StyleBasics;

import android.view.View;

public class StyleImage extends StyleBasics {
	
	public View.OnClickListener mClickListener = null;
	public int mWidth = -1;
	public int mHeight = -1;
	
	public StyleImage( int visibility, int background, int width, int height ) {
		super( visibility, background );
		mWidth = width;
		mHeight = height;
	}
	
};   
