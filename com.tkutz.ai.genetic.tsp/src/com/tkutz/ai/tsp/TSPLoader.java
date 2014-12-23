package com.tkutz.ai.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSPLoader {

	public static void loadFromFile(final String tspFilePath) {
		List<City> cities;
		try {
			cities = loadCities(tspFilePath);
			TSP.create(new HashSet<City>(cities));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadFromFile(final String tspFilePath, final String tspSolutionFilePath) {
		try {
			List<City> cities = loadCities(tspFilePath);
			List<City> solution = loadSolution(tspSolutionFilePath, cities);
			TSP.create(new HashSet<City>(cities), solution);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<City> loadCities(final String tspFilePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(tspFilePath));
		String line = null;
		Pattern coordPattern = Pattern.compile("^\\s*(\\d+)?\\s(\\d+(\\.\\d+)?)\\s(\\d+(\\.\\d+)?)");
		List<City> cities = new ArrayList<City>();
		while((line = reader.readLine() ) != null) {
			// create city if line is a coordinate definition
			Matcher matcher = coordPattern.matcher(line);
			if (matcher.matches()) {
				double xCoord = Float.parseFloat(matcher.group(2));
				double yCoord = Float.parseFloat(matcher.group(4));
				int x = (int) Math.round(xCoord);
				int y = (int) Math.round(yCoord);
				cities.add(new City(x,y));
			}
		}
		reader.close();
		return cities;
	}
	
	private static List<City> loadSolution(final String tspSolutionFilePath, final List<City> cities) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(tspSolutionFilePath));
		String line = null;
		Pattern indexPattern = Pattern.compile("^(\\d+)");
		List<City> solution = new ArrayList<City>();
		while((line = reader.readLine() ) != null) {
			// create city if line is a coordinate definition
			Matcher matcher = indexPattern.matcher(line);
			if (matcher.matches()) {
				int index = Integer.parseInt(matcher.group(1));
				solution.add(cities.get(index-1));
			}
		}
		reader.close();
		return solution;
	}
}
