package org.yaawp.hmi.adapter;

import android.app.Activity;
import android.widget.BaseAdapter;
import java.util.Collections;
import java.util.Vector;

import org.yaawp.R;
import org.yaawp.YCartridge;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.hmi.listitem.ListItemCartridge;
import org.yaawp.hmi.listitem.ListItemHeader;
import org.yaawp.preferences.PreferenceUtils;

public class ListAdapterCartridges extends ListItemAdapter {

	private Vector<AbstractListItem> mAnywhereCartridges;
	private Vector<AbstractListItem> mLocatedCartridges;
	private Vector<AbstractListItem> mAllCartridges;
	
	public ListAdapterCartridges( Activity activity ) {
		super( activity );
		mAnywhereCartridges = new Vector<AbstractListItem>();
		mLocatedCartridges = new Vector<AbstractListItem>();
		mAllCartridges = new Vector<AbstractListItem>();
	}
	
	public void AddCartridges( Vector<YCartridge> cartridges ) {
    	
		mAnywhereCartridges.removeAllElements();
		mLocatedCartridges.removeAllElements();
		mAllCartridges.removeAllElements();
		
    	for ( int i=0; i<cartridges.size(); i++ ) {
    		YCartridge cartridge = cartridges.get(i);
    		ListItemCartridge x = new ListItemCartridge( cartridge, null );
    		if ( cartridge.isPlayAnywhere() ) {
    			mAnywhereCartridges.add( x );
    		} else {
    			mLocatedCartridges.add( x );
    		}
    		mAllCartridges.add( x );
    	}
    	
    	
    	// TODO - insert information for user there is no cartridge
    	//
		// ListItem3ButtonsHint item = new ListItem3ButtonsHint( "Note" /* TODO I18N */,
		///*I18N.get(R.string.no_wherigo_cartridge_available,"<i>"+FileSystem.ROOT+"</i>", MainApplication.APP_NAME),
		// 0 // TODO
    	//		); 
    	// adapter.AddItem( item );

	}
	
	@Override
	public void notifyDataSetChanged() {
		RemoveAllItems();
		
    	CartridgeComparator comparator1 = null;
    	CartridgeComparator comparator2 = null;
    	
    	String headerRight1 = "";
    	String headerRight2 = "";
    	int sorting = PreferenceUtils.getPrefInteger(R.string.pref_cartridgelist_sorting);
    	switch (sorting)
		{
			case 0:
				comparator1 = new CartridgeComparatorNameAtoZ();
				comparator2 = comparator1;
				headerRight1 = "A-Z";
				headerRight2 = headerRight1;
				break;
			case 1:
				comparator1 = new CartridgeComparatorNameZtoA();
				comparator2 = comparator1;
				headerRight1 = "Z-A";
				headerRight2 = headerRight1;
				break;			
			case 2:
				comparator1 = new CartridgeComparatorDistanceNear();
				comparator2 = new CartridgeComparatorNameAtoZ();
				headerRight1 = "near";
				headerRight2 = "A-Z";
				break;
			case 3:
				comparator1 = new CartridgeComparatorDistanceFar();
				comparator2 = new CartridgeComparatorNameAtoZ();
				headerRight1 = "far";
				headerRight2 = "A-Z";
				break;
		}		

    	
    	Collections.sort( mLocatedCartridges, comparator1 );
    				
		switch (sorting)
		{
			case 0: // A-Z
			case 1: // Z-A
				AddItem( new ListItemHeader("Cartridge", headerRight1, null ) );
				Collections.sort( mAllCartridges, comparator1 );
				AddItems( mAllCartridges );
				break;			
			case 2: // near first
			case 3: // far first
				
		    	if ( PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first ) 
		    		 && mAnywhereCartridges.size() > 0 ) {
		    		AddItem( new ListItemHeader("Cartridge - location less", headerRight2, null ) );
		    		Collections.sort( mAnywhereCartridges, comparator2 );
		    		AddItems( mAnywhereCartridges );
		    	}				
				
		    	AddItem( new ListItemHeader("Cartridge", headerRight1, null ) );
				Collections.sort( mLocatedCartridges, comparator1 );
				AddItems( mLocatedCartridges );
				
		    	if ( !(PreferenceUtils.getPrefBoolean(R.string.pref_cartridgelist_anywhere_first ))
		    		 && mAnywhereCartridges.size() > 0 ) {
		    		AddItem( new ListItemHeader("Cartridge - location less", headerRight2, null ) );
		    		Collections.sort( mAnywhereCartridges, comparator2 );
		    		AddItems( mAnywhereCartridges );
		    	}				
				
				break;
				
		}  
		
    	super.notifyDataSetInvalidated();
	}

}
