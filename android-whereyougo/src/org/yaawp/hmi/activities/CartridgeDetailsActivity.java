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

package org.yaawp.hmi.activities;

import org.yaawp.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.yaawp.YCartridge;
import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;
import org.yaawp.guidance.WaypointGuide;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.hmi.panelbar.buttons.PanelBarButtonGuidance;
import org.yaawp.positioning.Location;
import org.yaawp.positioning.LocationState;
import org.yaawp.utils.UtilsFormat;

public class CartridgeDetailsActivity extends CustomActivity {
	
	private static final String TAG = "CartridgeDetails";
	private ThreeButtonPanelBar mPanelButtonBar = new ThreeButtonPanelBar(this);

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		YCartridge cartridgeFile = YaawpAppData.GetInstance().mCurrentCartridge;
		
		setContentView(R.layout.layout_details);

		TextView tvName = (TextView) findViewById(R.id.layoutDetailsTextViewName);
		tvName.setText(Html.fromHtml(cartridgeFile.getName()));
		
		TextView tvState = (TextView) findViewById(R.id.layoutDetailsTextViewState);
		tvState.setText(Html.fromHtml(I18N.get(R.string.author) + ": " + cartridgeFile.getAuthor()));

		TextView tvDescription = (TextView) findViewById(R.id.layoutDetailsTextViewDescription);
		tvDescription.setText(Html.fromHtml(cartridgeFile.getDescription()));
		
		ImageView ivImage = (ImageView) findViewById(R.id.layoutDetailsImageViewImage);
		try {
			byte[] is = cartridgeFile.getFile(cartridgeFile.getSplashId());
			Bitmap i = BitmapFactory.decodeByteArray(is, 0, is.length);
			CartridgeListActivity.setBitmapToImageView(i, ivImage);
		} catch (Exception e) {}
		
		TextView tvText = (TextView) findViewById(R.id.layoutDetailsTextViewImageText);
		tvText.setVisibility(View.GONE);
		
		TextView tvDistance = (TextView) findViewById(R.id.layoutDetailsTextViewDistance);

		Location loc = new Location(TAG);
		loc.setLatitude(cartridgeFile.getLatitude());
		loc.setLongitude(cartridgeFile.getLongitude());

		StringBuffer buff = new StringBuffer();
		buff.append(I18N.get(R.string.distance)).append(": ").append("<b>").
		append(UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(loc), false)).
		append("</b>").append("<br />").
		append(I18N.get(R.string.latitude)).append(": ").
		append(UtilsFormat.formatLatitude(cartridgeFile.getLatitude())).
		append("<br />").
		append(I18N.get(R.string.longitude)).append(": ").
		append(UtilsFormat.formatLongitude(cartridgeFile.getLongitude()));
		
		tvDistance.setText(Html.fromHtml(buff.toString()));
		
		/* ----------- */
			
		mPanelButtonBar.AddButton( new PanelBarButton( getString(R.string.start), 
			new PanelBarButton.OnClickListener() {
				@Override
				public boolean onClick() {
					CartridgeDetailsActivity.this.finish();
					CartridgeSession.Start( YaawpAppData.GetInstance().mCurrentCartridge, YaawpAppData.GetInstance().mWui ); 
					return true;
				}
			}
		));	
		
		if ( cartridgeFile.isPlayAnywhere() == false ) {
			WaypointGuide wpt = new WaypointGuide(cartridgeFile.getName(), loc);			
			mPanelButtonBar.AddButton( new PanelBarButtonGuidance( this, wpt, true, true ) );
		}	
			
	}
}
