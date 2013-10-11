package org.yaawp.utils.FileCollector;

import java.io.File;
import java.io.FilenameFilter;

public interface FileCollectorFilter extends FilenameFilter {

	public boolean accept(File dir, String name);
	
	public void setListener( FileCollectorListener listener );
}

