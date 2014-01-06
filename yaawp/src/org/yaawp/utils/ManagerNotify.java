/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.utils;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.preferences.Settings;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * @author menion
 * @since 10.2.2010 2010
 */
public class ManagerNotify {

	private static final String TAG = "ManagerNotify";
    
    public static void toastInternetProblem() {
    	toastLongMessage(I18N.get(R.string.problem_with_internet_connection));
    }
    
    public static void toastUnexpectedProblem() {
    	toastLongMessage(I18N.get(R.string.unexpected_problem));
    }
    
    public static void toastShortMessage(final int msg) {
    	toastShortMessage(I18N.get(msg));
    }
    
    public static void toastShortMessage(final String msg) {
    	toastShortMessage(Settings.getCurrentActivity(), msg);
    }
    
    public static void toastShortMessage(final Context context, final String msg) {
    	toastMessage(context, msg, Toast.LENGTH_SHORT);
    }
    
    public static void toastLongMessage(final int msg) {
    	toastLongMessage(I18N.get(msg));
    }
    
    public static void toastLongMessage(final String msg) {
    	toastLongMessage(Settings.getCurrentActivity(), msg);
    }
    
    public static void toastLongMessage(final Context context, final String msg) {
    	toastMessage(context, msg, Toast.LENGTH_LONG);
    }
    
    private static void toastMessage(final Context context, final String msg, final int time) {
    	Logger.d(TAG, "toastMessage(" + context + ", " + msg + ", " + time + ")");
    	if (context == null || msg == null || msg.length() == 0)
    		return;

    	try {
    		if (context instanceof Activity) {
    			((Activity) context).runOnUiThread(new Runnable() {
    				public void run() {
    					Toast.makeText(context, msg, time).show();
    				}
    			});
    		} else {
    			Toast.makeText(context, msg, time).show();
    		}
    	} catch (Exception e) {
    		Logger.e(TAG, "toastMessage(" + context + ", " + msg + ", " + time + ")", e);
		}
    }
}
