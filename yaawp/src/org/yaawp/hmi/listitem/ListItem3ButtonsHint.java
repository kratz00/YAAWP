package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.listitem.styles.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItem3ButtonsHint extends AbstractListItem {

	private static String TAG = ListItem3ButtonsHint.class.getSimpleName();

    protected ThreeButtonPanelBar mButtonPanelBar = null;
    private Vector<PanelBarButton> mButtons = new Vector<PanelBarButton>();
       
    protected String      mDataTextMajor;
    protected String      mDataTextMinor;     
    protected String      mDataTextMajorRight;
    protected String      mDataTextMinorRight;     
    protected Bitmap      mDataImageLeft = null;
    protected Bitmap      mDataImageRight = null;
    
    protected StyleBasics mStyleBackground; 
    protected StyleText   mStyleTextMajor;
    protected StyleText   mStyleTextMinor;
    protected StyleText   mStyleTextMajorRight;
    protected StyleText   mStyleTextMinorRight;    
    protected StyleImage  mStyleImageLeft = null;
    protected StyleImage  mStyleImageRight = null;
    protected StyleImage  mStyleCancelButton = null;
    
    static class ViewHolder {
    	View mBackground;
    	TextView mMajorTextLeft;
    	TextView mMajorTextRight;
    	TextView mMinorTextLeft;
    	TextView mMinorTextRight;
    	ImageView mLeftIcon;
    	ImageView mRightIcon;
    	ImageView mCancelButton;
    }
    
    @Override
	public View inflate( Context context ) {
    	View view = super.inflate(context);
    	ViewHolder holder = new ViewHolder();
        holder.mMajorTextLeft = (TextView)view.findViewById(R.id.textViewMajorLeft);
        holder.mMajorTextRight = (TextView)view.findViewById(R.id.textViewMajorRight);
        holder.mMinorTextLeft = (TextView)view.findViewById(R.id.textViewMinorLeft);
        holder.mMinorTextRight = (TextView)view.findViewById(R.id.textViewMinorRight);
        holder.mLeftIcon = (ImageView)view.findViewById(R.id.imageViewIconLeft);
        holder.mRightIcon = (ImageView)view.findViewById(R.id.imageViewIconRight);
        holder.mCancelButton = (ImageView)view.findViewById(R.id.imageViewCancelButton);
        view.setTag(holder);
        return view;
	}

    View.OnClickListener mOnClickListenerCancel = new View.OnClickListener() {
	    public void onClick(View v) {
	    	ListItem3ButtonsHint.this.mValid = false;
	    	ListItem3ButtonsHint.this.notifyDataSetChanged();
	    } };    
    
    public ListItem3ButtonsHint( boolean selectable, AbstractListItem parent ) {
    	super( selectable, R.layout.list_adapter_hint, parent );
    	
    	mDataTextMajor = null;   
    	mDataTextMinor = null;
    	mDataTextMajorRight = null;   
    	mDataTextMinorRight = null;    	
    	mDataImageLeft = null;
    	mDataImageRight = null;

    	mStyleBackground   = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor    = Styles.mStyleTextMajor;
    	mStyleTextMinor    = Styles.mStyleTextMinor;
    	mStyleTextMajorRight    = Styles.mStyleTextMajor;
    	mStyleTextMinorRight    = Styles.mStyleTextMinor;    	
    	mStyleImageLeft    = Styles.mStyleImageLeft;
    	mStyleImageRight    = Styles.mStyleImageRight;
    	mStyleCancelButton = null;
    }
    
    /*
    public ListItem3ButtonsHint( boolean selectable, AbstractListItem parent, String major, String minor, String majorRight, String minorRight, Bitmap left, Bitmap right ) {
    	super( selectable, R.layout.list_adapter_hint, parent );
    	
    	mDataTextMajor = major;   
    	mDataTextMinor = minor;
    	mDataTextMajorRight = majorRight;   
    	mDataTextMinorRight = minorRight;    	
    	mDataImageLeft = left;
    	mDataImageRight = right;

    	mStyleBackground   = Styles.mStyleBackgroundLightGray; 
    	mStyleTextMajor    = Styles.mStyleTextMajor;
    	mStyleTextMinor    = Styles.mStyleTextMinor;
    	mStyleTextMajorRight    = Styles.mStyleTextMajor;
    	mStyleTextMinorRight    = Styles.mStyleTextMinor;     	
    	mStyleImageLeft    = Styles.mStyleImageLeft;
    	mStyleImageRight    = Styles.mStyleImageRight;
    	mStyleCancelButton = null;
    }  
    */  
    
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
		
		Object object = view.getTag();
		if ( object instanceof ViewHolder ) {
			ViewHolder holder = (ViewHolder)object;
			// --- set layout of list elements
			layoutImageView( holder.mCancelButton, mStyleCancelButton, android.R.drawable.ic_menu_close_clear_cancel );
			layoutImageView( holder.mLeftIcon, mStyleImageLeft, mDataImageLeft );
			layoutImageView( holder.mRightIcon, mStyleImageRight, mDataImageRight );
			layoutTextView( holder.mMajorTextLeft, mStyleTextMajor, mDataTextMajor );
			layoutTextView( holder.mMinorTextLeft, mStyleTextMinor, mDataTextMinor );
			layoutTextView( holder.mMajorTextRight, mStyleTextMajorRight, mDataTextMajorRight );
			layoutTextView( holder.mMinorTextRight, mStyleTextMinorRight, mDataTextMinorRight );				
		}
		
	}
}
