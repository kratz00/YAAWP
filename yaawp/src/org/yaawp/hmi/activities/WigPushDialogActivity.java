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
import org.yaawp.hmi.panelbar.ThreeButtonPanelBar;
import org.yaawp.hmi.panelbar.buttons.PanelBarButton;
import org.yaawp.utils.Logger;

import se.krka.kahlua.vm.LuaClosure;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;

public class WigPushDialogActivity extends CustomActivity {

	private static final String TAG = "PushDialog";
	
	private static String menu01Text = null;
	private static String menu02Text = null;
	private ThreeButtonPanelBar mButtonPanelBar = null;

	// STATIC CONTENT
	private static String[] texts;
	private static Media[] media;
	private static LuaClosure callback;
	private static int page = -1;

	public static void setDialog(String[] texts, Media[] media,
			String button1, String button2, LuaClosure callback) {
		WigPushDialogActivity.texts = texts;
		WigPushDialogActivity.media = media;
		WigPushDialogActivity.callback = callback;
		WigPushDialogActivity.page = -1;
		
		if (button1 == null)
			button1 = "OK";
		
		menu01Text = button1;
		menu02Text = button2;
Logger.d(TAG, "setDialog() - finish, callBack:" + (callback != null));
	}
	
	private ImageView ivImage;
	private TextView tvImageText;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_details);
		findViewById(R.id.layoutDetailsTextViewName).setVisibility(View.GONE);
		findViewById(R.id.layoutDetailsTextViewState).setVisibility(View.GONE);
		findViewById(R.id.layoutDetailsTextViewDescription).setVisibility(View.GONE);
		ivImage = (ImageView) findViewById(R.id.layoutDetailsImageViewImage);
		tvImageText = (TextView) findViewById(R.id.layoutDetailsTextViewImageText);
		findViewById(R.id.layoutDetailsTextViewDistance).setVisibility(View.GONE);
		
		if (menu02Text == null || menu02Text.length() == 0) {
			menu02Text = null;
		}
		
		mButtonPanelBar = new ThreeButtonPanelBar(this);
		
		mButtonPanelBar.RemoveAllButtons();

		mButtonPanelBar.AddButton( new PanelBarButton( menu01Text, 
				new PanelBarButton.OnClickListener() {
					@Override
					public boolean onClick() {
						nextPage();
						return true;
					}
				}
			));	
		
		if ( menu02Text != null && menu02Text.isEmpty() == false ) {
			mButtonPanelBar.AddButton( new PanelBarButton( menu02Text, 
					new PanelBarButton.OnClickListener() {
						@Override
						public boolean onClick() {
							if (callback != null)
								Engine.invokeCallback(callback, "Button2");
							callback = null;
							WigPushDialogActivity.this.finish();
							return true;
						}
					}
				));						
		}

		if (page == -1) {
			nextPage();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	private void nextPage() {
Logger.d(TAG, "nextpage() - page:" + page + ", texts:" + texts.length + ", callback:" + (callback != null));
		page++;
		if (page >= texts.length) {
			if (callback != null) {
				LuaClosure call = callback;
				callback = null;
				Engine.invokeCallback(call, "Button1");
			}
			WigPushDialogActivity.this.finish();
			return;
		}
		
		tvImageText.setText("");
		
		Media m = media[page];
		if (m != null) {
			try {
				byte[] img = Engine.mediaFile(m);
				CartridgeListActivity.setBitmapToImageView(BitmapFactory.decodeByteArray(img, 0, img.length), ivImage);
			} catch (Exception e) {
				tvImageText.setText(m.altText);
			}
		} else {
			ivImage.setImageBitmap(null);
			ivImage.setMinimumWidth(0);
			ivImage.setMinimumHeight(0);
		}
		
		tvImageText.setText(tvImageText.getText().toString() + "\n" + texts[page]);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Logger.d(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
	    	return super.onKeyDown(keyCode, event);
		}
	}
}
