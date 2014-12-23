package com.tkutz.ai.genetic.tsp.operators;

import java.util.ArrayList;
import java.util.List;

import com.tkutz.ai.genetic.crossover.CrossOver;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.tsp.City;
import com.tkutz.ai.tsp.TSP;

/**
 * Crossover operator using sequential constructive crossover.
 * 
 * @author Thomas Kutz
 *
 */
public class SCXCrossOver implements CrossOver<TSPIndividual> {

	private boolean do2Opt;

	public SCXCrossOver(boolean do2Opt) {
		this.do2Opt = do2Opt;
	}

	public SCXCrossOver() {
		this(false);
	}

	@Override
	public TSPIndividual execute(TSPIndividual parent1, TSPIndividual parent2) {
		TSPIndividual offspring = new TSPIndividual(false);
		// take first city of first parent as start
		offspring.setCity(0, parent1.getCity(0));

		for (int i = 1; i < parent1.getSize(); i++) {
			City current = offspring.getCity(i - 1);
			City nextInParent1 = getNext(current, parent1, offspring);
			City nextInParent2 = getNext(current, parent2, offspring);

			double p1Cost = TSP.getCost(current, nextInParent1);
			double p2Cost = TSP.getCost(current, nextInParent2);

			City nextCity = p1Cost < p2Cost ? nextInParent1 : nextInParent2;

			offspring.setCity(i, nextCity);
		}
		if (do2Opt) {
			perform2Opt(offspring);
		}

		return offspring;
	}

	/**
	 * Performs 2-opt optimization for given individual. Thereby, iteratively
	 * for all cities, the first improvement is taken for edge switching.
	 * 
	 * Note that this operation changes the given individual.
	 * 
	 * @param tour
	 */
	private void perform2Opt(TSPIndividual tour) {
		
		outer: for (int i = 0; i<tour.getSize()-2; i++) {
			City sourceOne = tour.getCity(i);
			City targetOne = tour.getCity(i+1);
			double costEdgeOne = TSP.getCost(sourceOne, targetOne);
			// search a candidate for edge exchange
			for (int j=i+1; j<tour.getSize()-1; j++) {
				City sourceTwo = tour.getCity(j);
				City targetTwo = tour.getCity(j+1);
				double costEdgeTwo = TSP.getCost(sourceTwo, targetTwo);
				double costSwitchedEdgeOne = TSP.getCost(sourceOne, sourceTwo);
				double costSwitchedEdgeTwo = TSP.getCost(targetOne, targetTwo);
				
				if (costEdgeOne+costEdgeTwo > costSwitchedEdgeOne+costSwitchedEdgeTwo) {
					exchangeEdge(tour, i,j);
					continue outer;
				}
				
			}
			
		}
	}

	private void exchangeEdge(TSPIndividual tour, int i, int j) {
		City targetOne = tour.getCity(i+1);
		City sourceTwo = tour.getCity(j);
		
		tour.setCity(i+1, sourceTwo);
		// reverse city order between the two new edges, since we resolved a crossing
		List<City> subTour = new ArrayList<City>(tour.getCities().subList(i+2, j));
		for (int k = 0; k<subTour.size(); k++) {
			tour.setCity(j-(k+1), subTour.get(k));
		}
		tour.setCity(j, targetOne);
	}

	private City getNext(City current, TSPIndividual parent, TSPIndividual offspring) {
		int pos = parent.getPositionOf(current);
		City next;
		if (pos == parent.getSize() - 1) {
			next = parent.getCity(0);
		} else {
			next = parent.getCity(pos + 1);
		}
		// check if next already used in offspring => take first free city
		if (offspring.contains(next)) {
			next = getFirstUnused(parent, offspring);
		}
		return next;
	}

	private City getFirstUnused(TSPIndividual parent, TSPIndividual offspring) {
		for (City city : parent.getCities()) {
			if (!offspring.contains(city)) {
				return city;
			}
		}
		return null;
	}

}
