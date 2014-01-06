package org.yaawp.hmi.activities;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.utils.A;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;



public class AppInfoActivity extends CustomActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);  
		
    	WebView webView = new WebView(A.getMain());
		webView.loadData( getContent().toString(), "text/html", "utf-8");
		webView.setLayoutParams(new  ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		webView.setBackgroundColor(Color.WHITE);
		
        RelativeLayout contentArea = (RelativeLayout) this.findViewById(R.id.relative_layout_content);
        contentArea.removeAllViews();
        contentArea.addView(webView);  		
    }
    
    private StringBuffer getContent() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div align=\"center\"><h2><b>WhereYouGo</b></h2></div>");
		buffer.append("<div>");
		buffer.append("<b>Wherigo player for Android device</b><br /><br />");
		try {
			PackageManager pm = this.getPackageManager();
			buffer.append(I18N.get(R.string.version) + "<br />&nbsp;&nbsp;<b>" + 
					pm.getPackageInfo(this.getPackageName(), 0).versionName + "</b><br /><br />");
		} catch (Exception e) {}
		buffer.append(getString(R.string.author) + "<br />&nbsp;&nbsp;<b>Menion Asamm</b><br /><br />");
		buffer.append(getString(R.string.web_page) + "<br />&nbsp;&nbsp;<b><a href=\"http://forum.asamm.cz\">http://forum.asamm.cz</a></b><br /><br />");
		buffer.append(getString(R.string.libraries));
		buffer.append("<br />&nbsp;&nbsp;<b>OpenWig</b>");
		buffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;Matejicek");
		buffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;<small>http://code.google.com/p/openwig</small>");
		buffer.append("<br />&nbsp;&nbsp;<b>Kahlua</b>");
		buffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;Kristofer Karlsson");
		buffer.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;<small>http://code.google.com/p/kahlua/</small>");
		buffer.append("</div>");
		
		// add news
		/* TODO
		buffer.append(MainAfterStart.getNews(1, 
				Settings.getApplicationVersionActual()));
		*/  
		return buffer;
    }

}
