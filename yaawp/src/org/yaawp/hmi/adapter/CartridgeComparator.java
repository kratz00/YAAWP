package org.yaawp.hmi.adapter;

import java.util.Comparator;

import org.yaawp.YCartridge;
import org.yaawp.hmi.listitem.AbstractListItem;
import org.yaawp.hmi.listitem.ListItemCartridge;

public class CartridgeComparator implements Comparator<AbstractListItem> {

	@Override
    public int compare(AbstractListItem o1, AbstractListItem o2) {
		if ( !(o1 instanceof ListItemCartridge) )
			return 0;
		if ( !(o2 instanceof ListItemCartridge) )
			return 0;
				
    	YCartridge c1 = null; // ((ListItemCartridge)o1).mCartridge;
    	YCartridge c2 = null; // ((ListItemCartridge)o2).mCartridge;
    	
    	return compare(c1,c2);
	}
	
	public int compare(YCartridge c1, YCartridge c2) {
		return 0;
	}
}
