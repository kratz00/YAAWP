package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.listitem.styles.*;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
    private boolean mSelectable = false;
    protected Bitmap mIconLeft = null;
    protected String mTitle;
    protected String mBody;      
    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
    
    protected StyleBasics mStyleBackground; //  = new StyleDefine( 0xFFDDDDDD ); 
    protected TextStyle mStyleTextMajor; // = new TextStyle( );
    protected TextStyle mStyleTextMinor; // = new TextStyle( );
    protected ImageStyle mStyleImageLeft = null;
    protected ImageStyle mStyleCancelButton = null;

    View.OnClickListener mOnClickListenerCancel = new View.OnClickListener() {
	    public void onClick(View v) {
	    	ListItem3ButtonsHint.this.mValid = false;
	    	ListItem3ButtonsHint.this.notifyDataSetChanged();
	    } };
    
    public ListItem3ButtonsHint( String title, String body, boolean boldTitle, Bitmap iconLeft, AbstractListItem parent ) {
    	super( R.layout.list_adapter_hint, parent );
    	mTitle = title;   
    	mBody = body;
    	mIconLeft = iconLeft;
    	// ----
    	mStyleBackground   = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor    = Styles.mStyleTextMajor;
    	mStyleTextMinor    = Styles.mStyleTextMinor;
    	mStyleImageLeft    = Styles.mStyleImageLeft;
    	mStyleCancelButton = null;
    	
    	/*
    	mStyleCancelButton = new ImageStyle( Color.TRANSPARENT, -1, -1, new View.OnClickListener() {
		    public void onClick(View v) {
		    	ListItem3ButtonsHint.this.mValid = false;
		    	ListItem3ButtonsHint.this.notifyDataSetChanged();
		    } } );
    	*/
    	   	
    	enableCancelButton(false);
    }
    
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
    
	
	public boolean isEnabled() {
		return mSelectable; 
	}	
	
	public void enableCancelButton( boolean cancel ) {
		if ( mStyleCancelButton != null ) {
			mStyleCancelButton.mVisibility = cancel ? View.VISIBLE : View.GONE;
		}
	}
	
	public void setSelectable( boolean selectable ) {
		mSelectable = selectable;
	}
	
	public void layout( Context context, View view  ) {
		
		try {
			view.setBackgroundColor( mStyleBackground.mBackground );
						
			// --- set layout of button bar
			mButtonPanelBar = new ThreeButtonPanelBar(view);
			mButtonPanelBar.SetBackgroundColor( 0x00000000 ); // TODO read from style
			for ( int i=0; i<mButtons.size(); i++ ) {
				mButtonPanelBar.AddButton( mButtons.get(i) );
			}
			
			// --- set layout of list elements
			layoutImageView( view, R.id.layoutIconedListAdapterImageView01, mStyleCancelButton, android.R.drawable.ic_menu_close_clear_cancel );
			layoutImageView( view, R.id.image_leftside, mStyleImageLeft, mIconLeft );
			layoutTextView( view, R.id.layoutIconedListAdapterTextView01, mStyleTextMajor, mTitle );
			layoutTextView( view, R.id.layoutIconedListAdapterTextView02, mStyleTextMinor, mBody );
						
		} catch( Exception e ) {
			return;
		}
	}
}
