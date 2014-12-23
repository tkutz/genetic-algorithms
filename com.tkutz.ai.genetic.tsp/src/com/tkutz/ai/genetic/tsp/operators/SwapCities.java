package com.tkutz.ai.genetic.tsp.operators;

import com.tkutz.ai.genetic.mutation.Mutation;
import com.tkutz.ai.genetic.tsp.TSPIndividual;
import com.tkutz.ai.tsp.City;

/**
 * Iterates through the whole tour and swaps a city with a random other one with
 * the probability given by {@link #mutationRate}.
 *
 */
public class SwapCities implements Mutation<TSPIndividual> {

	/** Probability for each city to be switched. */
	private double mutationRate;

	public SwapCities(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	@Override
	public void execute(TSPIndividual tour) {
		for (int pos1 = 0; pos1 < tour.getSize(); pos1++) {
			if (Math.random() < mutationRate) {
				int pos2 = (int) (tour.getSize() * Math.random());

				City city1 = tour.getCity(pos1);
				City city2 = tour.getCity(pos2);

				tour.setCity(pos2, city1);
				tour.setCity(pos1, city2);
			}
		}

	}
	
	@Override
	public String toString() {
		return "Swap Cities ("+mutationRate+")";
	}

}
