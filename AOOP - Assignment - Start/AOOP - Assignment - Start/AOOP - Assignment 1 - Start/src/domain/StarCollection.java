package domain;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import domain.types.LuminosityClass;
import domain.types.StarType;

/**
 * Collection that stores stars with proximity validation rules.
 * Uses Delegation of Responsibility pattern by delegating to an internal HashSet.
 */
public class StarCollection {
	private final Set<Body> stars;
	
	/**
	 * Creates a new StarCollection and populates it from a CSV file.
	 * 
	 * @param csvFilePath Path to the CSV file containing star data
	 * @throws IOException if the file cannot be read
	 */
	public StarCollection(String csvFilePath) throws IOException {
		this.stars = new HashSet<>();
		loadFromCSV(csvFilePath);
	}
	
	/**
	 * Loads stars from a CSV file.
	 * CSV format: Designation,Name,StarType,TemperatureSequence,LuminosityClass,absoluteMagnitude,distance,longitude,latitude,goldilockZoneStart,goldilockZoneEnd
	 */
	private void loadFromCSV(String csvFilePath) throws IOException {
		try (FileReader reader = new FileReader(csvFilePath);
		     CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
			
			for (CSVRecord record : csvParser) {
				String designation = record.get("Designation");
				String name = record.get("Name");
				String starTypeStr = record.get("StarType");
				int temperatureSequence = Integer.parseInt(record.get("TemperatureSequence"));
				String luminosityStr = record.get("LuminosityClass");
				float absoluteMagnitude = Float.parseFloat(record.get("absoluteMagnitude"));
				float distance = Float.parseFloat(record.get("distance"));
				float longitude = Float.parseFloat(record.get("longitude"));
				float latitude = Float.parseFloat(record.get("latitude"));
				
				StarType starType = StarType.parse(starTypeStr);
				LuminosityClass luminosityClass = LuminosityClass.parse(luminosityStr);
				
				Star star = new Star(designation, name, starType, temperatureSequence, 
				                    luminosityClass, absoluteMagnitude, distance, longitude, latitude);
				
				add(star);
			}
		}
	}
	
	/**
	 * Adds a star to the collection with proximity validation.
	 * 
	 * @param body The body (must be a Star) to add
	 * @return true if the star was added, false if it's a duplicate or violates proximity rules
	 */
	public boolean add(Body body) {
		// Check for duplicate
		if (stars.contains(body)) {
			return false;
		}
		
		// Only accept Body subclasses (specifically Star)
		if (!(body instanceof Star)) {
			return false;
		}
		
		Star newStar = (Star) body;
		
		// Check proximity rules
		Star potentialSister = null;
		int nearbyStarCount = 0;
		
		for (Body existingBody : stars) {
			if (!(existingBody instanceof Star)) {
				continue;
			}
			
			Star existingStar = (Star) existingBody;
			double distance = newStar.calculateDistance(existingStar);
			
			// Reject if within 0.01 parsecs (too close to any star)
			if (distance < 0.01) {
				return false;
			}
			
			// Reject if within 0.3 parsecs of a double star (more restrictive for binaries)
			if (distance < 0.3 && existingStar.getSister() != null) {
				return false;
			}
			
			// Reject if between 0.03 and 0.3 parsecs of a single star
			if (distance >= 0.03 && distance < 0.3 && existingStar.getSister() == null) {
				return false;
			}
			
			// Check for potential sister star (within 0.1 parsecs and not already double)
			// This can only happen if distance is between 0.01 and 0.03 parsecs
			if (distance < 0.1 && existingStar.getSister() == null) {
				nearbyStarCount++;
				potentialSister = existingStar;
			}
		}
		
		// If exactly one nearby star (not already double), mark both as double stars
		if (nearbyStarCount == 1) {
			newStar.setSister(potentialSister);
			potentialSister.setSister(newStar);
		}
		
		// Add the star
		stars.add(newStar);
		return true;
	}
	
	/**
	 * Returns all stars of a given type.
	 * 
	 * @param type The StarType to filter by
	 * @return Set of stars matching the given type
	 */
	public Set<Star> getStarList(StarType type) {
		Set<Star> result = new HashSet<>();
		for (Body body : stars) {
			if (body instanceof Star) {
				Star star = (Star) body;
				if (star.getType() == type) {
					result.add(star);
				}
			}
		}
		return result;
	}
	
	/**
	 * Finds a star by its designation.
	 * 
	 * @param designation The designation to search for
	 * @return The star with the given designation, or null if not found
	 */
	public Star find(String designation) {
		for (Body body : stars) {
			if (body instanceof Star && body.getDesignation().equals(designation)) {
				return (Star) body;
			}
		}
		return null;
	}
	
	/**
	 * Removes a star from the collection.
	 * 
	 * @param star The star to remove
	 * @return true if the star was removed, false otherwise
	 */
	public boolean remove(Star star) {
		return stars.remove(star);
	}
	
	/**
	 * Returns the number of stars in the collection.
	 * 
	 * @return The size of the collection
	 */
	public int size() {
		return stars.size();
	}
	
	/**
	 * Returns a set of all stars in the collection.
	 * 
	 * @return Set of all stars
	 */
	public Set<Star> getAllStars() {
		Set<Star> result = new HashSet<>();
		for (Body body : stars) {
			if (body instanceof Star) {
				result.add((Star) body);
			}
		}
		return result;
	}
}
