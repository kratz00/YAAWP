package org.yaawp.hmi.adapter;

import java.util.Comparator;

import org.yaawp.YCartridge;

public class CartridgeComparatorNameZtoA extends CartridgeListAdapterItemComparator { 
    @Override
    public int compare(YCartridge c1, YCartridge c2) {
    	String s1 = c1.getName();
    	String s2 = c2.getName();
    	return s1.compareToIgnoreCase(s2)*-1;
    } 
}