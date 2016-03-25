package com.tkutz.ai.genetic.tsp.operators;

import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.tsp.City;
import com.tkutz.ai.tsp.TSP;
import com.tkutz.ai.tsp.TourUtils;

public class TwoOptAlgorithm {

	
	/**
	 * Performs 2-opt optimization for given individual. Thereby, iteratively
	 * for all cities, the first improvement is taken for edge switching.
	 * 
	 * Note that this operation changes the given individual.
	 * 
	 * @param tour
	 */
	public static void run(TSPIndividual tour) {
		int size = tour.getSize();
		outer: for (int i = 0; i<size-1; i++) {
			City sourceOne = tour.getCity(i);
			City targetOne = tour.getCity(i+1);
			double costEdgeOne = TSP.getCost(sourceOne, targetOne);
			// search a candidate for edge exchange
			for (int len=1; len<size/2; len++) {
				int sourceTwoIndex = (i+len)%size;
				int targetTwoIndex = (i+len+1)%size;
				City sourceTwo = tour.getCity(sourceTwoIndex);
				City targetTwo = tour.getCity(targetTwoIndex);
				double costEdgeTwo = TSP.getCost(sourceTwo, targetTwo);
				double costSwitchedEdgeOne = TSP.getCost(sourceOne, sourceTwo);
				double costSwitchedEdgeTwo = TSP.getCost(targetOne, targetTwo);
				
				if (costEdgeOne+costEdgeTwo > costSwitchedEdgeOne+costSwitchedEdgeTwo) {
					exchangeEdge(tour, i, sourceTwoIndex);
					continue outer;
				}
				
			}
			
		}
	}

	private static void exchangeEdge(TSPIndividual tour, int i, int j) {
		City targetOne = tour.getCity(i+1);
		City sourceTwo = tour.getCity(j);
		
		tour.setCity(i+1, sourceTwo);
		int size = tour.getSize();
		TourUtils.reverseEdges(tour, (i+2)%size, (size+j-1)%size);
		tour.setCity(j, targetOne);
	}

	

}
