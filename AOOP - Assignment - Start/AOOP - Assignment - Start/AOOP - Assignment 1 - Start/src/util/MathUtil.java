package util;

import domain.Body;
import domain.Coordinate;
import domain.Planet;
import domain.Star;
import domain.types.LuminosityClass;
import domain.types.PlanetType;
import domain.types.StarType;

public class MathUtil {
	
	
	public static <A extends Body, B extends Body> double calculateDistance(A a, B b) {
	
		if (a == null || b == null) {
			throw new IllegalArgumentException("Bodies must not be null");
		}

		
	Coordinate c1 = a.getCoordinate();
	Coordinate c2 = b.getCoordinate();
	
	return distance(c1, c2);
}	
	
	public static double distance(Coordinate c1, Coordinate c2) {
		if (c1 == null || c2 == null) {
			throw new IllegalArgumentException("Coordinates must not be null");
		}

		double r1 = c1.getDistance();
		double theta1 = Math.toRadians(c1.getLongitude());
		double phi1 = Math.toRadians(c1.getLatitude());
		double x1 = r1 * Math.cos(phi1) * Math.cos(theta1);
		double y1 = r1 * Math.cos(phi1) * Math.sin(theta1);
		double z1 = r1 * Math.sin(phi1);

	
		double r2 = c2.getDistance();
		double theta2 = Math.toRadians(c2.getLongitude());
		double phi2 = Math.toRadians(c2.getLatitude());
		double x2 = r2 * Math.cos(phi2) * Math.cos(theta2);
		double y2 = r2 * Math.cos(phi2) * Math.sin(theta2);
		double z2 = r2 * Math.sin(phi2);

		
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

}


