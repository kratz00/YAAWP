package org.yaawp.hmi.activities;

import java.io.File;
import android.os.SystemClock;

import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.openwig.WSaveFile;
import org.yaawp.openwig.WSeekableFile;

import android.os.Bundle;

import android.view.WindowManager;

import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class SplashscreenActivity extends Activity {

	private static String TAG = "YaawpPreferenceActivity";
	
    private static final int CARTRIDGE_LIST_UPDATING = 0;
    private static final int CARTRIDGE_LIST_UPDATED = 1;
    private static final int CARTRIDGE_NOT_AVAILABLE = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
		setContentView(R.layout.activity_splashscreen);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 		
        return;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    }   

    @Override
    public void onResume() {
    	super.onResume();
    	fetchCartridgeFiles();
    }    
    
    @Override
    public void onPause() {
    	super.onPause();
    	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
 
    /* ------- */
    
    public void fetchCartridgeFiles() {

    	/* 
    	 * if ( YaawpAppData.GetInstance().mCartridgeListItems.size() > 0 ) {
    	 
    		// invalidateCartridgeList(); 
    		return;
    	} */
    	
        new Thread( new Runnable() { 
        	public void run() {
        		
        		updateProgressText("Fetch cartridges...");
        		
                // load cartridge files
                File[] files = FileSystem.getFiles(FileSystem.ROOT, "gwc");
                // YaawpAppData.GetInstance().mCartridgeListItems.clear(); 
                // YaawpAppData.GetInstance().mCartridgeListItems.add( new CartridgeListSeparatorItem("Cartridges") );
                
                if (files != null) {
                    for (File file : files) {
                        try {
                        	updateProgressText( file.getName() );
                        	
                        	SystemClock.sleep(100);
                        	
                            // actualFile = file;
                            YCartridge cart = YCartridge.read(file.getAbsolutePath(), new WSeekableFile(file), new WSaveFile(file));
                            
                            if (cart != null) {               
                                // YaawpAppData.GetInstance().mCartridgeListItems.add( new CartridgeListGameItem(cart) );
                            }
                        } catch (Exception e) {
                            Logger.w(TAG, "updateCartridgeList(), file:" + file + ", e:" + e.toString());
                            ManagerNotify.toastShortMessage(I18N.get(R.string.invalid_cartridge, file.getName()));
                        }
                    }
                }

                Intent intent = new Intent( SplashscreenActivity.this, CartridgeListActivity.class );
                startActivity(intent);                 
                
       		
        	}
        } ).start();
        
        SystemClock.sleep(5000);
    }
   
    public void updateProgressText( final String text ) {
        runOnUiThread( new Runnable() {
            public void run() {
        		// set title
        		((TextView) findViewById(R.id.text_view_progress)).setText(text); 
            }
        });    	
    }
    
    /*
    public void fetchCartridgeFilesNotification( int msgid ) {
        switch( msgid) {
            case CARTRIDGE_LIST_UPDATING:
            	ProgressDialogHelper.Show( "", "Loading Cartridges" ); 
                break;
            case CARTRIDGE_LIST_UPDATED:
                // invalidateCartridgeList();
            	final ListView listview = (ListView) findViewById(R.id.listView1); 
                adapter = new CartridgeListAdapter( this, YaawpAppData.GetInstance().mCartridgeListItems, null );    
                adapter.setTextView02Visible(View.VISIBLE, false);
                    
                runOnUiThread( new Runnable() {
                    public void run() {
                        listview.setAdapter( adapter );
                    }
                }
                );
                    
        		ProgressDialogHelper.Hide();
                break;
            case CARTRIDGE_NOT_AVAILABLE:   
            	ProgressDialogHelper.Hide();
                UtilsGUI.showDialogInfo(Main.this, 
                                getString(R.string.no_wherigo_cartridge_available,
                                        FileSystem.ROOT, MainApplication.APP_NAME));            
                break;
        }
    } */     
}