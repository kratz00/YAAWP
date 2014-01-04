package org.yaawp.utils.FileCollector;

import java.io.File;
import java.io.FilenameFilter;

public interface FileCollectorFilter extends FilenameFilter {

	public final boolean CONTINUE = true;
	public final boolean STOP = false;
	
	public boolean accept(File dir, String name);
	
	public void setListener( FileCollectorListener listener );
}

