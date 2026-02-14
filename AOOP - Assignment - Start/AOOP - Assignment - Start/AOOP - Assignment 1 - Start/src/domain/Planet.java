package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.types.PlanetType;

public class Planet extends Body {
	private PlanetType type;
	private Star star;
	private final float averageDistanceToStar; // in Astronomical Units (which is the average distance between Earth and Sol)
	private final float tilt; // degrees relative to the plane of the star system
	private final float excentricity; // in degrees
	
	public Planet(String designation, String name, PlanetType type, Star star, float averageDistanceToStar, float tilt,
			float excentricity) {
		super(designation, name);
		this.type = type;
		this.star = star;
		this.averageDistanceToStar = averageDistanceToStar;
		this.tilt = tilt;
		this.excentricity = excentricity;
	}
	
	// This method needs some changing!
	public static Planet random(Star star, int index, float distance) {
		long seed = star.getDesignation().hashCode() + index + (long)(distance * 1000);
		Random r = new Random(seed);
		String designation = star.getDesignation() + " " + index;
		float tilt = r.nextFloat() * 10f;
		float excentricity = (float) ((distance / (star.getType().getMaxDistanceOfPlanets()) * 45f) * r.nextDouble());
		// Inhabitable?
		PlanetType type = null;
		if ((distance > star.getGoldilockZoneStart()) && (distance < star.getGoldilockZoneEnd())) {
			if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
				if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					type = PlanetType.M;
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					type = PlanetType.H;
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					type = PlanetType.K;
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					type = PlanetType.L;
				}
				else {
					type = PlanetType.N;
				}
			}
			else {
				type = PlanetType.D;
			}
		}
		else if ((distance > star.getGoldilockZoneEnd()) && (distance < star.getGoldilockZoneEnd() * 2)) {
			if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
				type = PlanetType.K;
			}
			else {
				type = PlanetType.D;
			}
		}
		else if (distance < star.getGoldilockZoneStart()) {
			type = PlanetType.Y;
		}
		else if (distance > star.getType().getMaxDistanceOfPlanets()) {
			type = PlanetType.N;
		}
		else {
			// Gas giants
			if (r.nextDouble() > 0.5d) {
				type = PlanetType.J;
			}
			else {
				type = PlanetType.T;
			}
		}
		return new Planet(designation, null, type, star, distance, tilt, excentricity);
	}
	
	/**
	 * Generates a list of planets for the given star.
	 * The number of planets is determined by the star's type, with random variation.
	 * Planet distances double for each subsequent planet starting from minDistance.
	 * 
	 * @param star The star to generate planets for
	 * @return List of generated planets
	 */
	public static List<Planet> generatePlanets(Star star) {
		List<Planet> planets = new ArrayList<>();
		
		// Get star type parameters
		int basePlanets = star.getType().getNumberOfPlanets();
		int variation = star.getType().getVariationOfPlanets();
		double minDistance = star.getType().getMinDistanceOfPlanets();
		double maxDistance = star.getType().getMaxDistanceOfPlanets();
		
		// Calculate number of planets with variation
		Random r = new Random(star.getDesignation().hashCode());
		int numberOfPlanets = basePlanets + (r.nextInt(variation * 2 + 1) - variation);
		
		if (numberOfPlanets <= 0) {
			return planets;
		}
		
		// Calculate distance factor so planets span from min to max
		// Distance doubles for each subsequent planet
		// Formula: minDistance * (2^n) = maxDistance
		// where n is the number of doublings
		double distanceFactor = 1.0;
		if (numberOfPlanets > 1) {
			distanceFactor = Math.pow(maxDistance / minDistance, 1.0 / (numberOfPlanets - 1));
		}
		
		// Generate planets
		float currentDistance = (float) minDistance;
		for (int i = 1; i <= numberOfPlanets; i++) {
			Planet planet = random(star, i, currentDistance);
			planets.add(planet);
			currentDistance *= distanceFactor;
		}
		
		return planets;
	}

	public PlanetType getType() {
		return type;
	}

	public void setType(PlanetType type) {
		this.type = type;
	}

	public Body getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	public float getAverageDistanceToStar() {
		return averageDistanceToStar;
	}

	public float getTilt() {
		return tilt;
	}

	public float getExcentricity() {
		return excentricity;
	}
	
	@Override
	public Coordinate getCoordinate() {
		return star.getCoordinate();
	}

	@Override
	public String toString() {
		return "Planet [designation=" + this.getDesignation() + ", type=" + type + ", starType=" + star.getType() + ", averageDistanceToStar="
				+ averageDistanceToStar + "]";
	}
}
