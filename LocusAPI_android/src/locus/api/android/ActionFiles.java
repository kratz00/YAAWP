package locus.api.android;

import java.io.File;

import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

public class ActionFiles {

	/**
	 * Generic call to system for applications that can import your file.
	 * @param context
	 * @param file
	 * @return
	 */
	public static boolean importFileSystem(Context context, File file) {
		if (!isReadyForImport(context, file))
			return false;
		
    	Intent sendIntent = new Intent(Intent.ACTION_VIEW);
    	Uri uri = Uri.fromFile(file);
    	sendIntent.setDataAndType(uri, getMimeType(file));
    	context.startActivity(sendIntent);
    	return true;
	}

	/**
	 * Import GPX/KML files directly into Locus application. 
	 * Return false if file don't exist or Locus is not installed
	 * @param context
	 * @param file
	 */
	public static boolean importFileLocus(Context context, File file) {
		return importFileLocus(context, file, true);
	}
	
	/**
	 * Import GPX/KML files directly into Locus application. 
	 * Return false if file don't exist or Locus is not installed
	 * @param context
	 * @param file
     * @param callImport
	 */
	public static boolean importFileLocus(Context context, File file, boolean callImport) {
		if (!isReadyForImport(context, file))
			return false;
		
    	Intent sendIntent = new Intent(Intent.ACTION_VIEW);
    	PackageInfo pi = LocusUtils.getLocusPackageInfo(context);
    	sendIntent.setClassName(pi.packageName, "menion.android.locus.core.MainActivity");
    	Uri uri = Uri.fromFile(file);
    	sendIntent.setDataAndType(uri, getMimeType(file));
    	sendIntent.putExtra(LocusConst.INTENT_EXTRA_CALL_IMPORT, callImport);
    	context.startActivity(sendIntent);
    	return true;
	}
	
	private static boolean isReadyForImport(Context context, File file) {
		if (file == null || !file.exists() || !LocusUtils.isLocusAvailable(context))
			return false;
		return true;
	}
	
	private static String getMimeType(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index == -1)
			return "*/*";
		return "application/" + name.substring(index + 1);
	}
}
