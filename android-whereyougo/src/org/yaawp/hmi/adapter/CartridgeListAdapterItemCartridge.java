package org.yaawp.hmi.adapter;

import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

import org.yaawp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.yaawp.YCartridge;

public class CartridgeListAdapterItemCartridge implements CartridgeListAdapterItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    
    /** visibility of bottom view */
    private int textView02Visibility = View.VISIBLE;
    /** hide bottom view if no text is available */
    private boolean textView02HideIfEmpty = false;
    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
    // rescale image size
    private float multiplyImageSize = 1.0f;	
    
    public YCartridge mCartridge;
      
    public CartridgeListAdapterItemCartridge( YCartridge cartridge ) {
    	mCartridge = cartridge;
    }
    
	public LinearLayout createView( Context context ) {
		
		LinearLayout view = (LinearLayout) LinearLayout.inflate( context, R.layout.iconed_list_adapter, null);
		
		try {
            byte[] iconData = mCartridge.getFile(mCartridge.getIconId());
            Bitmap iconLeft = null;
            try {
            	iconLeft = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
            } catch (Exception e) {
            	iconLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
            }
            
            String name = mCartridge.getName();
            String description = mCartridge.getType() + ", " + mCartridge.getAuthor() + ", " + mCartridge.getVersion();
                       
            Bitmap iconRight = null;
            try {
                if (mCartridge.getSavegame().exists()) {
                	iconRight = Images.getImageB(android.R.drawable.ic_menu_save);
                }
            } catch (Exception e) {
                Logger.e(TAG, "xxx() - xxxx", e);
            }   

            
            /* -------- */
			
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.linear_layout_main);
			llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (minHeight != Integer.MIN_VALUE) {
				llMain.setMinimumHeight(minHeight);
			}
	
			TextView tv01 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView01);
			TextView tv02 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView02);
	    	ImageView iv01 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);
			ImageView iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			
			// set TextView top
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setTextColor(Color.BLACK);
	
			if (name == null) {
				tv01.setVisibility(View.GONE);
			} else {
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(name));
			}
			
			// set TextView bottom
			tv02.setTextColor(Color.DKGRAY);

			// set additional parametres
			if (textView02Visibility != View.GONE) {
				tv02.setVisibility(View.VISIBLE);
				
				if (description == null) {
					description = "";
				}
				if (description.length() > 0) {
					tv02.setText(Html.fromHtml(description));
				} else {
					if (textView02HideIfEmpty) {
						tv02.setVisibility(View.GONE);
					} else {
						tv02.setText(R.string.no_description);	
					}
				}
			} else {
				tv02.setVisibility(View.GONE);
			}

			// compute MULTI
			float multi = 1.0f;
			multi *= multiplyImageSize;
			
			// set ImageView left
			int iv01Width = (int) (multi * Images.SIZE_BIG);
			
			if ( iconLeft != null) {
				// resize image if is too width
				Bitmap bitmap = iconLeft;
				if (bitmap.getWidth() > Const.SCREEN_WIDTH / 2 && name != null &&
						name.length() > 0) {
					bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH / 2);
				} else if (bitmap.getWidth() > Const.SCREEN_WIDTH) {
					bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH);
				}
				
				iv01.setImageBitmap(bitmap);
			} else {
				iv01Width = 0;
			}
			
			// set visibility and size
			ViewGroup.LayoutParams params = iv01.getLayoutParams();
			params.width = iv01Width;
			params.height = (int) (multi * Images.SIZE_BIG);
	        iv01.setLayoutParams(params);
	        iv01.setVisibility(View.VISIBLE);
			
			// set ImageView right
			iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			iv02.setVisibility(View.GONE);
			
			if ( iconRight != null){
				iv02.setVisibility(View.VISIBLE);
				iv02.setImageBitmap(iconRight);
			}
			
			llMain.setBackgroundColor(Color.TRANSPARENT);

		} catch (Exception e) {
			Logger.e(TAG, "getView( " + view + " )", e);
		}

		view.forceLayout();
		return view;
	}	
}
