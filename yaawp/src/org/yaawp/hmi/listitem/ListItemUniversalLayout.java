package org.yaawp.hmi.listitem;

import java.util.Vector;

import org.yaawp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.listitem.styles.*;
import android.widget.ImageView;
import android.widget.TextView;
import org.yaawp.utils.Logger;

public class ListItemUniversalLayout extends AbstractListItem {

	private static String TAG = ListItemUniversalLayout.class.getSimpleName();

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
    	int mViewType;
    }
    
    View.OnClickListener mOnClickListenerCancel = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Logger.i( TAG, "mOnClickListenerCancel()" );
	    	ListItemUniversalLayout.this.mVisible = false;
	    	ListItemUniversalLayout.this.notifyDataSetChanged();
	    } };    
    
    public ListItemUniversalLayout( boolean selectable, AbstractListItem parent ) {
    	super( selectable, R.layout.listitem_universal, parent );
    	
    	mDataTextMajor       = null;   
    	mDataTextMinor       = null;
    	mDataTextMajorRight  = null;   
    	mDataTextMinorRight  = null;    	
    	mDataImageLeft       = null;
    	mDataImageRight      = null;

    	mStyleBackground     = null; 
    	mStyleTextMajor      = null;
    	mStyleTextMinor      = null;
    	mStyleTextMajorRight = null;
    	mStyleTextMinorRight = null;    	
    	mStyleImageLeft      = null;
    	mStyleImageRight     = null;
    	mStyleCancelButton   = null;   
    	
    	
    }
       
	public void AddButton( PanelBarButton button ) {
		mButtons.add( button );
	}    
	
    @Override
	public View createView( Context context ) {
    	
    	// create view
    	View view = super.createView(context);
    	
    	// collect sub views
    	ViewHolder holder = new ViewHolder();
        holder.mMajorTextLeft  = (TextView)view.findViewById(R.id.textViewMajorLeft);
        holder.mMajorTextRight = (TextView)view.findViewById(R.id.textViewMajorRight);
        holder.mMinorTextLeft  = (TextView)view.findViewById(R.id.textViewMinorLeft);
        holder.mMinorTextRight = (TextView)view.findViewById(R.id.textViewMinorRight);
        holder.mLeftIcon       = (ImageView)view.findViewById(R.id.imageViewIconLeft);
        holder.mRightIcon      = (ImageView)view.findViewById(R.id.imageViewIconRight);
        holder.mCancelButton   = (ImageView)view.findViewById(R.id.imageViewCancelButton);
        holder.mViewType       = getViewType();
        view.setTag(holder);
        	
        return view;
	}	
	
	@Override
	public void updateView( View view ) {
		// Logger.i( TAG, "updateView( view="+view+" )" );
		
		if ( mStyleBackground != null ) {
			view.setBackgroundColor( mStyleBackground.mBackground );
		}
		
		Object object = view.getTag();
		if ( object instanceof ViewHolder ) {
			ViewHolder holder = (ViewHolder)object;
			layoutImageView( view, R.id.imageViewCancelButton, mStyleCancelButton, android.R.drawable.ic_menu_close_clear_cancel );
			layoutImageView( holder.mLeftIcon, mStyleImageLeft, mDataImageLeft );
			layoutImageView( holder.mRightIcon, mStyleImageRight, mDataImageRight );
			layoutTextView( holder.mMajorTextLeft, mStyleTextMajor, mDataTextMajor );
			layoutTextView( holder.mMinorTextLeft, mStyleTextMinor, mDataTextMinor );
			layoutTextView( holder.mMajorTextRight, mStyleTextMajorRight, mDataTextMajorRight );
			layoutTextView( holder.mMinorTextRight, mStyleTextMinorRight, mDataTextMinorRight );	
			if ( holder.mViewType != getViewType() ) {
				return ;
			}
			mButtonPanelBar = new ThreeButtonPanelBar(view);
			if ( mButtonPanelBar != null ) {
				mButtonPanelBar.SetBackgroundColor( 0x00000000 ); // TODO read from style
				mButtonPanelBar.RemoveAllButtons();
				for ( int i=0; i<mButtons.size(); i++ ) {
					mButtonPanelBar.AddButton( mButtons.get(i) );
				}				
				mButtonPanelBar.updateUI();
			} else {
				Logger.e( TAG, "updateView() - NPE button panel bar" );
			}
		} else {
			Logger.e( TAG, "updateView() - object mismatch" ); 
		}
			
		view.forceLayout();
		view.invalidate();
	}
	
	@Override
	public int getViewType() {
		return LISTITEM_VIEW_TYPE_UNIVERSAL_LAYOUT;
	}
}
