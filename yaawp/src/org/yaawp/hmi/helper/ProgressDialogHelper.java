package org.yaawp.hmi.helper;

import org.yaawp.utils.A;
import org.yaawp.utils.Logger;

import android.app.ProgressDialog;
import android.app.Activity;

public class ProgressDialogHelper {

	private static final String TAG = ProgressDialogHelper.class.getSimpleName();

	private static ProgressDialog progressDialog;
	
	public static void Show( final String headline, final String message ) {		
		// Logger.v(TAG, "Show()");

		A.getMain().runOnUiThread(new Runnable() {
            public void run() {		
            	// Logger.v(TAG, "UIThread context - Show()");
        		if (progressDialog != null) {
        			Logger.w(TAG, "close active dialog");
                    progressDialog.dismiss();    
        		}
        		
            	progressDialog = new ProgressDialog( (Activity)A.getMain() );
            	progressDialog.setMessage(message);
            	progressDialog.setTitle( headline );
            	progressDialog.show();  	
            	// Logger.v(TAG, "UIThread context - Show() end");
            }
        });
	}
	
	public static void Hide() {
		// Logger.v(TAG, "Hide()");
		
        A.getMain().runOnUiThread(new Runnable() {
            public void run() {
            	// Logger.v(TAG, "UIThread context - Hide()");
                if (progressDialog != null) {
                    progressDialog.dismiss();   
                	progressDialog = null;
                }
             // Logger.v(TAG, "UIThread context - Hide() end");
            }
        }); 		
	}
	
	public static void Update( final String message ) {
		// Logger.v(TAG, "Update(" + message + ")" );
        
		A.getMain().runOnUiThread(new Runnable() {
            public void run() {
            	// Logger.v(TAG, "UIThread context - Update(" + message + ")");
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
             // Logger.v(TAG, "UIThread context - Update() end");
            }
        }); 	
	}
	
}
