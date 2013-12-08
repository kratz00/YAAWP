package org.yaawp.utils;

import java.io.InputStream;


public class AssetHelper {

    public static String loadAssetString(String name) {
    	InputStream is = null;
    	try {
			is = A.getMain().getAssets().open(name);
			int size = is.available();

			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			return new String(buffer);
		} catch (Exception e) {
			Logger.e("AssetHelper", "loadAssetString(" + name + ")", e);
			return "";
		} finally {
			Utils.closeStream(is);
		}
    }	
}
