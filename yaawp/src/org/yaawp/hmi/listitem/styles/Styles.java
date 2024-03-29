package org.yaawp.hmi.listitem.styles;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

public class Styles {
    public static final StyleBasics mStyleBackgroundLightGray = new StyleBasics( View.VISIBLE, 0xFFDDDDDD ); 
    public static final StyleBasics mStyleBackgroundDarkGray  = new StyleBasics( View.VISIBLE, 0xff999999 ); 
    public static final StyleBasics mStyleBackgroundDarkHolo2 = new StyleBasics( View.VISIBLE, 0xff2d2d2d );
    public static final StyleBasics mStyleBackgroundDarkHolo = new StyleBasics( View.VISIBLE, 0xff000000 );
    
    public static final StyleText   mStyleTextMajor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.NORMAL );
    public static final StyleText   mStyleTextMajorBold = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff333333, 18, Typeface.BOLD );    
    public static final StyleText   mStyleTextMinor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xff555555, 12, Typeface.NORMAL );

    public static final StyleText   mStyleTextDarkHoloMajor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xfffefefe, 18, Typeface.NORMAL );
    public static final StyleText   mStyleTextDarkHoloMajorBold = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xfffefefe, 18, Typeface.BOLD );    
    public static final StyleText   mStyleTextDarkHoloMinor     = new StyleText( View.VISIBLE, Color.TRANSPARENT, 0xffa8a8a8, 12, Typeface.NORMAL );    
    
    // --- Images
    public static final StyleImage  mStyleImage         = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1 );
    public static final StyleImage  mStyleImageLarge    = new StyleImage( View.VISIBLE, Color.TRANSPARENT, 48, 48 );
    public static final StyleImage  mStyleImageSmall    = new StyleImage( View.VISIBLE, Color.TRANSPARENT, 32, 32 );
    
    public static final StyleImage  mStyleCancelButton  = new StyleImage( View.VISIBLE, Color.TRANSPARENT, -1, -1 );	
}
