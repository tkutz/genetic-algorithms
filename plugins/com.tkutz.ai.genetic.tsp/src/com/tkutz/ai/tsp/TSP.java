package com.tkutz.ai.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Definition of a symmetric traveling salesman problem consisting of a set of {@link City} instances and the euclidian distance as cost function.
 * 
 * @author thomas kutz
 *
 */
public class TSP {

	private static List<City> cities = new ArrayList<City>();
	
	private static Double[][] costMatrix;
	
	private static List<City> solution;

	public static List<City> getCities() {
		return cities;
	}

	public static int numberOfCities() {
		return cities.size();
	}
	
	/**
	 * Returns the solution of this TSP if it exists, otherwise null is returned.
	 * @return
	 */
	public static List<City> getSolution() {
		return solution;
	}

	/**
	 * Creates a new TSP with given number of cities randomly distributed within the given x and y limits.
	 * 
	 * @param numberOfCities
	 * @param xLimit
	 * @param yLimit
	 */
	public static void create(final int numberOfCities, final int xLimit, final int yLimit) {
		cities.clear();
		Random r = new Random();
		for (int i = 0; i < numberOfCities; i++) {
			int x = r.nextInt(xLimit);
			int y = r.nextInt(yLimit);
			cities.add(new City(x, y));
		}
		initCostMatrix();
	}
	
	/**
	 * Creates a new TSP from the given set of cities and its solution.
	 * 
	 * @param tspNodes
	 * @param tspSolution
	 */
	public static void create(final Set<City> tspNodes, final List<City> tspSolution) {
		// check that given solution contains valid cities
		if (tspNodes.size() != tspSolution.size()) {
			throw new IllegalArgumentException("Given TSP solution is not valid for the given cities.");
		}
		List<City> tmpSolution = new ArrayList<City>(tspSolution);
		if (tmpSolution.retainAll(tspNodes)) {
			throw new IllegalArgumentException("Given TSP solution is not valid for the given cities.");
		}
		cities.clear();
		cities.addAll(tspNodes);
		solution = tmpSolution;
		
		initCostMatrix();
	}
	
	/**
	 * Creates a new TSP from given set of cities.
	 * 
	 * @param tspNodes
	 */
	public static void create(final Set<City> tspNodes) {
		cities.clear();
		cities.addAll(tspNodes);
		initCostMatrix();
	}
	
	/**
	 * Calculates the cost between two cities which is the euclidean distance in this case.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static double getCost(final City source, final City target) {
		// return value in cost matrix
		int i = cities.indexOf(source);
		int j = cities.indexOf(target);
		return costMatrix[i][j];
	}
	
	private static void initCostMatrix() {
		costMatrix = new Double[cities.size()][cities.size()];
		for (int i = 0; i<cities.size(); i++) {
			// put infinity into diagonals, that's more safe than zeros
			costMatrix[i][i] = Double.MAX_VALUE;
			for (int j = i+1; j<cities.size(); j++) {
				City source = cities.get(i);
				City target = cities.get(j);
				double cost = computeCost(source, target);
				costMatrix[i][j] = cost;
				costMatrix[j][i] = cost;
			}
		}
	}
	
	private static double computeCost(final City source, final City target) {
		int xDistance = Math.abs(source.getX() - target.getX());
		int yDistance = Math.abs(source.getY() - target.getY());
		return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
	}
}