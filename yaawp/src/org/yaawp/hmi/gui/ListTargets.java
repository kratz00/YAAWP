/*
  * This file is part of Yaawp.
  *
  * Yaawp  is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * Yaawp  is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Yaawp.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2013
  *
  */ 

package org.yaawp.hmi.gui;

import java.util.Vector;

import org.yaawp.R;
import org.yaawp.hmi.activities.WigDetailsActivity;
import org.yaawp.hmi.gui.extension.UtilsGUI;
import org.yaawp.hmi.helper.ScreenHelper;

import se.krka.kahlua.vm.LuaTable;
import android.content.DialogInterface;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;

public class ListTargets extends ListVarious {

//	private static String title;
	private static Action action;
	private static Thing thing;

	private static Vector<Object> validStuff;
	
	public static void reset(String title, Action what, Thing actor) {
//		ListTargets.title = title;
		ListTargets.action = what;
		ListTargets.thing = actor;
		makeValidStuff();
	}
	
	private static void makeValidStuff() {
		LuaTable current = Engine.instance.cartridge.currentThings();
//		int size = current.len() + Engine.instance.player.inventory.len();
		validStuff = new Vector<Object>();
		Object key = null;
		while ((key = current.next(key)) != null)
			validStuff.addElement(current.rawget(key));
		while ((key = Engine.instance.player.inventory.next(key)) != null)
			validStuff.addElement(Engine.instance.player.inventory.rawget(key));
		
		for (int i = 0; i < validStuff.size(); i++) {
			Thing t = (Thing)validStuff.elementAt(i);
			if (! t.isVisible() || ! action.isTarget(t)) {
				validStuff.removeElementAt(i--);
			}
		}
	}
	
	public void refresh() {
		if (validStuff.isEmpty()) {
			UtilsGUI.showDialogInfo(this, R.string.no_target,
					new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ListTargets.this.finish();
				}
			});
		} else {
			super.refresh();
		}
	}
	
	@Override
	protected void callStuff(Object what) {
		ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, WigDetailsActivity.et);
		String eventName = "On" + action.getName();
		Engine.callEvent(action.getActor(), eventName, (Thing) what);
	}

	@Override
	protected String getStuffName(Object what) {
		return ((Thing)what).name;
	}

	@Override
	protected Vector<Object> getValidStuff() {
		return validStuff;
	}

	@Override
	protected boolean stillValid() {
		return thing.visibleToPlayer();
	}

}
