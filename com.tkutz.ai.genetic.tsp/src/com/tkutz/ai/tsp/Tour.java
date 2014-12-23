package com.tkutz.ai.tsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Representation of a tour.
 * 
 * @author thomas kutz
 *
 */
public class Tour {

	/** A tour is represented as a list of cities in their traveling order **/
	private final ArrayList<City> tour = new ArrayList<City>();

	/** Cache for fitness and cost values **/
	private double fitness = 0;
	private double cost = 0;

	/**
	 * Default constructor creating a tour. If fillFromTsp is set to true, the
	 * tour will be initialized with all cities from the TSP in random order.
	 * Otherwise, the tour will be initialized with as many null values as there
	 * are cities in the TSP.
	 * 
	 * @param fillFromTsp
	 */
	public Tour(boolean fillFromTsp) {
		for (City city : TSP.getCities()) {
			if (fillFromTsp) {
				tour.add(city);
			} else {
				tour.add(null);
			}
		}
		if (fillFromTsp) {
			Collections.shuffle(tour);
		}
	}

	public City getCity(int index) {
		return (City) tour.get(index);
	}

	public void setCity(int index, City city) {
		tour.set(index, city);
		// reset cached values
		fitness = 0;
		cost = 0;
	}

	/**
	 * The fittest tour is simply the one with minimal cost, hence f=1/cost is
	 * used as fitness function.
	 * 
	 * @return
	 */
	public double getFitness() {
		if (fitness == 0) {
			fitness = 1 / (double) getCost();
		}
		return fitness;
	}

	/**
	 * Calculates the tour's cost by iterating over all cities and delegating to
	 * {@link TSP#getCost(City, City)}.
	 * 
	 * @return The tour's cost as defined in {@link TSP}.
	 */
	public double getCost() {
		// check cached cost
		if (cost == 0) {
			for (int i = 0; i < getSize(); i++) {
				City fromCity = getCity(i);
				City toCity;
				// if last city reached, connect it with first city
				if (i == getSize() - 1) {
					toCity = getCity(0);
				} else {
					toCity = getCity(i + 1);
				}
				// Get the distance between the two cities
				cost += TSP.getCost(fromCity, toCity);
			}
		}
		return cost;
	}

	public int getSize() {
		return tour.size();
	}

	public boolean contains(final City city) {
		return tour.contains(city);
	}

	public ArrayList<City> getCities() {
		return tour;
	}
	
	public int getPositionOf(City city) {
		return tour.indexOf(city);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (int i = 0; i < getSize(); i++) {
			sb.append(getCity(i) + "|");
		}
		return sb.toString();
	}

}