package org.yaawp.hmi.listitem;

import org.yaawp.R;
import org.yaawp.hmi.helper.I18N;
import org.yaawp.utils.Images;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class ListItemWherigoCategoryHeaderTasks extends ListItemWherigoCategoryHeader {

	public ListItemWherigoCategoryHeaderTasks() {
		super();
		mDataImageLeft = Images.getImageB( R.drawable.icon_tasks );	
		refresh();
	}
	
	@Override
	public int getCountChild() {
		int count = Engine.instance.cartridge.visibleTasks();
		return count;
	}
	
	@Override
	public String getTitle() {
		return I18N.get(R.string.tasks);
	}
	
	@Override
	public String getSubtitle() {
		if ( getCountChild() == 0) {
			return "Nothing to do for you"; // TODO I18N	
		} 
		return getVisibleTasksDescription();	
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
