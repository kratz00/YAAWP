package org.yaawp.hmi.adapter;

import java.util.Comparator;

import org.yaawp.YCartridge;

public class CartridgeListAdapterItemComparator implements Comparator<AbstractListItem> {

	@Override
    public int compare(AbstractListItem o1, AbstractListItem o2) {
		if ( !(o1 instanceof CartridgeListAdapterItemCartridge) )
			return 0;
		if ( !(o2 instanceof CartridgeListAdapterItemCartridge) )
			return 0;
				
    	YCartridge c1 = ((CartridgeListAdapterItemCartridge)o1).mCartridge;
    	YCartridge c2 = ((CartridgeListAdapterItemCartridge)o2).mCartridge;
    	
    	return compare(c1,c2);
	}
	
	public int compare(YCartridge c1, YCartridge c2) {
		return 0;
	}
}
