package com.tkutz.ai.genetic.tsp.operators;

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
public class SCX implements CrossOver<TSPIndividual> {

	private boolean do2Opt;

	public SCX(boolean do2Opt) {
		this.do2Opt = do2Opt;
	}

	public SCX() {
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
			TwoOptAlgorithm.run(offspring);
		}

		return offspring;
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
