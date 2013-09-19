package org.yaawp.hmi.adapter;

import org.yaawp.R;

import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Vector;
import cz.matejcik.openwig.formats.CartridgeFile;


public class CartridgeListAdapter extends BaseAdapter {

	private static String TAG = "CartridgeListAdapter";

	Vector<CartridgeListItem> mData;
    
    private Context context;
    
    private ListView listView;
    
    public static final int TYPE_LIST_VIEW = 0;
    public static final int TYPE_SPINNER_VIEW = 1;
    public static final int TYPE_OTHER = 2;
    
    private int type = TYPE_LIST_VIEW;

    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    
    /** visibility of bottom view */
    private int textView02Visibility = View.VISIBLE;
    /** hide bottom view if no text is available */
    private boolean textView02HideIfEmpty = false;
    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
    // rescale image size
    private float multiplyImageSize = 1.0f;
    
//    public static final Drawable SEPARATOR = A.getApp().getResources().getDrawable(R.drawable.var_separator);
    
	public CartridgeListAdapter(Context context, Vector<CartridgeListItem> data, View view) {
    	this.mData = data;
    
		if (view instanceof ListView) {
			this.listView = (ListView) view;
			this.listView.setBackgroundColor(Color.WHITE);
			this.type = TYPE_LIST_VIEW;
		} else if (view instanceof Spinner) {
			this.type = TYPE_SPINNER_VIEW;
		} else {
			setTextView02Visible(View.GONE, true);
			
			this.type = TYPE_OTHER;
		}

		this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	try {
    		return true; // mData.get(position).enabled;	
    	} catch (Exception e) {
    		Logger.e(TAG, "isEnabled(" + position + ")", e);
    		return false;
    	}
    }
    
    @Override
	public int getCount() {
		return mData.size();
	}

    @Override
	public Object getItem(int position) {
		return mData.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public LinearLayout createEmptyView(Context context, boolean separator) {
		if ( separator ) {	
			return (LinearLayout) LinearLayout.inflate(
					context, R.layout.list_separator, null);				
		} else {
			return  (LinearLayout) LinearLayout.inflate(
					context, R.layout.iconed_list_adapter, null);
		}		
		

	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	if (convertView == null) {
			convertView = createEmptyView(context, mData.get( position ).isSeparator() );
		}
    	return getViewItem(position, convertView, true);
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	if (convertView == null) {
			convertView = createEmptyView(context, mData.get( position ).isSeparator() );
		}
		return getViewItem(position, convertView, false);
	}
	
	private View getViewItem(int position, View convertView, boolean dropDown) {
		try {
			if ( mData.get( position ).isSeparator() ) {
				return getViewItem_Separator( position, convertView, dropDown );
			} else {
				return getViewItem_Cartridge( position, convertView, dropDown );
			}
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + position + ", " + convertView + ")", e);
		}
		
		convertView.forceLayout();
		return convertView;		
	}

