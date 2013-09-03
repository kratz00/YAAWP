package gui;

import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.j2me.*;
import cz.matejcik.openwig.platform.*;
import java.io.*;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import util.BackgroundRunner;
import util.Config;

public class Browser extends List implements Pushable, CommandListener {

	private Hashtable cache = new Hashtable(10);
	private String currentPath;
	private String root;
	private String selectedFile = null;

	private static Alert restoreQuestion = new Alert("question", "Load previously saved game?", null, AlertType.CONFIRMATION);
	private static Command restoreYes = new Command("Yes", Command.SCREEN, 1);
	private static Command restoreNo = new Command("No", Command.SCREEN, 2);
	static {
		restoreQuestion.addCommand(restoreYes);
		restoreQuestion.addCommand(restoreNo);
	}
	
	private static Image gwc = null;
	private static Image ows = null;
	static {
		try {
			gwc = Image.createImage("/icons/compass.png");
			ows = Image.createImage("/icons/task-pending.png");

		} catch (IOException e) { }
	}

	// those are used to hand over cf and lf from restore dialog.
	// do not use for anything else ever.
	private CartridgeFile tmpCartridgeFile;
	private OutputStream tmpLogFile;

	public Browser() {
		super("wait...", List.IMPLICIT);
		/*currentPath = Midlet.config.get(Config.LAST_DIRECTORY);
		if (currentPath == null) currentPath = "";*/

		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}

	public void push () {
		// XXX TODO not refresh when returning from details
		chdir(Midlet.config.get(Config.LAST_DIRECTORY));
		restoreQuestion.setCommandListener(this);
		Midlet.show(this);
	}

	private void setCurrentPath (String path) {
		Midlet.config.set(Config.LAST_DIRECTORY, path);
		currentPath = path;
		setTitle(path);
	}

	private void handleProblem (Throwable e) {
		if (e instanceof IOException) {
			Midlet.error(e.getMessage());
		} else if (e instanceof SecurityException) {
			Midlet.error("you need to allow me to access your files!");
		}
	}

        private int filePriority(String fileName) {
            if (fileName.endsWith("/")) {
                return 2;
            } else if (fileName.endsWith(".gwc")) {
                return 1;
            } else {
                return 0;
            }
        }

	private class Chdir implements Runnable {
		public String where;
		public void run () {
			try {
				if (where.endsWith("/")) {
					FileConnection fc = (FileConnection)Connector.open("file:///" + where, Connector.READ);
					Enumeration list = fc.list();
					deleteAll();
					append("..", null);
					Vector files = new Vector();
					while (list.hasMoreElements()) {
						String file = list.nextElement().toString();
						String fileL = file.toLowerCase();
						int prio = filePriority(fileL);
						files.addElement("");
						int i;
						for (i = files.size() - 1; i > 0; i--) {
							String current = (String)files.elementAt(i - 1);
							String currentL = current.toLowerCase();
							int currentPrio = filePriority(currentL);
							if (currentPrio > prio || (currentPrio == prio && currentL.compareTo(fileL) <= 0)) {
								break;
							}
							files.setElementAt(current, i);
						}
						files.setElementAt(file, i);
					}

					for (int i = 0; i < files.size(); i++) {
						String file = (String)files.elementAt(i);
						Image image = null;
						if (file.toLowerCase().endsWith(".gwc"))
							image = gwc;
						/*else if (fn.toLowerCase().endsWith(".ows"))
						image = ows;*/
						append(file, image);
					}
					setCurrentPath(where);
				}
			} catch (IllegalArgumentException e) {
				listRoot.run();
			} catch (IOException e) {
				Midlet.error(e.getMessage());
				listRoot.run();
			} catch (Throwable e) {
				handleProblem(e);
			}
		}
	}
	private Chdir chdir = new Chdir();

	private void chdir (String where) {
		if (where == null || where.length() == 0) listRoot();
		else {
			chdir.where = where;
			BackgroundRunner.performTask(chdir);
		}
	}

	private void descend (String dirname) {
		chdir(currentPath + dirname);

	}

	private void ascend () {
		if ("".equals(currentPath) || currentPath.lastIndexOf('/', currentPath.length() - 2) == -1) {
			listRoot();
		} else {
			String below = currentPath.substring(0, currentPath.lastIndexOf('/', currentPath.length() - 2)) + "/";
			chdir(below);
		}
	}

	private Runnable listRoot = new Runnable() {
		public void run () {
			Enumeration roots = FileSystemRegistry.listRoots();
			deleteAll();
			while (roots.hasMoreElements()) {
				String root = roots.nextElement().toString();
				append(root, null);
			}
			setCurrentPath("");
		}
	};

	private void listRoot () {
		BackgroundRunner.performTask(listRoot);
	}

	private class OpenFile implements Runnable {
		public String filename;
		public void run () {
			String file = "file:///" + currentPath + filename;
			selectedFile = filename;
			try {
				CartridgeFile cf = CartridgeFile.read(new J2MESeekableFile(file), getSyncFile());
				OutputStream os = null;

				// open logfile
				if (Midlet.config.getInt(Config.ENABLE_LOGGING) > 0) try {
					FileConnection fc = (FileConnection)Connector.open(file.substring(0, file.length() - 3) + "gwl", Connector.READ_WRITE);
					if (!fc.exists()) fc.create();
					os = fc.openOutputStream(fc.fileSize());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (cf.getSavegame().exists()) {
					tmpCartridgeFile = cf;
					tmpLogFile = os;
					Midlet.display.setCurrent(restoreQuestion);
				} else {
					Midlet.push(Midlet.cartridgeDetails.reset(cf, os));
				}
			} catch (IOException e) {
				Midlet.error("Failed to load cartridge:\n" + e.getMessage());
			}
			selectedFile = filename;
		}
	}
	private OpenFile openFile = new OpenFile();

	private void openFile (String name) {
		openFile.filename = name;
		BackgroundRunner.performTask(openFile);
	}

	public FileHandle getSyncFile ()
	throws IOException {
		try {
			String filename = selectedFile.substring(0, selectedFile.length()-3) + "ows";
			FileConnection fc = (FileConnection)Connector.open("file:///" + currentPath + filename, Connector.READ_WRITE);
			//if (!fc.exists()) fc.create();
			return new J2MEFileHandle(fc);
		} catch (SecurityException e) {
			Midlet.error("you need to allow me to access your files!");
			return null;
		}
	}

	synchronized public void commandAction(Command cmd, Displayable disp) {
		if (disp == restoreQuestion) {
			if (cmd == restoreYes) {
				Midlet.restoreCartridge(tmpCartridgeFile, tmpLogFile);
			} else {
				Midlet.push(Midlet.cartridgeDetails.reset(tmpCartridgeFile, tmpLogFile));
			}
		} else {
			if (cmd == Midlet.CMD_BACK) {
				Midlet.push(Midlet.baseMenu);
			} else if (cmd == List.SELECT_COMMAND) {
				String sel = getString(getSelectedIndex());
				setTitle("wait...");
				if ("..".equals(sel)) ascend();
				else if (sel.endsWith("/")) descend(sel);
				else openFile(sel);
			}
		}
	}
}
