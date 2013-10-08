/*
  * This file is part of WhereYouGo.
  *
  * WhereYouGo is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * WhereYouGo is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with WhereYouGo.  If not, see <http://www.gnu.org/licenses/>.
  *
  * Copyright (C) 2012 Menion <whereyougo@asamm.cz>
  */ 

package menion.android.whereyougo.gui;

import java.io.File;
import java.io.FileOutputStream;

import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.Main;
import org.yaawp.R;
import menion.android.whereyougo.gui.extension.CustomActivity;
import menion.android.whereyougo.gui.extension.CustomDialog;
import menion.android.whereyougo.hardware.location.LocationState;
import menion.android.whereyougo.settings.Loc;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cz.matejcik.openwig.formats.ICartridge;

import org.yaawp.app.YaawpAppData;
import org.yaawp.bl.CartridgeSession;

public class CartridgeDetails extends CustomActivity {
	
	private static final String TAG = "CartridgeDetails";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ICartridge cartridgeFile = YaawpAppData.GetInstance().mCurrentCartridge;
		
		setContentView(R.layout.layout_details);

		TextView tvName = (TextView) findViewById(R.id.layoutDetailsTextViewName);
		tvName.setText(Html.fromHtml(cartridgeFile.getName()));
		
		TextView tvState = (TextView) findViewById(R.id.layoutDetailsTextViewState);
		tvState.setText(Html.fromHtml(Loc.get(R.string.author) + ": " + cartridgeFile.getAuthor()));

		TextView tvDescription = (TextView) findViewById(R.id.layoutDetailsTextViewDescription);
		tvDescription.setText(Html.fromHtml(cartridgeFile.getDescription()));
		
		ImageView ivImage = (ImageView) findViewById(R.id.layoutDetailsImageViewImage);
		try {
			byte[] is = cartridgeFile.getFile(cartridgeFile.getSplashId());
			Bitmap i = BitmapFactory.decodeByteArray(is, 0, is.length);
			Main.setBitmapToImageView(i, ivImage);
		} catch (Exception e) {}
		
		TextView tvText = (TextView) findViewById(R.id.layoutDetailsTextViewImageText);
		tvText.setVisibility(View.GONE);
		
		TextView tvDistance = (TextView) findViewById(R.id.layoutDetailsTextViewDistance);

		Location loc = new Location(TAG);
		loc.setLatitude(cartridgeFile.getLatitude());
		loc.setLongitude(cartridgeFile.getLongitude());

		StringBuffer buff = new StringBuffer();
		buff.append(Loc.get(R.string.distance)).append(": ").append("<b>").
		append(UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(loc), false)).
		append("</b>").append("<br />").
		append(Loc.get(R.string.latitude)).append(": ").
		append(UtilsFormat.formatLatitude(cartridgeFile.getLatitude())).
		append("<br />").
		append(Loc.get(R.string.longitude)).append(": ").
		append(UtilsFormat.formatLongitude(cartridgeFile.getLongitude()));
		
		tvDistance.setText(Html.fromHtml(buff.toString()));
		
		CustomDialog.setBottom(this, 
				getString(R.string.start), new CustomDialog.OnClickListener() {
			@Override
			public boolean onClick(CustomDialog dialog, View v, int btn) {
				CartridgeDetails.this.finish();
				CartridgeSession.Start( YaawpAppData.GetInstance().mCurrentCartridge, YaawpAppData.GetInstance().mWui ); 
				return true;
			}
		}, null, null,
		getString(R.string.navigate), new CustomDialog.OnClickListener() {
			@Override
			public boolean onClick(CustomDialog dialog, View v, int btn) {
			    ICartridge cartridge = YaawpAppData.GetInstance().mCurrentCartridge;
				Location loc = new Location(TAG);
				loc.setLatitude(cartridge.getLatitude());
				loc.setLongitude(cartridge.getLongitude());
				Waypoint wpt = new Waypoint(cartridge.getName(), loc);
				A.getGuidingContent().guideStart(wpt);
				Main.callGudingScreen(CartridgeDetails.this);
				CartridgeDetails.this.finish();
				return true;
			}
		});
	}
}