	private View getViewItem_Separator( int position, View convertView, boolean dropDown ) {
		try {
			CartridgeListSeparatorItem item = ((CartridgeListSeparatorItem)mData.get( position ));
			
			convertView.setBackgroundColor(Color.LTGRAY);
			
			View v = convertView.findViewById(R.id.linear_layout_separator);
			LinearLayout llMain = (LinearLayout) v;
			llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (minHeight != Integer.MIN_VALUE) {
				llMain.setMinimumHeight(minHeight);
			}
	
			TextView tv01 = (TextView) convertView.findViewById(R.id.linearLayoutSeparatorHeadline);

			
			llMain.setOnClickListener(null);
			llMain.setOnLongClickListener(null);
			llMain.setLongClickable(false);			
			
			// set TextView top
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setTextColor(Color.BLACK);
	
			if ( item.mHeadline == null) {
				tv01.setVisibility(View.GONE);
			} else {
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(item.mHeadline));
			}
			
		
			llMain.setBackgroundColor(Color.TRANSPARENT);
		
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + position + ", " + convertView + ")", e);
		}
		
		convertView.forceLayout();
		return convertView;		
	}
	
	private View getViewItem_Cartridge( int position, View convertView, boolean dropDown ) {
		try {
			CartridgeFile file = ((CartridgeListGameItem)mData.get( position )).mCartridge;
            byte[] iconData = file.getFile(file.iconId);
            Bitmap iconLeft = null;
            try {
            	iconLeft = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
            } catch (Exception e) {
            	iconLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
            }
            
            String name = file.name;
            String description = file.type + ", " + file.author + ", " + file.version;
            
            /* DataInfo di = new DataInfo(file.name, , icon);
            di.value01 = file.latitude;
            di.value02 = file.longitude;
            // TODO di.setDistAzi(actLoc);
            */
            
            Bitmap iconRight = null;
            try {
                if (file.getSavegame().exists()) {
                	iconRight = Images.getImageB(android.R.drawable.ic_menu_save);
                }
            } catch (Exception e) {
                Logger.e(TAG, "xxx() - xxxx", e);
            }   

            
            /* -------- */
			
			LinearLayout llMain = (LinearLayout) convertView.findViewById(R.id.linear_layout_main);
			llMain.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (minHeight != Integer.MIN_VALUE) {
				llMain.setMinimumHeight(minHeight);
			}
	
			TextView tv01 = (TextView) convertView.findViewById(R.id.layoutIconedListAdapterTextView01);
			TextView tv02 = (TextView) convertView.findViewById(R.id.layoutIconedListAdapterTextView02);
	    	ImageView iv01 = (ImageView) convertView.findViewById(R.id.layoutIconedListAdapterImageView01);
			ImageView iv02 = (ImageView) convertView.findViewById(R.id.layoutIconedListAdapterImageView02);
			
			// set TextView top
			tv01.setBackgroundColor(Color.TRANSPARENT);
			tv01.setTextColor(Color.BLACK);
	
			if (name == null) {
				tv01.setVisibility(View.GONE);
			} else {
				tv01.setVisibility(View.VISIBLE);
				tv01.setText(Html.fromHtml(name));
			}
			
			// set TextView bottom
			tv02.setTextColor(Color.DKGRAY);

			// set additional parametres
			if (textView02Visibility != View.GONE) {
				tv02.setVisibility(View.VISIBLE);
				
				if (description == null) {
					description = "";
				}
				if (description.length() > 0) {
					tv02.setText(Html.fromHtml(description));
				} else {
					if (textView02HideIfEmpty) {
						tv02.setVisibility(View.GONE);
					} else {
						tv02.setText(R.string.no_description);	
					}
				}
			} else {
				tv02.setVisibility(View.GONE);
			}

			// compute MULTI
			float multi = 1.0f;
			if (type == TYPE_SPINNER_VIEW && !dropDown) {
				multi = 0.75f;
			} else if (type == TYPE_SPINNER_VIEW && dropDown) {
				multi = 1.25f;
				// hack to fix spinnerView
				tv01.setHeight((int) (multi * Images.SIZE_BIG));
			} else if (type == TYPE_LIST_VIEW) {
				multi = 1.0f;
			} else if (type == TYPE_OTHER) {
				// for dialogs and similar things
				multi = 1.0f;
			}
			multi *= multiplyImageSize;
			
			// set ImageView left
			int iv01Width = (int) (multi * Images.SIZE_BIG);
			
			/*
			if (di.getImage() != -1) {
				iv01.setImageResource(di.getImage());
			} else if (di.getImageD() != null) {
				iv01.setImageDrawable(di.getImageD());
			} else */
			if ( iconLeft != null) {
				// resize image if is too width
				Bitmap bitmap = iconLeft;
				if (bitmap.getWidth() > Const.SCREEN_WIDTH / 2 && name != null &&
						name.length() > 0) {
					bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH / 2);
				} else if (bitmap.getWidth() > Const.SCREEN_WIDTH) {
					bitmap = Images.resizeBitmap(bitmap, Const.SCREEN_WIDTH);
				}
				
				iv01.setImageBitmap(bitmap);
			} else {
				iv01Width = 0;
			}
			
			// set visibility and size
			ViewGroup.LayoutParams params = iv01.getLayoutParams();
			params.width = iv01Width;
			params.height = (int) (multi * Images.SIZE_BIG);
	        iv01.setLayoutParams(params);
	        iv01.setVisibility(View.VISIBLE);
			
			// set ImageView right
			iv02 = (ImageView) convertView.findViewById(R.id.layoutIconedListAdapterImageView02);
			iv02.setVisibility(View.GONE);
			
			if ( iconRight != null){
				iv02.setVisibility(View.VISIBLE);
				iv02.setImageBitmap(iconRight);
			}
			
			// now set enabled
			if ( true /* TODO di.enabled */ )
				llMain.setBackgroundColor(Color.TRANSPARENT);
			else
				llMain.setBackgroundColor(Color.LTGRAY);
		} catch (Exception e) {
			Logger.e(TAG, "getView(" + position + ", " + convertView + ")", e);
		}

		convertView.forceLayout();
		return convertView;
	}
	
	public void setTextView02Visible(int visibility, boolean hideIfEmpty) {
		this.textView02Visibility = visibility;
		this.textView02HideIfEmpty = hideIfEmpty;
	}
	
	public void setMinHeight(int i) {
		this.minHeight = i;
	}
	
    public void setMultiplyImageSize(float multiplyImageSize) {
		this.multiplyImageSize = multiplyImageSize;
	}
}




