package org.yaawp.hmi.listitem.styles;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

public class Styles {
    public static final StyleBasics mStyleBackgroundLightGray = new StyleBasics( View.VISIBLE, 0xFFDDDDDD ); 
    public static final StyleBasics mStyleBackgroundDarkGray  = new StyleBasics( View.VISIBLE, 0xff999999 ); 
    
    public static final StyleText   mStyleTextMajor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.NORMAL );
    public static final StyleText   mStyleTextMajorBold = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.BOLD );    
    public static final StyleText   mStyleTextMinor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff555555, 12, Typeface.NORMAL );
    
    // --- Images
    public static final StyleImage  mStyleImageLeft     = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1, null );
    public static final StyleImage  mStyleImageRight    = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1, null );
    public static final StyleImage  mStyleCancelButton  = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1, null );	
}
