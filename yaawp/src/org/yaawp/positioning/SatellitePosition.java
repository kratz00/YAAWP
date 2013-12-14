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

package org.yaawp.positioning;

/* This class representing one satellite */
public class SatellitePosition {

    protected Integer prn;
    protected float mAzimuth;
    protected float mElevation;
    protected int mSignalNoiceRatio; /** signal to noise ratio */
    protected boolean mFixed; /** is satellite fixed */

    public SatellitePosition() {
        this.prn = new Integer(0);
        this.mAzimuth = 0.0f;
        this.mElevation = 0.0f;
        this.mSignalNoiceRatio = 0;
    }

    public Integer getPrn() {
        return prn;
    }

    public float getAzimuth() {
        return mAzimuth;
    }

    public float getElevation() {
        return mElevation;
    }

    public int getSnr() {
        return mSignalNoiceRatio;
    }
    
    public boolean isFixed() {
    	return mFixed;
    }
}
