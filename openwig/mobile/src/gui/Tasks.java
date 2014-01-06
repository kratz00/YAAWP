package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Task;

public class Tasks extends ListOfStuff {
	
	private static Image[] stateIcons;
	
	static {
		stateIcons = new Image[3];
		try {
			stateIcons[Task.PENDING] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-pending.png"));
			stateIcons[Task.DONE] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-done.png"));
			stateIcons[Task.FAILED] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-failed.png"));
		} catch (Exception e) { }
	}
	
	public Tasks () {
		super("Tasks");
	}

	protected void callStuff(Object what) {
		Task z = (Task)what;
		if (z.hasEvent("OnClick")) {
			Engine.callEvent(z, "OnClick", null);
		} else {
			Midlet.push(Midlet.details.reset(z, this));
		}
	}

	protected boolean stillValid() {
		return true;
	}

	protected Vector getValidStuff() {
		Vector newtasks = new Vector(Engine.instance.cartridge.tasks.size());
		for (int i = 0; i < Engine.instance.cartridge.tasks.size(); i++) {
			Task t = (Task)Engine.instance.cartridge.tasks.elementAt(i);
			if (t.isVisible()) newtasks.addElement(t);
		}
		return newtasks;
	}

	protected String getStuffName(Object what) {
		return ((Task)what).name;
	}
	
	protected Image getStuffIcon(Object what) {
		return stateIcons[((Task)what).state()];
	}	
}
