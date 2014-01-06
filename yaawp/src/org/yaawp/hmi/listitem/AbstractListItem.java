package org.yaawp.hmi.listitem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import org.yaawp.hmi.listitem.styles.*;
import org.yaawp.utils.Logger;

import android.widget.BaseAdapter;

public abstract class AbstractListItem {

	public final static int LISTITEM_VIEW_TYPE_UNIVERSAL_LAYOUT = 0;
	public final static int LISTITEM_VIEW_TYPE_UNIVERSAL_BUTTON_LAYOUT = 0;
	public final static int LISTITEM_VIEW_TYPE_SEPARATOR_LAYOUT = 2;

	
	
	protected boolean mVisible = true;
	protected int mLayoutId = -1;
	protected boolean mOpen = false;
	protected AbstractListItem mParent = null;
	public BaseAdapter mObserver;
	
	/* -------------------------------------------------------- *
	 * 
	 */
	public AbstractListItem( boolean selectable, int layoutId, AbstractListItem parent ) {
		mLayoutId = layoutId;
		mParent = parent;
	}
	
	public View createView( Context context ) {
		View view = (LinearLayout) LinearLayout.inflate( context, mLayoutId , null );
		return view;
	}
	
	public abstract void updateView( View view );

	public void attach() {
		return;
	}
	
	public void dettach() {
		return;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isVisible() {
		if ( mParent != null ) {
			if ( mParent.mOpen == true ) {
				return mVisible;
			} else {
				return false;
			}
		}
		return mVisible;		
	}
	
	public boolean createContextMenu( Activity activity, ContextMenu menu ) {       
        return true;
	}
	
	public boolean onContextItemSelected( Activity activity, int index ) {
		return false;
	}
	
	public void onListItemClicked( Activity activity ) {
		return;
	}	
	
	public abstract int getViewType(); 
	
	protected void layoutTextView( View view, int res, StyleText style, String text ) {
		TextView tv01 = (TextView) view.findViewById(res);
		layoutTextView(tv01,style,text);
	}
		
	protected void layoutTextView( TextView tv01, StyleText style, String text ) {
		if ( tv01 != null ) {
			if ( style != null && text != null && !text.isEmpty() ) {
				tv01.setTextColor( style.mTextColor );
				tv01.setTypeface(null, style.mTypeface );
				tv01.setTextSize( TypedValue.COMPLEX_UNIT_SP, style.mTextSize );
				tv01.setVisibility(style.mVisibility);
				if ( style.mHTML == true ) {
					tv01.setText(Html.fromHtml(text));
				} else {
					tv01.setText(text);
				}
			} else {
				tv01.setVisibility(View.GONE);			
			}
		} else {
			// TODO trace
		}
	}
	
	protected void layoutImageView( View view, int res, StyleImage style, Bitmap bitmap ) {
		ImageView image = (ImageView) view.findViewById(res);
		layoutImageView( image, style, bitmap );
	}
	protected void layoutImageView( ImageView image, StyleImage style, Bitmap bitmap ) {
		if ( image != null ) {
			if ( style != null && bitmap != null ) {
				image.setImageBitmap( bitmap );
				image.setVisibility(style.mVisibility);
				image.setOnClickListener( style.mClickListener );
				
				ViewGroup.LayoutParams params = image.getLayoutParams();
				if( style.mWidth != -1 ) {
					params.width = (int) (style.mWidth);
				}
				if ( style.mHeight != -1 ) {
					params.height = (int) (style.mHeight);
				}
		        image.setLayoutParams(params);
		        image.setVisibility(style.mVisibility);
			} else {
				image.setVisibility(View.GONE);			
			}
		} else {
			// TODO trace
		}
	}	
	
	protected void layoutImageView( View view, int res, StyleImage style, int imageResource ) {
		ImageView image = (ImageView) view.findViewById(res);
		layoutImageView( image, style, imageResource );
	}
	
	protected void layoutImageView( ImageView image, StyleImage style, int imageResource ) {
		if ( image != null ) {
			if ( style != null ) {
				image.setImageResource( imageResource );
				image.setVisibility(style.mVisibility);
				image.setOnClickListener( style.mClickListener );
				ViewGroup.LayoutParams params = image.getLayoutParams();
				if( style.mWidth != -1 ) {
					params.width = (int) (style.mWidth);
				}
				if ( style.mHeight != -1 ) {
					params.height = (int) (style.mHeight);
				}
		        image.setLayoutParams(params);
		        image.setVisibility(style.mVisibility);				
			} else {
				image.setVisibility(View.GONE);			
			}
		} else {
			// TODO trace
		}
	}	
	
	public void notifyDataSetChanged() {
		this.mObserver.notifyDataSetChanged();
	}
	
	public void notifyDataSetInvalidated() {
		this.mObserver.notifyDataSetInvalidated();
	}
}
