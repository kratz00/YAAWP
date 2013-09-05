package locus.api.objects.extra;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;

import locus.api.objects.GeoData;

public class Circle extends GeoData {

	// center location
	private Location loc;
	// radius of circle
	private float radius;
	// draw as precise geodetic circle
	private boolean drawPrecise;
	
	/**
	 * Empty constructor for storable object only,
	 * do not use directly
	 */
	public Circle() {
		super();
	}
	
	public Circle(Location loc, float radius) throws IOException {
		this(loc, radius, false);
	}
	
	public Circle(Location loc, float radius, boolean drawPrecise) throws IOException {
		super();
		this.loc = loc;
		this.radius = radius;
		this.drawPrecise = drawPrecise;
		checkData();
	}
	
	public Circle(DataInputStream dis) throws IOException {
		super(dis);
		checkData();
	}
	
	public Circle(byte[] data) throws IOException {
		super(data);
		checkData();
	}
	
	private void checkData() throws InvalidObjectException {
		if (loc == null) {
			throw new InvalidObjectException("Location cannot be 'null'");
		}
		// store radius
		if (radius <= 0.0f) {
			throw new InvalidObjectException("radius have to be bigger then 0");
		}
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public boolean isDrawPrecise() {
		return drawPrecise;
	}
	
	public void setDrawPrecise(boolean drawPrecise) {
		this.drawPrecise = drawPrecise;
	}

	// STORABLE PART
	
	@Override
	protected int getVersion() {
		return 1;
	}

	@Override
	protected void readObject(int version, DataInputStream dis) throws IOException {
		
		// GEODATA PART
		
		id = dis.readLong();
		name = readStringUTF(dis);
		readExtraData(dis);
		readStyles(dis);

		// PRIVATE PART
		
		loc = new Location(dis);
		radius = dis.readFloat();
		drawPrecise = dis.readBoolean();
		
		// version 1 extension
		if (version >= 1) {
			timeCreated = dis.readLong();
		}
	}

	@Override
	protected void writeObject(DataOutputStream dos) throws IOException {
		
		// GEODATA PART
		
		dos.writeLong(id);
		writeStringUTF(dos, name);
		writeExtraData(dos);
		writeStyles(dos);

		// PRIVATE PART
		
		loc.write(dos);
		dos.writeFloat(radius);
		dos.writeBoolean(drawPrecise);
		
		// version 1 extension
		dos.writeLong(timeCreated);
	}

	@Override
	public void reset() {
		loc = null;
		radius = 0.0f;
		drawPrecise = false;
		
		// version 1 extension
		timeCreated = System.currentTimeMillis();
	}
}
