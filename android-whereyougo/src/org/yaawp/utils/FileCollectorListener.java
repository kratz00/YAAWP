package org.yaawp.utils;

import java.io.File;

public interface FileCollectorListener {

	public boolean Begin( final File uri );
	
	public void End( final boolean abort );
	
	public boolean Update( final File uri ); 
	
}
