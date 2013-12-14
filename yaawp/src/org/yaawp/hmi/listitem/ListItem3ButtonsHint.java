package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
	private boolean mCancelButton = false;
    private boolean mSelectable = false;
    private boolean mBoldTitle = false;
    private Bitmap mIconLeft = null;
    protected String mTitle;
    protected String mBody;      
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    public ListItem3ButtonsHint( String title, String body, boolean boldTitle, Bitmap iconLeft ) {
    	super( R.layout.list_adapter_hint );
    	mTitle = title;   
    	mBody = body;
    	mIconLeft = iconLeft;
    	mBoldTitle = boldTitle;
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
		
		try {
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
			if ( mTitle != null && !mTitle.isEmpty() ) {
				tv01.setTypeface(null, (mBoldTitle==true) ? Typeface.BOLD : Typeface.NORMAL );
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(mTitle));
			} else {
				tv01.setVisibility(View.GONE);			
			}
			
			// ***
			TextView tv02 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView02);
			if ( mBody != null && !mBody.isEmpty() ) {
				tv02.setVisibility(View.VISIBLE);
				tv02.setText(Html.fromHtml(mBody));
			} else {
				tv02.setVisibility(View.GONE);			
			}
			
			// ***
			for ( int i=0; i<mButtons.size(); i++ ) {
				mButtonPanelBar.AddButton( mButtons.get(i) );
			}
			
		} catch( Exception e ) {
			return;
		}
	}
}
