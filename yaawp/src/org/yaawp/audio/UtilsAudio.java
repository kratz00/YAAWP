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

package org.yaawp.audio;

import org.yaawp.R;
import org.yaawp.utils.A;
import org.yaawp.utils.Logger;

public class UtilsAudio {

	private static final String TAG = "UtilsAudio";
	
	public static void playBeep(int count) {
		try {
			if (A.getApp() != null)
				new AudioClip(A.getApp(), R.raw.sound_beep_01).play(count);
			else if (A.getMain() != null)
				new AudioClip(A.getMain(), R.raw.sound_beep_01).play(count);
		} catch (Exception e) {
			Logger.e(TAG, "playBeep(" + count + ")", e);
		}
	}
}
