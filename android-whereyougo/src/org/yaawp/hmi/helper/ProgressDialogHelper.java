package org.yaawp.hmi.helper;

import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import android.app.ProgressDialog;

public class ProgressDialogHelper {

	private static final String TAG = "ProgressDialogHelper";
	
	private static ProgressDialog progressDialog;
	
	public static void Show( String headline, int message ) {
	}

	public static void Show( int headline, String message ) {
	}		
	
	public static void Show( int headline, int message ) {
	}	
	
	public static void Show( String headline, String message ) {		
		Logger.i(TAG, "Show()");
		
		if (progressDialog != null) {
			Logger.w(TAG, "progress dialog shows twice");
            progressDialog.dismiss();    
		}
        
        progressDialog = new ProgressDialog(((CustomActivity) A.getMain()));
        progressDialog.setMessage(message);
        progressDialog.setTitle( headline );
        progressDialog.show();  		
	}
	
	public static void Hide() {
        ((CustomActivity) A.getMain()).runOnUiThread(new Runnable() {
            public void run() {
            	Logger.i(TAG, "Hide()");
                if (progressDialog != null) {
                    progressDialog.dismiss();   
                	progressDialog = null;
                }
            }
        }); 		
	}
	
	public static void Update( final String message ) {
        ((CustomActivity) A.getMain()).runOnUiThread(new Runnable() {
            public void run() {
            	Logger.i(TAG, "Update(" + message + ")");
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
            }
        }); 		
	}
	
}
