package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;
import org.yaawp.hmi.listitem.AbstractListItem.StyleDefine;
import org.yaawp.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class ListItemWherigoCategoryHeaderTasks extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderTasks() {
		super();
		mIconLeft = Images.getImageB( R.drawable.icon_tasks );			
	}
	
	@Override
	public void layoutOpen( Context context, View view  ) {
		int count = Engine.instance.cartridge.visibleTasks();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.tasks);
			mBody = "Nothing to do for you"; // TODO I18N
		} else {
			mTitle = I18N.get(R.string.tasks);
			mBody = "";		
		}
		super.layoutOpen( context, view );
	}
	
	@Override
	public void layoutClose( Context context, View view  ) {
		int count = Engine.instance.cartridge.visibleTasks();
		if ( count == 0 ) {
			mTitle = I18N.get(R.string.tasks);
			mBody = "Nothing to do for you"; // TODO I18N
		} else {
			mTitle = I18N.get(R.string.tasks)+ " (" + count + ")";
			mBody = getVisibleTasksDescription();			
		}
		super.layoutClose( context, view );
	}
	
	/* public int getVisibleTasksCount() {
		int count = 0;
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) count++;
		}
		return count;
	} */
	
	public String getVisibleTasksDescription() {
		String description = null;
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task a = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (a.isVisible()) {
				if (description == null)
					description = "";
				else
					description += ", ";
				description += a.name;
			}
		}
		return description;
	}		
}
