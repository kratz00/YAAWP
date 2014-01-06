package org.yaawp.hmi.listitem.styles;

import android.graphics.Color;
import android.view.View;

public class StyleBasics {

	public int mBackground = Color.TRANSPARENT;
	public int mVisibility = View.GONE;
	
	public StyleBasics( int visibility, int background ) {
		mVisibility = visibility;
		mBackground = background;
	}
	
}
