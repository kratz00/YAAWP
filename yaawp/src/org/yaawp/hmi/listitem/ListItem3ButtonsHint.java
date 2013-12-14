package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;

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

import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    private boolean mCancelButton = false;
    private boolean mSelectable = false;
    // rescale image size
    private float multiplyImageSize = 1.0f;	
    
    Bitmap mIconLeft = null;
    
    protected String mTitle;
    protected String mBody;
       
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    public ListItem3ButtonsHint( String title, String body, int image_resource ) {
    	super( R.layout.list_adapter_hint );
    	mTitle = title;

    	if ( body == null ) {
    		mBody = "";
    	} else {
    		mBody = body;
    	}
    	
    	if ( image_resource != 0 ) {
    		mIconLeft = Images.getImageB(image_resource);
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
		ImageView imgCancelImage = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);	
		if ( mCancelButton == true ) {
			imgCancelImage.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	mValid = false;
			    	ListItem3ButtonsHint.this.notifyDataSetChanged();
			    }
			});		
			imgCancelImage.setVisibility(View.VISIBLE);
		} else {
			imgCancelImage.setVisibility(View.GONE);
		}
		
		// xx
		ImageView imgImageLeft = (ImageView) view.findViewById(R.id.image_leftside);	
		if ( imgImageLeft != null ) {
			if ( mIconLeft != null ) {
				imgImageLeft.setImageBitmap( mIconLeft );
				imgImageLeft.setVisibility(View.VISIBLE);
			} else {
				imgImageLeft.setVisibility(View.GONE);
			}				
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

	}
}
