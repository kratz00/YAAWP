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
	public void refresh() {
		int count = Engine.instance.cartridge.visibleTasks();
		if ( count == 0 ) {
			mTitleOpen = I18N.get(R.string.tasks);
			mBodyOpen = "Nothing to do for you"; // TODO I18N
			mTitleClose = mTitleOpen;	
			mBodyClose = mBodyOpen;					
		} else {
			mTitleOpen = I18N.get(R.string.tasks);
			mBodyOpen = "";
			mTitleClose = I18N.get(R.string.tasks) + " (" + count + ")";
			mBodyClose = getVisibleTasksDescription();				
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
