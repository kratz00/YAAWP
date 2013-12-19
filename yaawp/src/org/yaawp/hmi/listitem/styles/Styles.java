package org.yaawp.hmi.listitem.styles;

import org.yaawp.hmi.listitem.ListItemColor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

public class Styles {
    public static final StyleBasics mStyleBackgroundLightGray = new StyleBasics( View.VISIBLE, 0xFFDDDDDD ); 
    public static final StyleBasics mStyleBackgroundDarkGray  = new StyleBasics( View.VISIBLE, 0xff999999 ); 
    
    public static final TextStyle   mStyleTextMajor     = new TextStyle( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.NORMAL );
    public static final TextStyle   mStyleTextMajorBold = new TextStyle( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.BOLD );    
    public static final TextStyle   mStyleTextMinor     = new TextStyle( View.VISIBLE, Color.TRANSPARENT, 0xff555555, 12, Typeface.NORMAL );
    public static final ImageStyle  mStyleImageLeft     = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, -1, -1, null );
    public static final ImageStyle  mStyleCancelButton  = new ImageStyle( View.VISIBLE, Color.TRANSPARENT, -1, -1, null );	
}
