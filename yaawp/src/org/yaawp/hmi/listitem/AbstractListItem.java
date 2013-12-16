package org.yaawp.hmi.listitem;

import org.yaawp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class AbstractListItem {

	protected boolean mValid = true;
	protected boolean mVisible = true;
	protected int mLayoutId = -1;
	private BaseAdapter mChangeObserver = null;
	
    class StyleDefine {
    	public int mBackground = Color.TRANSPARENT;
    	public int mVisibility = View.VISIBLE;
    	
    	public StyleDefine( int background ) {
    		mBackground = background;
    	}
    }
    
	class ImageStyle extends StyleDefine {
		public View.OnClickListener mClickListener = null;
		
		public ImageStyle( int background, View.OnClickListener clickListener ) {
			super(background);
			mClickListener = clickListener;
		}
		
	};     
    
	class TextStyle extends StyleDefine  {
		public int mBackground = Color.TRANSPARENT;
		public int mTextColor = Color.BLACK;
		public int mTextSize = 18;
		public int mTypeface = Typeface.NORMAL;
		public boolean mHTML = true;
		
		TextStyle( int background, int textColor, int textSize, int typeface ) {
			super(background);
			mTextColor = textColor;
			mTextSize = textSize;
			mTypeface = typeface;
		}		 
	};    	
	
	/* -------------------------------------------------------- *
	 * 
	 */
	public AbstractListItem( int layoutId ) {
		mLayoutId = layoutId;
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
			} else {
				image.setVisibility(View.GONE);			
			}
		} else {
			// TODO trace
		}
	}	
}
