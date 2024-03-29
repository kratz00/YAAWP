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

package org.yaawp.hmi.helper;

import org.yaawp.utils.A;

public class I18N {

	public static final String get(int string) {
		if (A.getApp() != null) {
			return A.getApp().getString(string);
		} else {
			return "";
		}
	}
	
	public static final String get(int string, String replace) {
		if (A.getApp() != null) {
			return A.getApp().getString(string, replace);
		} else {
			return "";
		}
	}
	
	public static final String get(int string, String replace01, String replace02) {
		if (A.getApp() != null) {
			return A.getApp().getString(string, replace01, replace02);
		} else {
			return "";
		}
	}
}
