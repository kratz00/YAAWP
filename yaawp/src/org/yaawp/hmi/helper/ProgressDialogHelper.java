package org.yaawp.hmi.helper;

import org.yaawp.utils.A;
import org.yaawp.utils.Logger;

import android.app.ProgressDialog;
import android.app.Activity;

public class ProgressDialogHelper {

	private static final String TAG = "ProgressDialogHelper";
	
	private static ProgressDialog progressDialog;
	
	/*
	public static void Show( String headline, int message ) {
	}

	public static void Show( int headline, String message ) {
	}		
	
	public static void Show( int headline, int message ) {
	}	
	*/
	
	public static void Show( final String headline, final String message ) {		
		Logger.i(TAG, "Show()");
		
		A.getMain().runOnUiThread(new Runnable() {
            public void run() {		
            	Logger.i(TAG, "Show() - UIThread context");
        		if (progressDialog != null) {
        			Logger.w(TAG, "progress dialog shows twice");
                    progressDialog.dismiss();    
        		}
        		
            	progressDialog = new ProgressDialog( A.getApp() );
            	progressDialog.setMessage(message);
            	progressDialog.setTitle( headline );
            	progressDialog.show();  	
            }
        });
	}
	
	public static void Hide() {
		Logger.i(TAG, "Hide()");
		
        A.getMain().runOnUiThread(new Runnable() {
            public void run() {
            	Logger.i(TAG, "Hide() - UIThread context");
                if (progressDialog != null) {
                    progressDialog.dismiss();   
                	progressDialog = null;
                }
            }
        }); 		
	}
	
	public static void Update( final String message ) {
        A.getMain().runOnUiThread(new Runnable() {
            public void run() {
            	Logger.i(TAG, "Update(" + message + ")");
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
            }
        }); 		
	}
	
}
