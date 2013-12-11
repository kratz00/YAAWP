package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    private boolean mCancelButton = false;
    private boolean mSelectable = false;
    // rescale image size
    private float multiplyImageSize = 1.0f;	
    
    protected String mTitle;
    protected String mBody;
       
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    public ListItem3ButtonsHint( String title, String body ) {
    	super( R.layout.list_adapter_hint );
    	mTitle = title;

    	if ( body == null ) {
    		mBody = "";
    	} else {
    		mBody = body;
    	}
    }
    
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
    
	
	public boolean isEnabled() {
		return mSelectable; 
	}	
	
	public void enableCancelButton( boolean cancel ) {
		mCancelButton = cancel;
	}
	
	public void setSelectable( boolean selectable ) {
		mSelectable = selectable;
	}
	
	public void layout( Context context, View view  ) {
		
		mView = view;
		
		mButtonPanelBar = new ThreeButtonPanelBar(view);
		mButtonPanelBar.SetBackgroundColor( 0x00000000 );
		
		// cancel button
		ImageView img = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);	
		if ( mCancelButton == true ) {
			img.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	mValid = false;
			    	ListItem3ButtonsHint.this.notifyDataSetChanged();
			    }
			});		
			img.setVisibility(View.VISIBLE);
		} else {
			img.setVisibility(View.GONE);
		}
		
		// 
		TextView tv01 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView01);
		if ( !mTitle.isEmpty() ) {
			tv01.setVisibility(View.VISIBLE);
			tv01.setTextColor(Color.BLACK);
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setText(Html.fromHtml(mTitle));
		} else {
			tv01.setVisibility(View.GONE);			
		}
		
		// ***
		TextView tv02 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView02);
		if ( !mBody.isEmpty() ) {
			tv02.setVisibility(View.VISIBLE);
			tv02.setTextColor(Color.DKGRAY);
			tv02.setBackgroundColor(Color.TRANSPARENT);
			tv02.setText(Html.fromHtml(mBody));
		} else {
			tv02.setVisibility(View.GONE);			
		}
		
		// ***
		for ( int i=0; i<mButtons.size(); i++ ) {
			mButtonPanelBar.AddButton( mButtons.get(i) );
		}
		
		try {
			/*
            byte[] iconData = mCartridge.getFile(mCartridge.getIconId());
            Bitmap iconLeft = null;
            try {
            	iconLeft = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
            } catch (Exception e) {
            	iconLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
            }
            
			*/
		
			
	    	// ImageView iv01 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);
			// ImageView iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			
						

			/*

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
			
			

		} catch (Exception e) {
			Logger.e(TAG, "getView( " + view + " )", e);
		}

	}
	

    
   	
	
}
