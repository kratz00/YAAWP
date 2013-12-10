package org.yaawp.hmi.adapter;

import java.io.File;
import java.util.Vector;

import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.hmi.activities.CartridgeDetailsActivity;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.Const;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;
import org.yaawp.utils.UtilsFormat;
import org.yaawp.hmi.helper.I18N;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;

public class CartridgeListAdapterItemHint extends CartridgeListAdapterItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    
    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
    // rescale image size
    private float multiplyImageSize = 1.0f;	
    
    protected String mTitle;
    protected String mBody;
    
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    public CartridgeListAdapterItemHint( String title, String body ) {
    	mTitle = title;
    	mBody = body;
    }
    
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
    
	public LinearLayout createView( Context context ) {
		
		LinearLayout view = (LinearLayout) LinearLayout.inflate( context, R.layout.list_adapter_hint, null);
		
		mButtonPanelBar = new ThreeButtonPanelBar(view);
		mButtonPanelBar.SetBackgroundColor( 0x00000000 );
		
		try {
			/*
            byte[] iconData = mCartridge.getFile(mCartridge.getIconId());
            Bitmap iconLeft = null;
            try {
            	iconLeft = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
            } catch (Exception e) {
            	iconLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
            }
            
            String name = mCartridge.getName();
            String description = "";
            
            if ( !mCartridge.isPlayAnywhere() ) {
        		Location loc = new Location(TAG);
        		loc.setLatitude(mCartridge.getLatitude());
        		loc.setLongitude(mCartridge.getLongitude());
 
        		description += (description.length()>0 ? ", " : "") + UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(loc), false);
            }
           
            description += (description.length()>0 ? ", " : "") + mCartridge.getAuthor();
            description += (description.length()>0 ? ", " : "") + mCartridge.getVersion();
            
            Bitmap iconRight = null;
            try {
                if (mCartridge.getSavegame().exists()) {
                	iconRight = Images.getImageB(android.R.drawable.ic_menu_save);
                }
            } catch (Exception e) {
                Logger.e(TAG, "xxx() - xxxx", e);
            }   

            // description += "\n\r\n"+mCartridge.getDescription();
            /* -------- */
		
			
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.linear_layout_main);
			
		
			
			llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (minHeight != Integer.MIN_VALUE) {
				llMain.setMinimumHeight(minHeight);
			}
	
			TextView tv01 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView01);
			TextView tv02 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView02);
	    	// ImageView iv01 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);
			// ImageView iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			
			// set TextView top
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setTextColor(Color.BLACK);
		
			
			if ( mTitle == "") {
				tv01.setVisibility(View.GONE);
			} else {
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(mTitle));
			}
			
			if ( mBody == "") {
				tv01.setVisibility(View.GONE);
			} else {
				tv02.setVisibility(View.VISIBLE);
				tv02.setText(Html.fromHtml(mBody));
			}			
			
			// set TextView bottom
			tv02.setTextColor(Color.DKGRAY);

			
			for ( int i=0; i<mButtons.size(); i++ ) {
				mButtonPanelBar.AddButton( mButtons.get(i) );
			}
		
			/*
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
*/
			// compute MULTI
			float multi = 1.0f;
			multi *= multiplyImageSize;
			
			// set ImageView left
			int iv01Width = (int) (multi * Images.SIZE_BIG);
			
			/*
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
			*/
			
			/*
			// set visibility and size
			ViewGroup.LayoutParams params = iv01.getLayoutParams();
			params.width = iv01Width;
			params.height = (int) (multi * Images.SIZE_BIG);
	        iv01.setLayoutParams(params);
	        iv01.setVisibility(View.VISIBLE);
			
			// set ImageView right
			iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			iv02.setVisibility(View.GONE);
			*/
			/*
			if ( iconRight != null){
				iv02.setVisibility(View.VISIBLE);
				iv02.setImageBitmap(iconRight);
			}
			*/
			
			llMain.setBackgroundColor(Color.TRANSPARENT);

		} catch (Exception e) {
			Logger.e(TAG, "getView( " + view + " )", e);
		}

		view.forceLayout();
		return view;
	}
	

    
   	
	
}
