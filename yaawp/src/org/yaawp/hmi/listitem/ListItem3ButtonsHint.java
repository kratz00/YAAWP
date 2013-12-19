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

    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
       
    protected String      mDataTextMajor;
    protected String      mDataTextMinor;      
    protected Bitmap      mDataImageLeft = null;
    
    protected StyleBasics mStyleBackground; 
    protected TextStyle   mStyleTextMajor;
    protected TextStyle   mStyleTextMinor;
    protected ImageStyle  mStyleImageLeft = null;
    protected ImageStyle  mStyleCancelButton = null;

    View.OnClickListener mOnClickListenerCancel = new View.OnClickListener() {
	    public void onClick(View v) {
	    	ListItem3ButtonsHint.this.mValid = false;
	    	ListItem3ButtonsHint.this.notifyDataSetChanged();
	    } };    
    
    public ListItem3ButtonsHint( boolean selectable, AbstractListItem parent ) {
    	super( selectable, R.layout.list_adapter_hint, parent );
    	
    	
    	
    	mDataTextMajor = null;   
    	mDataTextMinor = null;
    	mDataImageLeft = null;

    	mStyleBackground   = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor    = Styles.mStyleTextMajor;
    	mStyleTextMinor    = Styles.mStyleTextMinor;
    	mStyleImageLeft    = Styles.mStyleImageLeft;
    	mStyleCancelButton = null;
    }
    
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
			
	public void layout( Context context, View view  ) {
		view.setBackgroundColor( mStyleBackground.mBackground );
		
		// --- set layout of button bar
		mButtonPanelBar = new ThreeButtonPanelBar(view);
		mButtonPanelBar.SetBackgroundColor( 0x00000000 ); // TODO read from style
		for ( int i=0; i<mButtons.size(); i++ ) {
			mButtonPanelBar.AddButton( mButtons.get(i) );
		}
		
		// --- set layout of list elements
		layoutImageView( view, R.id.layoutIconedListAdapterImageView01, mStyleCancelButton, android.R.drawable.ic_menu_close_clear_cancel );
		layoutImageView( view, R.id.image_leftside, mStyleImageLeft, mDataImageLeft );
		layoutTextView( view, R.id.layoutIconedListAdapterTextView01, mStyleTextMajor, mDataTextMajor );
		layoutTextView( view, R.id.layoutIconedListAdapterTextView02, mStyleTextMinor, mDataTextMinor );		
	}
}
