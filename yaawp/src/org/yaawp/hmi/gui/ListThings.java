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

import org.yaawp.hmi.helper.ScreenHelper;

import se.krka.kahlua.vm.LuaTable;
import android.os.Bundle;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;

public class ListThings extends ListVarious {

	public static final int INVENTORY = 0;
	public static final int SURROUNDINGS = 1;
	private int mode;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mode = getIntent().getIntExtra("mode", INVENTORY);
	}
	
	@Override
	protected void callStuff(Object what) {
		Thing t = (Thing) what;
		if (t.hasEvent("OnClick")) {
			Engine.callEvent(t, "OnClick", null);
		} else {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_DETAILSCREEN, t);
		}
		ListThings.this.finish();
	}

	@Override
	protected String getStuffName(Object what) {
		return ((Thing) what).name;
	}

	@Override
	protected Vector<Object> getValidStuff() {
		LuaTable container;
		if (mode == INVENTORY)
			container = Engine.instance.player.inventory;
		else
			container = Engine.instance.cartridge.currentThings();
		
		Vector<Object> newthings = new Vector<Object>();
		Object key = null;
		while ((key = container.next(key)) != null) {
			Thing t = (Thing) container.rawget(key);
			if (t.isVisible())
				newthings.add(t);
		}
		return newthings;
	}

	@Override
	protected boolean stillValid() {
		return true;
	}
	
	// TODO in TAB version
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			return getParent().onKeyDown(keyCode, event);
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}
}
