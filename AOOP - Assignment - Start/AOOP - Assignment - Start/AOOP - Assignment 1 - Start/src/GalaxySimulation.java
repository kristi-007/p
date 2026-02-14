import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Body;
import domain.Planet;
import domain.PlanetCollection;
import domain.Star;
import domain.StarCollection;
import domain.types.PlanetType;
import domain.types.StarType;

/**
 * Main simulation class for testing the galaxy model.
 * Loads stars, generates planets, filters based on criteria, and prints statistics.
 */
public class GalaxySimulation {
	
	public static void main(String[] args) {
		try {
			// 1. Instantiate StarCollection with CSV file
			System.out.println("Loading stars from CSV...");
			StarCollection starCollection = new StarCollection("data/teststars.csv");
			System.out.println("Loaded " + starCollection.size() + " stars\n");
			
			// 2. Create PlanetCollection and add all stars
			System.out.println("Creating planet collection...");
			PlanetCollection planetCollection = new PlanetCollection();
			for (Star star : starCollection.getAllStars()) {
				planetCollection.add(star);
			}
			System.out.println("Added " + planetCollection.size() + " stars to planet collection\n");
			
			// 3. Filter out stars with no planets or only R, T, Y planets
			System.out.println("Filtering stars...");
			List<Star> starsToRemove = new ArrayList<>();
			
			for (Star star : starCollection.getAllStars()) {
				List<Body> bodies = planetCollection.get(star);
				
				// Check if star has no planets (only the star itself in the list)
				if (bodies.size() <= 1) {
					starsToRemove.add(star);
					continue;
				}
				
				// Check if all planets are R, T, or Y type
				boolean hasOtherPlanets = false;
				for (int i = 1; i < bodies.size(); i++) {
					if (bodies.get(i) instanceof Planet) {
						Planet planet = (Planet) bodies.get(i);
						PlanetType type = planet.getType();
						if (type != PlanetType.R && type != PlanetType.T && type != PlanetType.Y) {
							hasOtherPlanets = true;
							break;
						}
					}
				}
				
				if (!hasOtherPlanets) {
					starsToRemove.add(star);
				}
			}
			
			// Remove filtered stars from both collections
			for (Star star : starsToRemove) {
				starCollection.remove(star);
				planetCollection.remove(star);
			}
			System.out.println("Removed " + starsToRemove.size() + " stars (no planets or only R/T/Y planets)\n");
			
			// 4. Print statistics
			System.out.println("=== GALAXY STATISTICS ===\n");
			
			// Count double stars
			int doubleStarCount = 0;
			for (Star star : starCollection.getAllStars()) {
				if (star.getSister() != null) {
					doubleStarCount++;
				}
			}
			
			// Count planets and their types
			int totalPlanets = 0;
			int habitablePlanets = 0; // M, H, K, L
			int gasGiants = 0; // J, T
			
			for (List<Body> bodies : planetCollection) {
				for (int i = 1; i < bodies.size(); i++) {
					if (bodies.get(i) instanceof Planet) {
						Planet planet = (Planet) bodies.get(i);
						totalPlanets++;
						
						PlanetType type = planet.getType();
						if (type == PlanetType.M || type == PlanetType.H || 
						    type == PlanetType.K || type == PlanetType.L) {
							habitablePlanets++;
						}
						if (type == PlanetType.J || type == PlanetType.T) {
							gasGiants++;
						}
					}
				}
			}
			
			// Count stars by type
			Map<StarType, Integer> starsByType = new HashMap<>();
			for (Star star : starCollection.getAllStars()) {
				starsByType.put(star.getType(), 
				               starsByType.getOrDefault(star.getType(), 0) + 1);
			}
			
			// Print results
			System.out.println("Total number of stars: " + starCollection.size());
			System.out.println("Number of double stars: " + doubleStarCount);
			System.out.println("Total number of planets: " + totalPlanets);
			System.out.println("Number of habitable planets (M, H, K, L): " + habitablePlanets);
			System.out.println("Number of gas giants (J, T): " + gasGiants);
			System.out.println();
			
			System.out.println("Stars by type:");
			for (StarType type : StarType.values()) {
				int count = starsByType.getOrDefault(type, 0);
				if (count > 0) {
					System.out.println("  " + type + ": " + count);
				}
			}
			
		} catch (IOException e) {
			System.err.println("Error reading CSV file: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error during simulation: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
