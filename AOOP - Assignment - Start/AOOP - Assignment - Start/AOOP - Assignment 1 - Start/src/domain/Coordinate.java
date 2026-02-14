package domain;

import util.MathUtil;

public final class Coordinate {
	private final float distance; // in parsecs counted from the center of the galaxy
	private final float longitude; // rotation relative to the N-S axis 
	private final float latitude; // rotation relative to the plane 
	
	public Coordinate(float distance, float longitude, float latitude) {
		super();
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	// Needs implementation!
    public double calculateDistance(Coordinate other) {
    	return MathUtil.distance(this, other);
    }
}
