package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.desktop.common.WigList;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.desktop.common.RefreshableListModel;
import java.util.ArrayList;
import java.util.List;


/** Generic class that can display a list of EventTable items.
 * <p>
 * Retrieves a list of EventTables from its
 * Source object and displays them in a pretty list.
 * Source is reread on <code>refresh()</code>
 * <p>
 * Instances of this class represent lists of zones, tasks, items in inventory
 * and visible items from active zones for {@link GameWindow}
 */
public class EventTableList extends WigList {

	/** interface representing a function object that retrieves data for the list */
	public static interface Source {

		List<EventTable> newSet ();
	}
	/** source for this list instance */
	private Source source;
	
	private List<EventTable> privateModel;

	private RefreshableListModel<EventTableListItem> model = new RefreshableListModel<EventTableListItem>();

	private GameWindow parent;

	/** Prepare data for display refresh.
	 * 
	 * This method updates RefreshableListModel's model
	 * with new information from source, so that subsequent
	 * call to <code>refresh()</code> can display everything
	 * in one go.
	 * 
	 * Called as part of the UI refresh routine.
	 * @see cz.matejcik.openwig.platform.UI#refresh()
	 */
	public void prepareRefresh () {
		privateModel = source.newSet();
		ArrayList<EventTableListItem> i = new ArrayList<EventTableListItem>(privateModel.size());
		for (EventTable e : privateModel) {
			i.add(new EventTableListItem(e));
		}
		model.update(i);
	}
	
	/** Update the display with new information from source.
	 * Called as part of the UI refresh routine.
	 * Must be called from Swing event thread.
	 * @see cz.matejcik.openwig.platform.UI#refresh()
	 * @see EventTableList#prepareRefresh() 
	 */
	public void refresh () {
		model.refresh();
	}

	@Override
	public void updateNavigation () {
		model.refresh();
	}

	public EventTableList (GameWindow parent, Source source) {
		this.parent = parent;
		this.source = source;
		setModel(model);
	}

	/** Returns the number of items currently in this list.
	 * This is used by {@link MainMenu} to update its summary display.
	 */
	public int length () {
		return privateModel.size();
	}

	/** Returns concatenated string of names of individual <code>EventTable</code>s
	 * in this list.
	 * This is used by {@link MainMenu} to update its summary display.
	 * @return comma-separated list of names of items in this list
	 */
	public String getContents () {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < privateModel.size() - 1; i++) {
			EventTable item = privateModel.get(i);
			sb.append(item.name);
			sb.append(", ");
		}
		if (!privateModel.isEmpty()) {
			EventTable item = privateModel.get(privateModel.size() - 1);
			sb.append(item.name);
		}
		return sb.toString();
	}

	@Override
	protected void onClick (int id, Object item) {
		EventTable table = ((EventTableListItem)item).getTable();
		parent.showDetails(table);
		if (table.hasEvent("OnClick")) Engine.callEvent(table, "OnClick", null);
	}
}
