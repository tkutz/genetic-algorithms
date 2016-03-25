package com.tkutz.ai.tsp;

import java.util.ArrayList;
import java.util.List;

import com.tkutz.ai.genetic.tsp.TSPIndividual;

public class TourUtils {
	
	public static void reverseEdges(final TSPIndividual tour, int startIndex, int endIndex) {
		// reverse city order between the two new edges, since we resolved a crossing
		int size = tour.getSize();
		List<City> subTour = getSubtour(tour, startIndex, (endIndex+1) % size);
		for (int k = 0; k<subTour.size(); k++) {
			int setIndex = (size+endIndex+1-(k+1)) % size;
			tour.setCity(setIndex, subTour.get(k));
		}
	}

	private static List<City> getSubtour(final TSPIndividual tour, int startIndex, int endIndex) {
		List<City> subTour = new ArrayList<City>();
		int index = startIndex;
		while(index!=endIndex) {
			subTour.add(tour.getCity(index));
			index++;
			index = index % tour.getSize();
		}
		return subTour;
	}
}
