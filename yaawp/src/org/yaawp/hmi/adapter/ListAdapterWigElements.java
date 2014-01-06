package org.yaawp.hmi.adapter;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
import java.util.Vector;
import org.yaawp.hmi.listitem.ListItemCartridgeHeadline;
import org.yaawp.hmi.listitem.ListItemGpsDisabledWarning;
import org.yaawp.hmi.listitem.ListItemGuidanceActive;
import org.yaawp.hmi.listitem.ListItemWherigoCategoryHeaderInventor;
import org.yaawp.hmi.listitem.ListItemWherigoCategoryHeaderTasks;
import org.yaawp.hmi.listitem.ListItemWherigoCategoryHeaderYouSee;
import org.yaawp.hmi.listitem.ListItemWherigoCategoryHeaderZones;
import org.yaawp.hmi.listitem.ListItemWherigoElementTask;
import org.yaawp.hmi.listitem.ListItemWherigoElementThing;
import org.yaawp.hmi.listitem.ListItemWherigoElementZone;




public class ListAdapterWigElements extends ListItemAdapter {

	private ListItemGpsDisabledWarning mGpsDisabledWarning = null;
	private ListItemGuidanceActive mGuidanceActive = null;
	private ListItemCartridgeHeadline mCartridgeHeadline = null;
	private ListItemWherigoCategoryHeaderInventor mWherigoInventory = null;
	private ListItemWherigoCategoryHeaderTasks mWherigoTasks = null;
	private ListItemWherigoCategoryHeaderYouSee mWherigoYouSee = null;
	private ListItemWherigoCategoryHeaderZones mWherigoZones = null;	
	
	
	
	public ListAdapterWigElements( Activity activity ) {
		super( activity );
		
		mGpsDisabledWarning = new ListItemGpsDisabledWarning( mActivity );
		mGuidanceActive     = new ListItemGuidanceActive(mActivity);
		mCartridgeHeadline  = new ListItemCartridgeHeadline();
		mWherigoYouSee      = new ListItemWherigoCategoryHeaderYouSee();
		mWherigoZones       = new ListItemWherigoCategoryHeaderZones();
	   	mWherigoInventory   = new ListItemWherigoCategoryHeaderInventor();
	   	mWherigoTasks       = new ListItemWherigoCategoryHeaderTasks(); 	
	   	
       	/*
       	AddItem( mGpsDisabledWarning );
       	AddItem( mGuidanceActive );
       	AddItem( mCartridgeHeadline );
       	
       	addElementsOfCategoryZones();
    	addElementsOfCategoryYouSee();
    	addElementsOfCategoryInventory();
    	addElementsOfCategoryTasks(); 	
    	*/	   	
	}	
	
	private void addElementsOfCategoryZones() {
    	mWherigoZones.refresh();
    	AddItem( mWherigoZones );
		for (int i = 0; i < Engine.instance.cartridge.zones.size(); i++) {
			Zone z = (Zone)Engine.instance.cartridge.zones.get(i);
			if (z.isVisible()) {
				AddItem( new ListItemWherigoElementZone( z, mWherigoZones ) );
			}
		}  		
	}
	
	private void addElementsOfCategoryYouSee() {
		mWherigoYouSee.refresh();
		AddItem( mWherigoYouSee );
		Vector<Zone> zones = Engine.instance.cartridge.zones;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);			
			if (z.showThings()) {
				Object key = null;
				while ((key = z.inventory.next(key)) != null) {
					Object o = z.inventory.rawget(key);
					if (o instanceof Player)
						continue;
					if (!(o instanceof Thing))
						continue;
					if (((Thing) o).isVisible()) {
						AddItem( new ListItemWherigoElementThing( (Thing) o, mWherigoYouSee ) );
					}
				}
			}
		}   		
	}
	
	private void addElementsOfCategoryInventory() {
		mWherigoInventory.refresh();
		AddItem( mWherigoInventory );
		Player p = Engine.instance.player;
		Object key = null;
		while ((key = p.inventory.next(key)) != null) {
			Object o = p.inventory.rawget(key);
			if (o instanceof Thing && ((Thing) o).isVisible()) {
				AddItem( new ListItemWherigoElementThing( (Thing) o, mWherigoInventory ) );
			}
		}		
	}
	
	private void addElementsOfCategoryTasks() {
    	mWherigoTasks.refresh();
    	AddItem( mWherigoTasks );
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) {
				AddItem( new ListItemWherigoElementTask( a, mWherigoTasks ) );
			}
		}   		
	}	
	
	public void refresh() {
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
		       	RemoveAllItems();
		       	AddItem( mGpsDisabledWarning );
		       	AddItem( mGuidanceActive );
		       	AddItem( mCartridgeHeadline );
		       	addElementsOfCategoryZones();
		    	addElementsOfCategoryYouSee();
		    	addElementsOfCategoryInventory();
		    	addElementsOfCategoryTasks(); 					
		    	notifyDataSetChanged();
			}
		});		
	}	
}
