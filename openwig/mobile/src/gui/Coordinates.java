package gui;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.platform.LocationService;
import javax.microedition.lcdui.*;

public class Coordinates extends Form implements CommandListener, Pushable, Runnable, LocationService {
	
	private TextField latitude = new TextField("latitude", null, 99, TextField.DECIMAL);
	private TextField longitude = new TextField("longitude", null, 99, TextField.DECIMAL);
	
	private double lat = 0;
	private double lon = 0;
	
	private StringItem lblGps = new StringItem("GPS: ", null);
	private StringItem lblLat = new StringItem("\nLatitude: ", null);
	private StringItem lblLon = new StringItem("\nLongitude: ", null);
	private StringItem lblAlt = new StringItem("\nAltitude: ", null);
	
	private static Command CMD_SET = new Command("Set", Command.SCREEN, 0);
	
	private static Command CMD_GPS_ON = new Command("Connect", Command.SCREEN, 5);
	private static Command CMD_GPS_OFF = new Command("Disconnect", Command.SCREEN, 5);
	
	private Alert gpsError;

	private static String[] states = { "offline", "connecting", "no fix", "online" };
	
	public Coordinates () {
		super("GPS");
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
	}
	
	private Displayable parent;
	private Displayable errorParent;
	
	public Coordinates reset (Displayable parent) {
		if (parent != this) this.parent = parent;
		return this;
	}
	
	private void setMode () {
		deleteAll();
		append(lblGps);
		switch (Midlet.gpsType) {
			case Midlet.GPS_MANUAL:
				removeCommand(CMD_GPS_ON);
				removeCommand(CMD_GPS_OFF);
				addCommand(CMD_SET);
				append(latitude);
				append(longitude);
				break;
			case Midlet.GPS_BLUETOOTH:
			case Midlet.GPS_SERIAL:
			case Midlet.GPS_SOCKET:
				if (Midlet.gps.getState() == LocationService.OFFLINE) {
					removeCommand(CMD_GPS_OFF);
					addCommand(CMD_GPS_ON);
				} else {
					removeCommand(CMD_GPS_ON);
					addCommand(CMD_GPS_OFF);
				}
			default:
				removeCommand(CMD_SET);
				append(lblLat);
				append(lblLon);
				append(lblAlt);
				break;
		}
	}
	
	public void push () {
		refresh();
		Midlet.show(this);
	}
	
	public void refresh () {
		setMode();
		if (Midlet.gps != this && Midlet.gps.getState() != LocationService.OFFLINE) {
			start();
		} else {
			stop();
			updateScreen();
		}
	}
	
	private boolean running = false;
	
	synchronized public void start () {
		if (!running) {
			running = true;
			Thread t = new Thread(this);
			t.start();
		}
	}
	synchronized public void stop () {
		running = false;
	}
	
	public void run () {
		while (running) {
			updateScreen();
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
			if (Midlet.currentDisplay != this) stop();
		}
	}
	
	private String shortenNokiaDecimal(double d) {
		// Nokia Series40 can't handle TextField.setString with more than
		// 8 places after decimal point (when said TextField is in DECIMAL
		// mode. This should shorten it to 7 places.
		String num = String.valueOf(d);
		int len = num.length();
		int dot = num.indexOf('.');
		if (len > dot + 8) {
			return num.substring(0,dot+8);
		} else {
			return num;
		}
	}
	
	private void updateScreen () {
		if (Midlet.gps == this) {
			lblGps.setText("input your coordinates:");
			return;
		}
		lblGps.setText(states[Midlet.gps.getState()]);
		if (Midlet.gps.getState() == LocationService.ONLINE) {
			lblLat.setText(ZonePoint.makeFriendlyLatitude(Midlet.gps.getLatitude()));
			lblLon.setText(ZonePoint.makeFriendlyLongitude(Midlet.gps.getLongitude()));
			lblAlt.setText(String.valueOf(Midlet.gps.getAltitude()));
			
			latitude.setString(shortenNokiaDecimal(Midlet.gps.getLatitude()));
			longitude.setString(shortenNokiaDecimal(Midlet.gps.getLongitude()));
		} else {
			lblLat.setText(null);
			lblLon.setText(null);
			lblAlt.setText(null);
		}
	}

	private void startGPS() {
		Midlet.gps.connect();
		removeCommand(CMD_GPS_ON);
		addCommand(CMD_GPS_OFF);
	}

	private void stopGPS() {
		Midlet.gps.disconnect();
		stop();
		updateScreen();
		removeCommand(CMD_GPS_OFF);
		addCommand(CMD_GPS_ON);
	}
	
	public double getLatitude() { return lat; }
	public double getLongitude() { return lon; }
	public double getAltitude() { return 100; }
	public double getHeading() { return 0; }
	public double getPrecision() { return 1; }
	
	private double scandbl (String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_GPS_ON) {
				startGPS();
			} else if (cmd == CMD_GPS_OFF) {
				stopGPS();
			} else if (cmd == CMD_SET) {
				lat = scandbl(latitude.getString());
				lon = scandbl(longitude.getString());
			} else if (cmd == Midlet.CMD_BACK) {
				Midlet.push(parent);
			}
		} else if (disp == gpsError) {
			if (cmd.getCommandType() == Command.OK) {
				Midlet.gps.connect();
				Midlet.show(errorParent);
			} else {
				Midlet.push(this.reset(errorParent));
			}
		}
	}
	
	public void gpsConnected () {
		refresh();
	}
	
	public void gpsDisconnected () {
		refresh();
	}
	
	public void fixChanged (boolean fix) {
		refresh();
	}
	
	public void gpsError (String error) {
		gpsDisconnected();
		errorParent = Midlet.currentDisplay;
		gpsError = new Alert("GPS connection failed", error+"\nTry to reconnect?",
			null, AlertType.CONFIRMATION);
		gpsError.addCommand(new Command("Yes", Command.OK, 1));
		gpsError.addCommand(new Command("No", Command.CANCEL, 2));
		gpsError.setTimeout(Alert.FOREVER);
		gpsError.setCommandListener(this);
		Midlet.display.setCurrent(gpsError);
	}

	public int getState() {
		return LocationService.ONLINE;
	}

	public void connect() { }

	public void disconnect() { }
}
