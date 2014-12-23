package com.tkutz.ai.genetic.tsp.operators;

import java.util.ArrayList;
import java.util.List;

import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.tsp.City;

/**
 * Selects two random cities of a given tour and reverses the path between them.
 * This is done with a probability given by {@link #mutationRate}.
 *
 */
public class EdgeInversion implements Mutation<TSPIndividual> {

	private double mutationRate;

	public EdgeInversion(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	@Override
	public void execute(TSPIndividual tour) {
		if (Math.random() < mutationRate) {
			int i = (int) (tour.getSize() * Math.random());
			int j = (int) (tour.getSize() * Math.random());
			int pos1, pos2;
			if (i < j) {
				pos1 = i;
				pos2 = j;
			}
			else if (i > j) {
				pos1 = j;
				pos2 = i;
			}
			else {
				return;
			}
			// reverse city order between the two cities
			List<City> subTour = new ArrayList<City>(tour.getCities().subList(pos1, pos2+1));
			for (int k = 0; k<subTour.size(); k++) {
				tour.setCity(pos2-k, subTour.get(k));
			}
		}
	}
	
	@Override
	public String toString() {
		return "Edge Inversion ("+mutationRate+")";
	}

}
