package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import android.util.TypedValue;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
    private boolean mSelectable = false;
    protected Bitmap mIconLeft = null;
    protected String mTitle;
    protected String mBody;      
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    protected StyleDefine mStyleBackground; //  = new StyleDefine( 0xFFDDDDDD ); 
    protected TextStyle mStyleTextMajor; // = new TextStyle( );
    protected TextStyle mStyleTextMinor; // = new TextStyle( );
    protected ImageStyle mStyleImageLeft = null;
    protected ImageStyle mStyleCancelButton = null;

    
    public ListItem3ButtonsHint( String title, String body, boolean boldTitle, Bitmap iconLeft, AbstractListItem parent ) {
    	super( R.layout.list_adapter_hint, parent );
    	mTitle = title;   
    	mBody = body;
    	mIconLeft = iconLeft;
    	// ----
    	mStyleBackground = new StyleDefine( 0xFFDDDDDD ); 
    	mStyleTextMajor = new TextStyle( Color.TRANSPARENT, 0xff333333, 18, boldTitle?Typeface.BOLD:Typeface.NORMAL );
    	mStyleTextMinor = new TextStyle( Color.TRANSPARENT, 0xff555555, 12, Typeface.NORMAL );
    	mStyleImageLeft = new ImageStyle( Color.TRANSPARENT, -1, -1, null );
    	mStyleCancelButton = new ImageStyle( Color.TRANSPARENT, -1, -1, new View.OnClickListener() {
		    public void onClick(View v) {
		    	ListItem3ButtonsHint.this.mValid = false;
		    	ListItem3ButtonsHint.this.notifyDataSetChanged();
		    } } );
    	
    	enableCancelButton(false);
    }
    
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
    
	
	public boolean isEnabled() {
		return mSelectable; 
	}	
	
	public void enableCancelButton( boolean cancel ) {
		mStyleCancelButton.mVisibility = cancel ? View.VISIBLE : View.GONE;
	}
	
	public void setSelectable( boolean selectable ) {
		mSelectable = selectable;
	}
	
	public void layout( Context context, View view  ) {
		
		try {
			view.setBackgroundColor( mStyleBackground.mBackground );
						
			
			mButtonPanelBar = new ThreeButtonPanelBar(view);
			mButtonPanelBar.SetBackgroundColor( 0x00000000 ); // TODO read from style
					
			
			// xx
			layoutImageView( view, R.id.layoutIconedListAdapterImageView01, mStyleCancelButton, android.R.drawable.ic_menu_close_clear_cancel );
			layoutImageView( view, R.id.image_leftside, mStyleImageLeft, mIconLeft );
			layoutTextView( view, R.id.layoutIconedListAdapterTextView01, mStyleTextMajor, mTitle );
			layoutTextView( view, R.id.layoutIconedListAdapterTextView02, mStyleTextMinor, mBody );
			
			
			// ***
			for ( int i=0; i<mButtons.size(); i++ ) {
				mButtonPanelBar.AddButton( mButtons.get(i) );
			}
			
		} catch( Exception e ) {
			return;
		}
	}
}
