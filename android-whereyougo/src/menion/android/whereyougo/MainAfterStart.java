/*
R.stringR.string  * This file is part of WhereYouGo.
  *
  * WhereYouGo is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * WhereYouGo is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with WhereYouGo.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2012 Menion <whereyougo@asamm.cz>
  */ 

package menion.android.whereyougo;

import menion.android.whereyougo.gui.extension.CustomMain;
import menion.android.whereyougo.gui.extension.MainApplication;
import menion.android.whereyougo.gui.extension.UtilsGUI;
import menion.android.whereyougo.utils.A;
import android.app.AlertDialog;
import android.content.DialogInterface;
import org.yaawp.R;
import org.yaawp.preferences.Settings;

/**
 * @author menion
 * @since 1.4.2010 2010
 */
public class MainAfterStart {

//	private static final String TAG = "MainAfterStart";
	
    /****************************************/
    /*         AFTER START ACTIONS          */
    /****************************************/
    
    private static boolean stage01Completed = false;
    
    public static void afterStartAction() {
    	if (!stage01Completed) {
    		int lastVersion = Settings.getApplicationVersionLast();
    		final int actualVersion = Settings.getApplicationVersionActual();
    		if (lastVersion == 0 || actualVersion != lastVersion) {
    			String news = getNews(lastVersion, actualVersion);
    			if (news != null && news.length() > 0) {
    	    		// show dialog
    		    	AlertDialog.Builder b = new AlertDialog.Builder(A.getMain());
    		    	b.setCancelable(false);
    		    	b.setTitle(MainApplication.APP_NAME);
    		    	b.setIcon(R.drawable.ic_launcher);
    		    	b.setView(UtilsGUI.getFilledWebView(A.getMain(), news));
    		    	b.setNeutralButton(R.string.yes, 
    		    			new DialogInterface.OnClickListener() {
								
    		    		@Override
    		    		public void onClick(DialogInterface dialog, int which) {
    						stage01Completed = true;
    						Settings.setApplicationVersionLast(actualVersion);	
    		    		}
    		    	});	
    				b.show(); 
    			} else {
    				stage01Completed = true;
    			}
    		} else {
    			stage01Completed = true;
    		}
    	}
    }
    
    public static String getNews(int lastVersion, int actualVersion) {
   		String newsInfo = "";
    	
   		if (lastVersion == 0) {
   			newsInfo += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
   			newsInfo += CustomMain.loadAssetString(Settings.getLanguageCode() + "_first.html");
           	newsInfo += "</body></html>";
        } else {
        	newsInfo = CustomMain.getNewsFromTo(lastVersion, actualVersion);
        }

   		return newsInfo;
    }
}
