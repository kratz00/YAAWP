package locus.api.objects.extra;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KmlVec2 {

	public static enum Units {
		FRACTION, PIXELS, INSET_PIXELS;
	};
	
	public double x = 0.5f;
	public Units xUnits = Units.FRACTION;
	public double y = 0.5f;
	public Units yUnits = Units.FRACTION;
	
	public KmlVec2() {}

	public KmlVec2(double x, Units xUnits, double y, Units yUnits) {
		this.x = x;
		this.xUnits = xUnits;
		this.y = y;
		this.yUnits = yUnits;
	}
	
	public double[] getCoords(double sourceWidth, double sourceHeight) {
		double[] p = new double[2];
		if (xUnits == Units.FRACTION)
			p[0] = sourceWidth * x;
		else if (xUnits == Units.PIXELS)
			p[0] = x;
		
		if (yUnits == Units.FRACTION)
			p[1] = sourceHeight * (1.0 - y);
		else if (yUnits == Units.PIXELS)
			p[1] = sourceHeight - y;
		return p;
	}

	public KmlVec2 getCopy() {
		KmlVec2 vec = new KmlVec2();
		vec.x = x;
		vec.xUnits = xUnits;
		vec.y = y;
		vec.yUnits = yUnits;
		return vec;
	}
	
	public void write(DataOutputStream dos) throws IOException {
		dos.writeDouble(x);
		dos.writeInt(xUnits.ordinal());
		dos.writeDouble(y);
		dos.writeInt(yUnits.ordinal());
	}
	
	public static KmlVec2 read(DataInputStream dis) throws IOException {
		KmlVec2 vec = new KmlVec2();
		vec.x = dis.readDouble();
		vec.xUnits = Units.values()[dis.readInt()];
		vec.y = dis.readDouble();
		vec.yUnits = Units.values()[dis.readInt()];
		return vec;
	}
}
