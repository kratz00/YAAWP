package org.yaawp.hmi.listitem;

import java.io.File;


import org.yaawp.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.Const;
import org.yaawp.utils.Images;
import org.yaawp.utils.Logger;
import org.yaawp.utils.Utils;
import org.yaawp.utils.UtilsFormat;

public class ListItemCartridge extends AbstractListItem {

	private static String TAG = "CartridgeListAdapterItemCartridge";
    
    private static final int PADDING = (int) Utils.getDpPixels(4.0f);
    

    /* min height for line */
    private int minHeight = Integer.MIN_VALUE;
    // rescale image size
    private float multiplyImageSize = 1.0f;	
    
    public YCartridge mCartridge;
      
    public ListItemCartridge( YCartridge cartridge ) {
    	super( R.layout.iconed_list_adapter );
    	mCartridge = cartridge;
    }
    
    @Override
    public void layout( Context context, View view  ) {
			
		try {
            byte[] iconData = mCartridge.getFile(mCartridge.getIconId());
            Bitmap iconLeft = null;
            try {
            	iconLeft = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
            } catch (Exception e) {
            	iconLeft = Images.getImageB(R.drawable.icon_gc_wherigo);
            }
            
            String name = mCartridge.getName();
            String description = "";
            
            if ( !mCartridge.isPlayAnywhere() ) {
        		Location loc = new Location(TAG);
        		loc.setLatitude(mCartridge.getLatitude());
        		loc.setLongitude(mCartridge.getLongitude());
 
        		description += (description.length()>0 ? ", " : "") + UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(loc), false);
            }
           
            description += (description.length()>0 ? ", " : "") + mCartridge.getAuthor();
            description += (description.length()>0 ? ", " : "") + mCartridge.getVersion();
            
            Bitmap iconRight = null;
            try {
                if (mCartridge.getSavegame().exists()) {
                	iconRight = Images.getImageB(android.R.drawable.ic_menu_save);
                }
            } catch (Exception e) {
                Logger.e(TAG, "xxx() - xxxx", e);
            }   

			TextView tv01 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView01);
			TextView tv02 = (TextView) view.findViewById(R.id.layoutIconedListAdapterTextView02);
	    	ImageView iv01 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView01);
			ImageView iv02 = (ImageView) view.findViewById(R.id.layoutIconedListAdapterImageView02);
			
			
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
			tv02.setBackgroundColor(Color.TRANSPARENT);
			tv02.setTextColor(Color.DKGRAY);

			tv02.setVisibility(View.GONE);
			if (description != null) {
				tv02.setVisibility(View.VISIBLE);
				tv02.setText(Html.fromHtml(description));				
			}

			// compute MULTI
			float multi = 1.0f;
			multi *= multiplyImageSize;
			
			// set ImageView left
			int iv01Width = (int) (multi * Images.SIZE_BIG);
			
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
			iv02.setVisibility(View.GONE);
			
			if ( iconRight != null){
				iv02.setVisibility(View.VISIBLE);
				iv02.setImageBitmap(iconRight);
			}
			


		} catch (Exception e) {
			Logger.e(TAG, "getView( " + view + " )", e);
		}

		// view.forceLayout();
		// return view;
	}
	
    @Override
	public boolean createContextMenu( Activity activity, ContextMenu menu ) {
        YCartridge cartridge = mCartridge;
        menu.setHeaderTitle( cartridge.getName() );
                           
        menu.add( Menu.NONE, R.string.ctx_menu_send_game, 1, activity.getString(R.string.ctx_menu_send_game) ); 
        
        if ( !cartridge.isPlayAnywhere()) {
        	menu.add( Menu.NONE, R.string.ctx_menu_show_on_map, 2, activity.getString(R.string.ctx_menu_show_on_map) );
        }
        
        try {
            if ( cartridge.existsSaveFile() ) {
                menu.add( Menu.NONE, R.string.ctx_menu_continue_game, 0, activity.getString(R.string.ctx_menu_continue_game) );
                menu.add( Menu.NONE, R.string.ctx_menu_restart_game, 0, activity.getString(R.string.ctx_menu_restart_game) );
                menu.add( Menu.NONE, R.string.ctx_menu_del_saved_game, 3, activity.getString(R.string.ctx_menu_del_saved_game) );
            } else {
                menu.add( Menu.NONE, R.string.ctx_menu_play, 0, activity.getString(R.string.ctx_menu_play) );
            }
            
            if ( cartridge.existsLogFile() ) {
                menu.add( Menu.NONE, R.string.ctx_menu_delete_log_file, 5, activity.getString(R.string.ctx_menu_delete_log_file) );
                menu.add( Menu.NONE, R.string.ctx_menu_send_log_file, 6, activity.getString(R.string.ctx_menu_send_log_file) );
            } else {
                
            }
        } catch (Exception e) {
            Logger.e(TAG, "onResume() - create empty saveGame file", e);
        }
        
        return true;
	}
	
	@Override
	public boolean onContextItemSelected( Activity activity, int index ) {
        YCartridge cartridge = mCartridge;   
        
        switch( index )
        {
            case R.string.ctx_menu_send_game:
                break;
            case R.string.ctx_menu_show_on_map:
            	/* TODO MAP
                Intent intent = new Intent( activity, CartridgeMapActivity.class );
                intent.putExtra( CartridgeMapActivity.MAPFILE, "/mnt/sdcard/Maps/germany.map" );
                Drawable defaultMarker = activity.getResources().getDrawable(R.drawable.icon_gc_wherigo);
                MapOverlays.clear();
            	MapWaypoint mapCartridge = MapOverlayFactory.createWaypoint( cartridge );
            	mapCartridge.setMarker( defaultMarker );
        		MapOverlays.mWaypoints.add( mapCartridge );                  
                activity.startActivity(intent);     
                */       	
                break;
            case R.string.ctx_menu_continue_game:
                CartridgeSession.Continue( cartridge, YaawpAppData.GetInstance().mWui );
                break;
            case R.string.ctx_menu_restart_game:
                CartridgeSession.Start( cartridge, YaawpAppData.GetInstance().mWui );
                break;
            case R.string.ctx_menu_del_saved_game:
        		try {
                    File file = new File( cartridge.getSaveFileName() );
                    file.delete();
        		} catch( Exception e ) {
                }
                break;
            case R.string.ctx_menu_play:
            	onListItemClicked( activity, cartridge );
                break;
            case R.string.ctx_menu_delete_log_file:
        		try {
                    File file = new File( cartridge.getLogFileName() );
                    file.delete();
        		} catch( Exception e ) {
                }
                break;

            case R.string.ctx_menu_send_log_file:
                break;
            default:
                break;    
        }
        return true;
	}
	
	@Override
    public void onListItemClicked( Activity activity ) {           	
        onListItemClicked( activity, mCartridge );
    }
    
    private void onListItemClicked( Activity activity, final YCartridge cartridge ) {
    	
        
        if ( cartridge.existsSaveFile() ) {
            UtilsGUI.showDialogQuestion( activity,
                    R.string.resume_previous_cartridge,
                    new DialogInterface.OnClickListener() {
        
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                    CartridgeSession.Continue( cartridge, YaawpAppData.GetInstance().mWui );
                }
            }, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int btn) {
                	YaawpAppData.GetInstance().mCurrentCartridge = cartridge;
                    ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
                }
            });
        } else {
        	YaawpAppData.GetInstance().mCurrentCartridge = cartridge;
        	ScreenHelper.activateScreen(ScreenHelper.SCREEN_CART_DETAIL, null);
        }
    }    	
}