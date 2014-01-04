package org.yaawp.hmi.adapter;

import android.app.Activity;
import java.util.Vector;

import org.yaawp.YCartridge;
import org.yaawp.hmi.listitem.ListItemCartridge;

public class ListAdapterCartridges extends ListItemAdapter {

	public ListAdapterCartridges( Activity activity ) {
		super( activity );
	}
	
	public void AddCartridges( Vector<YCartridge> cartridges ) {
    	if ( cartridges.size() > 0 ) {
        	for ( int i=0; i<cartridges.size(); i++ ) {
        		AddItem( new ListItemCartridge( cartridges.get(i), null ) );
        	}
    	}
    	
    	// TODO - insert information for user there is no cartridge
    	//
		// ListItem3ButtonsHint item = new ListItem3ButtonsHint( "Note" /* TODO I18N */,
		///*I18N.get(R.string.no_wherigo_cartridge_available,"<i>"+FileSystem.ROOT+"</i>", MainApplication.APP_NAME),
		// 0 // TODO
    	//		); 
    	// adapter.AddItem( item );

	}
}
