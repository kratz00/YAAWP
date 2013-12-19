package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.BaseAdapter;
import org.yaawp.hmi.listitem.styles.*;

public abstract class AbstractListItem {

	protected boolean mValid = true;
	protected boolean mVisible = true;
	protected int mLayoutId = -1;
	private BaseAdapter mChangeObserver = null;
	protected boolean mOpen = false;
	protected AbstractListItem mParent = null;
	
	/* -------------------------------------------------------- *
	 * 
	 */
	public AbstractListItem( int layoutId, AbstractListItem parent ) {
		mLayoutId = layoutId;
		mParent = parent;
	}
	
	public View inflate( Context context ) {
		return (LinearLayout) LinearLayout.inflate( context, mLayoutId , null );
	}
	
	public abstract void layout( Context context, View view  );

	public void attach() {
		return;
	}
	
	public void dettach() {
		return;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isVisibel() {
		return mVisible;
	}
	
	public boolean isValid() {
		if ( mParent != null ) {
			if ( mParent.mOpen == true ) {
				return mValid;
			} else {
				return false;
			}
		}
		return mValid;
	}
	
	public void setValid( boolean valid ) {
		mValid = valid;
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
	
	public void SetChangeObserver( BaseAdapter adapter ) {
		mChangeObserver = adapter;
	}
	
	protected void notifyDataSetChanged() {
		if ( mChangeObserver != null ) {
			mChangeObserver.notifyDataSetChanged();
		}
	}
	
	protected void layoutTextView( View view, int res, TextStyle style, String text ) {
		TextView tv01 = (TextView) view.findViewById(res);
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
	
	protected void layoutImageView( View view, int res, ImageStyle style, Bitmap bitmap ) {
		ImageView image = (ImageView) view.findViewById(res);
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
	
	protected void layoutImageView( View view, int res, ImageStyle style, int imageResource ) {
		ImageView image = (ImageView) view.findViewById(res);
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
}
