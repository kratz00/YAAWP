package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.hmi.helper.ScreenHelper;
import org.yaawp.hmi.listitem.ListItem3ButtonsHint;

import android.app.Activity;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class ListItemWherigoTasks extends ListItem3ButtonsHint {

	public ListItemWherigoTasks() {
		super(I18N.get(R.string.tasks) + " (" + Engine.instance.cartridge.visibleTasks() + ")",
				"", R.drawable.icon_tasks );
		mBody = getVisibleTasksDescription();
		setSelectable(true);		
	}
	
	@Override
	public void onListItemClicked( Activity activity ) {
		if ( Engine.instance.cartridge.visibleTasks() >= 1) {
			ScreenHelper.activateScreen(ScreenHelper.SCREEN_TASKSCREEN, null);
		}
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