package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Collection that behaves like Map<Star, List<Body>> with lazy planet generation.
 * Uses Delegation of Responsibility pattern by delegating to an internal HashMap.
 * Implements Iterable to return lists containing the star followed by its planets.
 */
public class PlanetCollection implements Iterable<List<Body>> {
	private final Map<Star, List<Body>> starMap;
	
	public PlanetCollection() {
		this.starMap = new HashMap<>();
	}
	
	/**
	 * Adds a star to the collection.
	 * Planets are not generated until get() is called.
	 * 
	 * @param star The star to add
	 */
	public void add(Star star) {
		if (!starMap.containsKey(star)) {
			starMap.put(star, null); // Lazy initialization - planets not yet generated
		}
	}
	
	/**
	 * Gets the list of bodies for a star, with lazy planet generation.
	 * The returned list contains the star at index 0, followed by its planets.
	 * 
	 * @param star The star to get bodies for
	 * @return List where index 0 is the star, and subsequent indices are planets
	 */
	public List<Body> get(Star star) {
		if (!starMap.containsKey(star)) {
			return null;
		}
		
		// Lazy generation - only generate planets when first requested
		List<Body> bodies = starMap.get(star);
		if (bodies == null) {
			bodies = new ArrayList<>();
			bodies.add(star); // Index 0: the star
			
			// Generate and add planets
			List<Planet> planets = Planet.generatePlanets(star);
			bodies.addAll(planets); // Index 1+: planets
			
			starMap.put(star, bodies);
		}
		
		return bodies;
	}
	
	/**
	 * Removes a star from the collection.
	 * 
	 * @param star The star to remove
	 * @return true if the star was removed, false otherwise
	 */
	public boolean remove(Star star) {
		return starMap.remove(star) != null;
	}
	
	/**
	 * Returns the number of stars in the collection.
	 * 
	 * @return The size of the collection
	 */
	public int size() {
		return starMap.size();
	}
	
	/**
	 * Returns an iterator over lists of bodies.
	 * Each list contains a star at index 0, followed by its planets.
	 * 
	 * @return Iterator of body lists
	 */
	@Override
	public Iterator<List<Body>> iterator() {
		return new Iterator<List<Body>>() {
			private final Iterator<Star> starIterator = starMap.keySet().iterator();
			
			@Override
			public boolean hasNext() {
				return starIterator.hasNext();
			}
			
			@Override
			public List<Body> next() {
				Star star = starIterator.next();
				return get(star); // This will trigger lazy generation if needed
			}
		};
	}
}
